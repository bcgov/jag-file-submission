import React, { useState } from "react";
import Dropzone from "react-dropzone";
import { Radio, Callout, Button, Input, Textarea } from "shared-components";

import "./Rush.css";

const calloutText = `Please provide the date of when the direction was made,
the name of the Judge who made the direction along with any additional details
you feel are necessary.`;

const generateInputField = (input, onChange) => {
  return (
    <div className="form-child">
      <Input input={input} onChange={onChange} />
    </div>
  );
};

export default function Rush() {
  const [rushReason, setRushReason] = useState(null);
  const [showPayment, setShowPayment] = useState(false);
  const [acceptedFiles, setAcceptedFiles] = useState([]);
  const input = {
    isReadOnly: false,
    styling: "editable-white",
    isRequired: true,
  };

  return (
    <div className="page">
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
          onSelect={(val) => setRushReason(val)}
        />
        <Radio
          id="court-directed"
          name="rush-reason"
          label="The court directed that the order be processed on an urgent basis."
          onSelect={(val) => setRushReason(val)}
        />
        <Radio
          id="other"
          name="rush-reason"
          label="Other (please explain)."
          onSelect={(val) => setRushReason(val)}
        />
        <Callout text={calloutText} />
        <Textarea
          id="detailed-reason"
          label="Clear and detailed reason(s)"
          onChange={(val) => console.log(val)}
        />
        <p>
          I understand the registry has the discretion to reject this request.
        </p>
        <div className="form-parent">
          {generateInputField(
            {
              ...input,
              label: "Last Name",
              id: "lastName",
            },
            () => console.log("lastname")
          )}
          {generateInputField(
            {
              ...input,
              label: "Organization",
              id: "org",
              isRequired: false,
            },
            () => console.log("org")
          )}
        </div>
        <div className="form-parent">
          {generateInputField(
            {
              ...input,
              label: "First Name",
              id: "firstName",
            },
            () => console.log("firstname")
          )}
          {generateInputField(
            {
              ...input,
              label: "Contact Country",
              id: "country",
              isRequired: false,
            },
            () => console.log("country")
          )}
        </div>
        <div className="form-parent">
          {generateInputField(
            {
              ...input,
              label: "Phone Number",
              id: "phoneNumber",
            },
            () => console.log("phone")
          )}
        </div>
        <br />
        <Dropzone
          onDrop={(droppedFiles) =>
            setAcceptedFiles(acceptedFiles.concat(droppedFiles))
          }
        >
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
            styling="normal-white btn"
          />
          <Button
            label="Submit Request"
            onClick={() => console.log("submit rush")}
            styling="normal-blue btn"
          />
        </section>
      </div>
    </div>
  );
}
