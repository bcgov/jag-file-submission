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
  handleShow: () => {},
  handleClose: () => {},
  handleConfirm: () => {},
  title: "Cancel process?",
  body
};
const mainButton = { mainLabel: "main label", mainStyling: "normal-blue btn" };
const confirmButton = {
  confirmLabel: "Yes, cancel my process please",
  confirmStyling: "normal-blue btn consistent-width"
};
const cancelButton = {
  cancelLabel: "No, resume my process please",
  cancelStyling: "normal-white btn consistent-width"
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
