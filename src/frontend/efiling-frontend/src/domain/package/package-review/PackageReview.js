/* eslint-disable react/jsx-one-expression-per-line */

import React, { useEffect, useState } from "react";
import moment from "moment";
import Tabs from "react-bootstrap/Tabs"; /* TODO: replace with shared-components */
import Tab from "react-bootstrap/Tab"; /* TODO: replace with shared-components */
import { Alert, Button, Sidecard, Table } from "shared-components";
import { BsEyeFill } from "react-icons/bs";
import { MdCancel, MdError } from "react-icons/md";
import { useLocation, useParams } from "react-router-dom";
import queryString from "query-string";
import validator from "validator";
import { getSidecardData } from "../../../modules/helpers/sidecardData";
import SafefyCheck from "../../../components/safety-check/SafetyCheck";
import {
  getFilingPackage,
  downloadSubmissionSheet,
  downloadRegistryNotice,
} from "./PackageReviewService";
import { noop } from "../../../modules/helpers/mockHelper";

import "./PackageReview.scss";
import DocumentList from "./DocumentList";
import PartyList from "./PartyList";
import PaymentList from "./PaymentList";
import { isClick, isEnter } from "../../../modules/helpers/eventUtil";
import { Toast } from "../../../components/toast/Toast";

export default function PackageReview() {
  const params = useParams();
  const packageId = Number(params.packageId);

  const location = useLocation();
  const queryParams = queryString.parse(location.search);
  const { returnUrl, returnAppName, defaultTab } = queryParams;
  const returnButtonName = `Return to ${returnAppName || "Parent App"}`;
  const defaultTabKey = defaultTab || "documents";

  const [error, setError] = useState(false);
  const [showToast, setShowToast] = useState(false);
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
  const [organizationParties, setOrganizationParties] = useState([]);
  const [payments, setPayments] = useState([]);
  const [submissionHistoryLink, setSubmissionHistoryLink] = useState("");
  const [hasRegistryNotice, setHasRegistryNotice] = useState(null);
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
          setOrganizationParties(response.data.organizationParties);
          setPayments(response.data.payments);
          setSubmissionHistoryLink(response.data.links.packageHistoryUrl);
          setHasRegistryNotice(response.data.hasRegistryNotice);
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

  function handleSubmissionSheet(e) {
    if (isEnter(e) || isClick(e)) {
      downloadSubmissionSheet(packageId).catch(() => {
        setShowToast(true);
      });
    }
  }

  function handleCsoLink(e) {
    if (submissionHistoryLink && (isClick(e) || isEnter(e))) {
      window.open(submissionHistoryLink, "_blank");
    }
  }

  function handleRegistryNotice(e) {
    if (isClick(e) || isEnter(e)) {
      downloadRegistryNotice(packageId).catch(() => {
        setShowToast(true);
      });
    }
  }

  const registryElement = (
    <p>
      You have a Registry Notice that requires an action. The item(s)
      highlighted in red below require an action.{" "}
      <span
        className="file-href"
        role="button"
        data-testid="btn-registry-notice"
        onClick={handleRegistryNotice}
        onKeyDown={handleRegistryNotice}
        tabIndex={0}
      >
        View Registry Notice
      </span>{" "}
      view all details.
    </p>
  );

  return (
    <>
      <div className="ct-package-review page">
        <div className="content col-md-8">
          <h1>View Recently Submitted Package # {packageId}</h1>
          {showToast && (
            <Toast
              content="Something went wrong while trying to download your document."
              setShow={setShowToast}
            />
          )}
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
                onClick={handleSubmissionSheet}
                onKeyDown={handleSubmissionSheet}
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
          {hasRegistryNotice && (
            <Alert
              icon={<MdError size={40} className="align-icon registry-align" />}
              type="error"
              styling="bcgov-error-background"
              element={registryElement}
            />
          )}
          <br />
          <Tabs defaultActiveKey={defaultTabKey} id="uncontrolled-tab">
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
              <PartyList
                parties={parties}
                organizationParties={organizationParties}
              />
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
          <div className="row note">
            <span className="fw-bold">Please Note: </span> Visit your CSO
            account to{" "}
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
          </div>
          {returnUrl && validator.isURL(returnUrl) && (
            <>
              <section className="buttons pt-2 row">
                <Button
                  label={returnButtonName}
                  onClick={() => window.open(returnUrl, "_self")}
                  styling="bcgov-normal-white btn"
                />
              </section>
            </>
          )}
          <SafefyCheck />
        </div>
        <div className="sidecard">
          <Sidecard sideCard={csoAccountDetailsSidecard} />
          <Sidecard sideCard={aboutCsoSidecard} />
        </div>
      </div>
    </>
  );
}
