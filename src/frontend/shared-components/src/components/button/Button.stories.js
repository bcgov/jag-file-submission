import React from "react";

import { Button } from "./Button";
import "./Button.css";

export default {
  title: "Button",
  component: Button
};

const onClick = () => {};
const label = "Button label";

const normalBlueComponent = (
  <Button onClick={onClick} label={label} styling="normal-blue btn" />
);
const normalWhiteComponent = (
  <Button onClick={onClick} label={label} styling="normal-white btn" />
);

export const NormalBlue = () => normalBlueComponent;

export const NormalBlueMobile = () => normalBlueComponent;

export const NormalWhite = () => normalWhiteComponent;

export const NormalWhiteMobile = () => normalWhiteComponent;

const mobileViewport = {
  parameters: {
    viewport: {
      defaultViewport: "mobile2"
    }
  }
};

NormalBlueMobile.story = mobileViewport;
NormalWhiteMobile.story = mobileViewport;
