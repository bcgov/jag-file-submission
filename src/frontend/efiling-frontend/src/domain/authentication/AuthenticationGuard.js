/* eslint-disable react/function-component-definition */
import React, { useState, useEffect } from "react";
import PropTypes from "prop-types";
import axios from "axios";
import Keycloak from "keycloak-js";
import createAuthRefreshInterceptor from "axios-auth-refresh";

const url = window.env
  ? window.env.REACT_APP_KEYCLOAK_URL
  : process.env.REACT_APP_KEYCLOAK_URL;
const realm = window.env
  ? window.env.REACT_APP_KEYCLOAK_REALM
  : process.env.REACT_APP_KEYCLOAK_REALM;
const clientId = window.env
  ? window.env.REACT_APP_KEYCLOAK_CLIENT_ID
  : process.env.REACT_APP_KEYCLOAK_CLIENT_ID;
export const BCSC_USERINFO_URL = window.env
  ? window.env.REACT_APP_BCSC_USERINFO_URL
  : process.env.REACT_APP_BCSC_USERINFO_URL;

const defaultIdentityProvider = window.env
  ? window.env.REACT_APP_DEFAULT_IDENTITY_PROVIDER
  : process.env.REACT_APP_DEFAULT_IDENTITY_PROVIDER;

export const KEYCLOAK = {
  realm,
  url,
  clientId,
};

// Initialize client
const keycloak = new Keycloak(KEYCLOAK);

keycloak.onAuthSuccess = () => localStorage.setItem("jwt", keycloak.token);
keycloak.onAuthRefreshSuccess = () =>
  localStorage.setItem("jwt", keycloak.token);

/**
 * A higher order component that checks for user authorization and returns the wrapped component if the user is authenticated
 */
export default function AuthenticationGuard(props) {
  const { children } = props;
  const [authedKeycloak, setAuthedKeycloak] = useState(null);

  async function keycloakInit() {
    await keycloak
      .init({
        checkLoginIframe: false,
      })
      .then((authenticated) => {
        if (authenticated) {
          keycloak.loadUserInfo().then();

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
      {authedKeycloak && children}
      {!authedKeycloak && null}
    </>
  );
}

const updateToken = () =>
  new Promise((resolve, reject) => {
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
  children: PropTypes.node.isRequired,
};
