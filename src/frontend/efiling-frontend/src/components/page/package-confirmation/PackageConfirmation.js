import React, { useState, useEffect } from "react";
import PropTypes from "prop-types";
import { MdDescription, MdCheckBox } from "react-icons/md";
import ConfirmationPopup, {
  Alert,
  Button,
  Sidecard,
  DisplayBox,
  Table,
} from "shared-components";
import FileSaver from "file-saver";
import Dinero from "dinero.js";
import axios from "axios";
import { getSidecardData } from "../../../modules/helpers/sidecardData";
import { propTypes } from "../../../types/propTypes";
import { errorRedirect } from "../../../modules/helpers/errorRedirect";
import { onBackButtonEvent } from "../../../modules/helpers/handleBackEvent";
import { generateFileSummaryData } from "../../../modules/helpers/generateFileSummaryData";

import "./PackageConfirmation.scss";
import Payment from "../payment/Payment";
import Upload from "../upload/Upload";

const downloadFile = (file, submissionId) => {
  const fileName = file.documentProperties.name;
  axios
    .get(`/submission/${submissionId}/document/${fileName}`, {
      responseType: "blob",
    })
    .then((response) => {
      const fileData = new Blob([response.data], { type: file.mimeType });
      const fileUrl = URL.createObjectURL(fileData);

      FileSaver.saveAs(fileUrl, fileName);
    })
    .catch((error) => {
      errorRedirect(sessionStorage.getItem("errorUrl"), error);
    });
};

const generateTable = (file, data, submissionId) => [
  {
    name: (
      <div style={{ width: "80%" }}>
        <span
          onKeyDown={() => downloadFile(file, submissionId)}
          role="button"
          tabIndex={0}
          className="file-href"
          onClick={() => downloadFile(file, submissionId)}
          data-test-id="uploaded-file"
        >
          {file.documentProperties.name}
        </span>
      </div>
    ),
    value: <Table elements={data} />,
  },
];

const generateTableData = (file, submissionId) => {
  const data = [
    {
      name: "Description:",
      value: file.description,
      isValueBold: true,
    },
  ];

  if (file.statutoryFeeAmount > 0) {
    data.push({
      name: "Statutory Fee:",
      value: Dinero({
        amount: parseInt((file.statutoryFeeAmount * 100).toFixed(0), 10),
      }).toFormat("$0,0.00"),
      isValueBold: true,
    });
  }

  return generateTable(file, data, submissionId);
};

const getFilingPackageData = (
  submissionId,
  setFiles,
  files,
  setCourtData,
  setSubmissionFee,
  setShowPayment
) => {
  if (files.length > 0) return;

  axios
    .get(`/submission/${submissionId}/filing-package`)
    .then(({ data: { documents, court, submissionFeeAmount } }) => {
      setCourtData(court);
      setSubmissionFee(submissionFeeAmount);
      setFiles(documents);
      if (sessionStorage.getItem("isBamboraRedirect") === "true")
        setShowPayment(true);
    })
    .catch((error) => errorRedirect(sessionStorage.getItem("errorUrl"), error));
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
  const aboutCsoSidecard = getSidecardData().aboutCso;
  const csoAccountDetailsSidecard = getSidecardData().csoAccountDetails;

  const resetState = () => {
    setShowUpload(false);
    setShowPayment(false);
  };

  useEffect(() => {
    if (!sessionStorage.getItem("listenerExists")) {
      sessionStorage.setItem("currentPage", "packageConfirmation");
      window.addEventListener("popstate", (e) =>
        onBackButtonEvent(e, resetState)
      );
      sessionStorage.setItem("listenerExists", true);
    }
  }, []);

  useEffect(() => {
    getFilingPackageData(
      submissionId,
      setFiles,
      files,
      setCourtData,
      setSubmissionFee,
      setShowPayment
    );
  }, [files, submissionId]);

  if (showPayment) {
    return (
      <Payment
        payment={{
          confirmationPopup,
          submissionId,
          courtData,
          files,
          submissionFee,
        }}
      />
    );
  }

  if (showUpload)
    return <Upload upload={{ confirmationPopup, submissionId, courtData }} />;

  return (
    <div className="ct-package-confirmation page">
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
        {files.map((file) => (
          <div key={file.documentProperties.name}>
            <DisplayBox
              styling="bcgov-border-background bcgov-display-file"
              icon={
                <div style={{ color: "rgb(252, 186, 25)" }}>
                  <MdDescription size={32} />
                </div>
              }
              element={
                <Table
                  elementStyles={{
                    columnStyle: "bcgov-vertical-middle bcgov-fill-width",
                  }}
                  elements={generateTableData(file, submissionId)}
                />
              }
            />
            <br />
          </div>
        ))}
        <h2>
          Do you have additional documents to upload?&nbsp;
          <span
            onKeyDown={() => setShowUpload(true)}
            role="button"
            tabIndex={0}
            className="file-href"
            data-test-id="upload-link"
            onClick={() => setShowUpload(true)}
          >
            Upload them now.
          </span>
        </h2>
        <br />
        <br />
        <h2>Summary</h2>
        <p />
        <div className="near-half-width">
          <Table
            elements={generateFileSummaryData(files, submissionFee, false).data}
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
            label="Continue to Payment"
            onClick={() => setShowPayment(true)}
            styling="bcgov-normal-blue btn"
            testId="continue-btn"
          />
        </section>
      </div>
      <div className="sidecard">
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
