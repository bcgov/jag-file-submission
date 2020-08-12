import React from "react";

import Rush from "./Rush";

export default {
  title: "Rush",
  component: Rush,
};

export const Default = () => <Rush />;

export const Mobile = () => <Rush />;

const mobileViewport = {
  parameters: {
    viewport: {
      defaultViewport: "mobile2",
    },
  },
};

Mobile.story = mobileViewport;
