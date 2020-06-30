import React from "react";
import { create } from "react-test-renderer";

import Table from "./Table";

describe("Table Component", () => {
  test("Matches the snapshot", () => {
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

    const simpleTable = create(<Table table={table} />);
    expect(simpleTable.toJSON()).toMatchSnapshot();
  });
});
