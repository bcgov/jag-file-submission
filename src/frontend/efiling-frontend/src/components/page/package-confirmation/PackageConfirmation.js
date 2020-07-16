import React, { useState } from "react";
import PropTypes from "prop-types";
import { MdDescription, MdCheckBox } from "react-icons/md";
import ConfirmationPopup, {
  Alert,
  Button,
  Sidecard,
  DisplayBox,
  Table
} from "shared-components";
import axios from "axios";
import { getSidecardData } from "../../../modules/sidecardData";
import { propTypes } from "../../../types/propTypes";

import "./PackageConfirmation.css";

const continueButton = {
  label: "Continue to Payment",
  onClick: () => console.log("continue on click"),
  styling: "normal-blue btn"
};

const uploadButton = {
  label: "Upload Documents",
  onClick: () => console.log("upload on click"),
  styling: "normal-white btn"
};

// TODO: Fix values
const feesData = [
  {
    name: "Statutory Fees:",
    value: "$100.00",
    isValueBold: true
  },
  {
    name: "Number of Documents in Package:",
    value: "10",
    isValueBold: true
  }
];

const elements = [
  {
    name: (
      <div style={{ width: "60%" }}>
        <Table isFeesData elements={feesData} />
      </div>
    ),
    value: "",
    isSideBySide: true
  }
];

const generateTableData = file => {
  return [
    {
      name: (
        <div style={{ width: "80%" }}>
          <p>{file.name}</p>
        </div>
      ),
      value: (
        <>
          <p>
            Description: <b>{file.description}</b>
          </p>
          <p>
            Statutory Fee: <b>Some tingg</b>
          </p>
        </>
      ),
      isSideBySide: true
    }
  ];
};

const documentIcon = (
  <div style={{ color: "rgb(252, 186, 25)" }}>
    <MdDescription size={32} />
  </div>
);

const getFilingPackageData = (submissionId, setFiles) => {
  axios
    .get(`/submission/${submissionId}/filing-package`)
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
  const aboutCsoSidecard = getSidecardData().aboutCso;
  const csoAccountDetailsSidecard = getSidecardData().csoAccountDetails;

  getFilingPackageData(submissionId, setFiles);

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
        <h2>Package Confirmation</h2>
        <p>Review your package for accuracy before submitting.</p>

        {files.map(
          file => (
            <div key={file.name}>
              <DisplayBox
                styling="border-background"
                icon={documentIcon}
                element={<Table elements={generateTableData(file)} />}
              />
              <br />
            </div>
          )
          // padding-top: inherit for display-icon in display box
        )}

        <br />

        <p>
          <a
            href={sessionStorage.getItem("cancelUrl")}
            data-test-id="return-link"
          >
            Return to the parent application website
          </a>
          &nbsp;to correct errors or missing information in this package.
        </p>

        <br />

        <h3>Summary</h3>

        <Table elements={elements} />

        <br />

        <section className="inline-block pt-2">
          <Button
            label={uploadButton.label}
            onClick={uploadButton.onClick}
            styling={uploadButton.styling}
            testId="upload-btn"
          />
        </section>

        <section className="buttons pt-2">
          <ConfirmationPopup
            modal={confirmationPopup.modal}
            mainButton={confirmationPopup.mainButton}
            confirmButton={confirmationPopup.confirmButton}
            cancelButton={confirmationPopup.cancelButton}
          />
          <Button
            label={continueButton.label}
            onClick={continueButton.onClick}
            styling={continueButton.styling}
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
