import React from "react";
import PropTypes from "prop-types";
import { MdInsertDriveFile } from "react-icons/md";
import ConfirmationPopup, {
  Button,
  Sidecard,
  DisplayBox
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

const aboutCsoSidecard = getSidecardData().aboutCso;
const csoAccountDetailsSidecard = getSidecardData().csoAccountDetails;

const documentIcon = (
  <div style={{ color: "rgb(252, 186, 25)" }}>
    <MdInsertDriveFile size={32} />
  </div>
);

export default function PackageConfirmation({
  packageConfirmation: { confirmationPopup }
}) {
  return (
    <div className="page">
      <div className="content col-md-8">
        <h2>Package Confirmation</h2>
        <p>The following documents will be submitted to</p>
        <h3>Review your package for accuracy before submitting</h3>

        <DisplayBox
          styling="border-background"
          icon={documentIcon}
          element={<p>View</p>}
        />

        <br />

        <p>
          If you have any supporting or related documents you may upload them
          now.
        </p>

        <section className="inline-block pt-2">
          <Button
            label={uploadButton.label}
            onClick={uploadButton.onClick}
            styling={uploadButton.styling}
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
