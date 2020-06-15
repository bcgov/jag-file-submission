import React from "react";

import Home from "./Home";

export default {
  title: "Home",
  component: Home
};

export const Default = () => <Home />;

export const Mobile = () => <Home />;

Mobile.story = {
  parameters: {
    viewport: {
      defaultViewport: "mobile2"
    }
  }
};
