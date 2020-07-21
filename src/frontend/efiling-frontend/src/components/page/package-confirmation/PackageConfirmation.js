import React, { useState, useEffect } from "react";
import PropTypes from "prop-types";
import { MdDescription, MdCheckBox } from "react-icons/md";
import ConfirmationPopup, {
  Alert,
  Button,
  Sidecard,
  DisplayBox,
  Table
} from "shared-components";
import Dinero from "dinero.js";
import axios from "axios";
import { getSidecardData } from "../../../modules/sidecardData";
import { propTypes } from "../../../types/propTypes";
import { errorRedirect } from "../../../modules/errorRedirect";

import "./PackageConfirmation.css";
import Payment from "../payment/Payment";

const calculateTotalStatutoryFee = files => {
  let totalStatFee = Dinero({ amount: 0 });
  files.forEach(file => {
    totalStatFee = totalStatFee.add(
      Dinero({
        amount: parseInt((file.statutoryFeeAmount * 100).toFixed(0), 10)
      })
    );
  });

  return totalStatFee;
};

const openFile = (file, submissionId) => {
  axios
    .get(`/submission/${submissionId}/document/${file.name}`, {
      responseType: "blob",
      headers: {
        "X-Auth-UserId": sessionStorage.getItem("universalId")
      }
    })
    .then(response => {
      // TODO: do not use hard coded type
      const fileData = new Blob([response.data], { type: "application/pdf" });
      const fileUrl = URL.createObjectURL(fileData);

      window.open(fileUrl);
    })
    .catch(error => {
      errorRedirect(sessionStorage.getItem("errorUrl"), error);
    });
};

const generateTotalFeeTable = files => {
  const feesData = [
    {
      name: "Statutory Fees:",
      value: calculateTotalStatutoryFee(files).toFormat("$0,0.00"),
      isValueBold: true
    },
    {
      name: "Number of Documents in Package:",
      value: `${files.length}`,
      isValueBold: true
    }
  ];

  return [
    {
      name: (
        <div style={{ width: "60%" }}>
          <Table elements={feesData} />
        </div>
      ),
      value: "",
      isSideBySide: true
    }
  ];
};

const generateTable = (file, data, submissionId) => {
  return [
    {
      name: (
        <div style={{ width: "80%" }}>
          <span
            onKeyDown={() => openFile(file, submissionId)}
            role="button"
            tabIndex={0}
            className="file-href"
            onClick={() => openFile(file, submissionId)}
          >
            {file.name}
          </span>
        </div>
      ),
      value: <Table elements={data} />,
      verticalMiddle: true
    }
  ];
};

const generateTableData = (file, submissionId) => {
  const data = [
    {
      name: "Description:",
      value: file.description,
      isValueBold: true,
      isClose: true
    }
  ];

  if (file.statutoryFeeAmount > 0) {
    data.push({
      name: "Statutory Fee:",
      value: Dinero({
        amount: parseInt((file.statutoryFeeAmount * 100).toFixed(0), 10)
      }).toFormat("$0,0.00"),
      isValueBold: true,
      isClose: true
    });
  }

  return generateTable(file, data, submissionId);
};

const getFilingPackageData = (submissionId, setFiles, files) => {
  if (files.length > 0) return;

  axios
    .get(`/submission/${submissionId}/filing-package`, {
      headers: {
        "X-Auth-UserId": sessionStorage.getItem("universalId")
      }
    })
    .then(({ data: { documents } }) => {
      setFiles(documents);
    })
    .catch(() => window.open(sessionStorage.getItem("errorUrl"), "_self"));
};

export default function PackageConfirmation({
  packageConfirmation: { confirmationPopup, submissionId },
  csoAccountStatus: { isNew }
}) {
  const [files, setFiles] = useState([]);
  const [showPayment, setShowPayment] = useState(false);
  const aboutCsoSidecard = getSidecardData().aboutCso;
  const csoAccountDetailsSidecard = getSidecardData().csoAccountDetails;

  useEffect(() => {
    getFilingPackageData(submissionId, setFiles, files);
  }, [files, submissionId]);

  if (showPayment)
    return <Payment payment={{ confirmationPopup, submissionId }} />;

  return (
    <div className="page">
      <div className="content col-md-8">
        {isNew && (
          <>
            <Alert
              icon={<MdCheckBox size={32} />}
              type="success"
              styling="success-background"
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

        {files.map(file => (
          <div key={file.name}>
            <DisplayBox
              styling="border-background display-file"
              icon={
                <div style={{ color: "rgb(252, 186, 25)" }}>
                  <MdDescription size={32} />
                </div>
              }
              element={
                <Table elements={generateTableData(file, submissionId)} />
              }
            />
            <br />
          </div>
        ))}

        {/* TODO: temporary lint disable, remove later */}
        {/* eslint-disable jsx-a11y/anchor-is-valid */}
        <h3>
          Do you have additional documents to upload?&nbsp;
          <a href="#">Upload them now.</a>
        </h3>
        <br />

        <h2>Summary</h2>
        <Table elements={generateTotalFeeTable(files)} />
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
            styling="normal-blue btn"
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
    submissionId: PropTypes.string.isRequired
  }).isRequired,
  csoAccountStatus: PropTypes.shape({
    isNew: PropTypes.bool
  }).isRequired
};
