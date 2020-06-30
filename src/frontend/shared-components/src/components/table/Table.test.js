import React from "react";
import { render } from "@testing-library/react";

import Table from "./Table";

describe("Table Component", () => {
  test("Matches the snapshot", () => {
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

    const { asFragment } = render(<Table table={table} />);
    expect(asFragment).toMatchSnapshot();
  });
});
