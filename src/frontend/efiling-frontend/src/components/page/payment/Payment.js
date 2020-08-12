/* eslint-disable react/jsx-wrap-multilines */
import React, { useState, useEffect } from "react";
import PropTypes from "prop-types";
import axios from "axios";
import ConfirmationPopup, {
  Button,
  Sidecard,
  Table,
  Callout,
} from "shared-components";
import { getSidecardData } from "../../../modules/helpers/sidecardData";
import { getCreditCardAlerts } from "../../../modules/helpers/creditCardAlerts";
import { errorRedirect } from "../../../modules/helpers/errorRedirect";
import { propTypes } from "../../../types/propTypes";
import PackageConfirmation from "../package-confirmation/PackageConfirmation";
import { generateFileSummaryData } from "../../../modules/helpers/generateFileSummaryData";

import "./Payment.css";

const calloutText = `I have reviewed the information and the documents in this filing
package and am prepared to submit them for filing. I agree that all
fees for this filing package may be charged to the credit card
registered to my account. Statutory fees will be processed when documents are filed.`;

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

const submitPackage = (submissionId) => {
  axios
    .post(`/submission/${submissionId}/submit`, {})
    .then(() => {
      sessionStorage.setItem("validExit", true);
      window.open(sessionStorage.getItem("successUrl"), "_self");
    })
    .catch((err) => errorRedirect(sessionStorage.getItem("errorUrl"), err));
};

const checkSubmitEnabled = (paymentAgreed, setSubmitBtnEnabled) => {
  if (paymentAgreed && sessionStorage.getItem("cardRegistered") === "true") {
    setSubmitBtnEnabled(true);
  } else if (!paymentAgreed) {
    setSubmitBtnEnabled(false);
  }
};

export default function Payment({
  payment: { confirmationPopup, submissionId, courtData, files, submissionFee },
}) {
  const aboutCsoSidecard = getSidecardData().aboutCso;
  const csoAccountDetailsSidecard = getSidecardData().csoAccountDetails;
  const rushSubmissionSidecard = getSidecardData().rushSubmission;
  const creditCardAlert =
    sessionStorage.getItem("cardRegistered") === "true"
      ? getCreditCardAlerts().existingCreditCard
      : getCreditCardAlerts().noCreditCard;
  const [paymentAgreed, setPaymentAgreed] = useState(false);
  const [submitBtnEnabled, setSubmitBtnEnabled] = useState(false);
  const [showPackageConfirmation, setShowPackageConfirmation] = useState(false);

  useEffect(() => {
    sessionStorage.setItem("currentPage", "payment");
    window.history.pushState(null, null, window.location.href);
  }, []);

  useEffect(() => {
    checkSubmitEnabled(paymentAgreed, setSubmitBtnEnabled);
  }, [paymentAgreed]);

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
        {creditCardAlert}
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
        <section className="inline-block pt-2 buttons">
          <Button
            label="< Back"
            onClick={() => setShowPackageConfirmation(true)}
            styling="normal-white btn"
          />
          <div className="button-container">
            <ConfirmationPopup
              modal={confirmationPopup.modal}
              mainButton={confirmationPopup.mainButton}
              confirmButton={confirmationPopup.confirmButton}
              cancelButton={confirmationPopup.cancelButton}
            />
            <Button
              label="Submit"
              onClick={() => submitPackage(submissionId)}
              styling="normal-blue btn"
              disabled={!submitBtnEnabled}
            />
          </div>
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
