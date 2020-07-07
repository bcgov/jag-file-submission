import React from "react";

import { Alert } from "./Alert";

export default {
  title: "Alert",
  component: Alert
};

export const Success = () => (
  <Alert type="success" element="This is a success message!" />
);

export const Warning = () => (
  <Alert type="warning" element="This is a warning message!" />
);

export const Danger = () => (
  <Alert type="danger" element="This is a danger message!" />
);

const mobileViewport = {
  parameters: {
    viewport: {
      defaultViewport: "mobile2"
    }
  }
};
