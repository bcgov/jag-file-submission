import React from "react";

import Footer from "./Footer";

export default {
  title: "Footer",
  component: Footer
};

export const Default = () => <Footer />;

export const Mobile = () => <Footer />;

Mobile.story = {
  parameters: {
    viewport: {
      defaultViewport: "mobile2"
    }
  }
};
