import React from "react";
import { getTestData } from "../../../modules/confirmationPopupTestData";

import PackageConfirmation from "./PackageConfirmation";

export default {
  title: "PackageConfirmation",
  component: PackageConfirmation
};

const confirmationPopup = getTestData();

const packageConfirmation = { confirmationPopup };

const csoAccountStatus = { isNew: false };

export const ExistingAccount = () => (
  <PackageConfirmation
    packageConfirmation={packageConfirmation}
    csoAccountStatus={csoAccountStatus}
  />
);

export const NewAccount = () => (
  <PackageConfirmation
    packageConfirmation={packageConfirmation}
    csoAccountStatus={{ ...csoAccountStatus, isNew: true }}
  />
);

export const Mobile = () => (
  <PackageConfirmation
    packageConfirmation={packageConfirmation}
    csoAccountStatus={csoAccountStatus}
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
