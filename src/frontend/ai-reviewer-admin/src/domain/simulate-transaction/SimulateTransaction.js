import React, { useEffect, useState } from "react";
import Dropzone from "react-dropzone";
import { DisplayBox, Button, Dropdown } from "shared-components";
import { v4 as uuidv4 } from "uuid";
import { MdDescription } from "react-icons/md";
import {
  getProcessedDocument,
  submitFileForExtraction,
} from "./SimulateTransactionService";
import { getDocumentTypeConfigurations } from "domain/documents/DocumentService";
import Toast from "components/toast/Toast";
import "./SimulateTransaction.scss";

function SimulateTransaction() {
  const [files, setFiles] = useState([]);
  const [docTypes, setDocTypes] = useState([]);
  const [errorMsg, setErrorMsg] = useState("");
  const [showErrorToast, setShowErrorToast] = useState(false);
  const [showProcessedToast, setShowProcessedToast] = useState(false);

  const transactionId = uuidv4();

  useEffect(() => {
    getDocumentTypeConfigurations()
      .then((data) => setDocTypes(data.map((obj) => obj.documentType.type)))
      .catch((err) => showError("Error: Could not load configurations."));
  }, []);

  const showError = (msg) => {
    setErrorMsg(msg);
    setShowErrorToast(true);
  };

  const handleDrop = (droppedFiles) => {
    droppedFiles[0].data = { type: "" };
    setFiles(droppedFiles);
  };

  const selectFileType = (e) => {
    const newFiles = [...files];
    newFiles[0].data.type = e;
    setFiles(newFiles);
  };

  const submitDocument = () => {
    submitFileForExtraction(files[0], transactionId)
      .then((res) =>
        getProcessedDocument(res.document.documentId, res.extract.transactionId)
          .then((res) => setShowProcessedToast(true))
          .catch((err) => showError(err.message))
      )
      .catch((err) => showError(err.message));
  };

  const generateDisplayBoxElement = (file) => {
    return (
      <>
        <div className="col d-flex align-items-center">
          <h4>{file.name}</h4>
        </div>
        <div className="col">
          <Dropdown data-testid="dropdown" items={docTypes} onSelect={(e) => selectFileType(e)} />
        </div>
        <div className="col"></div>
      </>
    );
  };

  return (
    <div className="simulate-transaction-ct">
      <h1>Simulate A Transaction</h1>
      <br />
      <Dropzone onDrop={(droppedFiles) => handleDrop(droppedFiles)}>
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
              </span>
            </div>
          </div>
        )}
      </Dropzone>
      <br />

      {showErrorToast && <Toast content={errorMsg} setShow={setShowErrorToast} />}
      {files.length > 0 && (
        <>
          <h2>Uploaded Document</h2>
          <br />
          {files.map((file) => (
            <div key={file.name}>
              <DisplayBox
                styling="bcgov-border-background bcgov-display-file"
                icon={
                  <div style={{ color: "rgb(252, 186, 25)" }}>
                    <MdDescription size={32} />
                  </div>
                }
                element={generateDisplayBoxElement(file)}
              />
            </div>
          ))}
        </>
      )}
      <br />
      {showProcessedToast && (
        <Toast
          content="Document Processed Successfuly"
          setShow={setShowProcessedToast}
          colorClass="alert-success"
        />
      )}
      <Button
        label="Submit"
        styling="bcgov-normal-blue btn"
        onClick={submitDocument}
      />
    </div>
  );
}

export default SimulateTransaction;
