// TODO: remove ignoring Rush.js from coverage report
import React from "react";
import { render } from "@testing-library/react";

import Rush from "./Rush";

describe("Rush Component", () => {
  test("Matches the snapshot", () => {
    const { asFragment } = render(<Rush />);

    expect(asFragment()).toMatchSnapshot();
  });
});
