import React, { useState, useEffect } from "react";
import TextField from '@material-ui/core/TextField';
import {Button} from 'shared-components'
import { getDocumentTypeConfigurations } from "domain/documents/DocumentService";

import "./DocumentTypeEditor.scss";
import DocumentList from "./DocumentList";

export default function DocumentTypeEditor() {
  const [configurations, setConfigurations] = useState([]);
  const [newConfigJson, setNewConfigJson] = useState({});
  const [newConfigInput, setNewConfigInput] = useState("")

  useEffect(() => {
    getDocumentTypeConfigurations()
      .then((data) => {
        setConfigurations(data);
      })
      .catch((error) => console.log(`${error}`));
  }, []);

  return (
    <div className="document-type-editor">
      <h1>Document Type Configurations</h1>
      <DocumentList configurations={configurations} />
      <br />

      <TextField
          id="new-config-textfield"
          label=""
          multiline
          rows={25}
          placeholder="Input a new configuration JSON"
          variant="outlined"
        />
      <br />
      <Button label="Submit" styling="bcgov-normal-blue btn new-config-submit" />

    </div>
    );
}
