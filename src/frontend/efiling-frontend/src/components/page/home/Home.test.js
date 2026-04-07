import React from "react";
import axios from "axios";
import { render, waitFor, fireEvent } from "@testing-library/react";
import MockAdapter from "axios-mock-adapter";

import Home, { saveUrlsToSessionStorage } from "./Home";
import { getUserDetails } from "../../../modules/test-data/userDetailsTestData";
import { getDocumentsData } from "../../../modules/test-data/documentTestData";
import { getNavigationData } from "../../../modules/test-data/navigationTestData";
import { getCourtData } from "../../../modules/test-data/courtTestData";
import { generateJWTToken } from "../../../modules/helpers/authentication-helper/authenticationHelper";

const authService = require("../../../domain/authentication/AuthenticationService");

const submissionId = "abc123";
const transactionId = "trans123";

describe("Home", () => {
  const apiRequest = `/submission/${submissionId}/config`;
  const getFilingPackagePath = `/submission/${submissionId}/filing-package`;
  const navigationUrls = getNavigationData();
  const documents = getDocumentsData();
  const court = getCourtData();
  const submissionFeeAmount = 25.5;
  const userDetails = getUserDetails();
  const clientAppName = "client app";
  const csoBaseUrl = "https://dev.justice.gov.bc.ca/cso";

  window.open = jest.fn();

  const token = generateJWTToken({
    preferred_username: "username@bceid",
    email: "username@example.com",
    identityProviderAlias: "bceid",
  });
  localStorage.setItem("jwt", token);

  process.env.REACT_APP_RUSH_TAB_FEATURE_FLAG = "true";

  let mock;
  beforeEach(() => {
    mock = new MockAdapter(axios);
    mock.onGet(apiRequest).reply(200, {
      navigationUrls,
      clientAppName,
      csoBaseUrl,
    });
    sessionStorage.clear();
    sessionStorage.setItem("submissionId", submissionId);
    sessionStorage.setItem("transactionId", transactionId);
  });

  const component = <Home />;

  test("Component matches the snapshot when user cso account exists", async () => {
    mock.onGet("/csoAccount").reply(200, {
      clientId: userDetails.clientId,
      internalClientNumber: userDetails.internalClientNumber,
    });
    mock
      .onGet(getFilingPackagePath)
      .reply(200, { documents, court, submissionFeeAmount });

    const { asFragment } = render(component);

    await waitFor(() => {});

    expect(asFragment()).toMatchSnapshot();
    expect(sessionStorage.getItem("cancelUrl")).toEqual("cancelurl.com");
  });

  test("Component matches the snapshot when user cso account does not exist", async () => {
    mock.onGet("csoAccount").reply(404);
    mock.onGet("/bceidAccount").reply(200, {
      firstName: "User",
      lastName: "Name",
      middleName: null,
    });

    const { asFragment } = render(component);

    await waitFor(() => {});

    expect(asFragment()).toMatchSnapshot();
    expect(sessionStorage.getItem("cancelUrl")).toEqual("cancelurl.com");
  });

  test("Component matches the snapshot when error encountered, does not attempt to redirect with no errorUrl", async () => {
    mock.onGet(apiRequest).reply(400, { message: "There was an error." });

    const { asFragment } = render(component);

    await waitFor(() => {});

    expect(asFragment()).toMatchSnapshot();
    expect(sessionStorage.getItem("cancelUrl")).toBeFalsy();
    expect(window.open).not.toHaveBeenCalled();
  });

  test("Component matches the snapshot when error encountered", async () => {
    mock.onGet(apiRequest).reply(400, { message: "There was an error." });
    sessionStorage.setItem("errorUrl", "error.com");

    const { asFragment } = render(component);

    await waitFor(() => {});

    expect(asFragment()).toMatchSnapshot();
    expect(window.open).toHaveBeenCalledWith(
      "error.com?status=400&message=There was an error.",
      "_self"
    );
  });

  test("saveUrlsToSessionStorage saves urls to session storage", () => {
    expect(sessionStorage.getItem("cancelUrl")).toBeFalsy();
    expect(sessionStorage.getItem("successUrl")).toBeFalsy();
    expect(sessionStorage.getItem("errorUrl")).toBeFalsy();

    saveUrlsToSessionStorage(navigationUrls);

    expect(sessionStorage.getItem("cancelUrl")).toEqual(navigationUrls.cancel);
    expect(sessionStorage.getItem("successUrl")).toEqual(
      navigationUrls.success
    );
    expect(sessionStorage.getItem("errorUrl")).toBeFalsy();

    sessionStorage.clear();

    saveUrlsToSessionStorage({
      ...navigationUrls,
      cancel: "",
      success: "",
      error: "error.com",
    });

    expect(sessionStorage.getItem("cancelUrl")).toBeFalsy();
    expect(sessionStorage.getItem("successUrl")).toBeFalsy();
    expect(sessionStorage.getItem("errorUrl")).toEqual("error.com");
  });

  test("Redirects to error page when lookup to bceid call fails", async () => {
    sessionStorage.setItem("errorUrl", "error.com");

    mock.onGet("csoAccount").reply(404);
    mock.onGet("/bceidAccount").reply(400, {
      message: "There was an error",
    });

    render(component);

    await waitFor(() => {});

    expect(window.open).toHaveBeenCalledWith(
      "error.com?status=400&message=There was an error.",
      "_self"
    );
  });

  test("Redirects back to client app when GET csoAccount call gets non 404 error response", async () => {
    sessionStorage.setItem("errorUrl", "error.com");

    mock.onGet("csoAccount").reply(500, {
      message: "There was an error",
    });

    render(component);

    await waitFor(() => {});

    expect(window.open).toHaveBeenCalledWith(
      "error.com?status=400&message=There was an error.",
      "_self"
    );
  });

  test("clicking cancel opens confirmation popup and clicking confirm takes user back to client app, success", async () => {
    mock.onGet("/csoAccount").reply(200, {
      clientId: userDetails.clientId,
      internalClientNumber: userDetails.internalClientNumber,
    });
    mock.onGet("/bceidAccount").reply(200, {
      firstName: "User",
      lastName: "Name",
      middleName: null,
    });
    mock.onDelete(`/submission/${submissionId}`).reply(200);

    const { getByText } = render(<Home />);
    await waitFor(() => {});
    const cancelBtn = getByText("Cancel");
    expect(cancelBtn).toBeInTheDocument();
    fireEvent.click(cancelBtn);
    await waitFor(() => {});

    // there should now be a modal popup
    render(<Home />);
    await waitFor(() => {});
    const confirmBtn = getByText("Yes, cancel E-File Submission");
    expect(confirmBtn).toBeInTheDocument();
    fireEvent.click(confirmBtn);
    await waitFor(() => {});

    expect(sessionStorage.getItem("validExit")).toEqual("true");
  });

  test("clicking cancel opens confirmation popup, missing cancelurl, success", async () => {
    mock.onGet(apiRequest).reply(200, {
      navigationUrls: {
        success: "successurl.com",
        error: "",
      },
      clientAppName,
      csoBaseUrl,
    });
    mock.onGet("/csoAccount").reply(200, {
      clientId: userDetails.clientId,
      internalClientNumber: userDetails.internalClientNumber,
    });
    mock.onGet("/bceidAccount").reply(200, {
      firstName: "User",
      lastName: "Name",
      middleName: null,
    });
    mock.onDelete(`/submission/${submissionId}`).reply(200);

    const { getByText } = render(<Home />);
    await waitFor(() => {});
    const cancelBtn = getByText("Cancel");
    expect(cancelBtn).toBeInTheDocument();
    fireEvent.click(cancelBtn);
    await waitFor(() => {});

    // there should now be a modal popup
    render(<Home />);
    await waitFor(() => {});
    const confirmBtn = getByText("Yes, cancel E-File Submission");
    expect(confirmBtn).toBeInTheDocument();
    fireEvent.click(confirmBtn);
    await waitFor(() => {});

    expect(sessionStorage.getItem("validExit")).toEqual("true");
  });

  test("clicking cancel opens confirmation popup and clicking confirm takes user back to client app, fail", async () => {
    mock.onGet("/csoAccount").reply(200, {
      clientId: userDetails.clientId,
      internalClientNumber: userDetails.internalClientNumber,
    });
    mock.onGet("/bceidAccount").reply(200, {
      firstName: "User",
      lastName: "Name",
      middleName: null,
    });
    mock.onDelete(`/submission/${submissionId}`).reply(404);

    const { getByText } = render(<Home />);
    await waitFor(() => {});
    const cancelBtn = getByText("Cancel");
    expect(cancelBtn).toBeInTheDocument();
    fireEvent.click(cancelBtn);
    await waitFor(() => {});

    // there should now be a modal popup
    render(<Home />);
    await waitFor(() => {});
    const confirmBtn = getByText("Yes, cancel E-File Submission");
    expect(confirmBtn).toBeInTheDocument();
    fireEvent.click(confirmBtn);
    await waitFor(() => {});

    expect(sessionStorage.getItem("validExit")).toEqual("true");
  });

  test("When user has authenticated with BCSC with no CSO account, retrieve userInfo from BCSC - success", async () => {
    // IDP is set to bcsc
    const tokenAlt = generateJWTToken({
      preferred_username: "username@bcsc",
      email: "username@example.com",
      identityProviderAlias: "bcsc",
    });
    localStorage.setItem("jwt", tokenAlt);

    // return 404 (no account) when querying CSO
    mock.onGet("csoAccount").reply(404);

    // stub out authService to return valid response.
    authService.getBCSCUserInfo = jest.fn().mockImplementation(() =>
      Promise.resolve({
        givenNames: "Arthur C",
        lastName: "Clark",
      })
    );

    const { asFragment } = render(component);
    await waitFor(() => {});

    expect(asFragment()).toMatchSnapshot();
  });

  test("When user has authenticated with BCSC with no CSO account, retrieve userInfo from BCSC - error", async () => {
    // IDP is set to bcsc
    const tokenAlt = generateJWTToken({
      preferred_username: "username@bceid",
      email: "username@example.com",
      identityProviderAlias: "bcsc",
    });
    localStorage.setItem("jwt", tokenAlt);

    // return 404 (no account) when querying CSO
    mock.onGet("csoAccount").reply(404);

    // stub out authService to return invalid response.
    authService.getBCSCUserInfo = jest
      .fn()
      .mockImplementation(() => Promise.reject(new Error("API is down")));

    const { asFragment } = render(component);
    await waitFor(() => {});

    expect(asFragment()).toMatchSnapshot();
  });

  test("When user has authenticated with a non BCeID nor BCSC account - fail", async () => {
    sessionStorage.setItem("errorUrl", "error.com");

    // IDP is set to google
    const tokenAlt = generateJWTToken({
      preferred_username: "username@google",
      email: "username@gmail.com",
      identityProviderAlias: "google",
    });
    localStorage.setItem("jwt", tokenAlt);

    // return 404 (no account) when querying CSO
    mock.onGet("csoAccount").reply(404);

    render(component);
    await waitFor(() => {});

    expect(window.open).toHaveBeenCalledWith(
      "error.com?status=400&message=There was an error.",
      "_self"
    );
  });
});
