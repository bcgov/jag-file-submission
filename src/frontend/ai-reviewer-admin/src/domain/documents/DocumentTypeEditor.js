import React, { useState, useEffect } from "react";
import { getDocumentTypeConfigurations } from "domain/documents/DocumentService";
import DocumentList from "./DocumentList";

import "./DocumentTypeEditor.scss";

export default function DocumentTypeEditor() {
  const [configurations, setConfigurations] = useState([]);

  useEffect(() => {
    getDocumentTypeConfigurations()
      .then((data) => {
        setConfigurations(data);
      })
      .catch(/* (error) => console.log(`error:${error}`) */);
  }, []);

  return (
    <div className="document-type-editor">
      <h1>Document Type Configurations</h1>
      <DocumentList configurations={configurations} />
    </div>
    );
}
