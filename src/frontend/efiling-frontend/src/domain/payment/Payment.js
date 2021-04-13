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
import { Toast } from "../../components/toast/Toast";
import Rush from "../../components/page/rush/Rush";
import { getSidecardData } from "../../modules/helpers/sidecardData";
import { getCreditCardAlerts } from "../../modules/helpers/creditCardAlerts";
import { errorRedirect } from "../../modules/helpers/errorRedirect";
import { getJWTData } from "../../modules/helpers/authentication-helper/authenticationHelper";
import { propTypes } from "../../types/propTypes";
import PackageConfirmation from "../package/package-confirmation/PackageConfirmation";
import { generateFileSummaryData } from "../../modules/helpers/generateFileSummaryData";

import "./Payment.scss";

const baseCalloutText = `I have reviewed the information and the documents in this filing
package and am prepared to submit them for filing.`;

const getCreditCardType = () => {
  const bamboraSuccess =
    sessionStorage.getItem("internalClientNumber") !== "null" ||
    sessionStorage.getItem("bamboraSuccess");
  if (bamboraSuccess && sessionStorage.getItem("bamboraErrorExists") !== "true")
    return getCreditCardAlerts().existingCreditCard;

  if (bamboraSuccess && sessionStorage.getItem("bamboraErrorExists") === "true")
    return getCreditCardAlerts().failedUpdateCreditCard;

  return getCreditCardAlerts().noCreditCard;
};

const updateCSOAccount = () => {
  const data = {
    internalClientNumber: sessionStorage.getItem("bamboraSuccess"),
  };

  axios
    .put("/csoAccount", data)
    .then(({ data: { internalClientNumber } }) =>
      sessionStorage.setItem("internalClientNumber", internalClientNumber)
    )
    .catch((err) => {
      sessionStorage.setItem("bamboraErrorExists", true);
      errorRedirect(sessionStorage.getItem("errorUrl"), err);
    });
};

const generateCourtDataTable = ({
  fileNumber,
  locationDescription,
  levelDescription,
  classDescription,
}) => [
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

const submitPackage = (
  submissionId,
  setSubmitBtnEnabled,
  setShowLoader,
  setShowToast
) => {
  setShowLoader(true);
  setSubmitBtnEnabled(false);

  axios
    .post(`/submission/${submissionId}/submit`, {})
    .then(({ data: { packageRef } }) => {
      const redirectUrl = `${sessionStorage.getItem(
        "successUrl"
      )}?packageRef=${packageRef}`;

      sessionStorage.setItem("validExit", true);
      window.open(redirectUrl, "_self");
    })
    .catch(() => setShowToast(true));
};

const hasSubmissionFee = (submissionFee) => submissionFee !== 0;

const checkSubmitEnabled = (
  paymentAgreed,
  setSubmitBtnEnabled,
  submissionFee
) => {
  const isEnabled =
    (paymentAgreed &&
      sessionStorage.getItem("internalClientNumber") !== "null") ||
    (paymentAgreed && !hasSubmissionFee(submissionFee));
  setSubmitBtnEnabled(isEnabled);
};

export default function Payment({
  payment: { confirmationPopup, submissionId, courtData, files, submissionFee },
}) {
  const rushFlagExists = getJWTData().realm_access.roles.includes("rush_flag");
  const creditCardAlert = getCreditCardType();

  const [paymentAgreed, setPaymentAgreed] = useState(false);
  const [submitBtnEnabled, setSubmitBtnEnabled] = useState(false);
  const [showPackageConfirmation, setShowPackageConfirmation] = useState(false);
  const [showRush, setShowRush] = useState(false);
  const [showLoader, setShowLoader] = useState(false);
  const [showToast, setShowToast] = useState(false);

  const aboutCsoSidecard = getSidecardData().aboutCso;
  const csoAccountDetailsSidecard = getSidecardData().csoAccountDetails;
  const rushSubmissionSidecard = getSidecardData(() => setShowRush(true))
    .rushSubmission;

  const fileSummary = generateFileSummaryData(files, submissionFee, true);
  const calloutText =
    fileSummary.totalFee > 0
      ? `${baseCalloutText} I agree that all fees for this filing package may be charged to the credit card registered to my account. Statutory fees will be processed when documents are filed.`
      : baseCalloutText;

  const paymentSectionElement = (
    <>
      <h1>Payment</h1>
      {creditCardAlert}
      <br />
      <div className="half-width">
        <Table isFeesData elements={fileSummary.data} />
      </div>
    </>
  );

  useEffect(() => {
    if (
      sessionStorage.getItem("bamboraSuccess") &&
      sessionStorage.getItem("internalClientNumber") === "null"
    )
      updateCSOAccount();

    sessionStorage.setItem("currentPage", "payment");
    window.history.pushState(null, null, window.location.href);
    window.scrollTo(0, 0);
  }, []);

  useEffect(() => {
    checkSubmitEnabled(paymentAgreed, setSubmitBtnEnabled, submissionFee);
  }, [paymentAgreed, submissionFee]);

  if (showPackageConfirmation) {
    return (
      <PackageConfirmation
        packageConfirmation={{ confirmationPopup, submissionId }}
        csoAccountStatus={{ isNew: false }}
      />
    );
  }

  if (showRush) {
    return (
      <Rush
        payment={{
          confirmationPopup,
          submissionId,
          courtData,
          files,
          submissionFee,
        }}
      />
    );
  }

  return (
    <div className="ct-payment page">
      <div className="content col-md-8">
        {hasSubmissionFee(submissionFee) && paymentSectionElement}
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
        {showToast && (
          <Toast
            content="Something went wrong while trying to submit your package."
            setShow={setShowToast}
          />
        )}
        <section className="pt-2 buttons">
          <Button
            label="< Back"
            onClick={() => {
              sessionStorage.removeItem("isBamboraRedirect");
              setShowPackageConfirmation(true);
            }}
            styling="bcgov-normal-white btn"
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
              onClick={() => {
                submitPackage(
                  submissionId,
                  setSubmitBtnEnabled,
                  setShowLoader,
                  setShowToast
                );
              }}
              styling="bcgov-normal-blue normal-blue-ml btn"
              disabled={!submitBtnEnabled}
              hasLoader={showLoader}
            />
          </div>
        </section>
      </div>
      <div className="sidecard">
        {rushFlagExists && <Sidecard sideCard={rushSubmissionSidecard} />}
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
