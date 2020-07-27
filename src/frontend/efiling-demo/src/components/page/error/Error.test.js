import React from "react";
import { createMemoryHistory } from "history";
import { render, getByText, fireEvent } from "@testing-library/react";
import Error from "./Error";

const header = {
  name: "eFiling Demo Client",
  history: createMemoryHistory(),
};

const status = "400";
const message = "Could not communicate with CSO.";

const page = { header, status, message };

describe("Error", () => {
  test("Component matches the snapshot", () => {
    const { asFragment } = render(<Error page={page} />);

    expect(asFragment()).toMatchSnapshot();
  });

  test("Clicking button on error page redirects to home", () => {
    const { container } = render(<Error page={page} />);

    fireEvent.click(getByText(container, "Return home"));

    expect(header.history.location.pathname).toEqual("/");
  });
});
