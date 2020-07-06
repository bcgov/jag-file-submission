import React from "react";
import testBasicSnapshot from "../../TestHelper";

import { Table } from "./Table";
import { getTableElementsTestData } from "../../modules/tableTestData";

const tableData = getTableElementsTestData();
const header = "BCeID Info";

describe("Table Component", () => {
  test("Matches the default snapshot", () => {
    testBasicSnapshot(<Table heading={header} elements={tableData} />);
  });

  test("Matches the blue striped snapshot", () => {
    testBasicSnapshot(<Table elements={tableData} styling="blue-stripe" />);
  });
});
