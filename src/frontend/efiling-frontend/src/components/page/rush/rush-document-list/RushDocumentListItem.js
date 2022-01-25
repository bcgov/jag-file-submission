/* eslint-disable react/function-component-definition */
import React, { useState } from "react";
import { MdDescription } from "react-icons/md";
import PropTypes from "prop-types";
import { isClick, isEnter } from "../../../../modules/helpers/eventUtil";
import { propTypes } from "../../../../types/propTypes";
import { Toast } from "../../../toast/Toast";

export default function RushDocumentListItem({ files, setFiles, file }) {
  const [showToast, setShowToast] = useState(false);
  const handleDownloadFile = (e) => {
    if (isClick(e) || isEnter(e)) {
      console.log("download");
      // TODO: download functionality
    }
  };
  const handleDeleteFile = (e) => {
    if (isClick(e) || isEnter(e)) {
      setFiles(files.filter((f) => f !== file));
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
        <span className="label col-sm-4 d-lg-none mb-0 mb-sm-2">
          Document Name:
        </span>
        <span
          className="file-href col-sm-8 col-lg-10"
          role="button"
          tabIndex={0}
          onKeyDown={(e) => handleDownloadFile(e)}
          onClick={(e) => handleDownloadFile(e)}
          data-test-id="uploaded-file"
        >
          <MdDescription size={30} color="#FCBA19" />
          {file.name}
        </span>
        <span className="label col-sm-4 d-lg-none">Action:</span>
        <span
          className="file-href col-sm-8 col-lg-2"
          role="button"
          tabIndex={0}
          onKeyDown={(e) => handleDeleteFile(e)}
          onClick={(e) => handleDeleteFile(e)}
        >
          Remove
        </span>
      </li>
    </>
  );
}

RushDocumentListItem.propTypes = {
  file: propTypes.file.isRequired,
  files: PropTypes.array.isRequired,
  setFiles: PropTypes.func.isRequired,
};
