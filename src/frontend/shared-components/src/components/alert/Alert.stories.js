import React from "react";
import mdx from "./Alert.mdx";

import { Alert } from "./Alert";

export default {
  title: "Alert",
  component: Alert,
  parameters: {
    docs: {
      page: mdx
    }
  }
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

export const Mobile = () => (
  <Alert type="success" element="This is a success message!" />
);

const mobileViewport = {
  parameters: {
    viewport: {
      defaultViewport: "mobile2"
    }
  }
};

Mobile.story = mobileViewport;
