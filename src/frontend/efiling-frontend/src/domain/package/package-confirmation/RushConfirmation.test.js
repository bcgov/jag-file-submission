import React from "react";
import { render } from "@testing-library/react";

import RushConfirmation from "./RushConfirmation";

describe("RushConfirmation Component", () => {
  const setShow = jest.fn();

  test("Matches the snapshot", () => {
    const { asFragment } = render(<RushConfirmation show setShow={setShow} />);

    expect(asFragment()).toMatchSnapshot();
  });
});
