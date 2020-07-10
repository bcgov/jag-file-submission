import React from "react";
import { MdPerson } from "react-icons/md";
import mdx from "./DisplayBox.mdx";

import { DisplayBox } from "./DisplayBox";
import { getTestTable } from "../../modules/displayBoxTestData";

export default {
  title: "DisplayBox",
  component: DisplayBox,
  parameters: {
    docs: {
      page: mdx
    }
  }
};

const table = getTestTable();

const icon = <MdPerson size={32} />;

export const WithoutIcon = () => <DisplayBox element={table} />;

export const WithIcon = () => <DisplayBox icon={icon} element={table} />;

export const WithBlueBackground = () => (
  <DisplayBox styling="blue-background" icon={icon} element={table} />
);

export const WithBorder = () => (
  <DisplayBox styling="border-background" icon={icon} element={table} />
);

export const Mobile = () => (
  <DisplayBox styling="blue-background" icon={icon} element={table} />
);

const mobileViewport = {
  parameters: {
    viewport: {
      defaultViewport: "mobile2"
    }
  }
};

Mobile.story = mobileViewport;
