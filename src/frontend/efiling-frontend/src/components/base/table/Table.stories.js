import React from "react";

import Table from "./Table";

export default {
  title: "Table",
  component: Table
};

const tableElements = [
  { name: "Org name", value: "Test Org Name" },
  {
    name: "Org Address",
    value: `123 Somewhere Lane\nNo where\nBritish Columbia\nV9V 9V9\nCanada`
  },
  { name: "Applicant Relationship", value: "Employee" }
];

const table = {
  header: "Org Info",
  tableElements
};

export const Default = () => <Table table={table} />;
