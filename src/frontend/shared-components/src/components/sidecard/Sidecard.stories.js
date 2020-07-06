import React from "react";
import { FaIdCard } from "react-icons/fa";
import { Sidecard } from "./Sidecard";

import "./Sidecard.css";

export default {
  title: "Sidecard",
  component: Sidecard
};

const sideCard = {
  heading: "About E-File Submission",
  content: [
    "E-File submission allows you to securely upload and electronically file documents."
  ],
  type: "blue"
};

const iconHeading = (
  <div className="row">
    <div className="side-card-row">
      <div className="round-icon-wrapper">
        <FaIdCard className="side-card-icon" />
      </div>
      <div className="side-card-title">About E-File Submission</div>
    </div>
  </div>
);

export const Blue = () => <Sidecard sideCard={sideCard} />;

export const BlueMobile = () => <Sidecard sideCard={sideCard} />;

export const Grey = () => <Sidecard sideCard={{ ...sideCard, type: "grey" }} />;

export const GreyMobile = () => (
  <Sidecard sideCard={{ ...sideCard, type: "grey" }} />
);

export const BlueGrey = () => (
  <Sidecard sideCard={{ ...sideCard, type: "bluegrey" }} />
);

export const BlueGreyMobile = () => (
  <Sidecard sideCard={{ ...sideCard, type: "bluegrey" }} />
);

export const WithIcon = () => (
  <Sidecard
    sideCard={{ ...sideCard, type: "bluegrey", heading: iconHeading }}
  />
);

export const WithIconMobile = () => (
  <Sidecard
    sideCard={{ ...sideCard, type: "bluegrey", heading: iconHeading }}
  />
);

const mobileViewport = {
  parameters: {
    viewport: {
      defaultViewport: "mobile2"
    }
  }
};

BlueMobile.story = mobileViewport;
GreyMobile.story = mobileViewport;
BlueGreyMobile.story = mobileViewport;
WithIconMobile.story = mobileViewport;
