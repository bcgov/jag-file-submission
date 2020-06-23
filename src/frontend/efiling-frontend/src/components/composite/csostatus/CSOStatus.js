import React from "react";
import PropTypes from "prop-types";

export default function CSOStatus({ accountExists }) {
  return (
    <div>
      {accountExists && <p>Account exists! Proceed</p>}
      {!accountExists && <p>Account does not exist, form will be here</p>}
    </div>
  );
}

CSOStatus.propTypes = {
  accountExists: PropTypes.bool.isRequired
};
