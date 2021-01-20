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
        checkLoginIframe: false,
      })
      .success((authenticated) => {
        if (authenticated) {
          keycloak.loadUserInfo().success();

          localStorage.setItem("jwt", keycloak.token);
          setAuthedKeycloak(keycloak);
        } else {
          keycloak.login();
        }
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
