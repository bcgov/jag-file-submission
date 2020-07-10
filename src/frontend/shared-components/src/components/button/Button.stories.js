import React from "react";
import { action } from "@storybook/addon-actions";
import mdx from "./Button.mdx";

import { Button } from "./Button";
import "./Button.css";

export default {
  title: "Button",
  component: Button,
  parameters: {
    docs: {
      page: mdx
    }
  }
};

const onClick = () => action("button was clicked");

export const Primary = () => (
  <Button onClick={onClick} label="Submit" styling="normal-blue btn" />
);

export const Secondary = () => (
  <Button onClick={onClick} label="Cancel" styling="normal-white btn" />
);

export const Disabled = () => (
  <Button
    onClick={onClick}
    label="Disabled"
    styling="normal-blue btn"
    disabled
  />
);

export const Mobile = () => (
  <Button onClick={onClick} label="Submit" styling="normal-blue btn" />
);

const mobileViewport = {
  parameters: {
    viewport: {
      defaultViewport: "mobile2"
    }
  }
};

Mobile.story = mobileViewport;
