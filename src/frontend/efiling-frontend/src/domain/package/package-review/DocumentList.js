import React from "react";
import PropTypes from "prop-types";

import "./DocumentList.css";

export default function DocumentList({ documents }) {
  return (
    <div>
      <div className="header">
        <span className="d-none d-md-inline col-md-3">Document Type</span>
        <span className="d-none d-md-inline col-md-3">File Name</span>
        <span className="d-none d-md-inline col-md-3">Status</span>
        <span className="d-none d-md-inline col-md-3">Action (s)</span>
      </div>
      <ul>
        {documents &&
          documents.map((document) => (
            <li key={document.identifier}>
              <span className="label col-sm-4 d-md-none">Document Type:</span>
              <span className="col-sm-8 col-md-3">{document.type}</span>
              <span className="label col-sm-4 d-md-none">File Name:</span>
              <span className="col-sm-8 col-md-3">{document.name}</span>
              <span className="label col-sm-4 d-md-none">Status:</span>
              <span className="col-sm-8 col-md-3">
                {document.status.description}
              </span>
              <span className="label col-sm-4 d-md-none">Action (s):</span>
              <span className="col-sm-8 col-md-3">withdraw</span>
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
