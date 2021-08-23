/* eslint-disable react/jsx-props-no-spreading */
import React, { useEffect, useState } from "react";
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
import Payment from "../../../domain/payment/Payment";

import "./Rush.scss";
import { getCountries } from "./RushService";
import RushDocumentList from "./rush-document-list/RushDocumentList";
import { Toast } from "../../toast/Toast";
import { Input } from "../../input/Input";
import { getJWTData } from "../../../modules/helpers/authentication-helper/authenticationHelper";

const calloutText = `Please provide the date of when the direction was made, the name of the Judge who made the direction along with any additional details you feel are necessary.  `;

const generateInputField = (input, onChange) => (
  <div className="form-child">
    <Input input={input} onChange={onChange} />
  </div>
);

const formatPhoneNumber = (phoneNumber) => {
  if (!phoneNumber) {
    return phoneNumber;
  }

  const phoneNumberDigits = phoneNumber.replace(/[^\d]/g, "");

  if (phoneNumberDigits.length < 4) {
    return phoneNumberDigits;
  }

  if (phoneNumberDigits.length < 7) {
    return `${phoneNumberDigits.slice(0, 3)}-${phoneNumberDigits.slice(3)}`;
  }

  return `${phoneNumberDigits.slice(0, 3)}-${phoneNumberDigits.slice(
    3,
    6
  )}-${phoneNumberDigits.slice(6, 10)}`;
};

export default function Rush({ payment }) {
  // eslint-disable-next-line no-unused-vars
  const [continueBtnEnabled, setContinueBtnEnabled] = useState(false);

  const input = {
    isReadOnly: false,
    styling: "bcgov-editable-white",
    isRequired: true,
  };
  const aboutCsoSidecard = getSidecardData().aboutCso;
  const csoAccountDetailsSidecard = getSidecardData().csoAccountDetails;
  const rushSubmissionSidecard = getSidecardData().rushSubmission;
  const contactMethods = [
    ["Email", "email"],
    ["Phone Number", "phoneNumber"],
  ];
  const clearFields = {
    surname: "",
    firstName: "",
    contactMethod: contactMethods[0],
    phoneNumber: "",
    email: "",
    org: "",
    country: null,
    details: null,
  };

  const [files, setFiles] = useState([]);
  const [selectedDate, setSelectedDate] = useState(new Date());
  const [showPayment, setShowPayment] = useState(false);
  const [radio1, setRadio1] = useState(false);
  const [radio2, setRadio2] = useState(false);
  const [radio3, setRadio3] = useState(false);
  const [fields, setFields] = useState(clearFields);
  const [numDocumentsError, setNumDocumentsError] = useState(false);
  const [duplicateFilenamesError, setDuplicateFilenamesError] = useState(false);
  const [emailError, setEmailError] = useState(null);
  const [toastMessage, setToastMessage] = useState("");
  const [showToast, setShowToast] = useState(false);
  const [countries, setCountries] = useState([]);

  console.log(fields.surname);

  const checkForDuplicateFilenames = (droppedFiles, previousFiles) => {
    let isDuplicate = false;
    for (let i = 0; i < previousFiles.length; i += 1) {
      isDuplicate = droppedFiles.some(
        (droppedFile) => droppedFile.name === previousFiles[i].name
      );
      if (isDuplicate) return isDuplicate;
    }

    return isDuplicate;
  };

  const handleMethodOfContactChange = (e) => {
    setFields({
      ...fields,
      contactMethod: contactMethods.filter((list) => list[0] === e && list)[0],
      phoneNumber: "",
      email: "",
      surname: null,
    });
    setEmailError(null);
  };

  const handleEmailChange = (email) => {
    if (!validator.isEmail(email)) {
      setEmailError("Invalid email address");
    } else {
      setEmailError(null);
    }
    setFields({ ...fields, email });
  };

  const handlePhoneNumberChange = (phoneNumber) => {
    const formattedPhoneNumber = formatPhoneNumber(phoneNumber);
    setFields({
      ...fields,
      phoneNumber: formattedPhoneNumber,
    });
  };

  const enforceCharacterLimit = (fieldValue, fieldName, characterLimit) => {
    const limitedValue = fieldValue.substring(0, characterLimit);

    setFields({ ...fields, [fieldName]: limitedValue });
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
          },
          (inputs) => enforceCharacterLimit(inputs, "surname", 30)
        )}
        {generateInputField(
          {
            ...input,
            label: "First Name",
            id: "firstName",
            value: fields.firstName,
            isControlled: true,
          },
          (inputs) => enforceCharacterLimit(inputs, "firstName", 30)
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
            label="Contact Country"
            items={countries}
            onSelect={(e) => setFields({ ...fields, country: e })}
          />
        </div>
      </div>
      <div className="form-parent">
        <div className="form-child">
          <Dropdown
            className="field-dropdown"
            isRequired
            label="Method of Contact"
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
              isControlled: true,
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

          if (checkForDuplicateFilenames(droppedFiles, files)) {
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
      {files.length > 0 && <RushDocumentList files={files} />}
    </>
  );

  useEffect(() => {
    getCountries()
      .then((res) => setCountries(res.data.map((obj) => obj.description)))
      .catch((err) => console.log(err));
  }, []);

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

  const resetFields = () => {
    const jwtData = getJWTData();
    setFields({
      ...clearFields,
      firstName: jwtData.given_name,
      surname: jwtData.family_name,
      email: jwtData.email,
    });

    if (validator.isEmail(jwtData.email)) {
      setEmailError(null);
    }
  };

  const setRadioStatusComponents = () => {
    setRadio1(false);
    setRadio2(false);
    setRadio3(false);
    resetFields();
    setFiles([]);
  };

  if (showPayment) {
    return <Payment payment={payment} />;
  }

  return (
    <div className="ct-rush page">
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
          }}
        />
        <Radio
          id="court-directed"
          name="rush-reason"
          label="The court directed that the order be processed on an urgent basis."
          onSelect={() => {
            setRadioStatusComponents();
            setRadio2(true);
          }}
        />
        <Radio
          id="other"
          name="rush-reason"
          label="Other (please explain)."
          onSelect={() => {
            setRadioStatusComponents();
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
            onClick={() => setShowPayment(true)}
            styling="bcgov-normal-white btn"
          />
          <Button
            label="Continue"
            disabled={!continueBtnEnabled}
            styling="bcgov-normal-blue btn"
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
};
