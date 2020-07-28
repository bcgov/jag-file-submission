/* eslint-disable react/jsx-props-no-spreading */
import React, { useState } from "react";
import PropTypes from "prop-types";
import Dropzone, { useDropzone } from "react-dropzone";
import { MdDescription, MdDeleteForever } from "react-icons/md";
import {
  Sidecard,
  Button,
  DisplayBox,
  Table,
  Dropdown,
  Radio,
} from "shared-components";
import { getSidecardData } from "../../../modules/sidecardData";
import { propTypes } from "../../../types/propTypes";

import "./Upload.css";
import PackageConfirmation from "../package-confirmation/PackageConfirmation";

const items = [
  "Select document description",
  "Affidavit",
  "Affidavit of Attempted Service",
  "Case Conference Brief",
];

const generateFileJSX = (fileName) => {
  return (
    <div className="center-alignment fill-space">
      <div style={{ color: "rgb(252, 186, 25)" }}>
        <MdDescription size={32} />
      </div>
      <span className="file-href minor-margin-left">{fileName}</span>
      <MdDeleteForever className="minor-margin-left" size={32} />
    </div>
  );
};

const generateRadioButtonJSX = (fileName, type) => {
  return (
    <div className="table-value">
      <div className="minor-margin-right">
        <Radio
          id={`no-${type}-${fileName}`}
          name={`${type}-${fileName}`}
          label="No"
          onSelect={(val) => console.log(val)}
        />
      </div>
      <Radio
        id={`yes-${type}-${fileName}`}
        name={`${type}-${fileName}`}
        label="Yes"
        onSelect={(val) => console.log(val)}
      />
    </div>
  );
};

const generateDropdownJSX = () => {
  return (
    <>
      <div className="table-value top-spacing">
        <Dropdown
          label="Description:"
          items={items}
          onSelect={(val) => console.log(val)}
        />
      </div>
      <br />
    </>
  );
};

const generateTable = (file) => {
  return [
    {
      name: generateFileJSX(file.name),
      value: generateDropdownJSX(),
    },
    {
      name: (
        <div className="major-padding-left">Is this document an amendment?</div>
      ),
      value: generateRadioButtonJSX(file.name, "amendment"),
    },
    {
      name: (
        <div className="major-padding-left">
          Is this document that needs to go to supreme court scheduling?
        </div>
      ),
      value: generateRadioButtonJSX(file.name, "supreme"),
    },
  ];
};

export default function Upload({
  upload: { confirmationPopup, submissionId },
}) {
  const amendmentsSidecard = getSidecardData().amendments;
  const supremeCourtSchedulingSidecard = getSidecardData()
    .supremeCourtScheduling;
  // const { acceptedFiles, getRootProps, getInputProps } = useDropzone();
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
        <Dropzone onDrop={(acceptedFile) => console.log(acceptedFile)}>
          {({ getRootProps, getInputProps }) => (
            <div
              data-testid="alan"
              {...getRootProps({ className: "dropzone-outer-box" })}
            >
              <div className="dropzone-inner-box">
                <input {...getInputProps()} />
                <span>
                  <h2 className="text-center-alignment">
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
          )}
        </Dropzone>
        <br />
        {/* {acceptedFiles.length > 0 && (
          <>
            <h2>Select Description for each uploaded document to continue</h2>
            <br />
            {acceptedFiles.map((file) => (
              <div>
                <DisplayBox
                  styling="border-background"
                  element={
                    <Table key={file.name} elements={generateTable(file)} />
                  }
                />
                <br />
              </div>
            ))}
          </>
        )} */}
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
    submissionId: PropTypes.string.isRequired,
  }).isRequired,
};
