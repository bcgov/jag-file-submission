/* eslint-disable react/require-default-props */
import React, { useState, useEffect } from "react";
import { MdPerson } from "react-icons/md";

import ConfirmationPopup, {
  TermsOfUse,
  Button,
  DisplayBox,
  Table,
  Sidecard
} from "shared-components";
import { getContent } from "../../../modules/csoAccountAgreementContent";
import { translateApplicantInfo } from "../../../modules/translateApplicantInfo";
import { propTypes } from "../../../types/propTypes";

const continueButton = {
  label: "Create CSO Account",
  styling: "normal-blue btn",
  onClick: () => {}
};

const sideCard = {
  heading: "About E-File Submission",
  content: [
    <p key="aboutCso">
      E-File submission is a service to help you securely and electronically
      file documents with the Government of British Columbia Court Services
      Online (CSO).&nbsp;
      <a
        href="https://justice.gov.bc.ca/cso/about/index.do"
        target="_blank"
        rel="noopener noreferrer"
      >
        Learn more about CSO
      </a>
      .
    </p>
  ],
  type: "bluegrey",
  isWide: true,
  icon: <MdPerson className="side-card-icon" />
};

const content = getContent();

export default function CSOAccount({ confirmationPopup, applicantInfo }) {
  const [termsAccepted, acceptTerms] = useState(false);
  const [continueBtnEnabled, setContinueBtnEnabled] = useState(false);

  useEffect(() => {
    if (termsAccepted) {
      setContinueBtnEnabled(true);
    } else {
      setContinueBtnEnabled(false);
    }
  }, [termsAccepted]);

  const icon = (
    <div style={{ color: "rgb(252,186,25)" }}>
      <MdPerson size={32} />
    </div>
  );
  const applicantTable = (
    <Table elements={translateApplicantInfo(applicantInfo)} />
  );

  return (
    <div className="page">
      <div className="content col-md-8">
        <div className="non-printable">
          <h2>Create a Court Services Online (CSO) Account</h2>
          <p>
            The following information will be used to create your CSO account.
          </p>
          <DisplayBox icon={icon} element={applicantTable} />
          <br />
        </div>

        <TermsOfUse
          acceptTerms={() => acceptTerms(!termsAccepted)}
          content={content}
          heading="Electronic Service Agreement"
          confirmText="I accept the Service Agreement"
        />

        <section className="non-printable buttons pt-4">
          <ConfirmationPopup
            modal={confirmationPopup.modal}
            mainButton={confirmationPopup.mainButton}
            confirmButton={confirmationPopup.confirmButton}
            cancelButton={confirmationPopup.cancelButton}
          />
          <Button
            label={continueButton.label}
            onClick={continueButton.onClick}
            styling={continueButton.styling}
            disabled={!continueBtnEnabled}
          />
        </section>
      </div>

      <div className="sidecard">
        <Sidecard sideCard={sideCard} />
      </div>
    </div>
  );
}

CSOAccount.propTypes = {
  confirmationPopup: propTypes.confirmationPopup,
  applicantInfo: propTypes.applicantInfo
};
