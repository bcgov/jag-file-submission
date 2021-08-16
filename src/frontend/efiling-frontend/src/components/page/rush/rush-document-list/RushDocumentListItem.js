import React, { useState } from "react";
import PropTypes from "prop-types";
import { MdDescription } from "react-icons/md";
import { isClick, isEnter } from "../../../../modules/helpers/eventUtil";
import { propTypes } from "../../../../types/propTypes";
import { Toast } from "../../../toast/Toast";

export default function RushDocumentListItem({ file }) {
  const [showToast, setShowToast] = useState(false);
  const handleDownloadFile = (e) => {
    if (isClick(e) || isEnter(e)) {
      console.log("download");
      // TODO: download functionality
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
          {file.name}
        </span>
        <span className="col-md-5 d-lg-none">Statutory Fee:</span>
      </li>
    </>
  );
}

RushDocumentListItem.propTypes = {
  file: propTypes.file.isRequired,
};
