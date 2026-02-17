/* eslint-disable react/function-component-definition */
import React, { useState } from "react";
import PropTypes from "prop-types";
import { MdDescription } from "react-icons/md";
import { propTypes } from "../../../types/propTypes";
import { Toast } from "../../../components/toast/Toast";
import { isClick, isEnter } from "../../../modules/helpers/eventUtil";
import { downloadRushDocument } from "./PackageReviewService";

const hash = require("object-hash");

export default function SupportingDocumentList({
  packageId,
  files,
  rushStatus,
}) {
  const [showToast, setShowToast] = useState(false);

  const handleDownloadFileEvent = (e, document) => {
    if (isClick(e) || isEnter(e)) {
      downloadRushDocument(packageId, document.fileName).catch(() => {
        setShowToast(true);
      });
    }
  };

  return (
    <div className="ct-document-list">
      <div className="header">
        <span className="d-none d-lg-inline col-lg-4">File Name</span>
        <span className="d-none d-lg-inline col-lg-4" />
        <span className="d-none d-lg-inline col-lg-4">Status</span>
      </div>
      {showToast && (
        <Toast
          content="Something went wrong while trying to download your file."
          setShow={setShowToast}
        />
      )}
      <ul>
        {files.map((file) => (
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
                data-testid="uploaded-file"
              >
                {file.fileName}
              </span>
            </span>
            <span className="label col-md-5 d-lg-none" />
            <span className="col-md-5 col-lg-4" />
            <span className="label col-md-5 d-lg-none">Status:</span>
            <span className="col-md-5 col-lg-4">{rushStatus}</span>
          </li>
        ))}
      </ul>
    </div>
  );
}

SupportingDocumentList.propTypes = {
  packageId: PropTypes.number.isRequired,
  files: PropTypes.arrayOf(propTypes.file.isRequired).isRequired,
  rushStatus: PropTypes.string.isRequired,
};
