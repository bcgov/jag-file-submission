import React from "react";
import PropTypes from "prop-types";
import ConfirmationPopup from "shared-components";
import { propTypes } from "../../../types/propTypes";

export default function CSOStatus({
  csoStatus: { accountExists, confirmationPopup }
}) {
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
  csoStatus: PropTypes.shape({
    accountExists: PropTypes.bool.isRequired,
    confirmationPopup: propTypes.confirmationPopup
  }).isRequired
};
