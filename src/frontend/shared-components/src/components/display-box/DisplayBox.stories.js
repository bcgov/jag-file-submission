import React from "react";
import { MdPerson } from "react-icons/md";

import DisplayBox from "./DisplayBox";
import { getTestTable } from "../../modules/displayBoxTestData";

export default {
  title: "DisplayBox",
  component: DisplayBox
};

const elemt = <h1>A title</h1>;

const table = getTestTable();

const icon = <MdPerson size={32} />;

export const Simple = () => <DisplayBox element={elemt} />;

export const Complex = () => (
  <DisplayBox style={"grey-background"} icon={icon} element={table} />
);

export const Mobile = () => <DisplayBox />;

const mobileViewport = {
  parameters: {
    viewport: {
      defaultViewport: "mobile2"
    }
  }
};

Mobile.story = mobileViewport;
