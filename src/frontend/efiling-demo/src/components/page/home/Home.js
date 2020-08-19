/* eslint-disable react/jsx-curly-newline, camelcase */
import React, { useState, useEffect } from "react";
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
      location: "1211",
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

const setRequestHeaders = (token, transactionId) => {
  // Use interceptor to inject the transactionId and token to all requests
  axios.interceptors.request.use((request) => {
    request.headers["X-Transaction-Id"] = transactionId;
    request.headers.Authorization = `Bearer ${token}`;
    return request;
  });
};

const getToken = (
  token,
  setToken,
  setErrorExists,
  keycloakClientId,
  keycloakBaseUrl,
  keycloakRealm,
  keycloakClientSecret
) => {
  if (token) return;

  const payloadString = `client_id=${keycloakClientId}&grant_type=client_credentials&client_secret=${keycloakClientSecret}`;

  axios
    .post(
      `${keycloakBaseUrl}/realms/${keycloakRealm}/protocol/openid-connect/token`,
      payloadString,
      {
        headers: {
          "Content-Type": "application/x-www-form-urlencoded",
        },
      }
    )
    .then(({ data: { access_token } }) => {
      setToken(access_token);
    })
    .catch(() => setErrorExists(true));
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

export const eFilePackage = (token, files, setErrorExists, filingPackage) => {
  setRequestHeaders(token, transactionId);
  if (!files || files.length === 0) return false;

  const { formData, updatedUrlBody } = generatePackageData(
    files,
    filingPackage
  );

  if (!formData || !updatedUrlBody) return false;

  axios
    .post("/submission/documents", formData, {
      headers: { "Content-Type": "multipart/form-data" },
    })
    .then(({ data: { submissionId } }) => {
      axios
        .post(`/submission/${submissionId}/generateUrl`, updatedUrlBody)
        .then(({ data: { efilingUrl } }) => {
          window.open(`${efilingUrl}`, "_self");
        })
        .catch(() => setErrorExists(true));
    })
    .catch(() => setErrorExists(true));

  return true;
};

export default function Home({ page: { header } }) {
  const [errorExists, setErrorExists] = useState(false);
  const [filingPackage, setFilingPackage] = useState(null);
  const [token, setToken] = useState(null);
  const [files, setFiles] = useState([]);
  const [submitBtnEnabled, setSubmitBtnEnabled] = useState(false);
  const [showLoader, setShowLoader] = useState(false);

  const keycloakClientId = sessionStorage.getItem("demoKeycloakClientId");
  const keycloakBaseUrl = sessionStorage.getItem("demoKeycloakUrl");
  const keycloakRealm = sessionStorage.getItem("demoKeycloakRealm");
  const keycloakClientSecret = sessionStorage.getItem(
    "demoKeycloakClientSecret"
  );

  const checkSubmitEnabled = () => {
    if (
      files.length > 0 &&
      filingPackage &&
      JSON.stringify(filingPackage) !== "{}"
    ) {
      setSubmitBtnEnabled(true);
    }
  };

  useEffect(() => {
    checkSubmitEnabled();
  }, [files, filingPackage]);

  useEffect(() => {
    getToken(
      token,
      setToken,
      setErrorExists,
      keycloakClientId,
      keycloakBaseUrl,
      keycloakRealm,
      keycloakClientSecret
    );
  }, [token]);

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
              setShowLoader(true);
              setSubmitBtnEnabled(false);
              eFilePackage(token, files, setErrorExists, filingPackage);
            }}
            label="E-File my Package"
            styling="normal-blue btn"
            testId="generate-url-btn"
            hasLoader={showLoader}
            disabled={!submitBtnEnabled}
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
