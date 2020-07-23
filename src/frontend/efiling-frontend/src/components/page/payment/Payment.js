/* eslint-disable react/jsx-wrap-multilines */
import React, { useState } from "react";
import PropTypes from "prop-types";
import { MdCreditCard } from "react-icons/md";
import ConfirmationPopup, {
  Button,
  Sidecard,
  Alert,
  DisplayBox,
  Table,
  Callout
} from "shared-components";
import { getSidecardData } from "../../../modules/sidecardData";
import { propTypes } from "../../../types/propTypes";
import PackageConfirmation from "../package-confirmation/PackageConfirmation";
import { generateFileSummaryData } from "../../../modules/generateFileSummaryData";

const generateCourtDataTable = ({
  fileNumber,
  locationDescription,
  levelDescription,
  classDescription
}) => {
  const courtElements = [
    {
      name: "Court File Number:",
      value: fileNumber,
      isValueBold: true
    },
    {
      name: "Location:",
      value: locationDescription,
      isValueBold: true
    },
    {
      name: "Level and Class:",
      value: `${levelDescription} ${classDescription}`,
      isValueBold: true
    }
  ];

  return [
    {
      name: (
        <div className="fit-table">
          <Table elements={courtElements} />
        </div>
      ),
      value: "",
      isSideBySide: true
    }
  ];
};

const getTableElements = (files, submissionFee) => {
  const fileSummary = generateFileSummaryData(files, submissionFee, true);

  const elements = [
    {
      name: (
        <div style={{ width: "60%", minWidth: "fit-content" }}>
          <Table isFeesData elements={fileSummary} />
        </div>
      ),
      value: (
        <p>
          The registry will process statutory fees when your documents are
          filed.
        </p>
      ),
      isSideBySide: true
    }
  ];

  return elements;
};

const calloutText = `I have reviewed the information and the documents in this filing
package and am prepared to submit them for filing. I agree that all
fees for this filing package may be charged to the credit card
registered to my account.`;

const submitButton = {
  label: "Submit",
  onClick: () => console.log("submit click"),
  styling: "normal-blue btn"
};

export default function Payment({
  payment: { confirmationPopup, submissionId, courtData, files, submissionFee }
}) {
  const aboutCsoSidecard = getSidecardData().aboutCso;
  const csoAccountDetailsSidecard = getSidecardData().csoAccountDetails;
  const rushSubmissionSidecard = getSidecardData().rushSubmission;
  const [paymentAgreed, setPaymentAgreed] = useState(false);
  const [showPackageConfirmation, setShowPackageConfirmation] = useState(false);

  if (showPackageConfirmation) {
    return (
      <PackageConfirmation
        packageConfirmation={{ confirmationPopup, submissionId }}
        csoAccountStatus={{ isNew: false }}
      />
    );
  }

  return (
    <div className="page">
      <div className="content col-md-8">
        <h1>Payment</h1>
        {/* TODO: Fix credit card info and link to register card */}
        <Alert
          icon={<MdCreditCard size={32} />}
          type="warning"
          styling="warning-background no-padding-bottom"
          element={
            <p>
              Your credit card, BLAH, will be charged.&nbsp;
              <a
                href="https://justice.gov.bc.ca/cso/about/index.do"
                target="_blank"
                rel="noopener noreferrer"
              >
                Register a new Credit Card
              </a>
              .
            </p>
          }
        />
        <br />
        <DisplayBox
          styling="display-left-element"
          element={<Table elements={getTableElements(files, submissionFee)} />}
        />
        <br />
        <h1>Package Submission Details</h1>
        <p>Your package will be filed to:</p>
        <Table elements={generateCourtDataTable(courtData)} />
        <Callout
          text={calloutText}
          checkboxLabel="I agree"
          agreeCallout={() => setPaymentAgreed(!paymentAgreed)}
        />
        <br />
        <section className="inline-block pt-2">
          <Button
            label="< Back"
            onClick={() => setShowPackageConfirmation(true)}
            styling="normal-white btn"
          />
        </section>
        <section className="buttons pt-2">
          <ConfirmationPopup
            modal={confirmationPopup.modal}
            mainButton={confirmationPopup.mainButton}
            confirmButton={confirmationPopup.confirmButton}
            cancelButton={confirmationPopup.cancelButton}
          />
          <Button
            label={submitButton.label}
            onClick={submitButton.onClick}
            styling={submitButton.styling}
            disabled={!paymentAgreed}
          />
        </section>
      </div>
      <div className="sidecard">
        <Sidecard sideCard={rushSubmissionSidecard} />
        <Sidecard sideCard={csoAccountDetailsSidecard} />
        <Sidecard sideCard={aboutCsoSidecard} />
      </div>
    </div>
  );
}

Payment.propTypes = {
  payment: PropTypes.shape({
    confirmationPopup: propTypes.confirmationPopup,
    submissionId: PropTypes.string.isRequired,
    courtData: PropTypes.object.isRequired,
    files: PropTypes.array.isRequired,
    submissionFee: PropTypes.number.isRequired
  }).isRequired
};
