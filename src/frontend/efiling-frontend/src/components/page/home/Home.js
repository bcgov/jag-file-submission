import React, { useState } from "react";
import PropTypes from "prop-types";
import queryString from "query-string";
import { useLocation } from "react-router-dom";
import axios from "axios";
import Header, { Footer, Loader } from "shared-components";
import CSOStatus from "../../composite/csostatus/CSOStatus";

import "../page.css";

/*
make call to submission/{37102811-af30-48bb-bb48-f85359d9f6ab}/userDetails
*/
export const checkCSOAccountStatus = (
  submissionId,
  setCsoAccountExists,
  setShowLoader
) => {
  axios
    .get(`/submission/${submissionId}/userDetail`)
    .then(({ data: { csoAccountExists } }) => {
      if (csoAccountExists) setCsoAccountExists(true);
      setShowLoader(false);
    })
    .catch(() => {
      throw new Error(
        "An error occurred with getting user details. Please try again."
      );
    });
};

export default function Home({ page: { header } }) {
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
          {!showLoader && <CSOStatus accountExists={csoAccountExists} />}
        </div>
      </div>
      <Footer />
    </main>
  );
}

Home.propTypes = {
  page: PropTypes.shape({
    header: PropTypes.shape({
      name: PropTypes.string.isRequired
    }).isRequired
  }).isRequired
};
