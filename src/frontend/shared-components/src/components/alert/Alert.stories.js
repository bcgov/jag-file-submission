import React from "react";
import { MdError, MdCancel, MdCheckBox, MdInfo } from "react-icons/md";
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
  <Alert
    icon={<MdCheckBox size={32} />}
    type="success"
    styling="success-background"
    element="This is a success message!"
  />
);

export const Warning = () => (
  <Alert
    icon={<MdError size={32} />}
    type="warning"
    styling="warning-background"
    element="This is a warning message!"
  />
);

export const Error = () => (
  <Alert
    icon={<MdCancel size={32} />}
    type="error"
    styling="error-background"
    element="This is an error message!"
  />
);

export const Info = () => (
  <Alert
    icon={<MdInfo size={32} />}
    type="info"
    styling="info-background"
    element="This is an info message!"
  />
);

export const Mobile = () => (
  <Alert
    icon={<MdCheckBox size={32} />}
    type="success"
    styling="success-background"
    element="This is a success message!"
  />
);

const mobileViewport = {
  parameters: {
    viewport: {
      defaultViewport: "mobile2"
    }
  }
};

Mobile.story = mobileViewport;
