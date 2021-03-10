import React from "react";
import { createMemoryHistory } from "history";
import { render, getByText, fireEvent } from "@testing-library/react";
import Success from "./Success";

const header = {
  name: "eFiling Demo Client",
  history: createMemoryHistory(),
};

const packageRef = "aHR0cHM6L2thZ2VObz0xMTQzMA==";
const buff = Buffer.from(packageRef, "base64");
const returnUrl =
  "https%3A%2F%2Fwww.google.ca%2Fsearch%3Fq%3Dbob%2Bross%26tbm%3Disch";
const url = `${buff.toString("ascii")}?returnUrl=${returnUrl}`;

const page = { header, packageRef };

window.open = jest.fn();

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

  test("Clicking on link to view package submission details decodes ref and opens the url", () => {
    const { container } = render(<Success page={page} />);

    fireEvent.click(getByText(container, "View my submitted package."));

    expect(window.open).toHaveBeenCalledWith(url);
  });

  test("Keydown on link to view package submission details decodes ref and opens the url", () => {
    const { container } = render(<Success page={page} />);

    fireEvent.keyDown(getByText(container, "View my submitted package."));

    expect(window.open).toHaveBeenCalledWith(url);
  });
});
