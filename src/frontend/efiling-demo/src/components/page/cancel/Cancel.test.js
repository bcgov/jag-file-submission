import React from "react";
import { render, getByText, fireEvent } from "@testing-library/react";
import { createMemoryHistory } from "history";
import Cancel from "./Cancel";

const header = {
  name: "eFiling Demo Client",
  history: {},
  navigate: createMemoryHistory,
};

const page = { header };

describe("Cancel", () => {
  test("Component matches the snapshot", () => {
    const { asFragment } = render(<Cancel page={page} />);

    expect(asFragment()).toMatchSnapshot();
  });

  test("Clicking button on cancel page redirects to home", () => {
    const { container } = render(<Cancel page={page} />);

    fireEvent.click(getByText(container, "Return home"));

    expect(window.location.pathname).toEqual("/");
  });
});
