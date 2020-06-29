import React from "react";
import { getTestData } from "../../../modules/confirmationPopupTestData";

import CSOStatus from "./CSOStatus";

export default {
  title: "CSOStatus",
  component: CSOStatus
};

const confirmationPopup = getTestData();

export const AccountExists = () => (
  <CSOStatus csoStatus={{ accountExists: true, confirmationPopup }} />
);

export const NoAccountExists = () => (
  <CSOStatus csoStatus={{ accountExists: false, confirmationPopup }} />
);

export const AccountExistsMobile = () => (
  <CSOStatus csoStatus={{ accountExists: true, confirmationPopup }} />
);

export const NoAccountExistsMobile = () => (
  <CSOStatus csoStatus={{ accountExists: false, confirmationPopup }} />
);

const mobileViewport = {
  parameters: {
    viewport: {
      defaultViewport: "mobile2"
    }
  }
};

AccountExistsMobile.story = mobileViewport;
NoAccountExistsMobile.story = mobileViewport;
