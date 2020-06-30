import React from "react";
import { render } from "@testing-library/react";

import Table from "./Table";

const tableElements = [
  { name: "BCeID", value: "bobross42" },
  {
    name: "Last Name",
    value: "Ross"
  },
  { name: "First Name", value: "Bob" },
  { name: "Email Address", value: "bob.ross@pbs.com" }
];

const table = {
  header: "BCeID Info",
  tableElements
};

describe("Table Component", () => {
  test("Matches the default snapshot", () => {
    const { asFragment } = render(<Table table={table} />);
    expect(asFragment).toMatchSnapshot();
  });

  test("Matches the blue striped snapshot", () => {
    const { asFragment } = render(
      <Table table={{ ...table, tableStyle: "blueStripe" }} />
    );
    expect(asFragment).toMatchSnapshot();
  });
});
