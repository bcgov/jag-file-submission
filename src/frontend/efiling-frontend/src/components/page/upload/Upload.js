import React, { useState } from "react";
import PropTypes from "prop-types";
import { useDropzone } from "react-dropzone";
import { Sidecard, Button } from "shared-components";
import { getSidecardData } from "../../../modules/sidecardData";
import { propTypes } from "../../../types/propTypes";

import "./Upload.css";
import PackageConfirmation from "../package-confirmation/PackageConfirmation";

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

  const files = acceptedFiles.map(file => (
    <li key={file.path}>
      {file.path} - {file.size} bytes
    </li>
  ));

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
