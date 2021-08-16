import React from "react";
import { Button } from "shared-components";
import PropTypes from "prop-types";
import Keycloak from "keycloak-js";

export default function Login({ keycloak }) {
  const authenticateWithKeycloak = (idp) => {
    sessionStorage.setItem("validExit", true);
    console.log(idp);
    keycloak.login({
      idpHint: `${idp}`,
    });
  };

  return (
    <div style={{ "text-align": "center", "line-height": "100px" }}>
      <Button
        label="Log in with BCeID"
        onClick={() => authenticateWithKeycloak("bceid")}
        styling={"bcgov-normal-blue btn"}
      />
      <Button
        label="Log in with IDIR"
        onClick={() => authenticateWithKeycloak("idir")}
        styling={"bcgov-normal-white btn"}
      />
      <Button
        label="Log in with BC Services Card"
        onClick={() => authenticateWithKeycloak("bcsc")}
        styling={"bcgov-normal-blue btn"}
      />
    </div>
  );
}

Login.propTypes = {
  keycloak: PropTypes.instanceOf(Keycloak).isRequired,
};
