/* eslint-disable react/function-component-definition, import/no-named-as-default, import/no-named-as-default-member */
import React from "react";
import PropTypes from "prop-types";
import FileListItem from "./FileListItem";
import { propTypes } from "../../../types/propTypes";

import "./FileList.scss";

const hash = require("object-hash");

export default function FileList({ submissionId, files }) {
  return (
    <div className="ct-file-list ">
      <div className="header">
        <span className="d-none d-lg-inline col-lg-6">Document Name</span>
        <span className="d-none d-lg-inline col-lg-3">Statutory Fee</span>
        <span className="d-none d-lg-inline col-lg-3">Document Type</span>
      </div>
      <ul>
        {files.map((file) => (
          <FileListItem
            submissionId={submissionId}
            file={file}
            key={hash(file)}
          />
        ))}
      </ul>
    </div>
  );
}

FileList.propTypes = {
  submissionId: PropTypes.string.isRequired,
  files: PropTypes.arrayOf(propTypes.file.isRequired).isRequired,
};
