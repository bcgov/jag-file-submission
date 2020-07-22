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

const generateCourtDataTable = () => {
  const courtData = [
    {
      name: "Court File Number:",
      value: "Valkjsdjks",
      isValueBold: true
    },
    {
      name: "Location:",
      value: "Valkjsdjks",
      isValueBold: true
    },
    {
      name: "Level and Class:",
      value: "Valkjsdjks",
      isValueBold: true
    }
  ];

  return [
    {
      name: (
        <div style={{ width: "40%", minWidth: "fit-content" }}>
          <Table elements={courtData} />
        </div>
      ),
      value: "",
      isSideBySide: true
    }
  ];
};

// TODO: Extract to module to generate this
const feesData = [
  { name: "Some Fees:", value: "$100.00", isValueBold: true },
  { name: "Some More Fees:", value: "$10.00", isValueBold: true },
  { name: "", value: "", isEmptyRow: true },
  { name: "Total Fees:", value: "$110.00", isValueBold: true }
];

// TODO: refactor and simplify/reduce LOC
// Possibly extract responsibility of generating this to a module
const getTableElements = (paymentAgreed, setPaymentAgreed) => {
  const elements = [
    {
      name: (
        <div style={{ width: "80%" }}>
          <Table isFeesData elements={feesData} />
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
  payment: { confirmationPopup, submissionId }
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
          element={
            <Table
              elements={getTableElements(paymentAgreed, setPaymentAgreed)}
            />
          }
        />
        <br />
        <h1>Package Submission Details</h1>
        <p>Your package will be filed to:</p>
        <Table elements={generateCourtDataTable()} />
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
    submissionId: PropTypes.string.isRequired
  }).isRequired
};
