import React from "react";
import { getTestData } from "../../../modules/confirmationPopupTestData";

import CSOAccount from "./CSOAccount";

export default {
  title: "CSOAccount",
  component: CSOAccount
};

const confirmationPopup = getTestData();

export const Default = () => (
  <CSOAccount confirmationPopup={confirmationPopup} />
);

export const Mobile = () => (
  <CSOAccount confirmationPopup={confirmationPopup} />
);

const mobileViewport = {
  parameters: {
    viewport: {
      defaultViewport: "mobile2"
    }
  }
};

Mobile.story = mobileViewport;
