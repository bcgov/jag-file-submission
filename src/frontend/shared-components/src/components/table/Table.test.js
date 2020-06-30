import React from "react";
import testBasicSnapshot from "../../TestHelper";

import Table from "./Table";
import { getTableTestData } from "../../modules/tableTestData";

const tableData = getTableTestData();

describe("Table Component", () => {
  test("Matches the default snapshot", () => {
    testBasicSnapshot(<Table table={tableData} />);
  });

  test("Matches the blue striped snapshot", () => {
    testBasicSnapshot(<Table table={{ ...tableData, style: "blue-stripe" }} />);
  });
});
