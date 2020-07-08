import React from "react";

import { Button } from "./Button";
import "./Button.css";

export default {
  title: "Button",
  component: Button
};

const onClick = () => {};
const label = "Button label";

export const NormalBlue = () => (
  <Button onClick={onClick} label={label} styling="normal-blue btn" />
);

export const NormalBlueMobile = () => (
  <Button onClick={onClick} label={label} styling="normal-blue btn" />
);

export const NormalWhite = () => (
  <Button onClick={onClick} label={label} styling="normal-white btn" />
);

export const NormalWhiteMobile = () => (
  <Button onClick={onClick} label={label} styling="normal-white btn" />
);

const mobileViewport = {
  parameters: {
    viewport: {
      defaultViewport: "mobile2"
    }
  }
};

NormalBlueMobile.story = mobileViewport;
NormalWhiteMobile.story = mobileViewport;
