/* eslint-disable no-unused-vars */
import React from "react";
import PropTypes from "prop-types";
import ConfirmationPopup from "shared-components";

export default function RushConfirmation({ show, setShow }) {
  const modal = {
    show,
    title: "placeholder",
    body: () => <></>,
  };
  const mainButton = <></>;
  const confirmButton = <></>;
  const cancelButton = <></>;

  const handleClose = () => setShow(false);
  const handleShow = () => setShow(true);
  const handleConfirm = () => {};

  const confirmationPopup = {
    modal,
    mainButton: { ...mainButton, onClick: handleShow },
    confirmButton: { ...confirmButton, onClick: handleConfirm },
    cancelButton: { ...cancelButton, onClick: handleClose },
  };

  return (
    <ConfirmationPopup
      modal={modal}
      mainButton={mainButton}
      confirmButton={confirmButton}
      cancelButton={cancelButton}
    />
  );
}

RushConfirmation.propTypes = {
  show: PropTypes.bool.isRequired,
  setShow: PropTypes.func.isRequired,
};
