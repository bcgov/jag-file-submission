/* eslint-disable react/jsx-one-expression-per-line */

import React, { useEffect, useState } from "react";
import moment from "moment";
import PropTypes from "prop-types";
import Tabs from "react-bootstrap/Tabs"; /* TODO: replace with shared-components */
import Tab from "react-bootstrap/Tab"; /* TODO: replace with shared-components */
import { Header, Footer, Button, Table, Alert } from "shared-components";
import { BsEyeFill } from "react-icons/bs";
import { MdCancel } from "react-icons/md";
import { propTypes } from "../../../types/propTypes";
import { errorRedirect } from "../../../modules/helpers/errorRedirect";
import { getFilingPackage, getSubmissionSheet } from "./PackageReviewService";

import "./PackageReview.css";
import DocumentList from "./DocumentList";

export default function PackageReview({ page: { header, packageId } }) {
  const [error, setError] = useState(false);
  const [packageDetails, setPackageDetails] = useState([
    { name: "Submitted By:", value: "", isNameBold: false, isValueBold: true },
    {
      name: "Submitted Date:",
      value: "",
      isNameBold: false,
      isValueBold: true,
    },
    { name: "Submitted To:", value: "", isNameBold: false, isValueBold: true },
  ]);
  const [courtFileDetails, setCourtFileDetails] = useState([
    {
      name: "Package Number:",
      value: "",
      isNameBold: false,
      isValueBold: true,
    },
    {
      name: "Court File Number:",
      value: "",
      isNameBold: false,
      isValueBold: true,
    },
  ]);
  const [filingComments, setFilingComments] = useState("");
  const [documents, setDocuments] = useState([]);

  useEffect(() => {
    getFilingPackage(packageId)
      .then((response) => {
        try {
          const packageNo = response.data.packageNumber || "";
          let submittedBy = "";
          if (response.data.submittedBy.firstName) {
            if (response.data.submittedBy.lastName) {
              submittedBy = `${response.data.submittedBy.firstName} ${response.data.submittedBy.lastName}`;
            }
          }
          let submittedDt = "";
          if (response.data.submittedDate) {
            submittedDt = moment(response.data.submittedDate).format(
              "DD-MMM-YYYY HH:mm"
            );
          }
          const fileNumber = response.data.court.fileNumber || "";
          const submittedTo = response.data.court.location || "";
          setPackageDetails([
            {
              name: "Submitted By:",
              value: submittedBy,
              isNameBold: false,
              isValueBold: true,
            },
            {
              name: "Submitted Date:",
              value: submittedDt,
              isNameBold: false,
              isValueBold: true,
            },
            {
              name: "Submitted To:",
              value: `${submittedTo}`,
              isNameBold: false,
              isValueBold: true,
            },
          ]);
          setCourtFileDetails([
            {
              name: "Package Number:",
              value: `${packageNo}`,
              isNameBold: false,
              isValueBold: true,
            },
            {
              name: "Court File Number:",
              value: `${fileNumber}`,
              isNameBold: false,
              isValueBold: true,
            },
          ]);
          setFilingComments(response.data.filingComments);
          setDocuments(response.data.documents);
        } catch (err) {
          setError(true);
        }
      })
      .catch(() => {
        setError(true);
      });
  }, [packageId]);

  function handleClick() {
    getSubmissionSheet(packageId).catch((err) => {
      errorRedirect(sessionStorage.getItem("errorUrl"), err);
    });
  }

  function handleKeyDown() {
    getSubmissionSheet(packageId).catch((err) => {
      errorRedirect(sessionStorage.getItem("errorUrl"), err);
    });
  }

  return (
    <main>
      <Header header={header} />
      <div className="page">
        <div className="content col-md-8">
          <h1>View Recently Submitted Package # {packageId}</h1>
          {error && (
            <div className="col-md-8">
              <Alert
                icon={<MdCancel size={24} />}
                type="error"
                styling="bcgov-error-background"
                element="There was a problem with your request."
              />
            </div>
          )}
          <br />
          <div className="row">
            <h2 className="col-sm-6">Package Details</h2>
            <div className="col-sm-6 text-sm-right mt-3 mt-sm-0">
              <span
                role="button"
                tabIndex={0}
                className="file-href"
                onClick={handleClick}
                onKeyDown={handleKeyDown}
              >
                Print Submission Sheet
              </span>
              <BsEyeFill size="24" color="#7F7F7F" className="align-icon" />
            </div>
          </div>
          <br />
          <div className="row">
            <div className="col-sm-12 col-lg-6">
              <Table id="packageDetails" elements={packageDetails} />
            </div>
            <div className="col-sm-12 col-lg-6">
              <Table elements={courtFileDetails} />
            </div>
          </div>
          <br />
          <Tabs defaultActiveKey="documents" id="uncontrolled-tab">
            <Tab eventKey="documents" title="Documents">
              <br />
              <DocumentList documents={documents} />
            </Tab>
            <Tab eventKey="comments" title="Filing Comments">
              <br />
              <h4>Filing Comments</h4>
              <div id="filingComments" className="tabContent">
                {filingComments}
              </div>
            </Tab>
          </Tabs>
          <br />
          <section className="buttons pt-2">
            <Button
              label="Cancel and Return to Parent App"
              onClick={() => window.open("http://google.com", "_self")}
              styling="normal-white btn"
            />
          </section>
        </div>
      </div>
      <Footer />
    </main>
  );
}

PackageReview.propTypes = {
  page: PropTypes.shape({
    header: propTypes.header,
    packageId: PropTypes.string.isRequired,
  }).isRequired,
};
