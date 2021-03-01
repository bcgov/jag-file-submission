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
import { isIdentityProviderBCeID } from "../../../modules/helpers/authentication-helper/authenticationHelper";
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
  const isBCeID = isIdentityProviderBCeID();

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

          {/* if BCSC and missing email, show email input fields. */}
          {!isBCeID && !applicantInfo.email && (
            <form className="email-form">
              <div className="row">
                <label className="cso-label">
                  Email Address
                  <span className="red">*</span>
                </label>
              </div>
              <div className="email-input row">
                <input
                  type="textarea"
                  className="cso-input"
                  name="email"
                  data-testid="email"
                  onChange={handleOnChange}
                />
                <span className="error red" data-testid="email-error">
                  {emailInputErrors.emailError}
                </span>
              </div>
              <div className="row">
                <label className="cso-label">
                  Please Confirm Your Email Address
                  <span className="red">*</span>
                </label>
              </div>
              <div className="row">
                <input
                  type="textarea"
                  className="cso-input"
                  name="confEmail"
                  data-testid="conf-email"
                  onChange={handleOnChange}
                />
                <span className="error red" data-testid="conf-email-error">
                  {emailInputErrors.confEmailError}
                </span>
              </div>
            </form>
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
