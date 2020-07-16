import React, { useState } from "react";
import PropTypes from "prop-types";
import axios from "axios";
import { Header, Footer, Input, Button } from "shared-components";
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

const urlBody = {
  clientApplication: {
    displayName: "Demo App",
    type: "app"
  },
  filingPackage: {
    court: {
      location: "string",
      level: "string",
      class: "string",
      division: "string",
      fileNumber: "string",
      participatingClass: "string"
    },
    documents: [
      {
        name: "string",
        description: "string",
        type: "string"
      }
    ]
  },
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

const generatePackageData = files => {
  const formData = new FormData();
  const documentData = [];

  for (let i = 0; i < files.length; i += 1) {
    formData.append("files", files[i].file);
    documentData.push({
      name: files[i].file.name,
      description: "file description",
      type: "file type"
    });
  }

  const updatedUrlBody = {
    ...urlBody,
    filingPackage: {
      ...urlBody.filingPackage,
      documents: documentData
    }
  };

  return { formData, updatedUrlBody };
};

export const eFilePackage = (files, accountGuid, setErrorExists) => {
  const { formData, updatedUrlBody } = generatePackageData(files);

  axios
    .post("/submission/documents", formData, {
      headers: {
        "X-Auth-UserId": accountGuid,
        "Content-Type": "multipart/form-data"
      }
    })
    .then(response => {
      const { submissionId } = response.data;

      axios
        .post(`/submission/${submissionId}/generateUrl`, updatedUrlBody, {
          headers: { "X-Auth-UserId": accountGuid }
        })
        .then(({ data: { efilingUrl } }) => {
          window.open(efilingUrl, "_self");
        })
        .catch(() => {
          setErrorExists(true);
        });
    })
    .catch(() => {
      setErrorExists(true);
    });
};

export default function Home({ page: { header } }) {
  const [errorExists, setErrorExists] = useState(false);
  const [accountGuid, setAccountGuid] = useState(null);
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
          <Button
            onClick={() => eFilePackage(files, accountGuid, setErrorExists)}
            label="E-File my Package"
            styling="normal-blue btn"
            testId="generate-url-btn"
          />
          <br />
          {errorExists && (
            <p className="error">
              An error occurred while generating the URL. Please try again.
            </p>
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
