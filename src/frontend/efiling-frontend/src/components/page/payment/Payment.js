/* eslint-disable react/jsx-wrap-multilines */
import React, { useState } from "react";
import PropTypes from "prop-types";
import ConfirmationPopup, {
  Button,
  Sidecard,
  Table,
  Callout,
} from "shared-components";
import { getSidecardData } from "../../../modules/sidecardData";
import { getCreditCardAlerts } from "../../../modules/creditCardAlerts";
import { propTypes } from "../../../types/propTypes";
import PackageConfirmation from "../package-confirmation/PackageConfirmation";
import { generateFileSummaryData } from "../../../modules/generateFileSummaryData";

import "./Payment.css";

const generateCourtDataTable = ({
  fileNumber,
  locationDescription,
  levelDescription,
  classDescription,
}) => {
  return [
    {
      name: "Court File Number:",
      value: fileNumber,
      isValueBold: true,
    },
    {
      name: "Location:",
      value: locationDescription,
      isValueBold: true,
    },
    {
      name: "Level and Class:",
      value: `${levelDescription} ${classDescription}`,
      isValueBold: true,
    },
  ];
};

const calloutText = `I have reviewed the information and the documents in this filing
package and am prepared to submit them for filing. I agree that all
fees for this filing package may be charged to the credit card
registered to my account. Statutory fees will be processed when documents are filed.`;

const submitButton = {
  label: "Submit",
  onClick: () => console.log("submit click"),
  styling: "normal-blue btn",
};

export default function Payment({
  payment: { confirmationPopup, submissionId, courtData, files, submissionFee },
}) {
  const aboutCsoSidecard = getSidecardData().aboutCso;
  const csoAccountDetailsSidecard = getSidecardData().csoAccountDetails;
  const rushSubmissionSidecard = getSidecardData().rushSubmission;
  const existingCreditCardAlert = getCreditCardAlerts().existingCreditCard;
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
        {existingCreditCardAlert}
        <br />
        <div className="half-width">
          <Table
            isFeesData
            elements={generateFileSummaryData(files, submissionFee, true)}
          />
        </div>
        <br />
        <h1>Package Submission Details</h1>
        <p>Your package will be filed to:</p>
        <div className="court-half-width">
          <Table elements={generateCourtDataTable(courtData)} />
        </div>
        <Callout
          text={calloutText}
          checkboxLabel="I agree"
          agreeCallout={() => setPaymentAgreed(!paymentAgreed)}
        />
        <br />
        <section className="inline-block pt-2 two-buttons">
          <Button
            label="< Back"
            onClick={() => setShowPackageConfirmation(true)}
            styling="normal-white btn"
          />
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
    submissionFee: PropTypes.number.isRequired,
  }).isRequired,
};
