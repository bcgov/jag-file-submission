import React from "react";
import axios from "axios";
import FileSaver from "file-saver";
import { createMemoryHistory } from "history";
import { render, waitFor, fireEvent, getByText } from "@testing-library/react";

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
  const filingComments =
    "Lorem ipsum dolor sit amet.<script>alert('Hi');</script>\n\nDuis aute irure dolor.";

  const page = {
    header,
    packageId,
  };

  FileSaver.saveAs = jest.fn();

  let mock;
  beforeEach(() => {
    mock = new MockAdapter(axios);
    window.open = jest.fn();
  });

  const apiRequest = `/filingpackage/${packageId}`;

  test("Matches the snapshot", async () => {
    mock.onGet(apiRequest).reply(200, {
      packageNumber: packageId,
      court: courtData,
      submittedBy,
      submittedDate,
      filingComments,
    });

    const { asFragment } = render(<PackageReview page={page} />);
    await waitFor(() => {});

    expect(asFragment()).toMatchSnapshot();

    // filingComments should have properly escaped html characters
    const fc = asFragment().querySelector("#filingComments");
    expect(fc.innerHTML).toEqual(
      "Lorem ipsum dolor sit amet.&lt;script&gt;alert('Hi');&lt;/script&gt;\n\nDuis aute irure dolor."
    );
  });

  test("Clicking cancel takes user back to parent app", async () => {
    const { container } = render(<PackageReview page={page} />);
    await waitFor(() => {});

    fireEvent.click(getByText(container, "Cancel and Return to Parent App"));

    expect(window.open).toHaveBeenCalledWith("http://google.com", "_self");
  });

  test("Api called successfully when page loads with valid packageId", async () => {
    window.open = jest.fn();
    mock.onGet(apiRequest).reply(200, {
      court: courtData,
      submittedBy,
      submittedDate,
    });

    const spy = jest.spyOn(axios, "get");

    render(<PackageReview page={page} />);
    await waitFor(() => {});

    expect(spy).toHaveBeenCalled();
  });

  test("Api called, empty response data", async () => {
    window.open = jest.fn();
    mock.onGet(apiRequest).reply(200, {
      court: {},
      submittedBy: {},
      submittedDate: "",
    });

    const spy = jest.spyOn(axios, "get");

    render(<PackageReview page={page} />);
    await waitFor(() => {});

    expect(spy).toHaveBeenCalled();
  });

  test("Api called, invalid submittedBy", async () => {
    window.open = jest.fn();
    mock.onGet(apiRequest).reply(200, {
      court: courtData,
      submittedBy: { firstName: "Bob" },
      submittedDate,
    });

    const spy = jest.spyOn(axios, "get");

    render(<PackageReview page={page} />);
    await waitFor(() => {});

    expect(spy).toHaveBeenCalled();
  });

  test("Api called, invalid submittedDate", async () => {
    window.open = jest.fn();
    mock.onGet(apiRequest).reply(200, {
      court: courtData,
      submittedDate: "123",
    });

    const spy = jest.spyOn(axios, "get");

    render(<PackageReview page={page} />);
    await waitFor(() => {});

    expect(spy).toHaveBeenCalled();
  });

  test("Api called, missing response data", async () => {
    window.open = jest.fn();
    mock.onGet(apiRequest).reply(200);

    const spy = jest.spyOn(axios, "get");

    render(<PackageReview page={page} />);
    await waitFor(() => {});

    expect(spy).toHaveBeenCalled();
  });

  test("View Submission Sheet (on click) - successful", async () => {
    const blob = new Blob(["foo", "bar"]);

    global.URL.createObjectURL = jest.fn();
    global.URL.createObjectURL.mockReturnValueOnce("fileurl.com");

    window.open = jest.fn();
    mock.onGet(apiRequest).reply(200, {
      court: courtData,
      submittedBy,
      submittedDate,
    });
    mock
      .onGet(`/filingpackage/${packageId}/submissionSheet`)
      .reply(200, { blob });

    const { container } = render(<PackageReview page={page} />);
    await waitFor(() => {});

    fireEvent.click(getByText(container, "Print Submission Sheet"));
    await waitFor(() => {});

    expect(FileSaver.saveAs).toHaveBeenCalled();
  });

  test("View Submission Sheet (on keyDown) - successful", async () => {
    const blob = new Blob(["foo", "bar"]);

    global.URL.createObjectURL = jest.fn();
    global.URL.createObjectURL.mockReturnValueOnce("fileurl.com");

    window.open = jest.fn();
    mock.onGet(apiRequest).reply(200, {
      court: courtData,
      submittedBy,
      submittedDate,
    });
    mock
      .onGet(`/filingpackage/${packageId}/submissionSheet`)
      .reply(200, { blob });

    const { container } = render(<PackageReview page={page} />);
    await waitFor(() => {});

    fireEvent.keyDown(getByText(container, "Print Submission Sheet"));
    await waitFor(() => {});

    expect(FileSaver.saveAs).toHaveBeenCalled();
  });

  test("View Submission Sheet (on click) - unsuccessful", async () => {
    sessionStorage.setItem("errorUrl", "error.com");

    window.open = jest.fn();
    mock
      .onGet(apiRequest)
      .reply(200, { court: courtData, submittedBy, submittedDate });
    mock
      .onGet(`/filingpackage/${packageId}/submissionSheet`)
      .reply(400, { message: "There was an error." });

    const { container } = render(<PackageReview page={page} />);
    await waitFor(() => {});

    fireEvent.click(getByText(container, "Print Submission Sheet"));
    await waitFor(() => {});

    expect(window.open).toHaveBeenCalledWith(
      "error.com?status=400&message=There was an error.",
      "_self"
    );
  });
});
