import React from "react";
import PropTypes from "prop-types";

import "./DocumentList.css";

export default function DocumentList({ documents }) {
  return (
    <div className="document-list">
      <div className="header">
        <span className="d-none d-lg-inline col-lg-3">Document Type</span>
        <span className="d-none d-lg-inline col-lg-4">File Name</span>
        <span className="d-none d-lg-inline col-lg-3">Status</span>
        <span className="d-none d-lg-inline col-lg-2">Action (s)</span>
      </div>
      <ul>
        {documents &&
          documents.map((document) => (
            <li key={document.identifier}>
              <span className="label col-sm-4 d-lg-none">Document Type:</span>
              <span className="col-sm-8 col-lg-3">{document.description}</span>
              <span className="label col-sm-4 d-lg-none">File Name:</span>
              <span className="col-sm-8 col-lg-4">{document.name}</span>
              <span className="label col-sm-4 d-lg-none">Status:</span>
              <span className="col-sm-8 col-lg-3">
                {document.status.description}
              </span>
              <span className="label col-sm-4 d-lg-none">Action (s):</span>
              <span className="col-sm-8 col-lg-2">withdraw</span>
            </li>
          ))}
      </ul>
    </div>
  );
}

DocumentList.propTypes = {
  documents: PropTypes.arrayOf(
    PropTypes.shape({
      identifier: PropTypes.string.isRequired,
      type: PropTypes.string.isRequired,
      name: PropTypes.string.isRequired,
      status: PropTypes.shape({
        description: PropTypes.string.isRequired,
      }),
    })
  ).isRequired,
};
