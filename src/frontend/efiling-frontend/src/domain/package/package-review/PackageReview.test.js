import React from "react";
import axios from "axios";
import FileSaver from "file-saver";
import { createMemoryHistory } from "history";
import { render, waitFor, fireEvent } from "@testing-library/react";

import MockAdapter from "axios-mock-adapter";
import moment from "moment-timezone";
import PackageReview from "./PackageReview";
import { getCourtData } from "../../../modules/test-data/courtTestData";

const mockHelper = require("../../../modules/helpers/mockHelper");

// hack fix to force GitHub to run tests in BC timezone
moment.tz.setDefault("America/Vancouver");

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
  const documents = [
    {
      identifier: "1",
      documentProperties: {
        type: "AFF",
        name: "test-document.pdf",
      },
      status: {
        description: "Submitted",
      },
      filingDate: "2020-05-05T00:00:00.000Z",
    },
  ];

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

  const apiRequest = `/filingpackages/${packageId}`;

  test("Matches the snapshot", async () => {
    mock.onGet(apiRequest).reply(200, {
      packageNumber: packageId,
      court: courtData,
      submittedBy,
      submittedDate,
      filingComments,
      documents,
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
    const { getByText } = render(<PackageReview page={page} />);
    await waitFor(() => {});

    fireEvent.click(getByText("Cancel and Return to Parent App"));

    expect(window.open).toHaveBeenCalledWith("http://google.com", "_self");
  });

  test("Api called successfully when page loads with valid packageId", async () => {
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

    mock.onGet(apiRequest).reply(200, {
      court: courtData,
      submittedBy,
      submittedDate,
    });
    mock
      .onGet(`/filingpackages/${packageId}/submissionSheet`)
      .reply(200, { blob });

    const { getByText } = render(<PackageReview page={page} />);
    await waitFor(() => {});

    fireEvent.click(getByText("Print Submission Sheet"));
    await waitFor(() => {});

    expect(FileSaver.saveAs).toHaveBeenCalled();
  });

  test("View Submission Sheet (on keyDown) - successful", async () => {
    const blob = new Blob(["foo", "bar"]);

    global.URL.createObjectURL = jest.fn();
    global.URL.createObjectURL.mockReturnValueOnce("fileurl.com");

    mock.onGet(apiRequest).reply(200, {
      court: courtData,
      submittedBy,
      submittedDate,
    });
    mock
      .onGet(`/filingpackages/${packageId}/submissionSheet`)
      .reply(200, { blob });

    const { getByText } = render(<PackageReview page={page} />);
    await waitFor(() => {});

    fireEvent.keyDown(getByText("Print Submission Sheet"));
    await waitFor(() => {});

    expect(FileSaver.saveAs).toHaveBeenCalled();
  });

  test("View Submission Sheet (on click) - unsuccessful", async () => {
    sessionStorage.setItem("errorUrl", "error.com");

    mock
      .onGet(apiRequest)
      .reply(200, { court: courtData, submittedBy, submittedDate });
    mock
      .onGet(`/filingpackages/${packageId}/submissionSheet`)
      .reply(400, { message: "There was an error." });

    const { getByText } = render(<PackageReview page={page} />);
    await waitFor(() => {});

    fireEvent.click(getByText("Print Submission Sheet"));
    await waitFor(() => {});

    expect(window.open).toHaveBeenCalledWith(
      "error.com?status=400&message=There was an error.",
      "_self"
    );
  });

  test("View Submission Sheet (on keyDown) - unsuccessful", async () => {
    sessionStorage.setItem("errorUrl", "error.com");

    mock
      .onGet(apiRequest)
      .reply(200, { court: courtData, submittedBy, submittedDate });
    mock
      .onGet(`/filingpackages/${packageId}/submissionSheet`)
      .reply(400, { message: "There was an error." });

    const { getByText } = render(<PackageReview page={page} />);
    await waitFor(() => {});

    fireEvent.keyDown(getByText("Print Submission Sheet"), {
      key: "Enter",
      keyCode: "13",
    });
    await waitFor(() => {});

    expect(window.open).toHaveBeenCalledWith(
      "error.com?status=400&message=There was an error.",
      "_self"
    );
  });

  test("Reload Document trigger", async () => {
    mock.onGet(apiRequest).reply(200, {
      packageNumber: packageId,
      court: courtData,
      submittedBy,
      submittedDate,
      documents,
    });
    mock.onDelete("/filingpackages/1/document/1").reply(200);
    const noop = jest.spyOn(mockHelper, "noop");

    const { getByText } = render(<PackageReview page={page} />);
    await waitFor(() => {});

    // get the span wrapping the file link, click it.
    const withdrawnLink = getByText("withdraw");
    fireEvent.click(withdrawnLink);
    await waitFor(() => {});

    // there should now be a modal popup
    const { asFragment } = render(<PackageReview page={page} />);
    await waitFor(() => {});
    expect(asFragment()).toMatchSnapshot();
    const confirmBtn = getByText("Confirm");
    expect(confirmBtn).toBeInTheDocument();

    // click Confirm
    fireEvent.click(confirmBtn);
    await waitFor(() => {});

    expect(noop).toHaveBeenCalled();
  });

  test("Withdraw document network error", async () => {
    mock.onGet(apiRequest).reply(200, {
      packageNumber: packageId,
      court: courtData,
      submittedBy,
      submittedDate,
      documents,
    });
    mock.onDelete("/filingpackages/1/document/1").reply(404);
    const noop = jest.spyOn(mockHelper, "noop");

    const { getByText } = render(<PackageReview page={page} />);
    await waitFor(() => {});

    // get the span wrapping the file link, click it.
    const withdrawnLink = getByText("withdraw");
    fireEvent.click(withdrawnLink);
    await waitFor(() => {});

    // there should now be a modal popup
    const { asFragment } = render(<PackageReview page={page} />);
    await waitFor(() => {});
    expect(asFragment()).toMatchSnapshot();
    const confirmBtn = getByText("Confirm");
    expect(confirmBtn).toBeInTheDocument();

    // click Confirm
    fireEvent.click(confirmBtn);
    await waitFor(() => {});

    expect(noop).toHaveBeenCalled();
  });
});
