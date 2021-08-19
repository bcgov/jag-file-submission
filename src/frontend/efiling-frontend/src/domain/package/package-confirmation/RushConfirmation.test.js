import React from "react";
import { render } from "@testing-library/react";

import RushConfirmation from "./RushConfirmation";

describe("RushConfirmation Component", () => {
  let setShow;
  beforeEach(() => {
    setShow = jest.fn();
  });

  test("Matches the snapshot", () => {
    const { asFragment } = render(<RushConfirmation setShow={setShow} />);

    expect(asFragment()).toMatchSnapshot();
  });
});
