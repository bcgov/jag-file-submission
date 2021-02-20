/* eslint-disable react/jsx-props-no-spreading */
import React, { useState } from "react";
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
} from "shared-components";
import { getSidecardData } from "../../../modules/helpers/sidecardData";
import Payment from "../../../domain/payment/Payment";

import "./Rush.scss";

const calloutText = `Please provide the date of when the direction was made,
the name of the Judge who made the direction along with any additional details
you feel are necessary.`;

const generateInputField = (input, onChange) => (
  <div className="form-child">
    <Input input={input} onChange={onChange} />
  </div>
);

export default function Rush({ payment }) {
  const input = {
    isReadOnly: false,
    styling: "bcgov-editable-white",
    isRequired: true,
  };
  const aboutCsoSidecard = getSidecardData().aboutCso;
  const csoAccountDetailsSidecard = getSidecardData().csoAccountDetails;

  const [selectedDate, setSelectedDate] = useState(new Date());
  const [showPayment, setShowPayment] = useState(false);

  if (showPayment) {
    return <Payment payment={payment} />;
  }

  return (
    <div className="ct-rush page">
      <div className="content col-md-8">
        <h1>Request Rush/Urgent Submission</h1>
        <p>
          You must provide a reason and contact information for your request for
          rush/urgent submission to be considered. My reason for requesting
          urgent (rush) filing is:
        </p>
        <Radio
          id="rule-85"
          name="rush-reason"
          label="The attached application is made under Rule 8-5 (1) SCR."
        />
        <Radio
          id="court-directed"
          name="rush-reason"
          label="The court directed that the order be processed on an urgent basis."
        />
        <Radio id="other" name="rush-reason" label="Other (please explain)." />
        <Callout text={calloutText} />
        <DatePick
          label="Date"
          isRequired
          selectedDate={selectedDate}
          setSelectedDate={setSelectedDate}
        />
        <br />
        <Textarea
          id="detailed-reason"
          isRequired
          label="Clear and detailed reason(s)"
        />
        <p>
          I understand the registry has the discretion to reject this request.
        </p>
        <div className="form-parent">
          {generateInputField({
            ...input,
            label: "Last Name",
            id: "lastName",
          })}
          {generateInputField({
            ...input,
            label: "Organization",
            id: "org",
            isRequired: false,
          })}
        </div>
        <div className="form-parent">
          {generateInputField({
            ...input,
            label: "First Name",
            id: "firstName",
          })}
          {generateInputField({
            ...input,
            label: "Contact Country",
            id: "country",
            isRequired: false,
          })}
        </div>
        <div className="form-parent">
          {generateInputField({
            ...input,
            label: "Phone Number",
            id: "phoneNumber",
          })}
        </div>
        <br />
        <Dropzone>
          {({ getRootProps, getInputProps }) => (
            <div
              data-testid="dropdownzone"
              {...getRootProps({ className: "dropzone-outer" })}
            >
              <div className="dropzone-inner-box">
                <input {...getInputProps()} />
                <span>
                  <h2 className="text-center-alignment">
                    Drag and drop or&nbsp;
                    <span className="file-href">choose documents</span>
                  </h2>
                  <div>
                    <span>
                      to upload supporting documents from counsel that explain
                      the reasons for an urgent/rush order.
                    </span>
                    <br />
                    <span>
                      Please note that documents uploaded on this screen will
                      not be processed into your eFile.
                    </span>
                  </div>
                </span>
              </div>
            </div>
          )}
        </Dropzone>
        <br />
        <section className="buttons pt-2">
          <Button
            label="Cancel Request"
            onClick={() => setShowPayment(true)}
            styling="bcgov-normal-white btn"
          />
          <Button label="Submit Request" styling="bcgov-normal-blue btn" />
        </section>
      </div>
      <div className="sidecard">
        <Sidecard sideCard={csoAccountDetailsSidecard} />
        <Sidecard sideCard={aboutCsoSidecard} />
      </div>
    </div>
  );
}

Rush.propTypes = {
  payment: PropTypes.object.isRequired,
};
