import React from "react";

import { Header } from "./Header";

export default {
  title: "Header",
  component: Header
};

// not passing in history as not required in snapshot, silence warning of unused prop
console.error = () => {};

const header = {
  name: "File Submission"
};

const component = <Header header={header} />;

export const Default = () => component;

export const Mobile = () => component;

Mobile.story = {
  parameters: {
    viewport: {
      defaultViewport: "mobile2"
    }
  }
};
