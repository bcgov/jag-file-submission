import React from "react";

import { Button } from "./Button";

export default {
  title: "Button",
  component: Button
};

const onClick = () => {};
const label = "button label";

const component = <Button onClick={onClick} label={label} />;

export const Default = () => component;

export const Mobile = () => component;

Mobile.story = {
  parameters: {
    viewport: {
      defaultViewport: "mobile2"
    }
  }
};
