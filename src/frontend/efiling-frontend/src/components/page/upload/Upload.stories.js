import React from "react";

import Upload from "./Upload";

export default {
  title: "Upload",
  component: Upload
};

export const Default = () => <Upload />;

export const Mobile = () => <Upload />;

const mobileViewport = {
  parameters: {
    viewport: {
      defaultViewport: "mobile2"
    }
  }
};

Mobile.story = mobileViewport;
