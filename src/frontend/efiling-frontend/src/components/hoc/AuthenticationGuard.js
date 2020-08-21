import React, { useState, useEffect } from "react";
import PropTypes from "prop-types";
import axios from "axios";
import Keycloak from "keycloak-js";
import createAuthRefreshInterceptor from "axios-auth-refresh";
import Home from "../page/home/Home";
import { propTypes } from "../../types/propTypes";

const url = window.REACT_APP_KEYCLOAK_URL
  ? window.REACT_APP_KEYCLOAK_URL
  : process.env.REACT_APP_KEYCLOAK_URL;
const realm = window.REACT_APP_KEYCLOAK_REALM
  ? window.REACT_APP_KEYCLOAK_REALM
  : process.env.REACT_APP_KEYCLOAK_REALM;
const clientId = window.REACT_APP_KEYCLOAK_CLIENT_ID
  ? window.REACT_APP_KEYCLOAK_CLIENT_ID
  : process.env.REACT_APP_KEYCLOAK_CLIENT_ID;

const defaultIdentityProvider = window.REACT_APP_DEFAULT_IDENTITY_PROVIDER
  ? window.REACT_APP_DEFAULT_IDENTITY_PROVIDER
  : process.env.REACT_APP_DEFAULT_IDENTITY_PROVIDER;

const KEYCLOAK = {
  realm,
  url,
  clientId,
};

// Initialize client
const keycloak = Keycloak(KEYCLOAK);

keycloak.onAuthSuccess = () => localStorage.setItem("jwt", keycloak.token);
keycloak.onAuthRefreshSuccess = () =>
  localStorage.setItem("jwt", keycloak.token);

/**
 * @constant authenticationGuard - a higher order component that checks for user authorization and returns the wrapped component if the user is authenticated
 */

export default function AuthenticationGuard({
  page: { header, submissionId, transactionId },
}) {
  const [authedKeycloak, setAuthedKeycloak] = useState(null);

  async function keycloakInit() {
    await keycloak
      .init({
        checkLoginIframe: false,
      })
      .success((authenticated) => {
        if (authenticated) {
          keycloak.loadUserInfo().success();

          localStorage.setItem("jwt", keycloak.token);
          setAuthedKeycloak(keycloak);
        } else {
          keycloak.login({
            idpHint: `${defaultIdentityProvider}`,
          });
        }
      });
  }

  useEffect(() => {
    keycloakInit();
  }, []);

  return (
    <>
      {authedKeycloak && (
        <Home page={{ header, submissionId, transactionId }} />
      )}
      {!authedKeycloak && null}
    </>
  );
}

const updateToken = () => {
  return new Promise((resolve, reject) => {
    keycloak
      .updateToken()
      .success(() => {
        resolve(keycloak.token);
      })
      .error(() => {
        // TODO: redirect to error page
        reject(new Error("Could not refresh token"));
      });
  });
};

// Function that will be called to refresh authorization
function refreshAuthLogic(failedRequest) {
  return updateToken().then((token) => {
    // eslint-disable-next-line no-param-reassign
    failedRequest.response.config.headers.Authorization = `Bearer ${token}`;
    return Promise.resolve();
  });
}

// Instantiate the interceptor (you can chain it as it returns the axios instance)
createAuthRefreshInterceptor(axios, refreshAuthLogic);

AuthenticationGuard.propTypes = {
  page: PropTypes.shape({
    header: propTypes.header,
    submissionId: PropTypes.string.isRequired,
    transactionId: PropTypes.string.isRequired,
  }).isRequired,
};
