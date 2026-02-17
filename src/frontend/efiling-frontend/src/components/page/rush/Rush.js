/* eslint-disable import/no-named-as-default, import/no-named-as-default-member, react/jsx-props-no-spreading, react/function-component-definition  */
import React, { useEffect, useState, useRef } from "react";
import PropTypes from "prop-types";
import Dropzone from "react-dropzone";
import {
  Radio,
  Callout,
  Button,
  Textarea,
  DatePick,
  Sidecard,
  Dropdown,
} from "shared-components";
import validator from "validator";
import { getSidecardData } from "../../../modules/helpers/sidecardData";
import RushConfirmation from "../../../domain/package/package-confirmation/RushConfirmation";

import "./Rush.scss";
import { getCountries, submitRush, submitRushDocuments } from "./RushService";
import RushDocumentList from "./rush-document-list/RushDocumentList";
import { Toast } from "../../toast/Toast";
import { Input } from "../../input/Input";
import { getJWTData } from "../../../modules/helpers/authentication-helper/authenticationHelper";
import { checkForDuplicateFilenames } from "../../../modules/helpers/filenameUtil";

const calloutText =
  "Please provide the date of when the direction was made, the name of the Judge who made the direction along with any additional details you feel are necessary.  ";

const generateInputField = (input, onChange) => (
  <div className="form-child">
    <Input input={input} onChange={onChange} />
  </div>
);

export default function Rush({
  payment,
  setShowRush,
  setIsRush,
  setHasRushInfo,
}) {
  const isValidPhoneNumber = (phoneNumber, country) => {
    // Size restriction on CSO database column
    if (phoneNumber.length > 13) {
      return false;
    }

    // These regexes are used to match the regexes used by CSO
    if (!country || country.code === "1") {
      const domesticRegex = /\d{3}-?\d{3}-?\d{4}/;
      return domesticRegex.test(phoneNumber);
    }

    const internationalRegex = /(\d+-?\d+-?)+\d+/;
    return internationalRegex.test(phoneNumber);
  };

  const checkPhoneNumberErrors = (phoneNumber, country, setPhoneError) => {
    if (
      !isValidPhoneNumber(phoneNumber, country) &&
      !validator.isEmpty(phoneNumber)
    ) {
      setPhoneError("Invalid phone number");
    } else {
      setPhoneError(null);
    }
  };

  const determinePhonePlaceholder = (country) =>
    country && country.code !== "1" ? "" : "xxx-xxx-xxxx";
  // eslint-disable-next-line no-unused-vars

  const input = {
    isReadOnly: false,
    styling: "bcgov-editable-white",
    isRequired: true,
  };
  const contactMethods = [
    ["Email", "email"],
    ["Phone Number", "phoneNumber"],
  ];
  const initFields = {
    rushType: "",
    surname: "",
    firstName: "",
    contactMethod: contactMethods[0],
    phoneNumber: "",
    email: "",
    org: "",
    country: null,
    details: "",
    date: "",
  };
  const [files, setFiles] = useState([]);
  const [selectedDate, setSelectedDate] = useState(new Date());
  const [radio1, setRadio1] = useState(false);
  const [radio2, setRadio2] = useState(false);
  const [radio3, setRadio3] = useState(false);
  const [fields, setFields] = useState(initFields);
  const [numDocumentsError, setNumDocumentsError] = useState(false);
  const [duplicateFilenamesError, setDuplicateFilenamesError] = useState(false);
  const [emailError, setEmailError] = useState(null);
  const [firstNameError, setFirstNameError] = useState(null);
  const [surnameError, setSurnameError] = useState(null);
  const [phoneError, setPhoneError] = useState(null);
  const [toastMessage, setToastMessage] = useState("");
  const [showToast, setShowToast] = useState(false);
  const [countries, setCountries] = useState([]);
  const [continueBtnEnabled, setContinueBtnEnabled] = useState(false);
  const [showModal, setShowModal] = useState(false);

  const aboutCsoSidecard = getSidecardData().aboutCso;
  const csoAccountDetailsSidecard = getSidecardData().csoAccountDetails;
  const rushSubmissionSidecard = getSidecardData(setShowModal).rushSubmission;

  const clearFields = {
    rushType: fields.rushType,
    surname: "",
    firstName: "",
    contactMethod: contactMethods[0],
    phoneNumber: "",
    email: "",
    org: "",
    country: null,
    details: "",
    date: "",
  };

  const handleContinue = () => {
    const supportingDocuments = [];
    for (let i = 0; i < files.length; i += 1) {
      const supportingDocument = {
        fileName: files[i].name,
        identifier: null,
      };

      supportingDocuments.push(supportingDocument);
    }

    const formData = new FormData();
    for (let i = 0; i < files.length; i += 1) {
      formData.append("files", files[i]);
    }

    const req = {
      rushType: fields.rushType,
      firstName: fields.firstName,
      lastName: fields.surname,
      organization: fields.org,
      phoneNumber: fields.phoneNumber,
      email: fields.email,
      courtDate: fields.date,
      country: fields.country.description,
      countryCode: fields.country.code,
      reason: fields.details,
      supportingDocuments,
    };

    if (files.length > 0) {
      submitRushDocuments(payment.submissionId, formData)
        .then(() => {})
        .catch(() => {
          setToastMessage(
            "Something went wrong while trying to submit your document(s)"
          );
          setShowToast(true);
        });
    }

    submitRush(payment.submissionId, req)
      .then(() => {
        sessionStorage.setItem("validRushExit", true);
        setHasRushInfo(true);
        setShowRush(false);
      })
      .catch(() => {
        setToastMessage(
          "Something went wrong while trying to process your submission"
        );
        setShowToast(true);
      });
  };

  const enforceCharacterLimit = (fieldValue, fieldName, characterLimit) => {
    const limitedValue = fieldValue.substring(0, characterLimit);

    setFields({ ...fields, [fieldName]: limitedValue });
  };

  const handleInputFieldChange = (
    inputs,
    fieldName,
    characterLimit,
    errorSetter
  ) => {
    enforceCharacterLimit(inputs, fieldName, characterLimit);

    if (validator.isEmpty(inputs)) {
      errorSetter("Invalid name");
    } else {
      errorSetter(null);
    }
  };

  const handleCountryChange = (countryDescription) => {
    const currentCountry = countries.filter(
      (countryObj) => countryObj.description === countryDescription
    )[0];

    setFields({ ...fields, country: currentCountry });
    checkPhoneNumberErrors(fields.phoneNumber, currentCountry, setPhoneError);
  };

  const handleMethodOfContactChange = (e) => {
    setFields({
      ...fields,
      contactMethod: contactMethods.filter((list) => list[0] === e && list)[0],
      phoneNumber: "",
      email: "",
    });
    setEmailError(null);
    setPhoneError(null);
  };

  const handleEmailChange = (email) => {
    if (!validator.isEmail(email) && !validator.isEmpty(email)) {
      setEmailError("Invalid email address");
    } else {
      setEmailError(null);
    }
    setFields({ ...fields, email });
  };

  const handlePhoneNumberChange = (phoneNumber) => {
    checkPhoneNumberErrors(phoneNumber, fields.country, setPhoneError);
    setFields({
      ...fields,
      phoneNumber,
    });
  };

  const canReject = (
    <p>
      <b>
        {" "}
        I understand the registry has the discretion to reject this request.
      </b>
    </p>
  );
  const textarea = (
    <Textarea
      id="detailed-reason"
      isRequired
      label="Clear and detailed reason(s)"
      value={fields.details}
      onChange={(inputs) => setFields({ ...fields, details: inputs })}
    />
  );
  const textFields = (
    <>
      <div className="form-parent">
        {generateInputField(
          {
            ...input,
            label: "Surname",
            id: "surname",
            value: fields.surname,
            isControlled: true,
            errorMsg: surnameError,
          },
          (inputs) =>
            handleInputFieldChange(inputs, "surname", 30, setSurnameError)
        )}
        {generateInputField(
          {
            ...input,
            label: "First Name",
            id: "firstName",
            value: fields.firstName,
            isControlled: true,
            errorMsg: firstNameError,
          },
          (inputs) =>
            handleInputFieldChange(inputs, "firstName", 30, setFirstNameError)
        )}
      </div>
      <div className="form-parent">
        {generateInputField(
          {
            ...input,
            label: "Organization",
            id: "org",
            isRequired: false,
            value: fields.org,
            isControlled: true,
          },
          (inputs) => enforceCharacterLimit(inputs, "org", 150)
        )}
        <div className="form-child">
          <Dropdown
            className="field-dropdown"
            id="country-dropdown"
            label="Contact Country"
            items={countries.map((countryObj) => countryObj.description)}
            onSelect={(e) => handleCountryChange(e, fields, setFields)}
          />
        </div>
      </div>
      <div className="form-parent">
        <div className="form-child">
          <Dropdown
            className="field-dropdown"
            id="contact-dropdown"
            isRequired
            label={
              <span>
                Method of Contact <span className="red">*</span>
              </span>
            }
            items={contactMethods.map((list) => list[0])}
            onSelect={(e) => handleMethodOfContactChange(e)}
          />
        </div>
        {fields.contactMethod[0] === contactMethods[0][0] &&
          generateInputField(
            {
              ...input,
              label: fields.contactMethod[0],
              id: fields.contactMethod[1],
              value: fields[fields.contactMethod[1]],
              errorMsg: emailError,
            },
            (inputs) => handleEmailChange(inputs)
          )}
        {fields.contactMethod[0] === contactMethods[1][0] &&
          generateInputField(
            {
              ...input,
              label: fields.contactMethod[0],
              id: fields.contactMethod[1],
              value: fields[fields.contactMethod[1]],
              placeholder: determinePhonePlaceholder(fields.country),
              isControlled: true,
              errorMsg: phoneError,
            },
            (inputs) => handlePhoneNumberChange(inputs)
          )}
      </div>
    </>
  );

  const dropzone = (
    <>
      <h2>Supporting Documentation</h2>
      <br />
      <p>
        <b>
          Attach any documents from counsel that explain the reasons for an
          urgent (rush) order.
        </b>
        <br />
        Please note that documents uploaded on this screen will not be processed
        into the file.
        <br />
        To submit a document for e-filing, upload it on the &quot;ADD
        DOCUMENTS&quot; screen.
      </p>
      {showToast && <Toast content={toastMessage} setShow={setShowToast} />}
      <Dropzone
        onDrop={(droppedFiles) => {
          let hasError = false;
          if (droppedFiles.length + files.length >= 6) {
            setNumDocumentsError(true);
            hasError = true;
          } else {
            setNumDocumentsError(false);
          }

          if (
            checkForDuplicateFilenames(
              droppedFiles,
              files.concat(payment.files)
            )
          ) {
            setDuplicateFilenamesError(true);
            hasError = true;
          } else {
            setDuplicateFilenamesError(false);
          }

          if (!hasError) {
            setFiles(files.concat(droppedFiles));
          }
        }}
      >
        {({ getRootProps, getInputProps }) => (
          <div
            data-testid="dropdownzone"
            {...getRootProps({ className: "dropzone-outer" })}
          >
            <div className="dropzone-inner-box mt-5 mb-5">
              <input {...getInputProps()} />
              <span>
                <h2 className="text-center-alignment text-center">
                  Drag and drop or&nbsp;
                  <span className="file-href">choose documents</span>
                </h2>
                <div>
                  <span>
                    to upload supporting documents from counsel that explain the
                    reasons for an urgent/rush order.
                  </span>
                  <br />
                  <span>
                    Please note that documents uploaded on this screen will not
                    be processed into your eFile.
                  </span>
                </div>
              </span>
            </div>
          </div>
        )}
      </Dropzone>
      <br />
      {canReject}
      <br />
      {files.length > 0 && (
        <RushDocumentList files={files} setFiles={setFiles} />
      )}
    </>
  );

  useEffect(() => {
    window.scrollTo(0, 0);
    getCountries()
      .then((res) => {
        setCountries(
          res.data.map((obj) => ({
            code: obj.code,
            description: obj.description,
          }))
        );
      })
      .catch((err) => console.log(err));
  }, []);

  const initialRender = useRef(true);

  useEffect(() => {
    // Do not trigger effects if on the initial render of Rush.js (ie. a radio button has not been selected)
    if (initialRender.current) {
      initialRender.current = false;
      return;
    }

    const isError = () => {
      const errorStates = [
        firstNameError,
        surnameError,
        emailError,
        duplicateFilenamesError,
        phoneError,
      ];
      let result = false;

      errorStates.forEach((error) => {
        if (error !== null && error !== false) result = true;
      });

      return result;
    };

    const canContinue = () => {
      let mandatoryFields = [];

      if (radio1) {
        mandatoryFields = [
          fields.firstName,
          fields.surname,
          fields[fields.contactMethod[1]],
        ];
      } else if (radio2 || radio3) {
        setFields({ ...fields, date: selectedDate });
        mandatoryFields = [
          fields.firstName,
          fields.surname,
          fields[fields.contactMethod[1]],
          fields.details,
          validator.toString(fields.date),
        ];
      }

      // return false if: there are any errors, any mandatory fields are empty, in the case of radio2 or radio3: fields.details is empty and there are no dropped files
      if (
        isError() ||
        mandatoryFields.some((field) => !field) ||
        (!radio1 &&
          validator.isEmpty(mandatoryFields[3] || "") &&
          files.length < 1)
      )
        return false;

      return true;
    };

    setContinueBtnEnabled(canContinue());
  }, [
    fields.firstName,
    fields.surname,
    fields[fields.contactMethod[1]],
    fields.details,
    fields.date,
    radio1,
    radio2,
    radio3,
  ]);

  useEffect(() => {
    const resetFields = () => {
      const jwtData = getJWTData() || {};
      setFields({
        ...clearFields,
        firstName: jwtData.given_name || "",
        surname: jwtData.family_name || "",
        email: jwtData.email || "",
        country: countries[0],
      });

      if (validator.isEmail(jwtData.email || "")) {
        setEmailError(null);
      }
    };
    resetFields();
  }, [radio1, radio2, radio3]);

  useEffect(() => {
    const displayAnyDocumentErrors = () => {
      const errors = [];
      if (numDocumentsError) {
        errors.push("You cannot upload more than five supporting documents.");
      }

      if (duplicateFilenamesError) {
        errors.push(
          "You cannot upload two supporting documents with the same filename."
        );
      }

      if (errors.length > 0) {
        let errorMessage = "";
        for (let i = 0; i < errors.length; i += 1) {
          errorMessage = errorMessage.concat(errors[i]).concat("\n");
        }

        setToastMessage(errorMessage);
        setShowToast(true);
      } else {
        setShowToast(false);
      }
    };

    displayAnyDocumentErrors();
  }, [numDocumentsError, duplicateFilenamesError]);

  const setRadioStatusComponents = () => {
    setRadio1(false);
    setRadio2(false);
    setRadio3(false);
    setFiles([]);
  };

  return (
    <div className="ct-rush page">
      {showModal && (
        <RushConfirmation show={showModal} setShow={setShowModal} />
      )}
      <div className="content col-md-8">
        <h1>Rush Details</h1>
        <h2>Submitting on a rush basis</h2>
        <br />
        <p>
          If you wish to request that this package be submitted on an urgent
          (rush) basis, <br />
          you must provide a reason and contact information for your request to
          be considered.
        </p>
        <br />
        <h3>My reason for requesting urgent (rush) filing is:</h3>
        <br />
        <Radio
          id="rule-85"
          name="rush-reason"
          label="The attached application is made under Rule 8-5 (1) SCR."
          onSelect={() => {
            setRadioStatusComponents();
            setRadio1(true);
            setFields({ ...fields, rushType: "rule" });
          }}
        />
        <Radio
          id="court-directed"
          name="rush-reason"
          label="The court directed that the order be processed on an urgent basis."
          onSelect={() => {
            setRadioStatusComponents();
            setFields({ ...fields, rushType: "court" });
            setRadio2(true);
          }}
        />
        <Radio
          id="other"
          name="rush-reason"
          label="Other (please explain)."
          onSelect={() => {
            setRadioStatusComponents();
            setFields({ ...fields, rushType: "other" });
            setRadio3(true);
          }}
        />
        <br />

        {radio1 && (
          <>
            {dropzone}
            <br />
            {textFields}
            <br />
          </>
        )}

        {radio2 && (
          <>
            <Callout text={calloutText} />
            <br />
            <DatePick
              label="Date"
              isRequired
              selectedDate={selectedDate}
              setSelectedDate={setSelectedDate}
            />
            <br />
            {textarea}
            <br />
            {dropzone}
            <br />
            {textFields}
            <br />
          </>
        )}

        {radio3 && (
          <>
            {textarea}
            <br />
            {dropzone}
            <br />
            {textFields}
            <br />
          </>
        )}

        <section className="buttons pt-2">
          <Button
            label="Cancel"
            onClick={() => {
              setIsRush(false);
              setShowRush(false);
            }}
            styling="bcgov-normal-white btn"
          />
          <Button
            label="Continue"
            disabled={!continueBtnEnabled}
            styling="bcgov-normal-blue btn"
            onClick={handleContinue}
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

Rush.propTypes = {
  payment: PropTypes.object.isRequired,
  setShowRush: PropTypes.func.isRequired,
  setIsRush: PropTypes.func.isRequired,
  setHasRushInfo: PropTypes.func.isRequired,
};
