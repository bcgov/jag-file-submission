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

const KEYCLOAK = {
  realm,
  url,
  clientId,
};

// Initialize client
const keycloak = Keycloak(KEYCLOAK);

keycloak.onAuthSuccess = () => {
  console.log("Auth Success", keycloak.token);
};

function keycloakUpdateToken() {
  console.log("inside function");

  // Try to get refresh tokens in the background
  keycloak
    .updateToken()
    .success((refreshed) => {
      console.log("KC refreshed token?:", refreshed);
    })
    .error((err) => {
      console.log("KC refresh error:", err);
    });
}

/**
 * @constant authenticationGuard - a higher order component that checks for user authorization and returns the wrapped component if the user is authenticated
 */

export default function AuthenticationGuard({
  page: { header, confirmationPopup },
}) {
  const [authedKeycloak, setAuthedKeycloak] = useState(null);

  async function keycloakInit() {
    await keycloak
      .init({
        onLoad: "login-required",
      })
      .success(() => {
        keycloak.loadUserInfo().success();

        localStorage.setItem("jwt", keycloak.token);
        setAuthedKeycloak(keycloak);
      });
  }

  useEffect(() => {
    keycloakInit();
  }, []);

  return (
    <>
      {authedKeycloak && <Home page={{ header, confirmationPopup }} />}
      {!authedKeycloak && null}
    </>
  );
}

// Function that will be called to refresh authorization
const refreshAuthLogic = (failedRequest) => {
  console.log("inside refresh");
  keycloakUpdateToken();
};

// Instantiate the interceptor (you can chain it as it returns the axios instance)
createAuthRefreshInterceptor(axios, refreshAuthLogic);

AuthenticationGuard.propTypes = {
  page: PropTypes.shape({
    header: propTypes.header,
    confirmationPopup: propTypes.confirmationPopup,
  }).isRequired,
};
