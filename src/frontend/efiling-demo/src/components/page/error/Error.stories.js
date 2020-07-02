import React from "react";
import { createMemoryHistory } from "history";

import Error from "./Error";

export default {
  title: "Error",
  component: Error
};

const header = {
  name: "eFiling Demo Client",
  history: createMemoryHistory()
};

const error = "Could not communicate with CSO.";

const page = { header, error };

export const Default = () => <Error page={page} />;

export const Mobile = () => <Error page={page} />;

Mobile.story = {
  parameters: {
    viewport: {
      defaultViewport: "mobile2"
    }
  }
};
