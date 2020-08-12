/* eslint-disable react/jsx-one-expression-per-line */
import React from "react";

const onButtonClick = () => {};

const mainButton = {
  label: "Cancel",
  styling: "normal-white btn",
  onClick: onButtonClick,
};

const confirmButton = {
  label: "Yes, cancel",
  styling: "normal-blue btn consistent-width",
  onClick: onButtonClick,
};

const cancelButton = {
  label: "No, dont cancel",
  styling: "normal-white btn consistent-width",
  onClick: onButtonClick,
};

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
    show: false,
    title: "title",
    body,
  };

  return { modal, mainButton, confirmButton, cancelButton };
}
