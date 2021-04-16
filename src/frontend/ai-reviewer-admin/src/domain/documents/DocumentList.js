import React from "react";
import PropTypes from "prop-types";
import { VscJson } from "react-icons/vsc";
import { MdEdit, MdLibraryAdd } from "react-icons/md";

import "./DocumentList.scss";

export default function DocumentList({ configurations }) {
  return (
    <div className="document-type-list">
      <div className="table-header">
        <span className="d-none d-sm-inline col-sm-3">Document Type</span>
        <span className="d-none d-sm-inline col-sm-3">Document Description</span>
        <span className="d-none d-sm-inline col-sm-3">Project ID</span>
        <span className="d-none d-sm-inline col-sm-3">Action</span>
      </div>
      <ul>
        {configurations && configurations.map && configurations.map((configuration) => (
          <li key={configuration.id}>
            <span className="col-12 d-inline d-sm-none label"><VscJson size="24" color="#FCBA19" strokeWidth="1" />{configuration.id}</span>

            <span className="col-6 d-inline d-sm-none">Document Type</span>
            <span className="col-6 col-sm-3 label">{configuration.documentType.type}</span>
            <span className="col-6 d-inline d-sm-none">Document Description</span>
            <span className="col-6 col-sm-3 label">{configuration.documentType.description}</span>
            <span className="col-6 d-inline d-sm-none">Project ID</span>
            <span className="col-6 col-sm-3 label">{configuration.projectId}</span>
            <span className="col-12 col-sm-3 pull-right"><MdEdit size="24" color="#FCBA19" /></span>
          </li>
        ))}
        <li><span className="col"><MdLibraryAdd size="24" color="#FCBA19" /></span></li>
      </ul>
    </div>
  );
}

DocumentList.propTypes = {
  configurations: PropTypes.arrayOf(PropTypes.object).isRequired,
};