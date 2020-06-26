import React from "react";
import { getTestData } from "../../../modules/confirmationPopupTestData";

import CSOStatus from "./CSOStatus";

export default {
  title: "CSOStatus",
  component: CSOStatus
};

const confirmationPopup = getTestData();

export const AccountExists = () => (
  <CSOStatus accountExists confirmationPopup={confirmationPopup} />
);

export const NoAccountExists = () => (
  <CSOStatus accountExists={false} confirmationPopup={confirmationPopup} />
);

export const AccountExistsMobile = () => (
  <CSOStatus accountExists confirmationPopup={confirmationPopup} />
);

export const NoAccountExistsMobile = () => (
  <CSOStatus accountExists={false} confirmationPopup={confirmationPopup} />
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
