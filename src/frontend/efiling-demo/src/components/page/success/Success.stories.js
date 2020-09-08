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

const packageRef = "aHR0cHM6L2thZ2VObz0xMTQzMA==";

const page = { header, packageRef };

export const Default = () => <Success page={page} />;

export const Mobile = () => <Success page={page} />;

Mobile.parameters = {
  viewport: {
    defaultViewport: "mobile2",
  },
};
