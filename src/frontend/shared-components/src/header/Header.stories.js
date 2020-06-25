import React from "react";

import Header from "./Header";

export default {
  title: "Header",
  component: Header
};

const header = {
  name: "File Submission"
};

export const Default = () => <Header header={header} />;

export const Mobile = () => <Header header={header} />;

Mobile.story = {
  parameters: {
    viewport: {
      defaultViewport: "mobile2"
    }
  }
};
