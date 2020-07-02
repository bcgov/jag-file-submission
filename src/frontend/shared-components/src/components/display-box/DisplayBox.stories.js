import React from "react";
import { MdPerson } from "react-icons/md";

import DisplayBox from "./DisplayBox";
import { getTestTable } from "../../modules/displayBoxTestData";

export default {
  title: "DisplayBox",
  component: DisplayBox
};

const table = getTestTable();

const icon = <MdPerson size={32} />;

export const WithoutIcon = () => <DisplayBox element={table} />;

export const WithIcon = () => <DisplayBox icon={icon} element={table} />;

export const WithBlueBackground = () => (
  <DisplayBox style={"blue-background"} icon={icon} element={table} />
);

export const WithoutIconMobile = () => <DisplayBox element={table} />;

export const WithIconMobile = () => <DisplayBox icon={icon} element={table} />;

export const WithBlueBackgroundMobile = () => (
  <DisplayBox style={"blue-background"} icon={icon} element={table} />
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
WithBlueBackgroundMobile.story = mobileViewport;
