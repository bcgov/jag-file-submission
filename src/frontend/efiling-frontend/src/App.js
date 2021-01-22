import React, { useState } from "react";
import axios from "axios";
import queryString from "query-string";
import { Switch, Route, useHistory, useLocation } from "react-router-dom";
import AuthenticationGuard from "./components/hoc/AuthenticationGuard";

const mainButton = {
  label: "Cancel",
  styling: "bcgov-normal-white btn",
};

const confirmButton = {
  label: "Yes, cancel E-File Submission",
  styling: "bcgov-normal-blue btn bcgov-consistent-width",
};

const cancelButton = {
  label: "No, resume E-File Submission",
  styling: "bcgov-normal-white btn bcgov-consistent-width",
};

export default function App() {
  const location = useLocation();
  const queryParams = queryString.parse(location.search);
  const {
    submissionId,
    transactionId,
    responseCode,
    customerCode,
  } = queryParams;

  if (responseCode === "19" || responseCode === "17")
    sessionStorage.setItem("bamboraErrorExists", true);
  if (responseCode === "1") {
    sessionStorage.setItem("bamboraSuccess", customerCode);
    sessionStorage.setItem("bamboraErrorExists", false);
  }

  if (typeof customerCode === "undefined")
    sessionStorage.removeItem("isBamboraRedirect");

  if (submissionId && transactionId) {
    sessionStorage.setItem("submissionId", submissionId);
    sessionStorage.setItem("transactionId", transactionId);
  }

  const header = {
    name: "E-File Submission",
    history: useHistory(),
  };

  const [show, setShow] = useState(false);
  const handleClose = () => setShow(false);
  const handleShow = () => setShow(true);

  const handleConfirm = () => {
    sessionStorage.setItem("validExit", true);
    sessionStorage.removeItem("isBamboraRedirect");
    const cancelUrl = sessionStorage.getItem("cancelUrl");

    if (cancelUrl) {
      axios
        .delete(`submission/${sessionStorage.getItem("submissionId")}`)
        .then(() => window.open(cancelUrl, "_self"))
        .catch(() => window.open(cancelUrl, "_self"));
    }
  };

  const modal = {
    show,
    title: "Cancel E-File Submission?",
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
        <Route path="">
          <AuthenticationGuard page={{ header, confirmationPopup }} />
        </Route>
        <Route path="packagereview/:packageId">
          <AuthenticationGuard page={{ header, confirmationPopup }} />
        </Route>
      </Switch>
    </div>
  );
}
