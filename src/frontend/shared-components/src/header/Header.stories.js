import React from "react";
import { Router } from "react-router-dom";
import { createMemoryHistory } from "history";

import { Header } from "./Header";

export default {
  title: "Header",
  component: Header
};

const header = {
  name: "File Submission"
};

const history = createMemoryHistory();

const component = (
  <Router history={history}>
    <Header header={header} />
  </Router>
);

export const Default = () => component;

export const Mobile = () => component;

Mobile.story = {
  parameters: {
    viewport: {
      defaultViewport: "mobile2"
    }
  }
};
