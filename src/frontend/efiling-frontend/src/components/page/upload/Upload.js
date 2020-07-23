import React from "react";
import { useDropzone } from "react-dropzone";

import "./Upload.css";

export default function Upload() {
  const { acceptedFiles, getRootProps, getInputProps } = useDropzone();

  const files = acceptedFiles.map(file => (
    <li key={file.path}>
      {file.path} - {file.size} bytes
    </li>
  ));

  return (
    <div className="page">
      <div className="content col-md-8">
        <h1>Document Upload</h1>
        <p>
          Upload any additional or supporting documents here (optional) or
          continue if you have none.
        </p>
        <div {...getRootProps({ className: "dropzone-outer-box" })}>
          <div className="dropzone-inner-box">
            <input {...getInputProps()} />
            <span>
              <h2>
                Drag and drop or{" "}
                <span className="file-href">choose documents</span>
              </h2>
              <div>
                <span>to upload additional documents to your package</span>
                <br />
                <span>(max 10mb per document)</span>
              </div>
            </span>
          </div>
        </div>
        <br />
        {files.length > 0 && (
          <h2>Select Document Type for each uploaded document to continue</h2>
        )}
      </div>
    </div>
  );
}
