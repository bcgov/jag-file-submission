/* eslint-disable jsx-a11y/no-static-element-interactions, jsx-a11y/click-events-have-key-events */
import React from "react";
import { MdInfoOutline, MdPerson, MdTimer } from "react-icons/md";
import { getJWTData } from "./authentication-helper/authenticationHelper";

const aboutCso = () => ({
  heading: "About E-File Submission",
  content: [
    <p key="aboutCso">
      E-File Submission is a service to help you securely and electronically
      file documents with the Courts of British Columbia via Court Services
      Online.&nbsp;
      <a
        href={`${
          window.env
            ? window.env.REACT_APP_CSO_BASE_URL
            : process.env.REACT_APP_CSO_BASE_URL
        }/about/index.do`}
        target="_blank"
        rel="noopener noreferrer"
      >
        Learn more about CSO
      </a>
      .
    </p>,
  ],
  type: "bluegrey",
  id: "aboutCsoCard",
  isWide: true,
  icon: <MdInfoOutline className="bcgov-side-card-icon" />,
});

const csoAccountDetails = () => {
  const jwtData = getJWTData();
  const preferredUsername = jwtData?.preferred_username || "";
  let username = preferredUsername;
  if (preferredUsername.includes("@")) {
    username = preferredUsername.substring(0, preferredUsername.indexOf("@"));
  }

  let idp = "";
  if (jwtData?.identityProviderAlias === "bcsc") {
    idp = "BC Services Card";
  } else if (jwtData) {
    idp = "Basic BCeID";
  }

  return {
    heading: "Your CSO Account",
    content: [
      <p key="csoAccountDetails">
        CSO account <strong>{sessionStorage.getItem("csoAccountId")}</strong> is
        linked to your {idp} account&nbsp;
        <strong>{username}</strong>
        &nbsp;and will be used to file documents.&nbsp;
        <a
          href={`${
            window.env
              ? window.env.REACT_APP_CSO_BASE_URL
              : process.env.REACT_APP_CSO_BASE_URL
          }/accounts/editProfile.do`}
          target="_blank"
          rel="noopener noreferrer"
        >
          View your CSO account details
        </a>
        .
      </p>,
    ],
    type: "bluegrey",
    id: "csoAccountDetailsCard",
    isWide: true,
    icon: <MdPerson className="bcgov-side-card-icon" />,
  };
};

const rushSubmission = (setShowModal) => ({
  heading: "About Rush Documents",
  content: [
    <p key="rushSubmission">
      When directed by the court, an order will be processed on an urgent(rush)
      basis. The registry will consider specific reasons for processing an order
      on an urgent(rush) basis.
    </p>,
    <p>
      <b>
        Only request processing on an urgent(rush) basis in exceptional
        circumstances.
      </b>
    </p>,
    <span className="file-href" onClick={() => setShowModal(true)}>
      Learn more about rush processing.
    </span>,
  ],
  type: "bluegrey",
  id: "rushSubmissionCard",
  isWide: true,
  icon: <MdTimer className="bcgov-side-card-icon" />,
});

const amendments = () => ({
  heading: "Amendments",
  content: [
    <p key="amendments">
      Please indicate document uploads that are amendments - changes or
      alterations to existing, previously submitted documents.
    </p>,
  ],
  type: "bluegrey",
  id: "amendmentsCard",
  isWide: true,
  icon: <MdInfoOutline className="bcgov-side-card-icon" />,
});

const supremeCourtScheduling = () => ({
  heading: "Supreme Court Scheduling",
  content: [
    <p key="scs">
      Please indicate if your document upload has been approved for Supreme
      Court Scheduling...
    </p>,
  ],
  type: "bluegrey",
  id: "supremeCourtSchedulingCard",
  isWide: true,
  icon: <MdInfoOutline className="bcgov-side-card-icon" />,
});

const rejectedDocuments = () => ({
  heading: "Rejected Documents",
  content: [
    <p key="rjctdDcmnts">
      If you received a notification that your document(s) is rejected by the
      court registry, you will need to ensure that you complete the following
      steps. Review the Registry Notice which registry staff prepare which
      explains the reason(s) for the rejection. The Registry Notice is located
      on the Package Details screen. Make the required corrections or complete
      any additional documents which may be required. Click the Resubmit
      Documents button to upload the documents that you wish to resubmit.
    </p>,
    <p>
      <b>Notes: </b>
      You will be charged the $7.00 submission fee to resubmit the documents.
      The registry will collect any statutory filing fee if the documents are
      accepted.
    </p>,
    <p>
      <span className="file-href">Learn more about rejected documents.</span>
    </p>,
    <p>
      There will be additional instructions for users when they click the Learn
      more about rejected documents but this will be based in part on the
      documentation provided for the parent apps.
    </p>,
  ],
  type: "bluegrey",
  id: "rejectedDocumentsCard",
  isWide: true,
  icon: <MdInfoOutline className="bcgov-side-card-icon" />,
});

export function getSidecardData(setShowModal) {
  const aboutCsoCard = aboutCso();
  const csoAccountDetailsCard = csoAccountDetails();
  const rushSubmissionCard = rushSubmission(setShowModal);
  const amendmentsCard = amendments();
  const supremeCourtSchedulingCard = supremeCourtScheduling();
  const rejectedDocumentsCard = rejectedDocuments();

  return {
    aboutCso: aboutCsoCard,
    csoAccountDetails: csoAccountDetailsCard,
    rushSubmission: rushSubmissionCard,
    amendments: amendmentsCard,
    supremeCourtScheduling: supremeCourtSchedulingCard,
    rejectedDocuments: rejectedDocumentsCard,
  };
}
