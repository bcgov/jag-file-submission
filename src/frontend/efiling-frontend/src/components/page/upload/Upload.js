/* eslint-disable react/jsx-props-no-spreading */
import React, { useState } from "react";
import PropTypes from "prop-types";
import { useDropzone } from "react-dropzone";
import { MdDescription } from "react-icons/md";
import {
  Sidecard,
  Button,
  DisplayBox,
  Table,
  Dropdown,
  Radio
} from "shared-components";
import { getSidecardData } from "../../../modules/sidecardData";
import { propTypes } from "../../../types/propTypes";

import "./Upload.css";
import PackageConfirmation from "../package-confirmation/PackageConfirmation";

const icon = (
  <div style={{ color: "rgb(252, 186, 25)" }}>
    <MdDescription size={32} />
  </div>
);

const generateTable = () => {
  return [
    {
      name: (
        <div className="center-alignment fill-space">
          {icon}
          <span className="minor-margin-left">jhsgdhjsgjh</span>
        </div>
      ),
      value: (
        <>
          <div className="table-value">
            <Dropdown
              label="Description:"
              items={items}
              onSelect={val => console.log(val)}
            />
          </div>
          <br />
        </>
      ),
      verticalMiddle: false
    },
    {
      name: (
        <div className="major-padding-left">Is this document an amendment?</div>
      ),
      value: (
        <div className="table-value">
          <div className="minor-margin-right">
            <Radio
              id="no"
              name="amendment"
              label="No"
              onSelect={val => console.log(val)}
            />
          </div>
          <Radio
            id="yes"
            name="amendment"
            label="Yes"
            onSelect={val => console.log(val)}
          />
        </div>
      )
    },
    {
      name: (
        <div className="major-padding-left">
          Is this document that needs to go to supreme court scheduling aln
          dodge man?
        </div>
      ),
      value: (
        <div className="table-value">
          <div className="minor-margin-right">
            <Radio
              id="no"
              name="amendment"
              label="No"
              onSelect={val => console.log(val)}
            />
          </div>
          <Radio
            id="yes"
            name="amendment"
            label="Yes"
            onSelect={val => console.log(val)}
          />
        </div>
      )
    }
  ];
};

const items = [
  "Select document description",
  "Option 2",
  "Option 3",
  "Option 4"
];

export default function Upload({
  upload: { confirmationPopup, submissionId }
}) {
  const amendmentsSidecard = getSidecardData().amendments;
  const supremeCourtSchedulingSidecard = getSidecardData()
    .supremeCourtScheduling;
  const { acceptedFiles, getRootProps, getInputProps } = useDropzone();
  const [showPackageConfirmation, setShowPackageConfirmation] = useState(false);

  if (showPackageConfirmation) {
    return (
      <PackageConfirmation
        packageConfirmation={{ confirmationPopup, submissionId }}
        csoAccountStatus={{ isNew: false }}
      />
    );
  }

  return (
    <div className="page">
      <div className="content col-md-8">
        <h1>Document Upload</h1>
        <div {...getRootProps({ className: "dropzone-outer-box" })}>
          <div className="dropzone-inner-box">
            <input {...getInputProps()} />
            <span>
              <h2>
                Drag and drop or&nbsp;
                <span className="file-href">choose documents</span>
              </h2>
              <div>
                <span>to upload additional documents to your package</span>
                <br />
                <span>(max 10mb per document)</span>
              </div>
            </span>
          </div>
        </div>
        <br />
        {acceptedFiles.length > 0 && (
          <>
            <h2>Select Description for each uploaded document to continue</h2>
            <br />
            <div>
              <DisplayBox
                styling="border-background"
                element={<Table elements={generateTable()} />}
              />
              <br />
            </div>
          </>
        )}
        <section className="buttons pt-2">
          <Button
            label="Cancel Upload"
            onClick={() => setShowPackageConfirmation(true)}
            styling="normal-white btn"
          />
          <Button
            label="Continue"
            onClick={() => console.log("on continue")}
            styling="normal-blue btn"
          />
        </section>
      </div>

      <div className="sidecard">
        <Sidecard sideCard={amendmentsSidecard} />
        <Sidecard sideCard={supremeCourtSchedulingSidecard} />
      </div>
    </div>
  );
}

Upload.propTypes = {
  upload: PropTypes.shape({
    confirmationPopup: propTypes.confirmationPopup,
    submissionId: PropTypes.string.isRequired
  }).isRequired
};
