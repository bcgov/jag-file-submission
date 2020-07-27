import React from "react";
import { createMemoryHistory } from "history";

import Success from "./Success";

export default {
  title: "Success",
  component: Success,
};

const header = {
  name: "eFiling Demo Client",
  history: createMemoryHistory(),
};

const page = { header };

export const Default = () => <Success page={page} />;

export const Mobile = () => <Success page={page} />;

Mobile.story = {
  parameters: {
    viewport: {
      defaultViewport: "mobile2",
    },
  },
};
