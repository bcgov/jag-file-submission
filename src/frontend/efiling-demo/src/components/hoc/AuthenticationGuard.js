import React, { useState, useEffect } from "react";
import PropTypes from "prop-types";
import Keycloak from "keycloak-js";
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
const demoSecret = window.REACT_APP_DEMO_CLIENT_SECRET
  ? window.REACT_APP_DEMO_CLIENT_SECRET
  : process.env.REACT_APP_DEMO_CLIENT_SECRET;
const demoUrl = window.REACT_APP_DEMO_URL
  ? window.REACT_APP_DEMO_URL
  : process.env.REACT_APP_DEMO_URL;

sessionStorage.setItem("demoKeycloakRealm", realm);
sessionStorage.setItem("demoKeycloakUrl", demoUrl);
sessionStorage.setItem("demoKeycloakClientId", clientId);
sessionStorage.setItem("demoKeycloakClientSecret", demoSecret);

const KEYCLOAK = {
  realm,
  url,
  clientId,
};

/**
 * @constant authenticationGuard - a higher order component that checks for user authorization and returns the wrapped component if the user is authenticated
 */

export default function AuthenticationGuard({ page: { header } }) {
  const [authedKeycloak, setAuthedKeycloak] = useState(null);

  async function keycloakInit() {
    // Initialize client
    const keycloak = Keycloak(KEYCLOAK);

    await keycloak
      .init({
        onLoad: "login-required",
        checkLoginIframe: false,
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
      {authedKeycloak && <Home page={{ header }} />}
      {!authedKeycloak && null}
    </>
  );
}

AuthenticationGuard.propTypes = {
  page: PropTypes.shape({
    header: propTypes.header,
  }).isRequired,
};
