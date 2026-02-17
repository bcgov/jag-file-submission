import React from "react";
import axios from "axios";
import { render, waitFor, fireEvent } from "@testing-library/react";
import userEvent from "@testing-library/user-event";
import MockAdapter from "axios-mock-adapter";
import SubmissionHistory from "../SubmissionHistory";
import { submissions } from "../../../../modules/test-data/submissionHistoryTestData";
import { generateJWTToken } from "../../../../modules/helpers/authentication-helper/authenticationHelper";

const routerDom = require("react-router-dom");

jest.mock("react-router-dom");

describe("Submission History Component", () => {
  let mock;
  beforeEach(() => {
    routerDom.useLocation = jest
      .fn()
      .mockReturnValue({ search: "?applicationCode=FLA" });

    const token = generateJWTToken({
      preferred_username: "username@bceid",
      email: "username@example.com",
      identityProviderAlias: "bcsc",
    });
    localStorage.setItem("jwt", token);

    mock = new MockAdapter(axios);
    window.open = jest.fn();
  });

  afterEach(() => {
    jest.clearAllMocks();
  });

  test("Matches the snapshot", async () => {
    mock.onGet("/filingpackages").reply(200, submissions);

    const { asFragment, getByText } = render(<SubmissionHistory />);
    await waitFor(() => getByText("1234567"));

    expect(asFragment()).toMatchSnapshot();
  });

  test("Searching package # updates ui with new list", async () => {
    mock.onGet("/filingpackages").reply(200, submissions);

    const { getByTestId, getByText, queryByText } = render(
      <SubmissionHistory />
    );
    await waitFor(() => getByText("1234567"));

    const searchBtn = getByText("Search");
    const searchBar = getByTestId("package-search");

    userEvent.type(searchBar, "123");
    await waitFor(() => {});

    fireEvent.click(searchBtn);
    await waitFor(() => {});

    expect(queryByText("7654321")).not.toBeInTheDocument();
  });

  test("Resetting search value resets visible submissions", async () => {
    mock.onGet("/filingpackages").reply(200, submissions);

    const { getByTestId, getByText, queryByText } = render(
      <SubmissionHistory />
    );
    await waitFor(() => getByText("1234567"));

    const searchBtn = getByText("Search");
    const searchBar = getByTestId("package-search");

    userEvent.type(searchBar, "");
    await waitFor(() => {});

    fireEvent.click(searchBtn);
    await waitFor(() => {});

    expect(queryByText("1234567")).toBeInTheDocument();
    expect(queryByText("7654321")).toBeInTheDocument();
  });

  test("Package number redirects to package url - click", async () => {
    mock.onGet("/filingpackages").reply(200, submissions);

    const { getByText } = render(<SubmissionHistory />);
    await waitFor(() => getByText("1234567"));

    const packageNumber = getByText("1234567");
    fireEvent.click(packageNumber);
    await waitFor(() => {});

    expect(window.open).toHaveBeenCalled();
  });

  test("Package number redirects to package url - keydown", async () => {
    mock.onGet("/filingpackages").reply(200, submissions);

    const { getByText } = render(<SubmissionHistory />);
    await waitFor(() => getByText("1234567"));

    const packageNumber = getByText("1234567");

    fireEvent.keyDown(packageNumber, {
      key: "Enter",
      keyCode: "13",
    });
    await waitFor(() => {});

    expect(window.open).toHaveBeenCalled();
  });

  test("Package number redirects to package url - incorrect input", async () => {
    mock.onGet("/filingpackages").reply(200, submissions);

    const { getByText } = render(<SubmissionHistory />);
    await waitFor(() => getByText("1234567"));

    const packageNumber = getByText("1234567");

    fireEvent.keyDown(packageNumber, {
      key: "Enter",
      keyCode: "9",
    });
    await waitFor(() => {});

    expect(window.open).not.toHaveBeenCalled();
  });

  test("Filter SafetyCheck - parent app from FLA", async () => {
    const token = generateJWTToken({
      preferred_username: "username@bceid",
      email: "username@example.com",
      identityProviderAlias: "bcsc",
      "cso-application-code": "FLA",
    });
    localStorage.setItem("jwt", token);

    const { queryByText } = render(<SubmissionHistory />);
    await waitFor(() => {});
    expect(queryByText(/SAFETY CHECK/i)).toBeInTheDocument();
  });

  test("Filter SafetyCheck - parent app not from FLA", async () => {
    const token = generateJWTToken({
      preferred_username: "username@bceid",
      email: "username@example.com",
      identityProviderAlias: "bcsc",
      "cso-application-code": "DEMO",
    });
    localStorage.setItem("jwt", token);

    const { queryByText } = render(<SubmissionHistory />);
    await waitFor(() => {});
    expect(queryByText(/SAFETY CHECK/i)).not.toBeInTheDocument();
  });
});
