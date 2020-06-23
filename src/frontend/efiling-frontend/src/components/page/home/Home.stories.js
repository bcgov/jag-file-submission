import React from "react";
import { MemoryRouter } from "react-router-dom";
import { createMemoryHistory } from "history";

import Home from "./Home";

export default {
  title: "Home",
  component: Home
};

const header = {
  name: "eFiling Demo Client",
  history: createMemoryHistory()
};

const page = { header };

export const Loader = () => (
  <MemoryRouter initialEntries={["/?submissionId=123"]}>
    <Home page={page} />
  </MemoryRouter>
);

export const Mobile = () => (
  <MemoryRouter initialEntries={["/?submissionId=123"]}>
    <Home page={page} />
  </MemoryRouter>
);

Mobile.story = {
  parameters: {
    viewport: {
      defaultViewport: "mobile2"
    }
  }
};
