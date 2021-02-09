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
  function handleDownloadFileEvent(e, document) {
    if (e && (e.type === "click" || e.keyCode === 13)) {
      downloadSubmittedDocument(packageId, document).catch((err) =>
        errorRedirect(sessionStorage.getItem("errorUrl"), err)
      );
    }
  }

  function handleWithdrawFileEvent(e, document) {
    if (e && (e.type === "click" || e.keyCode === 13)) {
      withdrawSubmittedDocument(packageId, document)
        .then(() => reloadDocumentList())
        .catch((err) => errorRedirect(sessionStorage.getItem("errorUrl"), err));
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
                  onClick={(e) => handleDownloadFileEvent(e, document)}
                  onKeyDown={(e) => handleDownloadFileEvent(e, document)}
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
                  onClick={(e) => handleWithdrawFileEvent(e, document)}
                  onKeyDown={(e) => handleWithdrawFileEvent(e, document)}
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
