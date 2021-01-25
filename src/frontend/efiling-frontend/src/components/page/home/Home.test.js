import React from "react";
import { createMemoryHistory } from "history";
import axios from "axios";
import { render, waitFor, fireEvent, getByText } from "@testing-library/react";
import MockAdapter from "axios-mock-adapter";

import Home, { saveUrlsToSessionStorage } from "./Home";
import { getTestData } from "../../../modules/test-data/confirmationPopupTestData";
import { getUserDetails } from "../../../modules/test-data/userDetailsTestData";
import { getDocumentsData } from "../../../modules/test-data/documentTestData";
import { getNavigationData } from "../../../modules/test-data/navigationTestData";
import { getCourtData } from "../../../modules/test-data/courtTestData";
import { generateJWTToken } from "../../../modules/helpers/authentication-helper/authenticationHelper";

const authService = require("../../../domain/authentication/services/AuthService");

const header = {
  name: "eFiling Frontend",
  history: createMemoryHistory(),
};
const confirmationPopup = getTestData();
const submissionId = "abc123";
const transactionId = "trans123";
const page = { header, confirmationPopup, submissionId, transactionId };

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
    identity_provider_alias: "bceid",
  });
  localStorage.setItem("jwt", token);

  let mock;
  beforeEach(() => {
    mock = new MockAdapter(axios);
    mock.onGet(apiRequest).reply(200, {
      navigationUrls,
      clientAppName,
      csoBaseUrl,
    });
    sessionStorage.clear();
  });

  const component = <Home page={page} />;

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
    expect(sessionStorage.getItem("csoBaseUrl")).toBeFalsy();

    saveUrlsToSessionStorage(navigationUrls, csoBaseUrl);

    expect(sessionStorage.getItem("cancelUrl")).toEqual(navigationUrls.cancel);
    expect(sessionStorage.getItem("successUrl")).toEqual(
      navigationUrls.success
    );
    expect(sessionStorage.getItem("errorUrl")).toBeFalsy();
    expect(sessionStorage.getItem("csoBaseUrl")).toEqual(csoBaseUrl);

    sessionStorage.clear();

    saveUrlsToSessionStorage(
      {
        ...navigationUrls,
        cancel: "",
        success: "",
        error: "error.com",
      },
      csoBaseUrl
    );

    expect(sessionStorage.getItem("cancelUrl")).toBeFalsy();
    expect(sessionStorage.getItem("successUrl")).toBeFalsy();
    expect(sessionStorage.getItem("errorUrl")).toEqual("error.com");
    expect(sessionStorage.getItem("csoBaseUrl")).toEqual(csoBaseUrl);
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

  test("clicking cancel opens confirmation popup and clicking confirm takes user back to client app", async () => {
    mock.onGet("csoAccount").reply(200, {
      clientId: userDetails.clientId,
      internalClientNumber: userDetails.internalClientNumber,
    });
    mock.onGet("/bceidAccount").reply(200, {
      firstName: "User",
      lastName: "Name",
      middleName: null,
    });

    const setShow = jest.fn();

    const newConfirmationPopup = {
      ...confirmationPopup,
      mainButton: {
        onClick: setShow,
        label: "Click to open confirmation popup",
        styling: "normal-blue btn",
      },
    };

    const { container } = render(
      <Home page={{ ...page, confirmationPopup: newConfirmationPopup }} />
    );

    await waitFor(() => {});

    fireEvent.click(getByText(container, "Click to open confirmation popup"));

    await waitFor(() => {});

    expect(setShow).toHaveBeenCalled();
  });

  test("When user has authenticated with BCSC with no CSO account, retrieve userInfo from BCSC - success", async () => {
    // IDP is set to bcsc
    const tokenAlt = generateJWTToken({
      preferred_username: "username@bceid",
      email: "username@example.com",
      identity_provider_alias: "bcsc",
    });
    localStorage.setItem("jwt", tokenAlt);

    // return 404 (no account) when querying CSO
    mock.onGet("csoAccount").reply(404);

    // stub out authService to return valid response.
    authService.getBCSCUserInfo = jest.fn().mockImplementation(() =>
      Promise.resolve({
        firstName: "Arthur",
        middleName: "C",
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
      identity_provider_alias: "bcsc",
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
});
