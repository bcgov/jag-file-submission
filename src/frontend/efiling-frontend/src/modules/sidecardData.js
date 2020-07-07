import React from "react";
import { MdInfoOutline } from "react-icons/md";

const aboutCsoSideCard = {
  heading: "About E-File Submission",
  content: [
    <p key="aboutCso">
      E-File submission is a service to help you securely and electronically
      file documents with the Government of British Columbia Court Services
      Online (CSO).&nbsp;
      <a
        href="https://justice.gov.bc.ca/cso/about/index.do"
        target="_blank"
        rel="noopener noreferrer"
      >
        Learn more about CSO
      </a>
      .
    </p>
  ],
  type: "bluegrey",
  isWide: true,
  icon: <MdInfoOutline className="side-card-icon" />
};

export function getSidecardData() {
  return {
    aboutCso: aboutCsoSideCard
  };
}
