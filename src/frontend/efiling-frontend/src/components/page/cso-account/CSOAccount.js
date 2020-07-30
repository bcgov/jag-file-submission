/* eslint-disable react/require-default-props */
import React, { useState, useEffect } from "react";
import { MdPerson, MdError } from "react-icons/md";
import axios from "axios";

import ConfirmationPopup, {
  TermsOfUse,
  Button,
  DisplayBox,
  Table,
  Alert,
  Sidecard,
} from "shared-components";
import { getContent } from "../../../modules/csoAccountAgreementContent";
import { getSidecardData } from "../../../modules/sidecardData";
import { translateApplicantInfo } from "../../../modules/translateApplicantInfo";
import { errorRedirect } from "../../../modules/errorRedirect";
import { getJWTData } from "../../../modules/authenticationHelper";
import { propTypes } from "../../../types/propTypes";

const content = getContent();

export default function CSOAccount({
  confirmationPopup,
  applicantInfo,
  setCsoAccountStatus,
}) {
  const sideCard = getSidecardData().aboutCso;
  const [termsAccepted, acceptTerms] = useState(false);
  const [continueBtnEnabled, setContinueBtnEnabled] = useState(false);

  useEffect(() => {
    if (termsAccepted) {
      setContinueBtnEnabled(true);
    } else {
      setContinueBtnEnabled(false);
    }
  }, [termsAccepted]);

  const createCSOAccount = (applicantDetails) => {
    axios
      .post("/csoAccount", applicantDetails, {
        headers: { "X-Auth-UserId": getJWTData()["universal-id"] },
      })
      .then(({ data: { accounts } }) => {
        accounts.forEach((account) => {
          if (account.type === "CSO") {
            sessionStorage.setItem("csoAccountId", account.identifier);
          }
        });
        setCsoAccountStatus({ exists: true, isNew: true });
      })
      .catch((error) => {
        errorRedirect(sessionStorage.getItem("errorUrl"), error);
      });
  };

  const continueButton = {
    label: "Create CSO Account",
    styling: "normal-blue btn",
    onClick: () => createCSOAccount(applicantInfo),
  };

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
          <h1>Create a Court Services Online (CSO) Account</h1>
          <Alert
            icon={<MdError size={32} />}
            type="warning"
            styling="warning-background"
            element="You need a CSO account to use E-File Submission. Create one now to continue."
          />
          <br />
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
            testId="create-cso-btn"
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
  applicantInfo: propTypes.applicantInfo,
  setCsoAccountStatus: propTypes.setState,
};
