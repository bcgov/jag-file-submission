import React from "react";

import { Alert } from "./Alert";

export default {
  title: "Alert",
  component: Alert,
  includeStories: []
};

export const Success = () => (
  <Alert type="success" element="This is a success message!" />
);

export const Warning = () => (
  <Alert type="warning" element="This is a warning message!" />
);

export const Error = () => (
  <Alert type="error" element="This is an error message!" />
);

export const Info = () => (
  <Alert type="info" element="This is an info message!" />
);

export const SuccessMobile = () => (
  <Alert type="success" element="This is a success message!" />
);

const mobileViewport = {
  parameters: {
    viewport: {
      defaultViewport: "mobile2"
    }
  }
};

SuccessMobile.story = mobileViewport;
