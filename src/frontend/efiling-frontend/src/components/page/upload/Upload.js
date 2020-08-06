/* eslint-disable react/jsx-props-no-spreading */
import React, { useState, useEffect } from "react";
import PropTypes from "prop-types";
import axios from "axios";
import Dropzone from "react-dropzone";
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

let filesToUpload = {
  documents: [],
};

const generateDropdownItems = ({ level, courtClass }, setItems, items) => {
  if (items.length > 0) return;

  axios
    .get(`/lookup/documentTypes/${level}/${courtClass}`)
    .then(({ data: { documentTypes } }) => setItems(documentTypes))
    .catch(() => window.open(sessionStorage.getItem("errorUrl"), "_self"));
};

const translateItems = (items) => {
  const translatedItems = ["Select document description"];

  items.forEach((item) => {
    translatedItems.push(item.description);
  });

  return translatedItems;
};

const removeUploadedFile = (fileName, acceptedFiles, setAcceptedFiles) => {
  filesToUpload.documents = filesToUpload.documents.filter((doc) => {
    return doc.name !== fileName;
  });

  setAcceptedFiles(acceptedFiles.filter((f) => f.name !== fileName));
};

const generateFileJSX = (fileName, acceptedFiles, setAcceptedFiles) => {
  console.log(filesToUpload);
  return (
    <div className="center-alignment fill-space">
      <div style={{ color: "rgb(252, 186, 25)" }}>
        <MdDescription size={32} />
      </div>
      <span className="file-href minor-margin-left">{fileName}</span>
      <MdDeleteForever
        className="minor-margin-left pointer"
        size={32}
        onClick={() =>
          removeUploadedFile(fileName, acceptedFiles, setAcceptedFiles)
        }
      />
    </div>
  );
};

const generateRadioButtonJSX = (fileName, type) => {
  let file;
  filesToUpload.documents.forEach((f) => {
    if (file.name === fileName) {
      file = f;
    }
  });

  return (
    <div className="table-value">
      <div className="minor-margin-right">
        <Radio
          id={`no-${type}-${fileName}`}
          name={`${type}-${fileName}`}
          label="No"
          onSelect={() => (file[type] = false)}
        />
      </div>
      <Radio
        id={`yes-${type}-${fileName}`}
        name={`${type}-${fileName}`}
        label="Yes"
        onSelect={() => (file[type] = true)}
      />
    </div>
  );
};

const generateDropdownJSX = (items, fileName) => {
  return (
    <>
      <div className="table-value top-spacing">
        <Dropdown
          label="Description:"
          items={translateItems(items)}
          onSelect={(val) => {
            filesToUpload.documents.forEach((f) => {
              const file = f;
              if (file.name === fileName) {
                file.type = items.find((item) => item.description === val).type;
              }
            });
          }}
        />
      </div>
      <br />
    </>
  );
};

const generateTable = (items, file, acceptedFiles, setAcceptedFiles) => {
  if (!filesToUpload.documents.some((f) => f.name === file.name)) {
    filesToUpload.documents.push({ name: file.name });
  }

  return [
    {
      key: file.name,
      name: generateFileJSX(file.name, acceptedFiles, setAcceptedFiles),
      value: generateDropdownJSX(items, file.name),
    },
    {
      key: `${file.name}-amendment`,
      name: (
        <div className="major-padding-left">Is this document an amendment?</div>
      ),
      value: generateRadioButtonJSX(file.name, "isAmendment"),
    },
    {
      key: `${file.name}-supreme`,
      name: (
        <div className="major-padding-left">
          Is this document that needs to go to supreme court scheduling?
        </div>
      ),
      value: generateRadioButtonJSX(file.name, "isSupremeCourtScheduling"),
    },
  ];
};

const generateFormData = (acceptedFiles) => {
  const formData = new FormData();

  for (let i = 0; i < acceptedFiles.length; i += 1) {
    formData.append("files", acceptedFiles[i]);
  }

  return formData;
};

const uploadDocuments = (submissionId, acceptedFiles) => {
  console.log(filesToUpload);
  // axios
  //   .post(
  //     `/submission/${submissionId}/documents`,
  //     generateFormData(acceptedFiles),
  //     {
  //       headers: { "Content-Type": "multipart/form-data" },
  //     }
  //   )
  //   .then(() => {
  //     axios
  //       .post(`/submission/${submissionId}/update-documents`, filesToUpload)
  //       .then((response) => {
  //         console.log(response);
  //       })
  //       .catch(() => window.open(sessionStorage.getItem("errorUrl"), "_self"));
  //   })
  //   .catch(() => window.open(sessionStorage.getItem("errorUrl"), "_self"));
};

export default function Upload({
  upload: { confirmationPopup, submissionId, courtData },
}) {
  const amendmentsSidecard = getSidecardData().amendments;
  const supremeCourtSchedulingSidecard = getSidecardData()
    .supremeCourtScheduling;
  const [showPackageConfirmation, setShowPackageConfirmation] = useState(false);
  const [acceptedFiles, setAcceptedFiles] = useState([]);
  const [items, setItems] = useState([]);

  useEffect(() => {
    generateDropdownItems(courtData, setItems, items);
  }, [items]);

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
        <Dropzone
          onDrop={(droppedFile) =>
            setAcceptedFiles(acceptedFiles.concat(droppedFile))
          }
        >
          {({ getRootProps, getInputProps }) => (
            <div
              data-testid="dropdownzone"
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
        {acceptedFiles.length > 0 && (
          <>
            <h2>Select Description for each uploaded document to continue</h2>
            <br />
            {acceptedFiles.map((file) => (
              <div key={file.name}>
                <DisplayBox
                  styling="border-background"
                  element={
                    <Table
                      elements={generateTable(
                        items,
                        file,
                        acceptedFiles,
                        setAcceptedFiles
                      )}
                    />
                  }
                />
                <br />
              </div>
            ))}
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
            onClick={() => uploadDocuments(submissionId, acceptedFiles)}
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
    courtData: PropTypes.object.isRequired,
  }).isRequired,
};
