import React from "react";
import { createMemoryHistory } from "history";

import Error from "./Error";

export default {
  title: "Error",
  component: Error,
};

const header = {
  name: "eFiling Demo Client",
  history: createMemoryHistory(),
};

const status = "400";
const message = "Could not communicate with CSO.";

const page = { header, status, message };

export const Default = () => <Error page={page} />;

export const Mobile = () => <Error page={page} />;

Mobile.parameters = {
  viewport: {
    defaultViewport: "mobile2",
  },
};
