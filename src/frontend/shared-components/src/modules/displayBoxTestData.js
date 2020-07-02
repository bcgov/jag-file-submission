import React from "react";
import { MdPerson } from "react-icons/md";

import Table from "../components/table/Table";
import { getTableTestData } from "./tableTestData";

const tableTestData = getTableTestData();

const displayBoxTestData = {
  element: <Table table={{ ...tableTestData, heading: "" }} />
};

export function getDisplayBoxTestDataNoIcon() {
  return displayBoxTestData;
}

export function getDisplayBoxTestDataIcon() {
  return { ...displayBoxTestData, icon: <MdPerson size={32} /> };
}

export function getDisplayBoxTestDataStyle() {
  return {
    ...displayBoxTestData,
    icon: <MdPerson size={32} />,
    style: "blue-background"
  };
}
