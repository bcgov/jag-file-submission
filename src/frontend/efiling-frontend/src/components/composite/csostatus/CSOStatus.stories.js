import React from "react";

import CSOStatus from "./CSOStatus";

export default {
  title: "CSOStatus",
  component: CSOStatus
};

export const AccountExists = () => <CSOStatus accountExists />;

export const NoAccountExists = () => <CSOStatus accountExists={false} />;

export const AccountExistsMobile = () => <CSOStatus accountExists />;

export const NoAccountExistsMobile = () => <CSOStatus accountExists={false} />;

const mobileViewport = {
  parameters: {
    viewport: {
      defaultViewport: "mobile2"
    }
  }
};

AccountExistsMobile.story = mobileViewport;
NoAccountExistsMobile.story = mobileViewport;
