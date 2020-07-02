import React from "react";

import DisplayBox from "./DisplayBox";
import {
  getDisplayBoxTestDataNoIcon,
  getDisplayBoxTestDataIcon,
  getDisplayBoxTestDataStyle
} from "../../modules/displayBoxTestData";

export default {
  title: "DisplayBox",
  component: DisplayBox
};

export const WithoutIcon = () => (
  <DisplayBox displayBox={getDisplayBoxTestDataNoIcon()} />
);

export const WithIcon = () => (
  <DisplayBox displayBox={getDisplayBoxTestDataIcon()} />
);

export const WithBlueBackground = () => (
  <DisplayBox displayBox={getDisplayBoxTestDataStyle()} />
);

export const WithoutIconMobile = () => (
  <DisplayBox displayBox={getDisplayBoxTestDataNoIcon()} />
);

export const WithIconMobile = () => (
  <DisplayBox displayBox={getDisplayBoxTestDataIcon()} />
);

const mobileViewport = {
  parameters: {
    viewport: {
      defaultViewport: "mobile2"
    }
  }
};

WithoutIconMobile.story = mobileViewport;
WithIconMobile.story = mobileViewport;
