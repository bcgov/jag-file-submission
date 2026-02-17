/* eslint-disable react/function-component-definition, import/no-named-as-default, import/no-named-as-default-member, */
import React, { useState, useEffect } from "react";
import PropTypes from "prop-types";
import { MdCheckBox, MdError } from "react-icons/md";
import ConfirmationPopup, {
  Alert,
  Button,
  Sidecard,
  Table,
} from "shared-components";
import axios from "axios";
import Rush from "../../../components/page/rush/Rush";
import RushConfirmation from "./RushConfirmation";
import { getSidecardData } from "../../../modules/helpers/sidecardData";
import { propTypes } from "../../../types/propTypes";
import { onBackButtonEvent } from "../../../modules/helpers/handleBackEvent";
import { generateFileSummaryData } from "../../../modules/helpers/generateFileSummaryData";
import { isClick, isEnter } from "../../../modules/helpers/eventUtil";

import "./PackageConfirmation.scss";
import Payment from "../../payment/Payment";
import Upload from "../../../components/page/upload/Upload";
import FileList from "./FileList";
import { Toast } from "../../../components/toast/Toast";
import { Radio } from "../../../components/radio/Radio";

const convertSupportingDocuments = (supportingDocuments) => {
  if (supportingDocuments) {
    const convertedSupportingDocuments = [];
    for (let i = 0; i < supportingDocuments.length; i += 1) {
      const convertedSupportingDocument = {
        name: supportingDocuments[i].fileName,
      };
      convertedSupportingDocuments.push(convertedSupportingDocument);
    }

    return convertedSupportingDocuments;
  }

  return [];
};

const getFilingPackageData = (
  submissionId,
  setFiles,
  setCourtData,
  setSubmissionFee,
  setShowPayment,
  setShowToast,
  setToastMessage,
  setHasRushInfo,
  setRushSupportingDocuments
) => {
  axios
    .get(`/submission/${submissionId}/filing-package`)
    .then(({ data: { documents, court, submissionFeeAmount, rush } }) => {
      setCourtData(court);
      setSubmissionFee(submissionFeeAmount);
      setFiles(documents);
      if (sessionStorage.getItem("isBamboraRedirect") === "true")
        setShowPayment(true);
      if (rush && rush.rushType) {
        setHasRushInfo(true);

        setRushSupportingDocuments(
          convertSupportingDocuments(rush.supportingDocuments)
        );
      } else {
        setHasRushInfo(false);
      }
    })
    .catch(() => {
      setToastMessage(
        "Something went wrong while trying to retrieve your filing package."
      );
      setShowToast(true);
    });
};

const checkRejectedFiles = (files, setHasRejectedDocuments) => {
  let hasRejectedDoc = false;
  if (files && files.length > 0) {
    files.forEach((file) => {
      if (file.actionDocument && file.actionDocument.status === "REJ") {
        hasRejectedDoc = true;
      }
    });
  }
  setHasRejectedDocuments(hasRejectedDoc);
};

const checkDuplicateFileNames = (files, setShowToast, setToastMessage) => {
  if (files && files.length > 0) {
    const filenames = [];
    files.forEach((file) => {
      const filename = file.documentProperties.name;
      if (filenames.includes(filename)) {
        setToastMessage(
          "This package contains duplicate file names. File names must be unique within a filing package."
        );
        setShowToast(true);
      }
      filenames.push(file.documentProperties.name);
    });
  }
};

const checkPorDocument = (files, setHasPorDocument) => {
  files.forEach((file) => {
    if (file.rushRequired) {
      setHasPorDocument(true);
    }
  });
};

export default function PackageConfirmation({
  packageConfirmation: { confirmationPopup, submissionId },
  csoAccountStatus: { isNew },
}) {
  const [files, setFiles] = useState([]);
  const [courtData, setCourtData] = useState(null);
  const [submissionFee, setSubmissionFee] = useState(null);
  const [showPayment, setShowPayment] = useState(false);
  const [showUpload, setShowUpload] = useState(false);
  const [showToast, setShowToast] = useState(false);
  const [toastMessage, setToastMessage] = useState(null);
  const [showRush, setShowRush] = useState(false);
  const [showModal, setShowModal] = useState(false);
  const [showSidecardModal, setShowSidecardModal] = useState(false);
  const [isRush, setIsRush] = useState(false);
  const [hasRushInfo, setHasRushInfo] = useState(false);
  const [hasPorDocument, setHasPorDocument] = useState(false);
  const [rushSupportingDocuments, setRushSupportingDocuments] = useState([]);

  const aboutCsoSidecard = getSidecardData().aboutCso;
  const csoAccountDetailsSidecard = getSidecardData().csoAccountDetails;
  const rushSubmissionSidecard =
    getSidecardData(setShowSidecardModal).rushSubmission;
  const rejectedDocumentsSideCard = getSidecardData().rejectedDocuments;
  const [hasRejectedDocuments, setHasRejectedDocuments] = useState(false);

  const rushFeatureFlag = window.env
    ? window.env.REACT_APP_RUSH_TAB_FEATURE_FLAG
    : process.env.REACT_APP_RUSH_TAB_FEATURE_FLAG;

  const resetState = () => {
    setShowUpload(false);
    setShowPayment(false);
    setShowRush(false);
  };

  const handleContinue = () => {
    if (isRush && rushFeatureFlag === "true" && !hasRushInfo) {
      setShowPayment(false);
      setShowModal(true);
    } else {
      setShowModal(false);
      setShowRush(false);
      setShowPayment(true);
    }
  };

  useEffect(() => {
    if (!sessionStorage.getItem("listenerExists")) {
      sessionStorage.setItem("currentPage", "packageConfirmation");
      window.addEventListener("popstate", (e) =>
        onBackButtonEvent(e, resetState)
      );
      sessionStorage.setItem("listenerExists", true);
    }
    if (sessionStorage.getItem("validRushExit") === "true") setIsRush(true);
  }, []);

  useEffect(() => {
    getFilingPackageData(
      submissionId,
      setFiles,
      setCourtData,
      setSubmissionFee,
      setShowPayment,
      setShowToast,
      setToastMessage,
      setHasRushInfo,
      setRushSupportingDocuments
    );
  }, [submissionId, showUpload, hasRushInfo]);

  useEffect(() => {
    checkDuplicateFileNames(files, setShowToast, setToastMessage);
    checkRejectedFiles(files, setHasRejectedDocuments);
    checkPorDocument(files, setHasPorDocument);
  }, [files]);

  function handleUploadFile(e) {
    if (isClick(e) || isEnter(e)) {
      setShowUpload(true);
    }
  }

  if (showPayment) {
    return (
      <Payment
        payment={{
          confirmationPopup,
          submissionId,
          courtData,
          files,
          submissionFee,
          hasRushInfo,
        }}
      />
    );
  }

  if (showUpload) {
    return (
      <Upload
        upload={{
          submissionId,
          courtData,
          setShowUpload,
          files: files.concat(rushSupportingDocuments),
        }}
      />
    );
  }

  if (showRush) {
    return (
      <Rush
        payment={{
          confirmationPopup,
          submissionId,
          courtData,
          files,
          submissionFee,
        }}
        setShowRush={setShowRush}
        setIsRush={setIsRush}
        setHasRushInfo={setHasRushInfo}
      />
    );
  }

  return (
    <div className="ct-package-confirmation page">
      {showModal && (
        <RushConfirmation
          show={showModal}
          setShow={setShowModal}
          setShowRush={setShowRush}
        />
      )}
      {showSidecardModal && (
        <RushConfirmation
          show={showSidecardModal}
          setShow={setShowSidecardModal}
        />
      )}
      <div className="content col-md-8">
        {isNew && (
          <>
            <Alert
              icon={<MdCheckBox size={32} />}
              type="success"
              styling="bcgov-success-background"
              element="CSO Account created successfully."
            />
            <br />
          </>
        )}
        <h1>Package Confirmation</h1>
        {showToast && (
          <Toast
            testId="duplicateDocMsg"
            content={toastMessage}
            setShow={setShowToast}
          />
        )}
        <span>
          Review your package for accuracy and upload any additional or
          supporting documents.
        </span>
        <br />
        <span>
          If there are any errors in these documents, please Cancel this process
          and re-submit.
        </span>
        <br />
        <br />
        <FileList submissionId={submissionId} files={files} />
        <br />
        {hasRejectedDocuments && (
          <div className="alert alert-danger show rejectedMsg" role="alert">
            <div>
              <MdError size={32} />
            </div>
            <div>
              <span>
                Please ensure you have uploaded all documents required to
                correct the issue(s) identified in the Registry Notice. If you
                still need to add documents use the upload link below.
              </span>
            </div>
          </div>
        )}
        <h4>
          Do you have additional documents to upload?&nbsp;
          <span
            onKeyDown={(e) => handleUploadFile(e)}
            role="button"
            tabIndex={0}
            className="file-href"
            data-test-id="upload-link"
            onClick={(e) => handleUploadFile(e)}
          >
            Upload them now.
          </span>
        </h4>

        {rushFeatureFlag === "true" &&
          hasRushInfo === false &&
          hasPorDocument === false && (
            <>
              <br />
              <div className="bcgov-row" data-testId="rushRadioOpts">
                <span>
                  Do you want to request that this submission be processed on a{" "}
                  <b>rush basis?</b>
                </span>
                <Radio
                  id="No"
                  label="No"
                  name="rush"
                  defaultChecked
                  onSelect={() => setIsRush(false)}
                />
                <Radio
                  id="Yes"
                  label="Yes"
                  name="rush"
                  onSelect={() => setIsRush(true)}
                />
              </div>
            </>
          )}
        <br />
        <h2>Summary</h2>
        <p />
        <div className="near-half-width">
          <Table
            elements={
              generateFileSummaryData(
                isRush || hasRushInfo,
                files,
                submissionFee,
                false,
                hasPorDocument
              ).data
            }
          />
        </div>
        <br />
        <section className="buttons pt-2">
          <ConfirmationPopup
            modal={confirmationPopup.modal}
            mainButton={confirmationPopup.mainButton}
            confirmButton={confirmationPopup.confirmButton}
            cancelButton={confirmationPopup.cancelButton}
          />
          <Button
            label="Continue"
            onClick={() => handleContinue()}
            styling="bcgov-normal-blue btn"
            testId="continue-btn"
            disabled={toastMessage !== null}
          />
        </section>
      </div>
      <div className="sidecard">
        {hasRejectedDocuments && (
          <Sidecard sideCard={rejectedDocumentsSideCard} />
        )}
        {(isRush || hasRushInfo) && rushFeatureFlag === "true" && (
          <Sidecard sideCard={rushSubmissionSidecard} />
        )}
        <Sidecard sideCard={csoAccountDetailsSidecard} />
        <Sidecard sideCard={aboutCsoSidecard} />
      </div>
    </div>
  );
}

PackageConfirmation.propTypes = {
  packageConfirmation: PropTypes.shape({
    confirmationPopup: propTypes.confirmationPopup,
    submissionId: PropTypes.string.isRequired,
  }).isRequired,
  csoAccountStatus: PropTypes.shape({
    isNew: PropTypes.bool,
  }).isRequired,
};
