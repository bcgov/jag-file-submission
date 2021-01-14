import React from "react";
import axios from "axios";
import { createMemoryHistory } from "history";
import { render, fireEvent, getByText } from "@testing-library/react";

import MockAdapter from "axios-mock-adapter";
import PackageReview from "./PackageReview";
import { getCourtData } from "../../../modules/test-data/courtTestData";

describe("PackageReview Component", () => {
  const header = {
    name: "eFiling Frontend",
    history: createMemoryHistory(),
  };
  const packageId = "1";
  const courtData = getCourtData();
  const submittedDate = new Date("2021-01-14T18:57:43.602Z").toISOString();
  const submittedBy = { firstName: "Han", lastName: "Solo" };

  const page = {
    header,
    packageId,
  };

  let mock;
  beforeEach(() => {
    mock = new MockAdapter(axios);
    window.open = jest.fn();
  });

  const apiRequest = `/filingpackage/${packageId}`;

  test("Matches the snapshot", () => {
    const { asFragment } = render(<PackageReview page={page} />);

    expect(asFragment()).toMatchSnapshot();
  });

  test("Clicking cancel takes user back to parent app", () => {
    const { container } = render(<PackageReview page={page} />);

    fireEvent.click(getByText(container, "Cancel and Return to Parent App"));

    expect(window.open).toHaveBeenCalledWith("http://google.com", "_self");
  });

  test("Api is called successfully when page loads with valid packageId", async () => {
    window.open = jest.fn();
    mock.onGet(apiRequest).reply(200, {
      court: courtData,
      submittedBy,
      submittedDate,
    });

    const spy = jest.spyOn(axios, "get");

    render(<PackageReview page={page} />);

    expect(spy).toHaveBeenCalled();
  });

  test("Api is called, missing or invalid response data", async () => {
    window.open = jest.fn();
    mock.onGet(apiRequest).reply(200, {
      court: courtData,
      submittedDate: "invalidISOString",
    });

    const spy = jest.spyOn(axios, "get");

    render(<PackageReview page={page} />);

    expect(spy).toHaveBeenCalled();
  });

  test("Api is called, missing or invalid response data", async () => {
    window.open = jest.fn();
    mock.onGet(apiRequest).reply(200);

    const spy = jest.spyOn(axios, "get");

    render(<PackageReview page={page} />);

    expect(spy).toHaveBeenCalled();
  });
});
