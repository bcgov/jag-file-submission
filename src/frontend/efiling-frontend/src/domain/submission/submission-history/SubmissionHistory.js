/* eslint-disable react/function-component-definition, import/no-named-as-default, import/no-named-as-default-member, react/jsx-no-bind */
import React, { useState, useEffect } from "react";
import { Sidecard, Button } from "shared-components";
import { useLocation } from "react-router-dom";
import queryString from "query-string";
import { getSidecardData } from "../../../modules/helpers/sidecardData";
import SubmissionList from "./SubmissionList";
import SafefyCheck from "../../../components/safety-check/SafetyCheck";
import { getSubmissionHistory } from "./SubmissionHistoryService";
import { isParentAppFLA } from "../../../modules/helpers/authentication-helper/authenticationHelper";
import "./SubmissionHistory.scss";
import { Toast } from "../../../components/toast/Toast";

export default function SubmissionHistory() {
  const [showToast, setShowToast] = useState(false);
  const [submissions, setSubmissions] = useState([]);
  const [visibleSubmissions, setVisibleSubmissions] = useState([]);
  const [search, setSearch] = useState("");
  const aboutCsoSidecard = getSidecardData().aboutCso;
  const csoAccountDetailsSidecard = getSidecardData().csoAccountDetails;

  const location = useLocation();
  const queryParams = queryString.parse(location.search);
  const { applicationCode } = queryParams;

  useEffect(() => {
    getSubmissionHistory(applicationCode)
      .then((response) => {
        setSubmissions(response.data);
        setVisibleSubmissions(response.data);
      })
      .catch(() => setShowToast(true));
  }, [applicationCode]);

  function handlePackageSearch() {
    if (search !== "") {
      setVisibleSubmissions(
        submissions.filter((submission) =>
          String(submission.packageNumber).startsWith(String(search))
        )
      );
    } else {
      setVisibleSubmissions(submissions);
    }
  }

  const searchPackagesElement = (
    <>
      <h3>Search Packages</h3>
      <br />
      <span>Package Number</span>
      <br />
      <input
        data-testid="package-search"
        type="number"
        name="search"
        className="package-search"
        onChange={(e) => setSearch(e.target.value)}
      />
      <Button
        data-testid="btn-package-search"
        label="Search"
        styling="bcgov-normal-blue btn search-btn"
        onClick={handlePackageSearch}
      />
    </>
  );

  const submittedPackagesElement = (
    <div className="submission-list-block">
      <h3>Submitted Packages</h3>
      <div className="submission-list-container">
        <SubmissionList submissions={visibleSubmissions} />
      </div>
    </div>
  );

  return (
    <div className="ct-submission-history page">
      <div className="content col-md-8">
        <h1>Submission History</h1>
        <br />
        {searchPackagesElement}
        {showToast && (
          <Toast
            content="Something went wrong while trying to retrieve your submissions."
            setShow={setShowToast}
          />
        )}
        {submittedPackagesElement}
        <br />
        <p>
          The View Filing Packages screen lets you view any packages you filed,
          and any packages that you have been given access to by other people in
          your organization. Packages will be available in this list for 1 year
          after the registry processes them.
        </p>
        {isParentAppFLA() && <SafefyCheck />}
      </div>

      <div className="sidecard">
        <Sidecard sideCard={csoAccountDetailsSidecard} />
        <Sidecard sideCard={aboutCsoSidecard} />
      </div>
    </div>
  );
}
