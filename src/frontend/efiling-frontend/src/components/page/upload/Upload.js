/* eslint-disable react/function-component-definition, react/jsx-props-no-spreading */
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
} from "shared-components";
import { getSidecardData } from "../../../modules/helpers/sidecardData";
import { errorRedirect } from "../../../modules/helpers/errorRedirect";
import { propTypes } from "../../../types/propTypes";

import "./Upload.scss";
import { getDocumentTypes } from "../../../domain/documents/DocumentService";

let filesToUpload = {
  documents: [],
};

const checkValidityOfUploadedFiles = () => {
  if (filesToUpload.documents.length >= 1) return true;
  return false;
};

const setDropdownItems = ({ level, courtClass }, setItems) => {
  getDocumentTypes(level, courtClass)
    .then((documentTypes) => setItems(documentTypes))
    .catch((error) => errorRedirect(sessionStorage.getItem("errorUrl"), error));
};

const translateItems = (items) => {
  const translatedItems = [];

  items.forEach((item) => {
    translatedItems.push(item.description);
  });

  return translatedItems;
};

const removeUploadedFile = (
  fileName,
  acceptedFiles,
  setAcceptedFiles,
  setContinueBtnEnabled
) => {
  filesToUpload.documents = filesToUpload.documents.filter(
    (doc) => doc.name !== fileName
  );

  setAcceptedFiles(acceptedFiles.filter((f) => f.name !== fileName));
  setContinueBtnEnabled(checkValidityOfUploadedFiles());
};

const generateFileLink = (file) => {
  const fileData = new Blob([file], { type: file.type });
  return URL.createObjectURL(fileData);
};

const generateFileJSX = (
  file,
  acceptedFiles,
  setAcceptedFiles,
  setContinueBtnEnabled
) => {
  const fileLink = generateFileLink(file);

  return (
    <div className="center-alignment fill-space">
      <div style={{ color: "rgb(252, 186, 25)" }}>
        <MdDescription size={32} />
      </div>
      <span
        data-testid={`file-link-${file.name}`}
        className="file-href minor-margin-left"
        onClick={() => window.open(fileLink)}
        onKeyDown={() => window.open(fileLink)}
        role="button"
        tabIndex={0}
      >
        {file.name}
      </span>
      <MdDeleteForever
        data-testid="remove-icon"
        className="minor-margin-left pointer"
        size={32}
        onClick={() =>
          removeUploadedFile(
            file.name,
            acceptedFiles,
            setAcceptedFiles,
            setContinueBtnEnabled
          )
        }
      />
    </div>
  );
};

const identifySelectedFile = (fileName) => {
  let file;

  filesToUpload.documents.forEach((f) => {
    if (f.name === fileName) {
      file = f;
    }
  });

  return file;
};

const generateRadioButtonJSX = (fileName, type) => {
  const file = identifySelectedFile(fileName);
  file[type] = false;

  return (
    <div className="table-value">
      <div className="minor-margin-right">
        <label className="bcgov-radio" htmlFor={`no-${type}-${fileName}`}>
          No
          <input
            type="radio"
            name={`${type}-${fileName}`}
            id={`no-${type}-${fileName}`}
            defaultChecked
            onChange={(e) => {
              if (e.target.checked) {
                file[type] = false;
              }
            }}
          />
          <span className="bcgov-dot" />
        </label>
      </div>
      <label className="bcgov-radio" htmlFor={`yes-${type}-${fileName}`}>
        Yes
        <input
          type="radio"
          name={`${type}-${fileName}`}
          id={`yes-${type}-${fileName}`}
          onChange={(e) => {
            if (e.target.checked) {
              file[type] = true;
            }
          }}
        />
        <span className="bcgov-dot" />
      </label>
    </div>
  );
};

const generateDropdownJSX = (items, fileName, setContinueBtnEnabled) => (
  <>
    <div className="table-value top-spacing">
      <Dropdown
        label="Description:"
        id="file-dropdown"
        items={translateItems(items)}
        onSelect={(val) => {
          filesToUpload.documents.forEach((f) => {
            const file = f;
            if (file.name === fileName) {
              file.type = items.find((item) => item.description === val).type;
            }
          });

          setContinueBtnEnabled(checkValidityOfUploadedFiles());
        }}
      />
    </div>
    <br />
  </>
);

const generateTable = (
  items,
  file,
  acceptedFiles,
  setAcceptedFiles,
  setContinueBtnEnabled
) => {
  if (!filesToUpload.documents.some((f) => f.name === file.name)) {
    filesToUpload.documents.push({ name: file.name, type: "AFF" });
    setContinueBtnEnabled(true);
  }

  return [
    {
      key: file.name,
      name: generateFileJSX(
        file,
        acceptedFiles,
        setAcceptedFiles,
        setContinueBtnEnabled
      ),
      value: generateDropdownJSX(items, file.name, setContinueBtnEnabled),
    },
    {
      key: `${file.name}-amendment`,
      name: (
        <div className="major-padding-left">Is this document an amendment?</div>
      ),
      value: generateRadioButtonJSX(
        file.name,
        "isAmendment",
        setContinueBtnEnabled
      ),
    },
    {
      key: `${file.name}-supreme`,
      name: (
        <div className="major-padding-left">
          Is this document that needs to go to supreme court scheduling?
        </div>
      ),
      value: generateRadioButtonJSX(
        file.name,
        "isSupremeCourtScheduling",
        setContinueBtnEnabled
      ),
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

const handleError = (error, setShowLoader, setContinueBtnEnabled) => {
  setShowLoader(false);
  setContinueBtnEnabled(true);
  errorRedirect(sessionStorage.getItem("errorUrl"), error);
};

export const uploadDocuments = (
  submissionId,
  acceptedFiles,
  setShowUpload,
  setShowLoader,
  setContinueBtnEnabled
) => {
  axios
    .post(
      `/submission/${submissionId}/documents`,
      generateFormData(acceptedFiles),
      {
        headers: { "Content-Type": "multipart/form-data" },
      }
    )
    .then(() => {
      axios
        .post(`/submission/${submissionId}/update-documents`, filesToUpload)
        .then(() => {
          filesToUpload = {
            documents: [],
          };
          setShowUpload(false);
        })
        .catch((error) =>
          handleError(error, setShowLoader, setContinueBtnEnabled)
        );
    })
    .catch((err) => handleError(err, setShowLoader, setContinueBtnEnabled));
};

const checkForDuplicateFiles = (
  droppedFiles,
  acceptedFiles,
  previouslyUploadedFiles
) => {
  let isDuplicate = false;

  for (let i = 0; i < acceptedFiles.length; i += 1) {
    isDuplicate = droppedFiles.some((df) => df.name === acceptedFiles[i].name);
    if (isDuplicate) return isDuplicate;
  }

  for (let i = 0; i < previouslyUploadedFiles.length; i += 1) {
    isDuplicate = droppedFiles.some(
      (df) => df.name === previouslyUploadedFiles[i].name
    );
    if (isDuplicate) return isDuplicate;
  }

  return isDuplicate;
};

export default function Upload({
  upload: {
    submissionId,
    courtData,
    setShowUpload,
    files: previouslyUploadedFiles,
  },
}) {
  const amendmentsSidecard = getSidecardData().amendments;
  const supremeCourtSchedulingSidecard =
    getSidecardData().supremeCourtScheduling;
  const [acceptedFiles, setAcceptedFiles] = useState([]);
  const [items, setItems] = useState([]);
  const [continueBtnEnabled, setContinueBtnEnabled] = useState(false);
  const [errorMessage, setErrorMessage] = useState(null);
  const [showLoader, setShowLoader] = useState(false);

  useEffect(() => {
    sessionStorage.setItem("currentPage", "upload");
    window.history.pushState(null, null, window.location.href);
    setDropdownItems(courtData, setItems);
  }, []);

  return (
    <div className="ct-upload page">
      <div className="content col-md-8">
        <h1>Document Upload</h1>
        <Dropzone
          onDrop={(droppedFiles) => {
            const hasDuplicates = checkForDuplicateFiles(
              droppedFiles,
              acceptedFiles,
              previouslyUploadedFiles
            );
            if (!hasDuplicates) {
              setAcceptedFiles(acceptedFiles.concat(droppedFiles));
              setErrorMessage(null);
            } else {
              setErrorMessage(
                "You cannot upload multiple files with the same name."
              );
            }
          }}
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
                  styling="bcgov-border-background"
                  element={
                    <Table
                      elements={generateTable(
                        items,
                        file,
                        acceptedFiles,
                        setAcceptedFiles,
                        setContinueBtnEnabled
                      )}
                    />
                  }
                />
                <br />
              </div>
            ))}
          </>
        )}
        {errorMessage && (
          <p className="error" data-testid="err-dup-file">
            {errorMessage}
          </p>
        )}
        <section className="buttons pt-2">
          <Button
            label="Cancel Upload"
            testId="cancel-upload-btn"
            onClick={() => {
              filesToUpload = {
                documents: [],
              };
              setShowUpload(false);
            }}
            styling="bcgov-normal-white btn"
          />
          <Button
            label="Continue"
            testId="continue-upload-btn"
            onClick={() => {
              setShowLoader(true);
              setContinueBtnEnabled(false);
              uploadDocuments(
                submissionId,
                acceptedFiles,
                setShowUpload,
                setShowLoader,
                setContinueBtnEnabled
              );
            }}
            styling="bcgov-normal-blue btn"
            disabled={!continueBtnEnabled}
            hasLoader={showLoader}
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
    submissionId: PropTypes.string.isRequired,
    courtData: PropTypes.object.isRequired,
    setShowUpload: PropTypes.func.isRequired,
    files: PropTypes.arrayOf(propTypes.file.isRequired).isRequired,
  }).isRequired,
};
