import React, { useState, useEffect } from "react";
import PropTypes from "prop-types";
import queryString from "query-string";
import { useLocation } from "react-router-dom";
import axios from "axios";
import { MdCancel } from "react-icons/md";

import { Header, Footer, Loader, Alert } from "shared-components";
import { errorRedirect } from "../../../modules/errorRedirect";
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

const addUserInfo = ({ bceid, firstName, middleName, lastName, email }) => {
  return {
    bceid,
    firstName,
    middleName,
    lastName,
    email
  };
};

const setRequiredState = (userDetails, setApplicantInfo, setShowLoader) => {
  const applicantInfo = addUserInfo(userDetails);

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
        "X-Auth-UserId": temp
      }
    })
    .then(({ data: { userDetails, navigation } }) => {
      saveDataToSessionStorage(navigation, userDetails);

      if (userDetails.accounts) {
        const csoAccount = userDetails.accounts.find(o => o.type === "CSO");

        if (csoAccount.identifier) {
          sessionStorage.setItem("csoAccountId", csoAccount.identifier);
          setCsoAccountStatus({ isNew: false, exists: true });
        }
      }

      setRequiredState(userDetails, setApplicantInfo, setShowLoader);
    })
    .catch(error => {
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
    isNew: false
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
    submissionId: queryParams.submissionId
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
    confirmationPopup: propTypes.confirmationPopup
  }).isRequired
};
