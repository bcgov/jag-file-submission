import React from "react";
import PropTypes from "prop-types";
import ConfirmationPopup, { Button } from "shared-components";

export default function CSOStatus({ accountExists, confirmationPopup }) {
  return (
    <div>
      {accountExists && <p>Account exists! Proceed</p>}
      {!accountExists && <p>Account does not exist, form will be here</p>}

      <ConfirmationPopup
        modal={confirmationPopup.modal}
        mainButton={confirmationPopup.mainButton}
        confirmButton={confirmationPopup.confirmButton}
        cancelButton={confirmationPopup.cancelButton}
      />
    </div>
  );
}

CSOStatus.propTypes = {
  accountExists: PropTypes.bool.isRequired
};
