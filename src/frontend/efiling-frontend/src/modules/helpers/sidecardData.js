/* eslint-disable react/jsx-one-expression-per-line, jsx-a11y/click-events-have-key-events, jsx-a11y/no-static-element-interactions */
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
  let username = getJWTData().preferred_username;
  username = username.substring(0, username.indexOf("@"));

  return {
    heading: "Your CSO Account",
    content: [
      <p key="csoAccountDetails">
        CSO account <strong>{sessionStorage.getItem("csoAccountId")}</strong> is
        linked to your Basic BCeID account&nbsp;
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

const rushSubmission = (setShowRush) => ({
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
    <span className="file-href" onClick={() => setShowRush(true)}>
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
    <p>
      <span className="file-href">Learn more about rejected documents.</span>
    </p>,
  ],
  type: "bluegrey",
  id: "rejectedDocumentsCard",
  isWide: true,
  icon: <MdInfoOutline className="bcgov-side-card-icon" />,
});

export function getSidecardData(setShowRush) {
  const aboutCsoCard = aboutCso();
  const csoAccountDetailsCard = csoAccountDetails();
  const rushSubmissionCard = rushSubmission(setShowRush);
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
