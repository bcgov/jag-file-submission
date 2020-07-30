import React, { useState, useEffect } from "react";
import PropTypes from "prop-types";
import Keycloak from "keycloak-js";
import Home from "../page/home/Home";
import { propTypes } from "../../types/propTypes";

let url;
let realm;
let clientId;

if (window.REACT_APP_KEYCLOAK_URL) {
  url = window.REACT_APP_KEYCLOAK_URL;
} else if (process.env.REACT_APP_KEYCLOAK_URL) {
  url = process.env.REACT_APP_KEYCLOAK_URL;
}

if (window.REACT_APP_KEYCLOAK_REALM) {
  realm = window.REACT_APP_KEYCLOAK_REALM;
} else if (process.env.REACT_APP_KEYCLOAK_REALM) {
  realm = process.env.REACT_APP_KEYCLOAK_REALM;
}

if (window.REACT_APP_KEYCLOAK_CLIENT_ID) {
  clientId = window.REACT_APP_KEYCLOAK_CLIENT_ID;
} else if (process.env.REACT_APP_KEYCLOAK_CLIENT_ID) {
  clientId = process.env.REACT_APP_KEYCLOAK_CLIENT_ID;
}

const KEYCLOAK = {
  realm,
  url,
  clientId,
};

/**
 * @constant authenticationGuard - a higher order component that checks for user authorization and returns the wrapped component if the user is authenticated
 */

export default function AuthenticationGuard({
  page: { header, confirmationPopup },
}) {
  const [authedKeycloak, setAuthedKeycloak] = useState(null);

  async function keycloakInit() {
    // Initialize client
    const keycloak = Keycloak(KEYCLOAK);

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

AuthenticationGuard.propTypes = {
  page: PropTypes.shape({
    header: propTypes.header,
    confirmationPopup: propTypes.confirmationPopup,
  }).isRequired,
};
