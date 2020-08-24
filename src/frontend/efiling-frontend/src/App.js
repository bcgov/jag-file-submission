import React, { useState } from "react";
import axios from "axios";
import queryString from "query-string";
import {
  Switch,
  Route,
  Redirect,
  useHistory,
  useLocation,
} from "react-router-dom";
import AuthenticationGuard from "./components/hoc/AuthenticationGuard";

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
  const location = useLocation();
  const queryParams = queryString.parse(location.search);
  const { submissionId, transactionId, isBambora } = queryParams;

  if (!isBambora) sessionStorage.removeItem("isBamboraRedirect");

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
    const cancelUrl = sessionStorage.getItem("cancelUrl");

    if (cancelUrl) {
      axios
        .delete(`submission/${submissionId}`)
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
        <Redirect exact from="/" to="/efiling" />
        <Route exact path="/efiling">
          <AuthenticationGuard
            page={{
              header,
              confirmationPopup,
              submissionId: sessionStorage.getItem("submissionId"),
              transactionId: sessionStorage.getItem("transactionId"),
            }}
          />
        </Route>
      </Switch>
    </div>
  );
}
