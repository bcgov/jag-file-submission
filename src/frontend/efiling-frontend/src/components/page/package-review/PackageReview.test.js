import React from "react";
import { createMemoryHistory } from "history";
import { render, fireEvent, getByText } from "@testing-library/react";

import PackageReview from "./PackageReview";

describe("PackageReview Component", () => {
  const header = {
    name: "eFiling Frontend",
    history: createMemoryHistory(),
  };
  const packageId = "1";

  const page = {
    header,
    packageId,
  };

  window.open = jest.fn();

  test("Matches the snapshot", () => {
    const { asFragment } = render(<PackageReview page={page} />);

    expect(asFragment()).toMatchSnapshot();
  });

  test("Clicking cancel takes user back to parent app", () => {
    const { container } = render(<PackageReview page={page} />);

    fireEvent.click(getByText(container, "Cancel and Return to Parent App"));

    expect(window.open).toHaveBeenCalledWith("http://google.com", "_self");
  });
});
