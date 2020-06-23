import React from "react";

import { Button } from "./Button";
import "./Button.css";

export default {
  title: "Button",
  component: Button
};

const onClick = () => {};
const label = "Button label";
const styling = "normal-blue btn";

const component = <Button onClick={onClick} label={label} styling={styling} />;

export const Default = () => component;

export const Mobile = () => component;

Mobile.story = {
  parameters: {
    viewport: {
      defaultViewport: "mobile2"
    }
  }
};
