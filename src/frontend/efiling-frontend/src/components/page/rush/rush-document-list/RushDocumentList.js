import React from "react";
import PropTypes from "prop-types";
import RushDocumentListItem from "./RushDocumentListItem";
import { propTypes } from "../../../../types/propTypes";

import "./RushDocumentList.scss";

const hash = require("object-hash");

export default function RushDocumentList({ files }) {
  return (
    <div className="ct-file-list ">
      <div className="header">
        <span className="d-none d-lg-inline col-lg-6">Document Name</span>
        <span className="d-none d-lg-inline col-lg-3">Action</span>
      </div>
      <ul>
        {files &&
          files.map((file) => (
            <RushDocumentListItem
              file={file}
              key={hash(file)}
            />
          ))}
      </ul>
    </div>
  );
}

RushDocumentList.propTypes = {
  files: PropTypes.arrayOf(propTypes.file.isRequired).isRequired,
};
