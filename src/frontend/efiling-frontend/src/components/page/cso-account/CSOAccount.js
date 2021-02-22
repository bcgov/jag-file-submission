/* eslint-disable react/require-default-props, jsx-a11y/label-has-associated-control */
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
import validator from "validator";
import { getContent } from "../../../modules/helpers/csoAccountAgreementContent";
import { getSidecardData } from "../../../modules/helpers/sidecardData";
import { translateApplicantInfo } from "../../../modules/helpers/translateApplicantInfo";
import { errorRedirect } from "../../../modules/helpers/errorRedirect";
import { propTypes } from "../../../types/propTypes";
import "./CSOAccount.scss";

const content = getContent();

export default function CSOAccount({
  confirmationPopup,
  applicantInfo,
  setCsoAccountStatus,
}) {
  const sideCard = getSidecardData().aboutCso;
  const [termsAccepted, acceptTerms] = useState(false);
  const [continueBtnEnabled, setContinueBtnEnabled] = useState(false);
  const [showLoader, setShowLoader] = useState(false);
  const [emailInput, setEmailInput] = useState({ email: "", confEmail: "" });
  const [emailInputErrors, setEmailInputErrors] = useState({
    emailError: "",
    confEmailError: "",
  });

  useEffect(() => {
    const emailErrors = () =>
      !(
        validator.isEmail(emailInput.email) &&
        emailInput.email === emailInput.confEmail
      );

    if (emailInput.email !== emailInput.confEmail && emailInput.confEmail) {
      setEmailInputErrors({
        ...emailInputErrors,
        confEmailError: "Email and confirmation email must match.",
      });
    } else {
      setEmailInputErrors({ ...emailInputErrors, confEmailError: "" });
    }

    if (termsAccepted && !emailErrors()) {
      setContinueBtnEnabled(true);
    } else {
      setContinueBtnEnabled(false);
    }
  }, [termsAccepted, emailInput.email, emailInput.confEmail]);

  const handleOnChange = (e) => {
    const input = e.target.value;

    setEmailInput({ ...emailInput, [e.target.name]: input });

    if (e.target.name === "email" && !validator.isEmail(input)) {
      setEmailInputErrors({
        ...emailInputErrors,
        emailError: "Must be a valid email.",
      });
    } else if (e.target.name === "email" && validator.isEmail(input)) {
      setEmailInputErrors({ ...emailInputErrors, emailError: "" });
    }
  };

  const createCSOAccount = ({
    firstName,
    lastName,
    email = emailInput.email,
  }) => {
    setShowLoader(true);
    setContinueBtnEnabled(false);

    const applicantDetails = { firstName, lastName, email };
    axios
      .post("/csoAccount", applicantDetails)
      .then(({ data: { clientId, internalClientNumber } }) => {
        sessionStorage.setItem("internalClientNumber", internalClientNumber);
        sessionStorage.setItem("csoAccountId", clientId);
        setCsoAccountStatus({ exists: true, isNew: true });
      })
      .catch((error) => {
        errorRedirect(sessionStorage.getItem("errorUrl"), error);
      });
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
      <div className="content col-md-8 ct-cso-account">
        <div className="non-printable">
          <h1>Create a Court Services Online (CSO) Account</h1>
          <Alert
            icon={<MdError size={32} />}
            type="warning"
            styling="bcgov-warning-background"
            element="You need a CSO account to use E-File Submission. Create one now to continue."
          />
          <br />
          <p>
            The following information will be used to create your CSO account.
          </p>
          <DisplayBox icon={icon} element={applicantTable} />

          {/* if missing email, show email input fields. */}
          {!applicantInfo.email && (
            <div>
              <label>Email: </label>
              <input
                type="textarea"
                name="email"
                data-testid="email"
                onChange={handleOnChange}
              />
              <label>Confirm Email: </label>
              <input
                type="textarea"
                name="confEmail"
                data-testid="conf-email"
                onChange={handleOnChange}
              />
              <br />
              <span className="mr-5 error" data-testid="email-error">
                {emailInputErrors.emailError}
              </span>
              <span className="ml-5 error" data-testid="conf-email-error">
                {emailInputErrors.confEmailError}
              </span>
            </div>
          )}
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
            label="Create CSO Account"
            id="create-cso-btn"
            testId="create-cso-btn"
            onClick={() => createCSOAccount(applicantInfo)}
            styling="bcgov-normal-blue btn"
            disabled={!continueBtnEnabled}
            hasLoader={showLoader}
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
