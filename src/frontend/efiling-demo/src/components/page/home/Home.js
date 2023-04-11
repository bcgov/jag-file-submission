/* eslint-disable react/jsx-props-no-spreading, no-param-reassign, no-return-assign */
import React, { useState } from "react";
import PropTypes from "prop-types";
import { MdDescription } from "react-icons/md";
import { AiOutlineClose } from "react-icons/ai";
import Dropzone from "react-dropzone";
import { v4 as uuidv4 } from "uuid";
import axios from "axios";
import {
  Header,
  Footer,
  Textarea,
  Button,
  DisplayBox,
  Table,
  Dropdown,
} from "shared-components";
import docTypeList from "../../../typeList.json";
import actionStatusList from "../../../actionStatusList.json";
import { getJWTData } from "../../../modules/authentication-helper/authenticationHelper";

import { propTypes } from "../../../types/propTypes";
import "../page.css";
import "./Home.css";

const defaultJson = {
  packageNumber: null,
  court: {
    location: "4801",
    level: "P",
    courtClass: "F",
    division: "I",
  },
  parties: [
    {
      partyType: "IND",
      roleType: "APP",
      firstName: "efile",
      middleName: "test",
      lastName: "qa",
    },
  ],
};

// Note: Some of these values are temporarily hard-coded
const urlBody = {
  clientAppName: "Demo App",
  filingPackage: {},
  navigationUrls: {
    success: `${window.location.origin}/success`,
    error: `${window.location.origin}/error`,
    cancel: `${window.location.origin}/cancel`,
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

    documentData.push({
      name: files[i].name,
      type: files[i].data.type,
      isSupremeCourtScheduling: files[i].data.isSupremeCourtScheduling,
      isAmendment: files[i].data.isAmendment,
      documentId: uuidv4(),
      actionDocument: {
        id: files[i].data.actionDocument.id,
        status: files[i].data.actionDocument.status,
        type: files[i].data.actionDocument.type,
      },
      data: { test: "somedata" },
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
  if (files.length === 0) return false;
  if (filingPackage === null) {
    alert("The JSON you provided is invalid, please update and try again");
    return false;
  }
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
  const [filingPackage, setFilingPackage] = useState(defaultJson);
  const [files, setFiles] = useState([]);
  const [submitBtnEnabled, setSubmitBtnEnabled] = useState(true);
  const [showLoader, setShowLoader] = useState(false);

  const docTypeListIds = docTypeList.map((obj) => obj.id);
  const actionStatusIds = actionStatusList.map((obj) => obj.id);

  const onSelect = (e, file) => {
    const newFiles = [...files];
    newFiles.forEach((f) => {
      if (f.name === file.name) {
        f.data.type = e;
      }
    });
    setFiles(newFiles);
  };

  const onSelectStatus = (e, file) => {
    const newFiles = [...files];
    newFiles.forEach((f) => {
      if (f === file) {
        f.data.actionDocument.status = e;
      }
    });
    setFiles(newFiles);
  };

  const onChange = (e, key, file) => {
    const newFiles = [...files];
    newFiles.forEach((f) => {
      if (f.name === file.name) {
        f.data[key] = e.target.checked;
      }
    });
    setFiles(newFiles);
  };

  const generateTable = (file, data) => [
    {
      name: (
        <div style={{ width: "55%" }}>
          <span
            onKeyDown={() => {}}
            role="button"
            tabIndex={0}
            className="file-href"
            onClick={() => {}}
            data-test-id="uploaded-file"
          >
            {file.name}
          </span>
        </div>
      ),
      value: (
        <div className="d-flex justify-content-around bcgov-row">
          {data.map((option) => (
            <div key={option.name} className="d-flex align-items-center">
              <h5 className="ml-5 mr-2 mt-3">{option.name}</h5>
              {option.value}
            </div>
          ))}
        </div>
      ),
    },
  ];

  const generateTableData = (file) => {
    const data = [
      {
        name: "Type:",
        value: (
          <Dropdown
            id="doctype-dropdown"
            data-testid="dropdown"
            items={docTypeListIds}
            onSelect={(e) => onSelect(e, file)}
          />
        ),
        isNameBold: true,
      },
      {
        name: "Supreme Court Scheduling",
        value: (
          <input
            type="checkbox"
            id="scs"
            onChange={(e) => onChange(e, "isSupremeCourtScheduling", file)}
          />
        ),
        isNameBold: true,
      },
      {
        name: "Ammendment",
        value: (
          <input
            type="checkbox"
            id="am"
            onChange={(e) => onChange(e, "isAmmendment", file)}
          />
        ),
        isNameBold: true,
      },
      {
        name: "Action Document",
        value: (
          <Dropdown
            id="action-dropdown"
            items={actionStatusIds}
            onSelect={(e) => onSelectStatus(e, file)}
            testId="actionDropdown"
          />
        ),
        isNameBold: true,
      },
      {
        name: "",
        value: (
          <button
            data-testid="close-button"
            type="submit"
            onClick={() => {
              setFiles(files.filter((item) => item.name !== file.name));
            }}
            style={{ backgroundColor: "#00000000", border: "none" }}
          >
            <AiOutlineClose color="red" />
          </button>
        ),
      },
    ];

    return generateTable(file, data);
  };

  return (
    <main>
      <Header header={header} />
      <div className="page">
        <div className="content col-md-12">
          <h1>Welcome to the eFiling Demo Client</h1>
          <br />
          <Dropzone
            onDrop={(droppedFiles) => {
              droppedFiles.forEach(
                (file) =>
                  (file.data = {
                    type: "",
                    isSupremeCourtScheduling: false,
                    isAmmendment: false,
                    actionDocument: {
                      id: 0,
                      status: "SUB",
                      type: "",
                    },
                  })
              );
              setFiles(files.concat(droppedFiles));
            }}
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
                <div key={file.name}>
                  <DisplayBox
                    styling="bcgov-border-background bcgov-display-file"
                    icon={
                      <div style={{ color: "rgb(252, 186, 25)" }}>
                        <MdDescription size={32} />
                      </div>
                    }
                    element={
                      <Table
                        elementStyles={{
                          columnStyle: "bcgov-vertical-middle bcgov-fill-width",
                        }}
                        elements={generateTableData(file)}
                      />
                    }
                  />
                  <br />
                </div>
              ))}
            </>
          )}

          <br />
          <Textarea
            id="1"
            label="Provide filing package JSON data:"
            onChange={(val) => {
              let parsedVal = null;
              if (val === null || val === "") {
                parsedVal = defaultJson;
              } else {
                try {
                  parsedVal = JSON.parse(val);
                } catch (e) {
                  // empty
                }
              }
              setFilingPackage(parsedVal);
            }}
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
