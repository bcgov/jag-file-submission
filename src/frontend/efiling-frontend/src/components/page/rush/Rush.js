/* eslint-disable react/jsx-props-no-spreading , react/no-unescaped-entities, no-unused-vars */
import React, { useEffect, useState } from "react";
import PropTypes from "prop-types";
import Dropzone from "react-dropzone";
import {
  Radio,
  Callout,
  Button,
  Input,
  Textarea,
  DatePick,
  Sidecard,
  Dropdown,
} from "shared-components";
import { getSidecardData } from "../../../modules/helpers/sidecardData";
import Payment from "../../../domain/payment/Payment";

import "./Rush.scss";
import { getCountries } from "./RushService";
import RushDocumentList from "./rush-document-list/RushDocumentList";

const calloutText = `Please provide the date of when the direction was made, the name of the Judge who made the direction along with any additional details you feel are necessary.  `;

const generateInputField = (input, onChange) => (
  <div className="form-child">
    <Input input={input} onChange={onChange} />
  </div>
);

export default function Rush({ payment }) {
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
    surname: null,
    firstName: null,
    contactMethod: contactMethods[0],
    phoneNumber: null,
    email: null,
    org: null,
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
  // const [numDocumentsError, setNumDocumentsError] = useState(false);
  const [countries, setCountries] = useState([]);

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
          },
          (inputs) => {
            setFields({ ...fields, surname: inputs });
          }
        )}
        {generateInputField(
          {
            ...input,
            label: "First Name",
            id: "firstName",
          },
          (inputs) => {
            setFields({ ...fields, firstName: inputs });
          }
        )}
      </div>
      <div className="form-parent">
        {generateInputField(
          {
            ...input,
            label: "Organization",
            id: "org",
            isRequired: false,
          },
          (inputs) => {
            setFields({ ...fields, org: inputs });
          }
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
            onSelect={(e) =>
              setFields({
                ...fields,
                contactMethod: contactMethods.filter(
                  (list) => list[0] === e && list
                )[0],
              })
            }
          />
        </div>
        {generateInputField(
          {
            ...input,
            label: fields.contactMethod[0],
            id: fields.contactMethod[1],
            value: fields[fields.contactMethod[1]],
          },
          (inputs) => {
            setFields({ ...fields, [fields.contactMethod[1]]: inputs });
          }
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
        To submit a document for e-filing, upload it on the "ADD DOCUMENTS"
        screen.
      </p>
      <Dropzone
        onDrop={(droppedFiles) => {
          if (droppedFiles.length + files.length < 6) {
            setFiles(files.concat(droppedFiles));
            // setNumDocumentsError(false);
          } else {
            // setNumDocumentsError(true);
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

  const setRadioStatusComponents = () => {
    setRadio1(false);
    setRadio2(false);
    setRadio3(false);
    setFields(clearFields);
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
