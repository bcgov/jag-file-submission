import React from "react";
import { createMemoryHistory } from "history";

import Cancel from "./Cancel";

export default {
  title: "Cancel",
  component: Cancel,
};

const header = {
  name: "eFiling Demo Client",
  history: createMemoryHistory(),
};

const page = { header };

export const Default = () => <Cancel page={page} />;

export const Mobile = () => <Cancel page={page} />;

Mobile.parameters = {
  viewport: {
    defaultViewport: "mobile2",
  },
};
