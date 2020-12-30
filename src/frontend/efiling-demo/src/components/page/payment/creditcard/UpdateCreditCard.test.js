import React from "react";
import { createMemoryHistory } from "history";
import { render, getByText, fireEvent } from "@testing-library/react";
import UpdateCreditCard from "./UpdateCreditCard";

const header = {
  name: "eFiling Demo Client",
  history: createMemoryHistory(),
};

const buff = Buffer.from(packageRef, "base64");
const url = buff.toString("ascii");

const page = { header, packageRef };

window.open = jest.fn();

describe("Success", () => {
  test("Component matches the snapshot", () => {
    const { asFragment } = render(<UpdateCreditCard page={page} />);

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
