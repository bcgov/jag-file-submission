/* eslint-disable react/function-component-definition */
import React, { useState } from "react";
import PropTypes from "prop-types";
import { MdDescription } from "react-icons/md";
import { downloadFileByName } from "../../documents/DocumentService";
import { isClick, isEnter } from "../../../modules/helpers/eventUtil";
import { formatCurrency } from "../../../modules/helpers/CurrencyUtil";
import { propTypes } from "../../../types/propTypes";
import { Toast } from "../../../components/toast/Toast";

export default function FileListItem({ submissionId, file }) {
  const [showToast, setShowToast] = useState(false);
  const handleDownloadFile = (e) => {
    if (isClick(e) || isEnter(e)) {
      downloadFileByName(submissionId, file).catch(() => {
        setShowToast(true);
      });
    }
  };

  return (
    <>
      {showToast && (
        <Toast
          content="Something went wrong while trying to download your file."
          setShow={setShowToast}
        />
      )}
      <li>
        <span
          className="file-href col-md-12 col-lg-6"
          role="button"
          tabIndex={0}
          onKeyDown={(e) => handleDownloadFile(e)}
          onClick={(e) => handleDownloadFile(e)}
          data-test-id="uploaded-file"
        >
          <MdDescription size={30} color="#FCBA19" />
          {file.documentProperties.name}
        </span>
        <span className="col-md-5 d-lg-none">Statutory Fee:</span>
        <span className="col-md-5 col-lg-3">
          {formatCurrency(file.statutoryFeeAmount)}
        </span>
        <span className="col-md-5 d-lg-none">Document Type:</span>
        <span className="col-md-5 col-lg-3">{file.description}</span>
      </li>
    </>
  );
}

FileListItem.propTypes = {
  submissionId: PropTypes.string.isRequired,
  file: propTypes.file.isRequired,
};
