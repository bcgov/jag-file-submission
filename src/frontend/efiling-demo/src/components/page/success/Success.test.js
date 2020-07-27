import React from "react";
import { createMemoryHistory } from "history";
import { render, getByText, fireEvent } from "@testing-library/react";
import Success from "./Success";

const header = {
  name: "eFiling Demo Client",
  history: createMemoryHistory(),
};

const page = { header };

describe("Success", () => {
  test("Component matches the snapshot", () => {
    const { asFragment } = render(<Success page={page} />);

    expect(asFragment()).toMatchSnapshot();
  });

  test("Clicking button on success page redirects to home", () => {
    const { container } = render(<Success page={page} />);

    fireEvent.click(getByText(container, "Return home"));

    expect(header.history.location.pathname).toEqual("/");
  });
});
