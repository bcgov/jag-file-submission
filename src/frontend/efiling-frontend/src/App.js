/* eslint-disable react/jsx-one-expression-per-line */
import React, { useState } from "react";
import { Switch, Route, Redirect, useHistory } from "react-router-dom";
import Home from "./components/page/home/Home";

const mainButton = {
  label: "Cancel",
  styling: "normal-white btn",
};

const confirmButton = {
  label: "Yes, cancel E-File Submission",
  styling: "normal-blue btn consistent-width",
};

const cancelButton = {
  label: "No, resume E-File Submission",
  styling: "normal-white btn consistent-width",
};

export default function App() {
  const header = {
    name: "E-File Submission",
    history: useHistory(),
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

  const handleConfirm = () => {
    const cancelUrl = sessionStorage.getItem("cancelUrl");

    if (cancelUrl) {
      window.open(cancelUrl, "_self");
    }
  };

  const modal = {
    show,
    title: "Cancel E-File Submission?",
    body,
  };

  const confirmationPopup = {
    modal,
    mainButton: { ...mainButton, onClick: handleShow },
    confirmButton: { ...confirmButton, onClick: handleConfirm },
    cancelButton: { ...cancelButton, onClick: handleClose },
  };

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
