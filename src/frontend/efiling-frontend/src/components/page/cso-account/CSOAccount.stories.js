import React from "react";
import { getTestData } from "../../../modules/confirmationPopupTestData";
import { getApplicantInfo } from "../../../modules/applicantInfoTestData";

import CSOAccount from "./CSOAccount";

export default {
  title: "CSOAccount",
  component: CSOAccount
};

const confirmationPopup = getTestData();
const applicantInfo = getApplicantInfo();

export const Default = () => (
  <CSOAccount
    confirmationPopup={confirmationPopup}
    applicantInfo={applicantInfo}
  />
);

export const Mobile = () => (
  <CSOAccount
    confirmationPopup={confirmationPopup}
    applicantInfo={applicantInfo}
  />
);

const mobileViewport = {
  parameters: {
    viewport: {
      defaultViewport: "mobile2"
    }
  }
};

Mobile.story = mobileViewport;
