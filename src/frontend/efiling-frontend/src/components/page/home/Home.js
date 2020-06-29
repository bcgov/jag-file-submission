import React, { useState } from "react";
import PropTypes from "prop-types";
import queryString from "query-string";
import { useLocation } from "react-router-dom";
import axios from "axios";
import { Header, Footer, Loader } from "shared-components";
import CSOStatus from "../../composite/csostatus/CSOStatus";

import "../page.css";

export const saveNavigationToSession = ({ cancel, success, error }) => {
  if (cancel.url) sessionStorage.setItem("cancelUrl", cancel.url);
  if (success.url) sessionStorage.setItem("successUrl", success.url);
  if (error.url) sessionStorage.setItem("errorUrl", error.url);
};

// make call to submission/{id} to get the user and navigation details
const checkCSOAccountStatus = (
  submissionId,
  setCsoAccountExists,
  setShowLoader
) => {
  axios
    .get(`/submission/${submissionId}`)
    .then(({ data: { userDetails, navigation } }) => {
      saveNavigationToSession(navigation);

      if (userDetails.accounts) {
        const csoAccountExists = userDetails.accounts.some(
          element => element.type === "CSO"
        );
        if (csoAccountExists) setCsoAccountExists(true);
      }
      setShowLoader(false);
    })
    .catch(() => {});
};

export default function Home({ page: { header, confirmationPopup } }) {
  const [showLoader, setShowLoader] = useState(true);
  const [csoAccountExists, setCsoAccountExists] = useState(false);
  const location = useLocation();
  const queryParams = queryString.parse(location.search);

  checkCSOAccountStatus(
    queryParams.submissionId,
    setCsoAccountExists,
    setShowLoader
  );

  return (
    <main>
      <Header header={header} />
      <div className="page">
        <div className="content col-md-10">
          {showLoader && <Loader page />}
          {!showLoader && (
            <CSOStatus
              accountExists={csoAccountExists}
              confirmationPopup={confirmationPopup}
            />
          )}
        </div>
      </div>
      <Footer />
    </main>
  );
}

Home.propTypes = {
  page: PropTypes.shape({
    header: PropTypes.shape({
      name: PropTypes.string.isRequired,
      history: PropTypes.object.isRequired
    }).isRequired,
    confirmationPopup: PropTypes.shape({
      modal: PropTypes.shape({
        show: PropTypes.bool.isRequired,
        handleShow: PropTypes.func.isRequired,
        handleClose: PropTypes.func.isRequired,
        handleConfirm: PropTypes.func.isRequired,
        title: PropTypes.string.isRequired,
        body: PropTypes.func.isRequired
      }).isRequired,
      mainButton: PropTypes.shape({
        mainLabel: PropTypes.string.isRequired,
        mainStyling: PropTypes.string.isRequired
      }).isRequired,
      confirmButton: PropTypes.shape({
        confirmLabel: PropTypes.string.isRequired,
        confirmStyling: PropTypes.string.isRequired
      }).isRequired,
      cancelButton: PropTypes.shape({
        cancelLabel: PropTypes.string.isRequired,
        cancelStyling: PropTypes.string.isRequired
      }).isRequired
    }).isRequired
  }).isRequired
};
