import React, { useState, useEffect } from "react";
import Keycloak from "keycloak-js";
import Home from "../page/home/Home";

let url;
let realm;
let clientId;
let accessRole;

if (process.env.REACT_APP_KEYCLOAK_URL)
  url = process.env.REACT_APP_KEYCLOAK_URL;

if (process.env.REACT_APP_KEYCLOAK_REALM)
  realm = process.env.REACT_APP_KEYCLOAK_REALM;

if (process.env.REACT_APP_KEYCLOAK_CLIENT_ID)
  clientId = process.env.REACT_APP_KEYCLOAK_CLIENT_ID;

if (process.env.REACT_APP_KEYCLOAK_ACCESS_ROLE)
  accessRole = process.env.REACT_APP_KEYCLOAK_ACCESS_ROLE;

const KEYCLOAK = {
  url,
  realm,
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
      })
      .success(() => {
        keycloak.loadUserInfo().success();
        console.log(keycloak);
        if (
          accessRole &&
          keycloak.tokenParsed.realm_access.roles.indexOf(accessRole) !== -1
        ) {
          localStorage.setItem("jwt", keycloak.token);
          setAuthedKeycloak(keycloak);
        } else {
          keycloak.clearToken();
          localStorage.clear();
          alert(
            "Authenticated but not Authorized, request access from your portal administrator"
          );
        }
      });
  }

  useEffect(() => {
    keycloakInit();
  }, []);

  return (
    <>
      {authedKeycloak && <Home page={{ header }} />}
      {!authedKeycloak && <p>Unauthenticated!</p>}
    </>
  );
}
