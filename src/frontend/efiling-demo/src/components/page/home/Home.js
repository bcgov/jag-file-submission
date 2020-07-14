import React, { useState } from "react";
import PropTypes from "prop-types";
import axios from "axios";
import { Header, Footer, Input, Button } from "shared-components";
import { propTypes } from "../../../types/propTypes";

// Import React FilePond
import { FilePond, registerPlugin } from "react-filepond";

// Import FilePond styles
import "filepond/dist/filepond.min.css";

// Import the Image EXIF Orientation and Image Preview plugins
import FilePondPluginImageExifOrientation from "filepond-plugin-image-exif-orientation";
import FilePondPluginImagePreview from "filepond-plugin-image-preview";
import "filepond-plugin-image-preview/dist/filepond-plugin-image-preview.css";

import "../page.css";

// Register the plugins
registerPlugin(FilePondPluginImageExifOrientation, FilePondPluginImagePreview);

const urlBody = {
  documentProperties: {
    type: "string",
    subType: "string",
    submissionAccess: {
      url: "string",
      verb: "GET",
      headers: {
        additionalProp1: "string",
        additionalProp2: "string",
        additionalProp3: "string"
      }
    }
  },
  navigation: {
    success: {
      url: "string"
    },
    error: {
      url: "string"
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

const generateUrl = (accountGuid, setErrorExists) => {
  axios
    .post(`/submission/generateUrl`, urlBody, {
      headers: { "X-Auth-UserId": accountGuid }
    })
    .then(({ data: { efilingUrl } }) => {
      window.open(efilingUrl, "_self");
    })
    .catch(() => {
      setErrorExists(true);
    });
};

const uploadFiles = files => {
  const formData = new FormData();

  console.log(files[0].file);

  for (let i = 0; i < files.length; i++) {
    formData.append(`files[${i}]`, files[i]);
  }

  axios
    .post(`http://demo0217811.mockable.io/`, formData)
    .then(response => {
      console.log(response);
    })
    .catch(() => {
      console.log("An error occurred with the upload. Please try again.");
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
          <Button
            onClick={() => generateUrl(accountGuid, setErrorExists)}
            label="Generate URL"
            styling="normal-blue btn"
            testId="generate-url-btn"
          />
          <br />
          <FilePond
            files={files}
            allowMultiple
            onupdatefiles={setFiles}
            labelIdle='Drag and Drop your files or <span class="filepond--label-action">Browse</span>'
          />
          <br />
          <Button
            onClick={() => uploadFiles(files)}
            label="Upload"
            styling="normal-blue btn"
          />
          <br />
          <FilePond
            files={files}
            allowMultiple
            onupdatefiles={setFiles}
            labelIdle='Drag and Drop your files or <span class="filepond--label-action">Browse</span>'
          />
          <br />
          <Button
            onClick={() => uploadFiles(files)}
            label="Upload"
            styling="normal-blue btn"
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
