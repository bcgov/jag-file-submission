import React, { useState, useEffect } from "react";
import PropTypes from "prop-types";
import queryString from "query-string";
import { useLocation } from "react-router-dom";
import axios from "axios";
import { Header, Footer, Loader } from "shared-components";
import CSOStatus from "../../composite/cso-status/CSOStatus";
import CSOAccount from "../cso-account/CSOAccount";
import { propTypes } from "../../../types/propTypes";

import "../page.css";

export const saveNavigationToSession = ({ cancel, success, error }) => {
  if (cancel.url) sessionStorage.setItem("cancelUrl", cancel.url);
  if (success.url) sessionStorage.setItem("successUrl", success.url);
  if (error.url) sessionStorage.setItem("errorUrl", error.url);
};

const addUserInfo = (bceid, firstName, middleName, lastName, email) => {
  return {
    bceID: bceid,
    firstName,
    middleName,
    lastName,
    emailAddress: email
  };
};

// make call to submission/{id} to get the user and navigation details
const checkCSOAccountStatus = (
  submissionId,
  setCsoAccountExists,
  setShowLoader,
  setApplicantInfo
) => {
  axios
    .get(`/submission/${submissionId}`)
    .then(({ data: { userDetails, navigation } }) => {
      saveNavigationToSession(navigation);

      if (userDetails.accounts) {
        const csoAccountExists = userDetails.accounts.some(
          element => element.type === "CSO"
        );
        if (csoAccountExists) {
          setCsoAccountExists(true);
        }
      }

      const applicantInfo = addUserInfo(
        userDetails.bceid,
        userDetails.firstName,
        userDetails.middleName,
        userDetails.lastName,
        userDetails.email
      );

      setApplicantInfo(applicantInfo);

      setShowLoader(false);
    })
    .catch(() => {});
};

export default function Home({ page: { header, confirmationPopup } }) {
  const [showLoader, setShowLoader] = useState(true);
  const [csoAccountExists, setCsoAccountExists] = useState(false);
  const [applicantInfo, setApplicantInfo] = useState({});
  const location = useLocation();
  const queryParams = queryString.parse(location.search);

  useEffect(() => {
    checkCSOAccountStatus(
      queryParams.submissionId,
      setCsoAccountExists,
      setShowLoader,
      setApplicantInfo
    );
  }, [queryParams.submissionId]);

  const csoStatus = {
    accountExists: csoAccountExists,
    confirmationPopup
  };

  return (
    <main>
      <Header header={header} />
      <div className="page">
        <div className="content col-md-12">
          {showLoader && <Loader page />}
          {!showLoader && !csoAccountExists && (
            <CSOAccount
              confirmationPopup={confirmationPopup}
              applicantInfo={applicantInfo}
            />
          )}
          {!showLoader && csoAccountExists && (
            <CSOStatus csoStatus={csoStatus} />
          )}
        </div>
      </div>
      <Footer />
    </main>
  );
}

Home.propTypes = {
  page: PropTypes.shape({
    header: propTypes.header,
    confirmationPopup: propTypes.confirmationPopup,
    applicantInfo: propTypes.applicantInfo
  }).isRequired
};
