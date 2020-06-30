import React from "react";
import PropTypes from "prop-types";
import Modal from "react-bootstrap/Modal";
import { Button } from "../button/Button";
import { propTypes } from "../../types/propTypes";

import "./ConfirmationPopup.css";

export default function ConfirmationPopup({
  modal: { show, handleShow, handleClose, handleConfirm, title, body },
  mainButton,
  confirmButton,
  cancelButton
}) {
  return (
    <>
      <Button
        onClick={handleShow}
        label={mainButton.label}
        styling={mainButton.styling}
      />

      <Modal show={show} onHide={handleClose}>
        <Modal.Header className="hide-border padding-left" closeButton>
          <Modal.Title className="mt-3 larger-font">{title}</Modal.Title>
        </Modal.Header>
        <Modal.Body className="padding-left">{body()}</Modal.Body>
        <div className="mx-auto mb-5">
          <Button
            styling={confirmButton.styling}
            onClick={handleConfirm}
            label={confirmButton.label}
          />
          <br />
          <Button
            styling={cancelButton.styling}
            onClick={handleClose}
            label={cancelButton.label}
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
    body: PropTypes.func.isRequired
  }).isRequired,
  mainButton: propTypes.button,
  confirmButton: propTypes.button,
  cancelButton: propTypes.button
};
