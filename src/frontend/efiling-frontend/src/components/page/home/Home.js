import React, { useState, useEffect } from "react";
import PropTypes from "prop-types";
import queryString from "query-string";
import { useLocation } from "react-router-dom";
import axios from "axios";
import { Header, Footer, Loader } from "shared-components";
import PackageConfirmation from "../package-confirmation/PackageConfirmation";
import CSOAccount from "../cso-account/CSOAccount";
import { propTypes } from "../../../types/propTypes";

import "../page.css";

export const saveNavigationToSession = ({ cancel, success, error }) => {
  if (cancel.url) sessionStorage.setItem("cancelUrl", cancel.url);
  if (success.url) sessionStorage.setItem("successUrl", success.url);
  if (error.url) sessionStorage.setItem("errorUrl", error.url);
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

// make call to submission/{id} to get the user and navigation details
const checkCSOAccountStatus = (
  submissionId,
  setCsoAccountStatus,
  setShowLoader,
  setApplicantInfo
) => {
  axios
    .get(`/submission/${submissionId}`)
    .then(({ data: { userDetails, navigation } }) => {
      saveNavigationToSession(navigation);
      sessionStorage.setItem("universalId", userDetails.universalId);

      if (userDetails.accounts) {
        const csoAccountExists = userDetails.accounts.some(
          element => element.type === "CSO"
        );
        if (csoAccountExists)
          setCsoAccountStatus({ isNew: false, exists: true });
      }

      const applicantInfo = addUserInfo(userDetails);

      setApplicantInfo(applicantInfo);

      setShowLoader(false);
    })
    .catch(error => {
      const errorUrl = sessionStorage.getItem("errorUrl");
      window.open(
        `${errorUrl}?status=${error.response.status}&message=${error.response.data.message}`,
        "_self"
      );
    });
};

export default function Home({ page: { header, confirmationPopup } }) {
  const [showLoader, setShowLoader] = useState(true);
  const [csoAccountStatus, setCsoAccountStatus] = useState({
    exists: false,
    isNew: false
  });
  const [applicantInfo, setApplicantInfo] = useState({});
  const location = useLocation();
  const queryParams = queryString.parse(location.search);

  useEffect(() => {
    checkCSOAccountStatus(
      queryParams.submissionId,
      setCsoAccountStatus,
      setShowLoader,
      setApplicantInfo
    );
  }, [queryParams.submissionId]);

  const packageConfirmation = {
    confirmationPopup
  };

  return (
    <main>
      <Header header={header} />
      {showLoader && <Loader page />}
      {!showLoader && !csoAccountStatus.exists && (
        <CSOAccount
          confirmationPopup={confirmationPopup}
          applicantInfo={applicantInfo}
          setCsoAccountStatus={setCsoAccountStatus}
        />
      )}
      {!showLoader && csoAccountStatus.exists && (
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
