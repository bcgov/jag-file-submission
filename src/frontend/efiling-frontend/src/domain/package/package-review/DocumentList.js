/* eslint-disable react/jsx-one-expression-per-line */
import React, { useState } from "react";
import PropTypes from "prop-types";
import { MdHelp } from "react-icons/md";
import moment from "moment";

import Modal from "react-bootstrap/Modal";
import { Button } from "shared-components";
import { errorRedirect } from "../../../modules/helpers/errorRedirect";
import { withdrawTooltipData } from "../../../modules/test-data/withdrawTooltipData";
import {
  downloadSubmittedDocument,
  withdrawSubmittedDocument,
} from "./PackageReviewService";
import "./DocumentList.scss";
import BcGovTooltip from "./tooltip/Tooltip";

export default function DocumentList({
  packageId,
  documents,
  reloadDocumentList,
}) {
  const [showModal, setShowModal] = useState(false);
  const [modalData, setModalData] = useState({
    description: "",
    documentProperties: {
      name: "",
    },
    filingDate: "",
    status: "",
  });

  const handleModalClose = () => setShowModal(false);
  const handleModalConfirm = () => {
    setShowModal(false);

    withdrawSubmittedDocument(packageId, modalData)
      .then(() => reloadDocumentList())
      .catch((err) => errorRedirect(sessionStorage.getItem("errorUrl"), err));
  };

  function isClick(e) {
    return e && e.type === "click";
  }

  function isEnter(e) {
    return e && e.type === "keydown" && e.keyCode === 13;
  }

  function handleDownloadFileEvent(e, document) {
    if (isClick(e) || isEnter(e)) {
      downloadSubmittedDocument(packageId, document).catch((err) =>
        errorRedirect(sessionStorage.getItem("errorUrl"), err)
      );
    }
  }

  function handleWithdrawFileEvent(e, document) {
    if (isClick(e) || isEnter(e)) {
      setModalData(document);
      setShowModal(true);
    }
  }

  return (
    <div className="ct-document-list">
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
                <MdHelp
                  style={{
                    fontSize: "20px",
                    marginLeft: "10px",
                    color: "#7598ca",
                  }}
                  id="withdraw-tooltip"
                />
                <BcGovTooltip
                  target={`withdraw-tooltip`}
                  content={withdrawTooltipData}
                />
              </span>
            </li>
          ))}
      </ul>

      <Modal show={showModal} onHide={handleModalClose}>
        <Modal.Header closeButton>
          <Modal.Title>Withdraw Document</Modal.Title>
        </Modal.Header>

        <Modal.Body>
          <p className="modalBody">
            Please confirm the withdrawl of document{" "}
            {modalData.documentProperties.name} from your latest submission.
            This will remove the document permanently from your submitted
            package number
            {packageId}.
          </p>

          <div className="row">
            <div className="col-sm-4">Package Number:</div>
            <div className="col-sm-8 data">{packageId}</div>
          </div>
          <div className="row">
            <div className="col-sm-4">Document Status:</div>
            <div className="col-sm-8 data">{modalData.status.description}</div>
          </div>
          <div className="row">
            <div className="col-sm-4">Date Filed:</div>
            <div className="col-sm-8 data">
              {moment(modalData.filingDate).format("DD-MMM-YYYY")}
            </div>
          </div>
          <div className="row">
            <div className="col-sm-4">Document Type:</div>
            <div className="col-sm-8 data">{modalData.description}</div>
          </div>
          <div className="row">
            <div className="col-sm-4">File Name:</div>
            <div className="col-sm-8 data">
              {modalData.documentProperties.name}
            </div>
          </div>
        </Modal.Body>
        <Modal.Footer>
          <Button
            onClick={handleModalClose}
            label="Cancel"
            styling="btn btn-secondary"
          />
          <Button
            onClick={handleModalConfirm}
            label="Confirm"
            styling="btn btn-primary"
          />
        </Modal.Footer>
      </Modal>
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
