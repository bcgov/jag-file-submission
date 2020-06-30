/* eslint-disable react/jsx-one-expression-per-line */
import React from "react";

const mainButton = {
  label: "Cancel",
  styling: "normal-white btn"
};

const confirmButton = {
  label: "Yes, cancel",
  styling: "normal-blue btn consistent-width"
};

const cancelButton = {
  label: "No, dont cancel",
  styling: "normal-white btn consistent-width"
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
