/* eslint-disable react/jsx-wrap-multilines */
import React, { useState } from "react";
import PropTypes from "prop-types";
import { MdCreditCard } from "react-icons/md";
import ConfirmationPopup, {
  Button,
  Sidecard,
  Alert,
  DisplayBox,
  Table
} from "shared-components";
import { getSidecardData } from "../../../modules/sidecardData";
import { propTypes } from "../../../types/propTypes";

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
          <br />
          <br />
          <p>
            The registry will process statutory fees when your documents are
            filed.
          </p>
        </div>
      ),
      value: (
        <>
          <p>
            I have reviewed the information and the documents in this filing
            package and am prepared to submit them for filing. I agree that all
            fees for this filing package may be charged to the credit card
            registered to my account.
          </p>
          <label
            className="pt-3"
            style={{ float: "right" }}
            htmlFor="agreePayment"
          >
            <input
              id="agreePayment"
              type="checkbox"
              onClick={() => setPaymentAgreed(!paymentAgreed)}
            />
            &nbsp;
            <b>I agree</b>
            <span id="asterisk" className="mandatory">
              *
            </span>
          </label>
        </>
      ),
      isSideBySide: true
    }
  ];

  return elements;
};

const submitButton = {
  label: "Submit",
  onClick: () => console.log("submit click"),
  styling: "normal-blue btn"
};

const backButton = {
  label: "< Back",
  onClick: () => console.log("back click"),
  styling: "normal-white btn"
};

const aboutCsoSidecard = getSidecardData().aboutCso;
const csoAccountDetailsSidecard = getSidecardData().csoAccountDetails;
const rushSubmissionSidecard = getSidecardData().rushSubmission;

export default function Payment({ payment: { confirmationPopup } }) {
  const [paymentAgreed, setPaymentAgreed] = useState(false);

  return (
    <div className="page">
      <div className="content col-md-8">
        <h2>Payment</h2>

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

        <section className="inline-block pt-2">
          <Button
            label={backButton.label}
            onClick={backButton.onClick}
            styling={backButton.styling}
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
    confirmationPopup: propTypes.confirmationPopup
  }).isRequired
};
