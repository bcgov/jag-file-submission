import React from "react";
import { createMemoryHistory } from "history";
import axios from "axios";
import Home from "./Home";
import { render, getByText, wait, fireEvent } from "@testing-library/react";

const MockAdapter = require("axios-mock-adapter");

window.open = jest.fn();

const header = {
  name: "eFiling Demo Client",
  history: createMemoryHistory()
};

const page = { header };

describe("Home", () => {
  let mock;

  beforeEach(() => {
    mock = new MockAdapter(axios);
  });

  test("Component matches the snapshot", () => {
    const { asFragment } = render(<Home page={page} />);

    expect(asFragment()).toMatchSnapshot();
  });

  test("generateUrl function redirects to frontend app on success", async () => {
    const efilingUrl = "example.com";

    mock.onPost("/submission/generateUrl").reply(200, { efilingUrl });

    const { container } = render(<Home page={page} />);

    fireEvent.click(getByText(container, "With CSO Account"));

    await wait(() => {
      expect(window.open).toHaveBeenCalledTimes(1);
      expect(window.open).toHaveBeenCalledWith(efilingUrl, "_self");
    });
  });

  test("generateUrl function displays an error message on page on failure", async () => {
    mock.onPost("/submission/generateUrl").reply(400);

    const { container } = render(<Home page={page} />);

    fireEvent.click(getByText(container, "With CSO Account"));

    await wait(() => {
      expect(
        getByText(
          container,
          "An error occurred while generating the URL. Please try again."
        )
      ).toBeInTheDocument();
    });
  });
});
