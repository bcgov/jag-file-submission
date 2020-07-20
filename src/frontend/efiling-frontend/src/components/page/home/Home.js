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

// make call to submission/{id} to get the user and navigation details
const checkCSOAccountStatus = (
  submissionId,
  temp,
  setCsoAccountStatus,
  setShowLoader,
  setApplicantInfo
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

      const applicantInfo = addUserInfo(userDetails);
      setApplicantInfo(applicantInfo);
      setShowLoader(false);
    })
    .catch(error => {
      window.open(
        `${sessionStorage.getItem("errorUrl")}?status=${
          error.response.status
        }&message=${error.response.data.message}`,
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
      queryParams.temp,
      setCsoAccountStatus,
      setShowLoader,
      setApplicantInfo
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
