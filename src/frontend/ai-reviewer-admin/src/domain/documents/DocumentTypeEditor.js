import React, { useState, useEffect } from "react";
import TextField from "@material-ui/core/TextField";
import { Button } from "shared-components";
import {
  getDocumentTypeConfigurations,
  submitDocumentTypeConfigurations,
} from "domain/documents/DocumentService";
import { isValidJSON } from "utils/JsonUtils";
import DocumentList from "./DocumentList";

import "./DocumentTypeEditor.scss";

export default function DocumentTypeEditor() {
  const [configurations, setConfigurations] = useState([]);
  const [newConfigInput, setNewConfigInput] = useState("");
  const [invalidJsonError, setInvalidJsonError] = useState(false);
  const [submissionError, setSubmissionError] = useState("");
  const [reloadConfigs, setReloadConfigs] = useState(false);

  useEffect(() => {
    getDocumentTypeConfigurations()
      .then((data) => {
        setConfigurations(data);
      })
      .catch((error) => console.log(error));
  }, [reloadConfigs]);

  const submitNewConfig = () => {
    if (isValidJSON(newConfigInput)) {
      submitDocumentTypeConfigurations(newConfigInput)
        .then(() => {
          setReloadConfigs(!reloadConfigs);
          setSubmissionError("");
        })
        .catch((error) => setSubmissionError(error.message));
    } else {
      setInvalidJsonError(true);
    }
  };

  return (
    <div className="document-type-editor">
      <h1>Document Type Configurations</h1>
      <DocumentList configurations={configurations} />
      <br />

      <TextField
        id="new-config-textfield"
        fullWidth
        multiline
        rows={25}
        rowsMax={200}
        placeholder="Input a new configuration JSON"
        variant="outlined"
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
          <span className="error">{submissionError}</span>
          <br />
        </>
      )}
      <Button
        label="Submit"
        styling="bcgov-normal-blue btn new-config-submit"
        onClick={submitNewConfig}
      />
    </div>
  );
}
