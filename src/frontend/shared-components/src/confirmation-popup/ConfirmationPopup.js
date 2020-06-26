import React from "react";
import PropTypes from "prop-types";
import Modal from "react-bootstrap/Modal";
import { Button } from "../button/Button";

import "./ConfirmationPopup.css";

export default function ConfirmationPopup({
  modal: { show, handleShow, handleClose, handleConfirm, title, body1, body2 },
  mainButton: { mainLabel, mainStyling },
  confirmButton: { confirmLabel, confirmStyling },
  cancelButton: { cancelLabel, cancelStyling }
}) {
  return (
    <>
      <Button onClick={handleShow} label={mainLabel} styling={mainStyling} />

      <Modal show={show} onHide={handleClose}>
        <Modal.Header className="hide-border padding-left" closeButton>
          <Modal.Title className="mt-3 larger-font">{title}</Modal.Title>
        </Modal.Header>
        <Modal.Body className="padding-left">
          <p>{body1}</p>
          <p>{body2}</p>
        </Modal.Body>
        <div className="mx-auto mb-5">
          <Button
            styling={confirmStyling}
            onClick={handleConfirm}
            label={confirmLabel}
          />
          <br />
          <Button
            styling={cancelStyling}
            onClick={handleClose}
            label={cancelLabel}
          />
        </div>
      </Modal>
    </>
  );
}

ConfirmationPopup.propTypes = {
  modal: PropTypes.shape({
    show: PropTypes.bool.isRequired,
    handleShow: PropTypes.func.isRequired,
    handleClose: PropTypes.func.isRequired,
    handleConfirm: PropTypes.func.isRequired,
    title: PropTypes.string.isRequired,
    body1: PropTypes.string.isRequired,
    body2: PropTypes.string
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
};
