import React from "react";
import Table from "../components/table/Table";
import { getTableTestData } from "./tableTestData";

export function getTestTable() {
  const tableTestData = getTableTestData();
  return <Table table={{ ...tableTestData, heading: "" }} />;
}
