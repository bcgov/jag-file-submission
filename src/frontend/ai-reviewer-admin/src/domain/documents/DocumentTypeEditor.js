import React, { useState, useEffect } from "react";
import { getDocumentTypeConfigurations } from "domain/documents/DocumentService";

import "./DocumentTypeEditor.scss";

export default function DocumentTypeEditor() {
  const [configurations, setConfigurations] = useState("");

  useEffect(() => {
    getDocumentTypeConfigurations("RCC")
      .then((data) => setConfigurations(JSON.stringify(data, null, "\t")))
      .catch(/* (error) => console.log(`error:${error}`) */);
  }, []);

  return <div className="list">{configurations}</div>;
}
