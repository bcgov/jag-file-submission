import React from "react";

const mainButton = {
  mainLabel: "Cancel",
  mainStyling: "normal-white btn"
};

const confirmButton = {
  confirmLabel: "Yes, cancel",
  confirmStyling: "normal-blue btn consistent-width"
};

const cancelButton = {
  cancelLabel: "No, dont cancel",
  cancelStyling: "normal-white btn consistent-width"
};

const handleClose = () => {};
const handleShow = () => {};

const body = () => (
  <>
    <p>Your files will not be submitted.</p>
    <p>
      You will be returned to:
      <br />
      <b>Original</b> website
    </p>
  </>
);

export function getTestData() {
  const modal = {
    show: true,
    handleShow,
    handleClose,
    handleConfirm: () => {},
    title: "title",
    body
  };

  return { modal, mainButton, confirmButton, cancelButton };
}
