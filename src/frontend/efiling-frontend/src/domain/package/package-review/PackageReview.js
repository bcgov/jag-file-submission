/* eslint-disable react/jsx-one-expression-per-line */

import React, { useEffect, useState } from "react";
import moment from "moment";
import Tabs from "react-bootstrap/Tabs"; /* TODO: replace with shared-components */
import Tab from "react-bootstrap/Tab"; /* TODO: replace with shared-components */
import { Alert, Button, Sidecard, Table } from "shared-components";
import { BsEyeFill } from "react-icons/bs";
import { MdCancel } from "react-icons/md";
import { useParams } from "react-router-dom";
import { errorRedirect } from "../../../modules/helpers/errorRedirect";
import { getSidecardData } from "../../../modules/helpers/sidecardData";
import {
  getFilingPackage,
  downloadSubmissionSheet,
} from "./PackageReviewService";
import { noop } from "../../../modules/helpers/mockHelper";

import "./PackageReview.scss";
import DocumentList from "./DocumentList";
import PartyList from "./PartyList";
import PaymentList from "./PaymentList";
import { isClick, isEnter } from "../../../modules/helpers/eventUtil";

export default function PackageReview() {
  const params = useParams();
  const packageId = Number(params.packageId);
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
  const [reloadTrigger, setReloadTrigger] = useState(false);
  const [documents, setDocuments] = useState([]);
  const [parties, setParties] = useState([]);
  const [payments, setPayments] = useState([]);
  const [submissionHistoryLink, setSubmissionHistoryLink] = useState("");
  const aboutCsoSidecard = getSidecardData().aboutCso;
  const csoAccountDetailsSidecard = getSidecardData().csoAccountDetails;

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
          const fileNumber =
            response.data.court.fileNumber || "Not Assigned Yet";
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
          setParties(response.data.parties);
          setPayments(response.data.payments);
          setSubmissionHistoryLink(response.data.links.packageHistoryUrl);
        } catch (err) {
          setError(true);
        }
      })
      .catch(() => {
        setError(true);
      });
  }, [packageId, reloadTrigger]);

  /** Whenever this function is called, it'll trigger a reload of the document list. */
  function reloadDocumentList() {
    setReloadTrigger(!reloadTrigger);
    noop();
  }

  function handleClick() {
    downloadSubmissionSheet(packageId).catch((err) => {
      errorRedirect(sessionStorage.getItem("errorUrl"), err);
    });
  }

  function handleKeyDown(e) {
    if (e && e.keyCode === 13) {
      downloadSubmissionSheet(packageId).catch((err) => {
        errorRedirect(sessionStorage.getItem("errorUrl"), err);
      });
    }
  }

  function handleCsoLink(e) {
    if (submissionHistoryLink && (isClick(e) || isEnter(e))) {
      window.open(submissionHistoryLink, "_self");
    }
  }

  return (
    <>
      <div className="ct-package-review page">
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
          <div className="row" data-test-id="detailsTable">
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
              <DocumentList
                packageId={packageId}
                documents={documents}
                reloadDocumentList={reloadDocumentList}
              />
            </Tab>
            <Tab eventKey="parties" title="Parties">
              <br />
              <PartyList parties={parties} />
            </Tab>
            <Tab eventKey="payment" title="Payment Status">
              <br />
              <PaymentList payments={payments} packageId={packageId} />
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
          <b>Please Note: </b> Visit your CSO account to{" "}
          <span
            role="button"
            data-testid="cso-link"
            tabIndex={0}
            className="file-href"
            onClick={handleCsoLink}
            onKeyDown={handleCsoLink}
          >
            view all your previously submitted packages.
          </span>
          <section className="buttons pt-2">
            <Button
              label="Cancel and Return to Parent App"
              onClick={() => window.open("http://google.com", "_self")}
              styling="normal-white btn"
            />
          </section>
        </div>
        <div className="sidecard">
          <Sidecard sideCard={csoAccountDetailsSidecard} />
          <Sidecard sideCard={aboutCsoSidecard} />
        </div>
      </div>
    </>
  );
}
