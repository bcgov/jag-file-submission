import React from "react";
import { getTestData } from "../../../modules/confirmationPopupTestData";

import PackageConfirmation from "./PackageConfirmation";

export default {
  title: "PackageConfirmation",
  component: PackageConfirmation
};

const confirmationPopup = getTestData();

const packageConfirmation = { confirmationPopup };

export const Default = () => (
  <PackageConfirmation packageConfirmation={packageConfirmation} />
);

export const Mobile = () => (
  <PackageConfirmation packageConfirmation={packageConfirmation} />
);

const mobileViewport = {
  parameters: {
    viewport: {
      defaultViewport: "mobile2"
    }
  }
};

Mobile.story = mobileViewport;
