import React from "react";
import PropTypes from "prop-types";
import FileListItem from "./FileListItem";

import "./FileList.scss";

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
          <FileListItem submissionId={submissionId} file={file} />
        ))}
      </ul>
    </div>
  );
}

FileList.propTypes = {
  submissionId: PropTypes.string.isRequired,
  files: PropTypes.arrayOf(
    PropTypes.shape({
      mimeType: PropTypes.string.isRequired,
      statutoryFeeAmount: PropTypes.number,
      documentProperties: PropTypes.shape({
        name: PropTypes.string.isRequired,
        type: PropTypes.string.isRequired,
      }),
    }).isRequired
  ).isRequired,
};
