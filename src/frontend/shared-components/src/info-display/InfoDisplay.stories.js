import React from "react";

import InfoDisplay from "./InfoDisplay";

export default {
  title: "InfoDisplay",
  component: InfoDisplay
};

export const Default = () => <InfoDisplay />;

export const Mobile = () => <InfoDisplay />;

Mobile.story = {
  parameters: {
    viewport: {
      defaultViewport: "mobile2"
    }
  }
};
