import React, { useState, useEffect } from "react";
import TextField from "@material-ui/core/TextField";
import { Button } from "shared-components";
import {
  deleteDocumentTypeConfiguration,
  getDocumentTypeConfigurations,
  submitNewDocumentTypeConfigurations,
  submitUpdatedDocumentTypeConfigurations,
} from "domain/documents/DocumentService";
import { isValidJSON } from "utils/JsonUtils";
import Toast from "components/toast/Toast";
import DocumentList from "./DocumentList";

import "./DocumentTypeEditor.scss";

export default function DocumentTypeEditor() {
  const [configurations, setConfigurations] = useState([]);
  const [showToast, setShowToast] = useState(false);
  const [errorMsg, setErrorMsg] = useState("");
  const [newConfigInput, setNewConfigInput] = useState("");
  const [invalidJsonError, setInvalidJsonError] = useState(false);
  const [submissionError, setSubmissionError] = useState(null);
  const [reloadConfigs, setReloadConfigs] = useState(false);
  const [showAdd, setShowAdd] = useState(false);
  const [isUpdate, setIsUpdate] = useState(false);
  const [isNew, setIsNew] = useState(false);

  const handleDeleteConfiguration = (id) => {
    deleteDocumentTypeConfiguration(id)
      .then(() => setReloadConfigs(!reloadConfigs))
      .catch(() => showError("Error: Could not delete configuration."));
  };

  const showError = (msg) => {
    setErrorMsg(msg);
    setShowToast(true);
  };

  const cancelConfig = () => {
    setShowAdd(false);
    setNewConfigInput("");
    setSubmissionError(null);
    setInvalidJsonError(false);
  }

  useEffect(() => {
    getDocumentTypeConfigurations()
      .then((data) => setConfigurations(data))
      .catch(() => showError("Error: Could not load configurations."));
  }, [reloadConfigs]);

  const successfullySubmitted = () => {
    setReloadConfigs(!reloadConfigs);
    setSubmissionError(null);
    setNewConfigInput("");
    setShowAdd(false);
  };

  const submitConfig = () => {
    if (isValidJSON(newConfigInput) && isNew) {
      submitNewDocumentTypeConfigurations(newConfigInput)
        .then(() => {
          successfullySubmitted();
        })
        .catch((error) => setSubmissionError(error.message));
    } else if (isValidJSON(newConfigInput) && isUpdate) {
      submitUpdatedDocumentTypeConfigurations(newConfigInput)
        .then(() => {
          successfullySubmitted();
        })
        .catch((error) => setSubmissionError(error.message));
    } else {
      setInvalidJsonError(true);
    }
  };

  return (
    <div className="document-type-editor">
      <h1>Document Type Configurations</h1>
      {showToast && (
        <Toast
          content={errorMsg}
          setShow={setShowToast}
        />
      )}

      <DocumentList
        configurations={configurations}
        setters={{ setNewConfigInput, showAdd, setShowAdd, setIsNew, setIsUpdate }} 
        onDelete={handleDeleteConfiguration}
      />

      {showAdd && (
        <>
          <TextField
            id="new-config-textfield"
            fullWidth
            multiline
            spellCheck={false}
            rows={25}
            rowsMax={200}
            placeholder="Input a new configuration JSON"
            variant="outlined"
            value={newConfigInput}
            onChange={(e) => {
              setNewConfigInput(e.target.value);
              setInvalidJsonError(false);
            }}
          />
          <br />

          {invalidJsonError && (
            <>
              <span className="error">Sorry this JSON is invalid!</span>
              <br />
            </>
          )}
          {submissionError && (
            <>
              <span className="error" data-testid="submission-error">
                {submissionError}
              </span>
              <br />
            </>
          )}
          <Button
            label="Cancel"
            styling="btn btn-secondary pull-left"
            onClick={cancelConfig}
          />
          <Button
            label="Submit"
            styling="btn btn-primary pull-right"
            onClick={submitConfig}
          />
        </>
      )}
    </div>
  );
}
