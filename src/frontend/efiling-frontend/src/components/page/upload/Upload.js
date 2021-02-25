/* eslint-disable react/jsx-props-no-spreading, react/jsx-curly-newline */
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
import { getSidecardData } from "../../../modules/helpers/sidecardData";
import { errorRedirect } from "../../../modules/helpers/errorRedirect";
import { propTypes } from "../../../types/propTypes";

import "./Upload.scss";
import PackageConfirmation from "../../../domain/package/package-confirmation/PackageConfirmation";
import { getDocumentTypes } from "../../../domain/documents/DocumentService";

const filesToUpload = {
  documents: [],
};

const checkValidityOfUploadedFiles = () => {
  const isValid = (currentValue) =>
    Object.prototype.hasOwnProperty.call(currentValue, "isAmendment") &&
    Object.prototype.hasOwnProperty.call(
      currentValue,
      "isSupremeCourtScheduling"
    );

  if (
    filesToUpload.documents.length > 0 &&
    filesToUpload.documents.every(isValid)
  )
    return true;
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

const generateRadioButtonJSX = (fileName, type, setContinueBtnEnabled) => {
  const file = identifySelectedFile(fileName);

  return (
    <div className="table-value">
      <div className="minor-margin-right">
        <Radio
          id={`no-${type}-${fileName}`}
          name={`${type}-${fileName}`}
          label="No"
          onSelect={() => {
            file[type] = false;
            setContinueBtnEnabled(checkValidityOfUploadedFiles());
          }}
        />
      </div>
      <Radio
        id={`yes-${type}-${fileName}`}
        name={`${type}-${fileName}`}
        label="Yes"
        onSelect={() => {
          file[type] = true;
          setContinueBtnEnabled(checkValidityOfUploadedFiles());
        }}
      />
    </div>
  );
};

const generateDropdownJSX = (items, fileName, setContinueBtnEnabled) => (
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
    setContinueBtnEnabled(checkValidityOfUploadedFiles());
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
  setShowPackageConfirmation,
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
        .then(() => setShowPackageConfirmation(true))
        .catch((error) =>
          handleError(error, setShowLoader, setContinueBtnEnabled)
        );
    })
    .catch((err) => handleError(err, setShowLoader, setContinueBtnEnabled));
};

const checkForDuplicateFiles = (droppedFiles, acceptedFiles) => {
  let isDuplicate = false;

  for (let i = 0; i < acceptedFiles.length; i += 1) {
    isDuplicate = droppedFiles.some((df) => df.name === acceptedFiles[i].name);
    if (isDuplicate) break;
  }

  return isDuplicate;
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
  const [continueBtnEnabled, setContinueBtnEnabled] = useState(false);
  const [errorMessage, setErrorMessage] = useState(null);
  const [showLoader, setShowLoader] = useState(false);

  useEffect(() => {
    sessionStorage.setItem("currentPage", "upload");
    window.history.pushState(null, null, window.location.href);
    setDropdownItems(courtData, setItems);
  }, []);

  if (showPackageConfirmation) {
    return (
      <PackageConfirmation
        packageConfirmation={{ confirmationPopup, submissionId }}
        csoAccountStatus={{ isNew: false }}
      />
    );
  }

  return (
    <div className="ct-upload page">
      <div className="content col-md-8">
        <h1>Document Upload</h1>
        <Dropzone
          onDrop={(droppedFiles) => {
            const hasDuplicates = checkForDuplicateFiles(
              droppedFiles,
              acceptedFiles
            );
            if (!hasDuplicates) {
              setAcceptedFiles(acceptedFiles.concat(droppedFiles));
              setErrorMessage(null);
            } else
              setErrorMessage(
                "You cannot upload multiple files with the same name."
              );
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
        {errorMessage && <p className="error">{errorMessage}</p>}
        <section className="buttons pt-2">
          <Button
            label="Cancel Upload"
            testId="cancel-upload-btn"
            onClick={() => setShowPackageConfirmation(true)}
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
                setShowPackageConfirmation,
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
    confirmationPopup: propTypes.confirmationPopup,
    submissionId: PropTypes.string.isRequired,
    courtData: PropTypes.object.isRequired,
  }).isRequired,
};
