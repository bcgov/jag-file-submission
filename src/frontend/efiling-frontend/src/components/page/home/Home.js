/* eslint-disable camelcase */
import React, { useState, useEffect } from "react";
import PropTypes from "prop-types";
import axios from "axios";
import { MdCancel } from "react-icons/md";

import { Header, Footer, Loader, Alert } from "shared-components";
import { errorRedirect } from "../../../modules/helpers/errorRedirect";
import { getJWTData } from "../../../modules/helpers/authentication-helper/authenticationHelper";
import PackageConfirmation from "../package-confirmation/PackageConfirmation";
import CSOAccount from "../cso-account/CSOAccount";
import { propTypes } from "../../../types/propTypes";

import "../page.css";

const setRequestHeaders = (transactionId) => {
  // Use interceptor to inject the transactionId to all requests
  axios.interceptors.request.use((request) => {
    const token = localStorage.getItem("jwt");
    request.headers["X-Transaction-Id"] = transactionId;
    request.headers.Authorization = `Bearer ${token}`;
    return request;
  });
};

export const saveDataToSessionStorage = (
  cardRegistered,
  { cancel, success, error }
) => {
  if (cancel.url) sessionStorage.setItem("cancelUrl", cancel.url);
  if (success.url) sessionStorage.setItem("successUrl", success.url);
  if (error.url) sessionStorage.setItem("errorUrl", error.url);
  sessionStorage.setItem("cardRegistered", cardRegistered);
};

const addUserInfo = (firstName, middleName, lastName) => {
  const { preferred_username, email } = getJWTData();
  let username = preferred_username;
  username = username.substring(0, username.indexOf("@"));

  return { bceid: username, email, firstName, middleName, lastName };
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
  setError
) => {
  axios
    .get(`/submission/${submissionId}`)
    .then(({ data: { userDetails, navigation } }) => {
      saveDataToSessionStorage(userDetails.cardRegistered, navigation);

      if (userDetails.accounts) {
        const csoAccountIdentifier = userDetails.accounts.find(
          (o) => o.type === "CSO"
        ).identifier;
        sessionStorage.setItem("csoAccountId", csoAccountIdentifier);
        setCsoAccountStatus({ isNew: false, exists: true });
      } else {
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
          .catch((err) =>
            errorRedirect(sessionStorage.getItem("errorUrl"), err)
          );
      }
    })
    .catch((error) => {
      errorRedirect(sessionStorage.getItem("errorUrl"), error);
      setError(true);
    })
    .finally(() => {
      setShowLoader(false);
    });
};

export default function Home({
  page: { header, confirmationPopup, submissionId, transactionId },
}) {
  const [showLoader, setShowLoader] = useState(true);
  const [csoAccountStatus, setCsoAccountStatus] = useState({
    exists: false,
    isNew: false,
  });
  const [applicantInfo, setApplicantInfo] = useState({});
  const [error, setError] = useState(false);

  setRequestHeaders(transactionId);

  useEffect(() => {
    checkCSOAccountStatus(
      submissionId,
      setCsoAccountStatus,
      setShowLoader,
      setApplicantInfo,
      setError
    );
  }, [submissionId]);

  const packageConfirmation = {
    confirmationPopup,
    submissionId,
  };

  return (
    <main>
      <Header header={header} />
      {showLoader && <Loader page />}
      {!showLoader && error && (
        <div className="page">
          <div className="content col-md-8">
            <Alert
              icon={<MdCancel size={32} />}
              type="error"
              styling="error-background"
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
            confirmationPopup={confirmationPopup}
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
      <Footer />
    </main>
  );
}

Home.propTypes = {
  page: PropTypes.shape({
    header: propTypes.header,
    confirmationPopup: propTypes.confirmationPopup,
    submissionId: PropTypes.string.isRequired,
    transactionId: PropTypes.string.isRequired,
  }).isRequired,
};
