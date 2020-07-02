/* eslint-disable react/jsx-one-expression-per-line */
import React from "react";

import ConfirmationPopup from "./ConfirmationPopup";

export default {
  title: "ConfirmationPopup",
  component: ConfirmationPopup
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

const modal = {
  show: true,
  title: "Cancel process?",
  body
};

const onButtonClick = () => {};

const mainButton = {
  label: "main label",
  styling: "normal-blue btn",
  onClick: onButtonClick
};

const confirmButton = {
  label: "Yes, cancel my process please",
  styling: "normal-blue btn consistent-width",
  onClick: onButtonClick
};

const cancelButton = {
  label: "No, resume my process please",
  styling: "normal-white btn consistent-width",
  onClick: onButtonClick
};

export const Default = () => (
  <ConfirmationPopup
    modal={modal}
    mainButton={mainButton}
    confirmButton={confirmButton}
    cancelButton={cancelButton}
  />
);

export const Mobile = () => (
  <ConfirmationPopup
    modal={modal}
    mainButton={mainButton}
    confirmButton={confirmButton}
    cancelButton={cancelButton}
  />
);

Mobile.story = {
  parameters: {
    viewport: {
      defaultViewport: "mobile2"
    }
  }
};
