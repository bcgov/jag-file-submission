/* eslint-disable react/jsx-one-expression-per-line */
import React from "react";
import { State, Store } from "@sambego/storybook-state";

import ConfirmationPopup from "./ConfirmationPopup";

export default {
  title: "ConfirmationPopup",
  component: ConfirmationPopup
};

const store = new Store({
  show: false
});

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
  title: "Cancel process?",
  body
};

const onButtonClick = () => store.set({ show: !store.get("show") });

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
  <State store={store}>
    {state => [
      <ConfirmationPopup
        key="popup"
        modal={{ ...modal, show: state.show }}
        mainButton={mainButton}
        confirmButton={confirmButton}
        cancelButton={cancelButton}
      />
    ]}
  </State>
);

export const Mobile = () => (
  <State store={store}>
    {state => [
      <ConfirmationPopup
        key="popup"
        modal={{ ...modal, show: state.show }}
        mainButton={mainButton}
        confirmButton={confirmButton}
        cancelButton={cancelButton}
      />
    ]}
  </State>
);

Mobile.story = {
  parameters: {
    viewport: {
      defaultViewport: "mobile2"
    }
  }
};
