import React from "react";

import { Loader } from "./Loader";

export default {
  title: "Loader",
  component: Loader
};

export const Page = () => <Loader page />;

export const Mobile = () => <Loader page />;

Mobile.story = {
  parameters: {
    viewport: {
      defaultViewport: "mobile2"
    }
  }
};
