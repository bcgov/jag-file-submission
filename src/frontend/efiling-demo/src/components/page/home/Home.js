/* eslint-disable react/jsx-curly-newline */
import React, { useState } from "react";
import PropTypes from "prop-types";
import axios from "axios";
import { Header, Footer, Input, Textarea, Button } from "shared-components";
import { FilePond, registerPlugin } from "react-filepond";

// Import FilePond styles
import "filepond/dist/filepond.min.css";

// Import the Image EXIF Orientation and Image Preview plugins
import FilePondPluginImageExifOrientation from "filepond-plugin-image-exif-orientation";
import FilePondPluginImagePreview from "filepond-plugin-image-preview";
import "filepond-plugin-image-preview/dist/filepond-plugin-image-preview.css";

import { propTypes } from "../../../types/propTypes";
import "../page.css";

// Register the plugins
registerPlugin(FilePondPluginImageExifOrientation, FilePondPluginImagePreview);

// Note: Some of these values are temporarily hard-coded
const urlBody = {
  clientApplication: {
    displayName: "Demo App",
    type: "app"
  },
  filingPackage: {},
  navigation: {
    success: {
      url: `${window.location.origin}/efiling-demo/success`
    },
    error: {
      url: `${window.location.origin}/efiling-demo/error`
    },
    cancel: {
      url: `${window.location.origin}/efiling-demo/cancel`
    }
  }
};

const input = {
  label: "Account GUID",
  id: "textInputId",
  styling: "editable-white",
  isRequired: true,
  placeholder: "77da92db-0791-491e-8c58-1a969e67d2fa"
};

const generatePackageData = (files, filingPackage) => {
  const formData = new FormData();
  const documentData = [];

  for (let i = 0; i < files.length; i += 1) {
    formData.append("files", files[i].file);

    const document = filingPackage.documents.find(
      doc => doc.name === files[i].file.name
    );
    if (!document || !document.type) return {};

    documentData.push({
      name: files[i].file.name,
      type: document.type
    });
  }

  const updatedUrlBody = {
    ...urlBody,
    filingPackage: {
      ...filingPackage,
      documents: documentData
    }
  };

  return { formData, updatedUrlBody };
};

export const eFilePackage = (
  files,
  accountGuid,
  setErrorExists,
  filingPackage
) => {
  if (!files || files.length === 0) return false;

  const { formData, updatedUrlBody } = generatePackageData(
    files,
    filingPackage
  );

  if (!formData || !updatedUrlBody) return false;

  axios
    .post("/submission/documents", formData, {
      headers: {
        "X-Auth-UserId": accountGuid,
        "Content-Type": "multipart/form-data"
      }
    })
    .then(({ data: { submissionId } }) => {
      axios
        .post(`/submission/${submissionId}/generateUrl`, updatedUrlBody, {
          headers: { "X-Auth-UserId": accountGuid }
        })
        .then(({ data: { efilingUrl } }) => {
          window.open(efilingUrl, "_self");
        })
        .catch(() => setErrorExists(true));
    })
    .catch(() => setErrorExists(true));
};

export default function Home({ page: { header } }) {
  const [errorExists, setErrorExists] = useState(false);
  const [accountGuid, setAccountGuid] = useState(null);
  const [filingPackage, setFilingPackage] = useState(null);
  const [files, setFiles] = useState([]);

  return (
    <main>
      <Header header={header} />
      <div className="page">
        <div className="content col-md-12">
          <Input input={input} onChange={setAccountGuid} />
          <br />
          <br />
          <FilePond
            files={files}
            allowMultiple
            onupdatefiles={setFiles}
            labelIdle='Drag and Drop your files or <span class="filepond--label-action">Browse</span>'
          />
          <br />
          <Textarea
            id="1"
            label="Provide filing package JSON data:"
            onChange={val => setFilingPackage(JSON.parse(val))}
          />
          <br />
          <br />
          <Button
            onClick={() => {
              const result = eFilePackage(
                files,
                accountGuid,
                setErrorExists,
                filingPackage
              );
              if (!result) setErrorExists(true);
            }}
            label="E-File my Package"
            styling="normal-blue btn"
            testId="generate-url-btn"
          />
          <br />
          <br />
          {errorExists && (
            <>
              <span className="error">
                An error occurred while eFiling your package. Please make sure
                you upload at least one file and try again.
              </span>
              <br />
              <span className="error">
                Also, ensure your JSON is valid and that the document file
                name(s) match with your uploaded documents.
              </span>
            </>
          )}
        </div>
      </div>
      <Footer />
    </main>
  );
}

Home.propTypes = {
  page: PropTypes.shape({
    header: propTypes.header
  }).isRequired
};
