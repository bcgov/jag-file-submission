import React, { useState } from "react";
import { Switch, Route, Redirect, useHistory } from "react-router-dom";
import Home from "./components/page/home/Home";

// For cancel button and confirmation popup
const mainButton = {
  mainLabel: "Cancel",
  mainStyling: "normal-white btn"
};
const confirmButton = {
  confirmLabel: "Yes, cancel E-File Submission",
  confirmStyling: "normal-blue btn consistent-width"
};
const cancelButton = {
  cancelLabel: "No, resume E-File Submission",
  cancelStyling: "normal-white btn consistent-width"
};

export default function App() {
  const header = {
    name: "eFiling Frontend",
    history: useHistory()
  };

  const [show, setShow] = useState(false);

  const handleClose = () => setShow(false);
  const handleShow = () => setShow(true);

  const body = () => (
    <>
      <p>Your files will not be submitted.</p>
      <p>
        You will be returned to:
        <br />
        <b>Family Law Protection Order</b> website
      </p>
    </>
  );

  const modal = {
    show,
    handleShow,
    handleClose,
    handleConfirm: () => {
      const cancelUrl = sessionStorage.getItem("cancelUrl");

      if (cancelUrl) {
        window.open(cancelUrl, "_self");
      }
    },
    title: "Cancel E-File Submission?",
    body
  };

  const confirmationPopup = { modal, mainButton, confirmButton, cancelButton };

  return (
    <div>
      <Switch>
        <Redirect exact from="/" to="/efiling" />
        <Route exact path="/efiling">
          <Home page={{ header, confirmationPopup }} />
        </Route>
      </Switch>
    </div>
  );
}
