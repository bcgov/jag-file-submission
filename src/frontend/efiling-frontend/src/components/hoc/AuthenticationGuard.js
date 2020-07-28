import React, { useState, useEffect } from "react";
import Keycloak from "keycloak-js";
import Home from "../page/home/Home";

let url;
let realm;
let clientId;

if (process.env.REACT_APP_KEYCLOAK_URL)
  url = process.env.REACT_APP_KEYCLOAK_URL;

if (process.env.REACT_APP_KEYCLOAK_REALM)
  realm = process.env.REACT_APP_KEYCLOAK_REALM;

if (process.env.REACT_APP_KEYCLOAK_CLIENT_ID)
  clientId = process.env.REACT_APP_KEYCLOAK_CLIENT_ID;

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
