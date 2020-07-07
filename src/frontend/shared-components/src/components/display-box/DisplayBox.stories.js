import React from "react";
import { MdPerson } from "react-icons/md";

import { DisplayBox } from "./DisplayBox";
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
  <DisplayBox styling={"blue-background"} icon={icon} element={table} />
);

export const WithSuccess = () => (
  <DisplayBox
    styling={"success-background"}
    icon={icon}
    element="This is a success!"
  />
);

export const WithAlert = () => (
  <DisplayBox
    styling={"warning-background"}
    icon={icon}
    element="This is an alert!"
  />
);

export const WithWarning = () => (
  <DisplayBox
    styling={"error-background"}
    icon={icon}
    element="This is a warning!"
  />
);

export const WithoutIconMobile = () => <DisplayBox element={table} />;

export const WithIconMobile = () => <DisplayBox icon={icon} element={table} />;

export const WithBlueBackgroundMobile = () => (
  <DisplayBox styling={"blue-background"} icon={icon} element={table} />
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
