/* eslint-disable camelcase */
import React, { useState, useEffect } from "react";
import PropTypes from "prop-types";
import queryString from "query-string";
import { useLocation } from "react-router-dom";
import axios from "axios";
import { MdCancel } from "react-icons/md";

import { Header, Footer, Loader, Alert } from "shared-components";
import { errorRedirect } from "../../../modules/errorRedirect";
import { getJWTData } from "../../../modules/authenticationHelper";
import PackageConfirmation from "../package-confirmation/PackageConfirmation";
import CSOAccount from "../cso-account/CSOAccount";
import { propTypes } from "../../../types/propTypes";

import "../page.css";

export const saveDataToSessionStorage = (
  { cancel, success, error },
  { universalId }
) => {
  if (cancel.url) sessionStorage.setItem("cancelUrl", cancel.url);
  if (success.url) sessionStorage.setItem("successUrl", success.url);
  if (error.url) sessionStorage.setItem("errorUrl", error.url);

  sessionStorage.setItem("universalId", universalId);
};

const addUserInfo = () => {
  const { preferred_username, name, email } = getJWTData();
  let username = preferred_username;
  username = username.substring(0, username.indexOf("@"));

  return {
    bceid: username,
    name,
    email,
  };
};

const setRequiredState = (setApplicantInfo, setShowLoader) => {
  const applicantInfo = addUserInfo();

  setApplicantInfo(applicantInfo);
  setShowLoader(false);
};

// make call to submission/{id} to get the user and navigation details
const checkCSOAccountStatus = (
  submissionId,
  temp,
  setCsoAccountStatus,
  setShowLoader,
  setApplicantInfo,
  setError
) => {
  axios
    .get(`/submission/${submissionId}`, {
      headers: {
        "X-Auth-UserId": temp,
      },
    })
    .then(({ data: { userDetails, navigation } }) => {
      saveDataToSessionStorage(navigation, userDetails);

      if (userDetails.accounts) {
        const csoAccountIdentifier = userDetails.accounts.find(
          (o) => o.type === "CSO"
        ).identifier;
        sessionStorage.setItem("csoAccountId", csoAccountIdentifier);
        setCsoAccountStatus({ isNew: false, exists: true });
      }

      setRequiredState(setApplicantInfo, setShowLoader);
    })
    .catch((error) => {
      errorRedirect(sessionStorage.getItem("errorUrl"), error);

      setError(true);
    })
    .finally(() => {
      setShowLoader(false);
    });
};

export default function Home({ page: { header, confirmationPopup } }) {
  const [showLoader, setShowLoader] = useState(true);
  const [csoAccountStatus, setCsoAccountStatus] = useState({
    exists: false,
    isNew: false,
  });
  const [applicantInfo, setApplicantInfo] = useState({});
  const [error, setError] = useState(false);
  const location = useLocation();
  const queryParams = queryString.parse(location.search);

  useEffect(() => {
    checkCSOAccountStatus(
      queryParams.submissionId,
      queryParams.temp,
      setCsoAccountStatus,
      setShowLoader,
      setApplicantInfo,
      setError
    );
  }, [queryParams.submissionId]);

  const packageConfirmation = {
    confirmationPopup,
    submissionId: queryParams.submissionId,
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
      {!showLoader && !error && !csoAccountStatus.exists && (
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
  }).isRequired,
};
