import React from "react";
import PropTypes from "prop-types";
import { MdDescription } from "react-icons/md";
import ConfirmationPopup, {
  Button,
  Sidecard,
  DisplayBox,
  Table
} from "shared-components";
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
const summaryTableData = [
  { name: "Statutory Fees:", value: "$100.00", isValueBold: true },
  { name: "Number of Documents in Package:", value: "10", isValueBold: true }
];

const aboutCsoSidecard = getSidecardData().aboutCso;
const csoAccountDetailsSidecard = getSidecardData().csoAccountDetails;

const documentIcon = (
  <div style={{ color: "rgb(252, 186, 25)" }}>
    <MdDescription size={32} />
  </div>
);

export default function PackageConfirmation({
  packageConfirmation: { confirmationPopup }
}) {
  return (
    <div className="page">
      <div className="content col-md-8">
        <h2>Package Confirmation</h2>
        <p>Review your package for accuracy before submitting.</p>

        <DisplayBox
          styling="border-background"
          icon={documentIcon}
          element={<p>View</p>}
        />

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

        <Table elements={summaryTableData} />

        <br />

        <section className="inline-block pt-2" data-test-id="upload-btn">
          <Button
            label={uploadButton.label}
            onClick={uploadButton.onClick}
            styling={uploadButton.styling}
          />
        </section>

        <section className="buttons pt-2" data-test-id="cancel-btn">
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
    confirmationPopup: propTypes.confirmationPopup
  }).isRequired
};
