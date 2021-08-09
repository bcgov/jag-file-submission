import React, { useState } from "react";
import PropTypes from "prop-types";
import { MdDescription } from "react-icons/md";
import { propTypes } from "../../../types/propTypes";
import { Toast } from "../../../components/toast/Toast";
import { isClick, isEnter } from "../../../modules/helpers/eventUtil";
import { downloadSubmittedDocument } from "./PackageReviewService";

const hash = require("object-hash");

export default function SupportingDocumentList({ packageId, files }) {
  const [showToast, setShowToast] = useState(false);

  const handleDownloadFileEvent = (e, document) => {
    if (isClick(e) || isEnter(e)) {
      downloadSubmittedDocument(packageId, document).catch(() => {
        setShowToast(true);
      });
    }
  };

  return (
    <div className="ct-document-list">
      <div className="header">
        <span className="d-none d-lg-inline col-lg-4">File Name</span>
        <span className="d-none d-lg-inline col-lg-4">Status</span>
        <span className="d-none d-lg-inline col-lg-4">Registry Notice</span>
      </div>
      {showToast && (
        <Toast
          content="Something went wrong while trying to download your file."
          setShow={setShowToast}
        />
      )}
      <ul>
        {files.map((file) => (
          // TODO: Fix the classname to be based on the document status when supporting document statuses are added
          <li key={hash(file)} className="SUB">
            <span className="label col-md-5 d-lg-none">File Name:</span>
            <span className="col-md-5 col-lg-4 file-cell">
              <MdDescription size={30} color="#FCBA19" />
              <span
                className="file-href"
                role="button"
                tabIndex={0}
                onKeyDown={(e) => handleDownloadFileEvent(e, file)}
                onClick={(e) => handleDownloadFileEvent(e, file)}
                data-test-id="uploaded-file"
              >
                {file.fileName}
              </span>
            </span>
            <span className="label col-md-5 d-lg-none">Status:</span>
            <span className="col-md-5 col-lg-4">Submitted</span>
            <span className="label col-md-5 d-lg-none">Registry Notice:</span>
            <span className="col-md-5 col-lg-4">Registry notice goes here</span>
          </li>
        ))}
      </ul>
    </div>
  );
}

SupportingDocumentList.propTypes = {
  packageId: PropTypes.number.isRequired,
  files: PropTypes.arrayOf(propTypes.file.isRequired).isRequired,
};
