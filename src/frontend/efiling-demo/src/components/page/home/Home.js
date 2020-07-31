/* eslint-disable react/jsx-curly-newline */
import React, { useState } from "react";
import PropTypes from "prop-types";
import { v4 as uuidv4 } from "uuid";
import axios from "axios";
import { Header, Footer, Textarea, Button } from "shared-components";
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
    type: "app",
  },
  filingPackage: {
    court: {
      location: "string",
      level: "P",
      courtClass: "F",
      division: "string",
      fileNumber: "string",
      participatingClass: "string",
    },
    documents: [
      {
        name: "string",
        type: "AFF",
      },
    ],
  },
  navigation: {
    success: {
      url: `${window.location.origin}/efiling-demo/success`,
    },
    error: {
      url: `${window.location.origin}/efiling-demo/error`,
    },
    cancel: {
      url: `${window.location.origin}/efiling-demo/cancel`,
    },
  },
};

const transactionId = uuidv4();

const generatePackageData = (files, filingPackage) => {
  const formData = new FormData();
  const documentData = [];

  for (let i = 0; i < files.length; i += 1) {
    formData.append("files", files[i].file);

    const document = filingPackage.documents.find(
      (doc) => doc.name === files[i].file.name
    );
    if (!document || !document.type) return {};

    documentData.push({
      name: files[i].file.name,
      type: document.type,
    });
  }

  const updatedUrlBody = {
    ...urlBody,
    filingPackage: {
      ...filingPackage,
      documents: documentData,
    },
  };

  return { formData, updatedUrlBody };
};

export const eFilePackage = (files, setErrorExists, filingPackage) => {
  if (!files || files.length === 0) return false;

  const { formData, updatedUrlBody } = generatePackageData(
    files,
    filingPackage
  );

  if (!formData || !updatedUrlBody) return false;

  axios
    .post("/submission/documents", formData, {
      headers: {
        "X-Transaction-Id": transactionId,
        "Content-Type": "multipart/form-data",
      },
    })
    .then(({ data: { submissionId } }) => {
      axios
        .post(`/submission/${submissionId}/generateUrl`, updatedUrlBody, {
          headers: { "X-Transaction-Id": transactionId },
        })
        .then(({ data: { efilingUrl } }) => {
          window.open(`${efilingUrl}&transactionId=${transactionId}`, "_self");
        })
        .catch(() => setErrorExists(true));
    })
    .catch(() => setErrorExists(true));

  return true;
};

export default function Home({ page: { header } }) {
  const [errorExists, setErrorExists] = useState(false);
  const [filingPackage, setFilingPackage] = useState(null);
  const [files, setFiles] = useState([]);

  return (
    <main>
      <Header header={header} />
      <div className="page">
        <div className="content col-md-12">
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
            onChange={(val) => setFilingPackage(JSON.parse(val))}
          />
          <br />
          <br />
          <Button
            onClick={() => {
              const result = eFilePackage(files, setErrorExists, filingPackage);
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
    header: propTypes.header,
  }).isRequired,
};
