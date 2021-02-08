import React from "react";
import PropTypes from "prop-types";

import { errorRedirect } from "../../../modules/helpers/errorRedirect";
import {
  downloadSubmittedDocument,
  withdrawSubmittedDocument,
} from "./PackageReviewService";
import "./DocumentList.scoped.css";

export default function DocumentList({
  packageId,
  documents,
  reloadDocumentList,
}) {
  function handleClickFile(document) {
    downloadSubmittedDocument(packageId, document).catch((err) =>
      errorRedirect(sessionStorage.getItem("errorUrl"), err)
    );
  }

  function handleKeyDownFile(e, document) {
    if (e && e.keyCode === 13) {
      downloadSubmittedDocument(packageId, document).catch((err) =>
        errorRedirect(sessionStorage.getItem("errorUrl"), err)
      );
    }
  }

  function withdrawDocument(document) {
    withdrawSubmittedDocument(packageId, document)
      .then(() => reloadDocumentList())
      .catch((err) => errorRedirect(sessionStorage.getItem("errorUrl"), err));
  }

  function handleClickDeleteFile(document) {
    withdrawDocument(document);
  }

  function handleKeyDownDeleteFile(e, document) {
    if (e && e.keyCode === 13) {
      withdrawDocument(document);
    }
  }

  return (
    <div>
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
              <span className="col-sm-8 col-lg-4 file-cell">
                <span
                  className="file-href"
                  role="button"
                  tabIndex={0}
                  onClick={() => handleClickFile(document)}
                  onKeyDown={(e) => handleKeyDownFile(e, document)}
                >
                  {document.documentProperties.name}
                </span>
              </span>
              <span className="label col-sm-4 d-lg-none">Status:</span>
              <span className="col-sm-8 col-lg-3">
                {document.status.description}
              </span>
              <span className="label col-sm-4 d-lg-none">Action (s):</span>
              <span className="col-sm-8 col-lg-2 file-cell">
                <span
                  id={`withdraw_${document.identifier}`}
                  className="file-href"
                  role="button"
                  tabIndex={0}
                  onClick={() => handleClickDeleteFile(document)}
                  onKeyDown={(e) => handleKeyDownDeleteFile(e, document)}
                >
                  withdraw
                </span>
              </span>
            </li>
          ))}
      </ul>
    </div>
  );
}

DocumentList.propTypes = {
  packageId: PropTypes.string.isRequired,
  documents: PropTypes.arrayOf(
    PropTypes.shape({
      identifier: PropTypes.string.isRequired,
      documentProperties: PropTypes.shape({
        type: PropTypes.string.isRequired,
        name: PropTypes.string.isRequired,
      }),
      status: PropTypes.shape({
        description: PropTypes.string.isRequired,
      }),
    })
  ).isRequired,
  reloadDocumentList: PropTypes.func.isRequired,
};
