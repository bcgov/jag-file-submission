/* eslint-disable react/function-component-definition */
/* eslint-disable camelcase, react/jsx-one-expression-per-line */
import React, { useState, useEffect } from "react";
import axios from "axios";
import { MdCancel } from "react-icons/md";
import { Loader, Alert } from "shared-components";
import { errorRedirect } from "../../../modules/helpers/errorRedirect";
import {
  getJWTData,
  getIdentityProviderAlias,
} from "../../../modules/helpers/authentication-helper/authenticationHelper";
import { getBCSCUserInfo } from "../../../domain/authentication/AuthenticationService";
import PackageConfirmation from "../../../domain/package/package-confirmation/PackageConfirmation";
import CSOAccount from "../cso-account/CSOAccount";

import "../page.css";
import { IDP_BCEID, IDP_BCSC } from "../../../EnvConfig";

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

const setRequestHeaders = (transactionId) => {
  // Use interceptor to inject the transactionId to all requests
  axios.interceptors.request.use((request) => {
    const token = localStorage.getItem("jwt");
    request.headers["X-Transaction-Id"] = transactionId;
    request.headers.Authorization = `Bearer ${token}`;
    return request;
  });
};

export const saveUrlsToSessionStorage = ({ cancel, success, error }) => {
  sessionStorage.setItem("cancelUrl", cancel);
  sessionStorage.setItem("successUrl", success);
  sessionStorage.setItem("errorUrl", error);
};

const addUserInfo = (firstName, middleName, lastName) => {
  const { preferred_username, email } = getJWTData();
  let username = preferred_username;
  username = username.substring(0, username.indexOf("@"));

  return {
    bceid: username,
    email,
    firstName,
    middleName,
    lastName,
  };
};

const setRequiredState = (
  firstName,
  middleName,
  lastName,
  setApplicantInfo,
  setShowLoader
) => {
  const applicantInfo = addUserInfo(firstName, middleName, lastName);

  setApplicantInfo(applicantInfo);
  setShowLoader(false);
};

// make call to submission/{id} to get the user and navigation details
const checkCSOAccountStatus = (
  submissionId,
  setCsoAccountStatus,
  setShowLoader,
  setApplicantInfo,
  setClientApplicationName,
  setError
) => {
  if (!submissionId) {
    setShowLoader(false);
    return;
  }
  axios
    .get(`/submission/${submissionId}/config`)
    .then(({ data: { navigationUrls, clientAppName } }) => {
      setClientApplicationName(clientAppName);
      saveUrlsToSessionStorage(navigationUrls);

      axios
        .get("/csoAccount")
        .then(({ data: { clientId, internalClientNumber } }) => {
          sessionStorage.setItem("paymentProfileId", internalClientNumber); // Worldline (formerly Bambora) now calls this paymentProfileId (formerly internalClientNumber)
          sessionStorage.setItem("csoAccountId", clientId);
          setCsoAccountStatus({ isNew: false, exists: true });
        })
        .catch((err) => {
          if (err.response === undefined || err.response.status !== 404) {
            errorRedirect(sessionStorage.getItem("errorUrl"), err);
          } else if (getIdentityProviderAlias() === IDP_BCEID) {
            axios
              .get("/bceidAccount")
              .then(({ data: { firstName, middleName, lastName } }) => {
                setRequiredState(
                  firstName,
                  middleName,
                  lastName,
                  setApplicantInfo,
                  setShowLoader
                );
              })
              .catch((e) =>
                errorRedirect(sessionStorage.getItem("errorUrl"), e)
              );
          } else if (getIdentityProviderAlias() === IDP_BCSC) {
            getBCSCUserInfo()
              .then((userInfo) => {
                setRequiredState(
                  userInfo.givenNames,
                  "",
                  userInfo.lastName,
                  setApplicantInfo,
                  setShowLoader
                );
              })
              .catch((e) => {
                errorRedirect(sessionStorage.getItem("errorUrl"), e);
              });
          } else {
            errorRedirect(sessionStorage.getItem("errorUrl"), {
              status: 401,
              message: "Not authorized.",
            });
          }
        });
    })
    .catch((error) => {
      errorRedirect(sessionStorage.getItem("errorUrl"), error);
      setError(true);
    })
    .finally(() => {
      setShowLoader(false);
    });
};

export default function Home() {
  const [showLoader, setShowLoader] = useState(true);
  const [csoAccountStatus, setCsoAccountStatus] = useState({
    exists: false,
    isNew: false,
  });
  const [applicantInfo, setApplicantInfo] = useState({});
  const [error, setError] = useState(false);
  const [clientApplicationName, setClientApplicationName] = useState("");

  const submissionId = sessionStorage.getItem("submissionId");
  const transactionId = sessionStorage.getItem("transactionId");

  const [show, setShow] = useState(false);
  const handleClose = () => setShow(false);
  const handleShow = () => setShow(true);

  const handleConfirm = () => {
    sessionStorage.setItem("validExit", true);
    sessionStorage.removeItem("isBamboraRedirect");
    const cancelUrl = sessionStorage.getItem("cancelUrl");

    if (cancelUrl && cancelUrl !== "undefined") {
      axios
        .delete(`submission/${sessionStorage.getItem("submissionId")}`)
        .then(() => window.open(cancelUrl, "_self"))
        .catch(() => window.open(cancelUrl, "_self"));
    }
  };

  sessionStorage.setItem("validRushExit", false);

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

  setRequestHeaders(transactionId);

  useEffect(() => {
    checkCSOAccountStatus(
      submissionId,
      setCsoAccountStatus,
      setShowLoader,
      setApplicantInfo,
      setClientApplicationName,
      setError
    );
  }, [submissionId]);

  const body = () => (
    <>
      <p>Your files will not be submitted.</p>
      <p>
        You will be returned to:
        <br />
        <b>{clientApplicationName}</b> website
      </p>
    </>
  );
  const updatedModal = { ...confirmationPopup.modal, body };

  const packageConfirmation = {
    confirmationPopup: { ...confirmationPopup, modal: updatedModal },
    submissionId,
  };

  return (
    <>
      {showLoader && <Loader page />}
      {!submissionId && (
        <div className="page">
          <div className="content col-md-8">
            <Alert
              icon={<MdCancel size={32} />}
              type="error"
              styling="bcgov-error-background"
              element="No submission ID in session."
            />
          </div>
        </div>
      )}
      {!showLoader && error && (
        <div className="page">
          <div className="content col-md-8">
            <Alert
              icon={<MdCancel size={32} />}
              type="error"
              styling="bcgov-error-background"
              element="Authorized users only."
            />
          </div>
        </div>
      )}
      {!showLoader &&
        !error &&
        !csoAccountStatus.exists &&
        JSON.stringify(applicantInfo) !== "{}" && (
          <CSOAccount
            confirmationPopup={{ ...confirmationPopup, modal: updatedModal }}
            applicantInfo={applicantInfo}
            setCsoAccountStatus={setCsoAccountStatus}
          />
        )}
      {!showLoader && !error && csoAccountStatus.exists && (
        <PackageConfirmation
          packageConfirmation={packageConfirmation}
          csoAccountStatus={csoAccountStatus}
        />
      )}
    </>
  );
}
