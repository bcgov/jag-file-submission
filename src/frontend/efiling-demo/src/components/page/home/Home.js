/* eslint-disable react/jsx-curly-newline, camelcase, react/jsx-props-no-spreading */
import React, { useState } from "react";
import PropTypes from "prop-types";
import Dropzone from "react-dropzone";
import { v4 as uuidv4 } from "uuid";
import axios from "axios";
import { Header, Footer, Textarea, Button } from "shared-components";
import { getJWTData } from "../../../modules/authentication-helper/authenticationHelper";

import { propTypes } from "../../../types/propTypes";
import "../page.css";
import "./Home.css";

// Note: Some of these values are temporarily hard-coded
const urlBody = {
  clientAppName: "Demo App",
  filingPackage: {
    court: {
      location: "1211",
      level: "P",
      courtClass: "F",
      division: "I",
      fileNumber: "1234",
      participatingClass: "string",
    },
    documents: [
      {
        name: "string",
        type: "AFF",
        data: {},
        md5: "string",
      },
    ],
    parties: [
      {
        partyType: "IND",
        roleType: "CLA",
        firstName: "Bob",
        middleName: "Alan",
        lastName: "Ross",
      },
    ],
  },
  navigationUrls: {
    success: `${window.location.origin}/efiling-demo/success`,
    error: `${window.location.origin}/efiling-demo/error`,
    cancel: `${window.location.origin}/efiling-demo/cancel`,
  },
};

const setRequestHeaders = (transactionId) => {
  // Use interceptor to inject the transactionId and token to all requests
  axios.interceptors.request.use((request) => {
    const token = localStorage.getItem("jwt");
    request.headers["X-Transaction-Id"] = transactionId;
    request.headers["X-User-Id"] = getJWTData()["universal-id"];
    request.headers.Authorization = `Bearer ${token}`;
    return request;
  });
};

const transactionId = uuidv4();

const generatePackageData = (files, filingPackage) => {
  const formData = new FormData();
  const documentData = [];

  for (let i = 0; i < files.length; i += 1) {
    formData.append("files", files[i]);

    const document = filingPackage.documents.find(
      (doc) => doc.name === files[i].name
    );
    if (!document || !document.type) return {};

    documentData.push({
      name: files[i].name,
      type: document.type,
      data: document.data,
      md5: document.md5,
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

const eFilePackage = (
  files,
  setErrorExists,
  filingPackage,
  setSubmitBtnEnabled,
  setShowLoader
) => {
  setRequestHeaders(transactionId);
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
        .catch(() => {
          setErrorExists(true);
          setSubmitBtnEnabled(true);
          setShowLoader(false);
        });
    })
    .catch(() => {
      setErrorExists(true);
      setSubmitBtnEnabled(true);
      setShowLoader(false);
    });

  return true;
};

export default function Home({ page: { header } }) {
  const [errorExists, setErrorExists] = useState(false);
  const [filingPackage, setFilingPackage] = useState(null);
  const [files, setFiles] = useState([]);
  const [submitBtnEnabled, setSubmitBtnEnabled] = useState(true);
  const [showLoader, setShowLoader] = useState(false);

  return (
    <main>
      <Header header={header} />
      <div className="page">
        <div className="content col-md-12">
          <h1>Welcome to the eFiling Demo Client</h1>
          <br />
          <Dropzone
            onDrop={(droppedFiles) => setFiles(files.concat(droppedFiles))}
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
                  </span>
                </div>
              </div>
            )}
          </Dropzone>
          <br />
          {files.length > 0 && (
            <>
              <h2>Uploaded Files</h2>
              <br />
              {files.map((file) => (
                <p key={file.name}>{file.name}</p>
              ))}
            </>
          )}
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
              const result = eFilePackage(
                files,
                setErrorExists,
                filingPackage,
                setSubmitBtnEnabled,
                setShowLoader
              );
              if (!result) {
                setErrorExists(true);
                setSubmitBtnEnabled(true);
                setShowLoader(false);
              }
            }}
            label="E-File my Package"
            styling="bcgov-normal-blue btn"
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
