/* eslint-disable react/function-component-definition, import/no-named-as-default, import/no-named-as-default-member */
import React from "react";
import PropTypes from "prop-types";
import RushDocumentListItem from "./RushDocumentListItem";
import { propTypes } from "../../../../types/propTypes";

import "./RushDocumentList.scss";

const hash = require("object-hash");

export default function RushDocumentList({ files, setFiles }) {
  return (
    <div className="ct-file-list ">
      <div className="header">
        <span className="d-none d-lg-inline col-lg-10">Document Name</span>
        <span className="d-none d-lg-inline col-lg-2">Action</span>
      </div>
      <ul>
        {files &&
          files.map((file) => (
            <RushDocumentListItem
              file={file}
              files={files}
              key={hash(file)}
              setFiles={setFiles}
            />
          ))}
      </ul>
    </div>
  );
}

RushDocumentList.propTypes = {
  files: PropTypes.arrayOf(propTypes.file.isRequired).isRequired,
  setFiles: PropTypes.func.isRequired,
};
