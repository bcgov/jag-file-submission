import React from "react";
import { render } from "@testing-library/react";

import Table from "./Table";
import { getTableTestData } from "../../modules/tableTestData";

const tableData = getTableTestData();

describe("Table Component", () => {
  test("Matches the default snapshot", () => {
    const { asFragment } = render(<Table table={tableData} />);
    expect(asFragment).toMatchSnapshot();
  });

  test("Matches the blue striped snapshot", () => {
    const { asFragment } = render(
      <Table table={{ ...tableData, style: "blue-stripe" }} />
    );
    expect(asFragment).toMatchSnapshot();
  });
});
