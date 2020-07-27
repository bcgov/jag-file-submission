import React from "react";
import { createMemoryHistory } from "history";

import Home from "./Home";

export default {
  title: "Home",
  component: Home,
};

const header = {
  name: "eFiling Demo Client",
  history: createMemoryHistory(),
};

const page = { header };

export const Default = () => <Home page={page} />;

export const Mobile = () => <Home page={page} />;

Mobile.story = {
  parameters: {
    viewport: {
      defaultViewport: "mobile2",
    },
  },
};
