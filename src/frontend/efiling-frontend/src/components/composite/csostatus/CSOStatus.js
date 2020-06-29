import React from "react";
import PropTypes from "prop-types";
import ConfirmationPopup from "shared-components";

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
  accountExists: PropTypes.bool.isRequired,
  confirmationPopup: PropTypes.shape({
    modal: PropTypes.shape({
      show: PropTypes.bool.isRequired,
      handleShow: PropTypes.func.isRequired,
      handleClose: PropTypes.func.isRequired,
      handleConfirm: PropTypes.func.isRequired,
      title: PropTypes.string.isRequired,
      body: PropTypes.func.isRequired
    }).isRequired,
    mainButton: PropTypes.shape({
      mainLabel: PropTypes.string.isRequired,
      mainStyling: PropTypes.string.isRequired
    }).isRequired,
    confirmButton: PropTypes.shape({
      confirmLabel: PropTypes.string.isRequired,
      confirmStyling: PropTypes.string.isRequired
    }).isRequired,
    cancelButton: PropTypes.shape({
      cancelLabel: PropTypes.string.isRequired,
      cancelStyling: PropTypes.string.isRequired
    }).isRequired
  }).isRequired
};
