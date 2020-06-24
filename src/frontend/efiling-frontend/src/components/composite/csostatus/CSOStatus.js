import React from "react";
import PropTypes from "prop-types";
import { Button } from "shared-components";

export default function CSOStatus({ accountExists }) {
  return (
    <div>
      {accountExists && <p>Account exists! Proceed</p>}
      {!accountExists && <p>Account does not exist, form will be here</p>}
      <Button
        onClick={() => {
          window.open(sessionStorage.getItem("cancelUrl"), "_self");
        }}
        label="Cancel and return to client"
        styling="normal-white btn"
      />
    </div>
  );
}

CSOStatus.propTypes = {
  accountExists: PropTypes.bool.isRequired
};
