import React from "react";
import { Table } from "../components/table/Table";
import { getTableElementsTestData } from "./tableTestData";

export function getTestTable() {
  const tableTestData = getTableElementsTestData();
  return <Table elements={tableTestData} />;
}
