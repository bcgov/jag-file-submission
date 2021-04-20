import React from "react";
import FileSaver from "file-saver";
import { render, waitFor, fireEvent } from "@testing-library/react";

import MockAdapter from "axios-mock-adapter";
import moment from "moment-timezone";
import api from "../../../../AxiosConfig";
import PackageReview from "../PackageReview";
import { getCourtData } from "../../../../modules/test-data/courtTestData";
import * as packageReviewTestData from "../../../../modules/test-data/packageReviewTestData";
import { generateJWTToken } from "../../../../modules/helpers/authentication-helper/authenticationHelper";

const routerDom = require("react-router-dom");
const mockHelper = require("../../../../modules/helpers/mockHelper");

jest.mock("react-router-dom");

// hack fix to force GitHub to run tests in BC timezone
moment.tz.setDefault("America/Vancouver");

describe("PackageReview Component", () => {
  const packageId = "1";
  const links = { packageHistoryUrl: "http://google.com" };
  const courtData = getCourtData();
  const submittedDate = new Date("2021-01-14T18:57:43.602Z").toISOString();
  const submittedBy = { firstName: "Han", lastName: "Solo" };
  const filingComments =
    "Lorem ipsum dolor sit amet.<script>alert('Hi');</script>\n\nDuis aute irure dolor.";
  const { documents } = packageReviewTestData;
  const { parties } = packageReviewTestData;
  const { payments } = packageReviewTestData;

  const csoRedirectResponse = {
    links,
    packageNumber: packageId,
    court: courtData,
    submittedBy,
    submittedDate,
    documents,
    hasRegistryNotice: true,
  };

  FileSaver.saveAs = jest.fn();

  let mock;
  beforeEach(() => {
    routerDom.useParams = jest.fn().mockReturnValue({ packageId: 1 });
    routerDom.useLocation = jest
      .fn()
      .mockReturnValue({ search: "?returnUrl=http://www.google.ca" });

    // IDP is set in the session
    const token = generateJWTToken({
      preferred_username: "username@bceid",
      email: "username@example.com",
      identityProviderAlias: "bcsc",
    });
    localStorage.setItem("jwt", token);

    mock = new MockAdapter(api);
    window.open = jest.fn();
  });

  afterEach(() => {
    jest.clearAllMocks();
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
      parties,
      payments,
    });

    const { asFragment } = render(<PackageReview />);
    await waitFor(() => {});

    expect(asFragment()).toMatchSnapshot();

    // filingComments should have properly escaped html characters
    const fc = asFragment().querySelector("#filingComments");
    expect(fc.innerHTML).toEqual(
      "Lorem ipsum dolor sit amet.&lt;script&gt;alert('Hi');&lt;/script&gt;\n\nDuis aute irure dolor."
    );
  });

  test("Clicking cancel takes user back to parent app", async () => {
    const url = "http://www.google.ca";
    routerDom.useLocation = jest
      .fn()
      .mockReturnValue({ search: `?returnUrl=${url}` });

    const { getByText } = render(<PackageReview />);
    await waitFor(() => {});

    fireEvent.click(getByText("Return to Parent App"));

    expect(window.open).toHaveBeenCalledWith("http://www.google.ca", "_self");
  });

  test("Clicking cancel takes user back to parent app, encoded URL", async () => {
    const encodedUrlWithParams = encodeURIComponent(
      "https://www.google.ca/search?q=bob+ross&tbm=isch"
    );
    expect(encodedUrlWithParams).toEqual(
      "https%3A%2F%2Fwww.google.ca%2Fsearch%3Fq%3Dbob%2Bross%26tbm%3Disch"
    );
    routerDom.useLocation = jest
      .fn()
      .mockReturnValue({ search: `?returnUrl=${encodedUrlWithParams}` });

    const { getByText } = render(<PackageReview />);
    await waitFor(() => {});

    fireEvent.click(getByText("Return to Parent App"));

    expect(window.open).toHaveBeenCalledWith(
      decodeURIComponent(encodedUrlWithParams),
      "_self"
    );
  });

  test("Clicking cancel takes user back to parent app, invalid URL", async () => {
    const invalidUrl = encodeURIComponent("abcdefg");
    routerDom.useLocation = jest
      .fn()
      .mockReturnValue({ search: `?returnUrl=${invalidUrl}` });

    const { queryByText } = render(<PackageReview />);
    await waitFor(() => {});

    expect(queryByText("Return to Parent App")).toBeNull();
  });

  test("Api called successfully when page loads with valid packageId", async () => {
    mock.onGet(apiRequest).reply(200, {
      court: courtData,
      submittedBy,
      submittedDate,
    });

    const spy = jest.spyOn(api, "get");

    render(<PackageReview />);
    await waitFor(() => {});

    expect(spy).toHaveBeenCalled();
  });

  test("Api called, empty response data", async () => {
    mock.onGet(apiRequest).reply(200, {
      court: {},
      submittedBy: {},
      submittedDate: "",
    });

    const spy = jest.spyOn(api, "get");

    render(<PackageReview />);
    await waitFor(() => {});

    expect(spy).toHaveBeenCalled();
  });

  test("Api called, invalid submittedBy", async () => {
    mock.onGet(apiRequest).reply(200, {
      court: courtData,
      submittedBy: { firstName: "Bob" },
      submittedDate,
    });

    const spy = jest.spyOn(api, "get");

    render(<PackageReview />);
    await waitFor(() => {});

    expect(spy).toHaveBeenCalled();
  });

  test("Api called, invalid submittedDate", async () => {
    mock.onGet(apiRequest).reply(200, {
      court: courtData,
      submittedDate: "123",
    });

    const spy = jest.spyOn(api, "get");

    render(<PackageReview />);
    await waitFor(() => {});

    expect(spy).toHaveBeenCalled();
  });

  test("Api called, missing response data", async () => {
    mock.onGet(apiRequest).reply(200);

    const spy = jest.spyOn(api, "get");

    render(<PackageReview />);
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
      links,
    });
    mock
      .onGet(`/filingpackages/${packageId}/submissionSheet`)
      .reply(200, { blob });

    const { getByText } = render(<PackageReview />);
    await waitFor(() => {});

    fireEvent.click(getByText("Print Submission Sheet"));
    await waitFor(() => {});

    expect(FileSaver.saveAs).toHaveBeenCalled();
  });

  test("Redirect to CSO - onClick (successful)", async () => {
    mock.onGet(apiRequest).reply(200, csoRedirectResponse);

    const { getByText } = render(<PackageReview />);
    await waitFor(() => {});

    const button = getByText("view all your previously submitted packages.");

    fireEvent.click(button);
    await waitFor(() => {});

    expect(window.open).toHaveBeenCalled();
  });

  test("Redirect to CSO - keydown (successful)", async () => {
    mock.onGet(apiRequest).reply(200, csoRedirectResponse);

    const { getByText } = render(<PackageReview />);
    await waitFor(() => {});

    const button = getByText("view all your previously submitted packages.");

    fireEvent.keyDown(button, {
      key: "Enter",
      keyCode: "13",
    });
    await waitFor(() => {});

    expect(window.open).toHaveBeenCalled();
  });

  test("Redirect to CSO - incorrect input (unsuccessful)", async () => {
    mock.onGet(apiRequest).reply(200, csoRedirectResponse);

    const { getByText } = render(<PackageReview />);
    await waitFor(() => {});

    const button = getByText("view all your previously submitted packages.");

    fireEvent.keyDown(button, {
      key: "Enter",
      keyCode: "9",
    });
    await waitFor(() => {});

    expect(window.open).not.toHaveBeenCalled();
  });

  test("Redirect to CSO - no link (unsuccesful)", async () => {
    mock.onGet(apiRequest).reply(200, {
      links: { packageHistoryUrl: "" },
      packageNumber: packageId,
      court: courtData,
      submittedBy,
      submittedDate,
      documents,
    });

    const { getByText } = render(<PackageReview />);
    await waitFor(() => {});

    const button = getByText("view all your previously submitted packages.");

    fireEvent.click(button);
    await waitFor(() => {});

    fireEvent.keyDown(button, {
      key: "Enter",
      keyCode: "13",
    });
    await waitFor(() => {});

    expect(window.open).not.toHaveBeenCalled();
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

    const { getByText } = render(<PackageReview />);
    await waitFor(() => {});

    fireEvent.keyDown(getByText("Print Submission Sheet"), {
      key: "Enter",
      keyCode: "13",
    });
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

    const { getByText, queryByText, getByTestId } = render(<PackageReview />);
    await waitFor(() => {});

    fireEvent.click(getByText("Print Submission Sheet"));
    await waitFor(() => {});

    expect(
      queryByText(
        "Something went wrong while trying to download your document."
      )
    ).toBeInTheDocument();

    fireEvent.click(getByTestId("toast-close"));
    await waitFor(() => {});

    expect(
      queryByText(
        "Something went wrong while trying to download your document."
      )
    ).not.toBeInTheDocument();
  });

  test("View Submission Sheet (on keyDown) - unsuccessful", async () => {
    sessionStorage.setItem("errorUrl", "error.com");

    mock
      .onGet(apiRequest)
      .reply(200, { court: courtData, submittedBy, submittedDate });
    mock
      .onGet(`/filingpackages/${packageId}/submissionSheet`)
      .reply(400, { message: "There was an error." });

    const { getByText, queryByText } = render(<PackageReview />);
    await waitFor(() => {});

    fireEvent.keyDown(getByText("Print Submission Sheet"), {
      key: "Enter",
      keyCode: "13",
    });
    await waitFor(() => {});

    expect(
      queryByText(
        "Something went wrong while trying to download your document."
      )
    ).toBeInTheDocument();
  });

  test("View Submission Sheet (on keyDown) - tab", async () => {
    sessionStorage.setItem("errorUrl", "error.com");

    mock
      .onGet(apiRequest)
      .reply(200, { court: courtData, submittedBy, submittedDate });
    mock
      .onGet(`/filingpackages/${packageId}/submissionSheet`)
      .reply(400, { message: "There was an error." });

    const { getByText } = render(<PackageReview />);
    await waitFor(() => {});

    fireEvent.keyDown(getByText("Print Submission Sheet"), {
      key: "Tab",
      keyCode: "9",
    });
    await waitFor(() => {});

    expect(window.open).not.toHaveBeenCalled();
  });

  test("Reload", async () => {
    mock.onGet(apiRequest).reply(200, {
      packageNumber: packageId,
      court: courtData,
      submittedBy,
      submittedDate,
      documents,
    });
    mock.onDelete("/filingpackages/1/document/1").reply(200);
    const noop = jest.spyOn(mockHelper, "noop");

    const { getByText } = render(<PackageReview />);
    await waitFor(() => {});

    // get the span wrapping the file link, click it.
    const withdrawnLink = getByText("withdraw");
    fireEvent.click(withdrawnLink);
    await waitFor(() => {});

    // there should now be a modal popup
    const { asFragment } = render(<PackageReview />);
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

    const { getByText } = render(<PackageReview />);
    await waitFor(() => {});

    // get the span wrapping the file link, click it.
    const withdrawnLink = getByText("withdraw");
    fireEvent.click(withdrawnLink);
    await waitFor(() => {});

    // there should now be a modal popup
    const { asFragment } = render(<PackageReview />);
    await waitFor(() => {});
    expect(asFragment()).toMatchSnapshot();
    const confirmBtn = getByText("Confirm");
    expect(confirmBtn).toBeInTheDocument();

    // click Confirm
    fireEvent.click(confirmBtn);
    await waitFor(() => {});

    expect(noop).not.toHaveBeenCalled();
  });

  test("Download Registry Notice - success (click)", async () => {
    const blob = new Blob(["foo", "bar"]);

    global.URL.createObjectURL = jest.fn();
    global.URL.createObjectURL.mockReturnValueOnce("fileurl.com");

    mock.onGet(apiRequest).reply(200, csoRedirectResponse);
    mock
      .onGet(`/filingpackages/${packageId}/registryNotice`)
      .reply(200, { blob });

    const { getByText } = render(<PackageReview />);
    await waitFor(() => {});

    const button = getByText("View Registry Notice");

    fireEvent.click(button);
    await waitFor(() => {});

    expect(FileSaver.saveAs).toHaveBeenCalled();
  });

  test("Download Registry Notice - success (keydown)", async () => {
    const blob = new Blob(["foo", "bar"]);

    global.URL.createObjectURL = jest.fn();
    global.URL.createObjectURL.mockReturnValueOnce("fileurl.com");

    mock.onGet(apiRequest).reply(200, csoRedirectResponse);
    mock
      .onGet(`/filingpackages/${packageId}/registryNotice`)
      .reply(200, { blob });

    const { getByText } = render(<PackageReview />);
    await waitFor(() => {});

    const button = getByText("View Registry Notice");

    fireEvent.keyDown(button, { key: "Enter", keyCode: "13" });
    await waitFor(() => {});

    expect(FileSaver.saveAs).toHaveBeenCalled();
  });

  test("Download Registry Notice - unsuccessful (keydown)", async () => {
    const blob = new Blob(["foo", "bar"]);

    global.URL.createObjectURL = jest.fn();
    global.URL.createObjectURL.mockReturnValueOnce("fileurl.com");

    mock.onGet(apiRequest).reply(200, csoRedirectResponse);
    mock
      .onGet(`/filingpackages/${packageId}/registryNotice`)
      .reply(200, { blob });

    const { getByText } = render(<PackageReview />);
    await waitFor(() => {});

    const button = getByText("View Registry Notice");

    fireEvent.keyDown(button, { key: "Enter", keyCode: "9" });
    await waitFor(() => {});

    expect(FileSaver.saveAs).not.toHaveBeenCalled();
  });

  test("Download Registry Notice - unsuccessful (error)", async () => {
    mock.onGet(apiRequest).reply(200, csoRedirectResponse);
    mock.onGet(`/filingpackages/${packageId}/registryNotice`).reply(400);

    const { getByText } = render(<PackageReview />);
    await waitFor(() => {});

    const button = getByText("View Registry Notice");

    fireEvent.click(button);
    await waitFor(() => {});

    expect(FileSaver.saveAs).not.toHaveBeenCalled();
  });
});
