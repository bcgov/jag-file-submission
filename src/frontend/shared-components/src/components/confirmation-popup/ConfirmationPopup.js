/* eslint-disable react/require-default-props */
import React from "react";
import PropTypes from "prop-types";
import Modal from "react-bootstrap/Modal";
import { Button } from "../button/Button";
import { propTypes } from "../../types/propTypes";

import "./ConfirmationPopup.css";

export default function ConfirmationPopup({
  modal: { show, title, body },
  mainButton,
  confirmButton,
  cancelButton
}) {
  return (
    <>
      <Button
        onClick={mainButton.onClick}
        label={mainButton.label}
        styling={mainButton.styling}
      />

      <Modal show={show} onHide={cancelButton.onClick}>
        <Modal.Header className="hide-border padding-left" closeButton>
          <Modal.Title className="mt-3 larger-font">{title}</Modal.Title>
        </Modal.Header>
        <Modal.Body className="padding-left">{body()}</Modal.Body>
        <div className="mx-auto mb-5">
          <Button
            styling={confirmButton.styling}
            onClick={confirmButton.onClick}
            label={confirmButton.label}
          />
          <br />
          <Button
            styling={cancelButton.styling}
            onClick={cancelButton.onClick}
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
    title: PropTypes.string.isRequired,
    body: PropTypes.func.isRequired
  }).isRequired,
  mainButton: propTypes.button,
  confirmButton: propTypes.button,
  cancelButton: propTypes.button
};
