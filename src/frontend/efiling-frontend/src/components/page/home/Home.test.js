import React from "react";
import { createMemoryHistory } from "history";
import { MemoryRouter } from "react-router-dom";
import axios from "axios";
import { render, waitFor } from "@testing-library/react";
import MockAdapter from "axios-mock-adapter";

import Home, { saveDataToSessionStorage } from "./Home";
import { getTestData } from "../../../modules/test-data/confirmationPopupTestData";
import { getUserDetails } from "../../../modules/test-data/userDetailsTestData";
import { getDocumentsData } from "../../../modules/test-data/documentTestData";
import { getNavigationData } from "../../../modules/test-data/navigationTestData";
import { getCourtData } from "../../../modules/test-data/courtTestData";
import { generateJWTToken } from "../../../modules/helpers/authentication-helper/authenticationHelper";

const header = {
  name: "eFiling Frontend",
  history: createMemoryHistory(),
};

const confirmationPopup = getTestData();
const page = { header, confirmationPopup };

describe("Home", () => {
  const submissionId = "abc123";
  const apiRequest = `/submission/${submissionId}`;
  const getFilingPackagePath = `/submission/${submissionId}/filing-package`;
  const navigation = getNavigationData();
  const documents = getDocumentsData();
  const court = getCourtData();
  const submissionFeeAmount = 25.5;
  const userDetails = getUserDetails();

  window.open = jest.fn();

  const token = generateJWTToken({
    preferred_username: "username@bceid",
    email: "username@example.com",
  });
  localStorage.setItem("jwt", token);

  let mock;
  beforeEach(() => {
    mock = new MockAdapter(axios);
    sessionStorage.clear();
  });

  const component = (
    <MemoryRouter initialEntries={[`?submissionId=${submissionId}`]}>
      <Home page={page} />
    </MemoryRouter>
  );

  test("Component matches the snapshot when user cso account exists", async () => {
    mock.onGet(apiRequest).reply(200, { userDetails, navigation });
    mock
      .onGet(getFilingPackagePath)
      .reply(200, { documents, court, submissionFeeAmount });

    const { asFragment } = render(component);

    await waitFor(() => {});

    expect(asFragment()).toMatchSnapshot();
    expect(sessionStorage.getItem("cancelUrl")).toEqual("cancelurl.com");
  });

  test("Component matches the snapshot when user cso account does not exist", async () => {
    mock.onGet(apiRequest).reply(200, {
      userDetails: { ...userDetails, accounts: null },
      navigation,
    });

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

  test("saveDataToSessionStorage saves urls to session storage", () => {
    expect(sessionStorage.getItem("cancelUrl")).toBeFalsy();
    expect(sessionStorage.getItem("successUrl")).toBeFalsy();
    expect(sessionStorage.getItem("errorUrl")).toBeFalsy();

    saveDataToSessionStorage(userDetails.cardRegistered, navigation);

    expect(sessionStorage.getItem("cancelUrl")).toEqual("cancelurl.com");
    expect(sessionStorage.getItem("successUrl")).toEqual("successurl.com");
    expect(sessionStorage.getItem("errorUrl")).toBeFalsy();
    expect(sessionStorage.getItem("cardRegistered")).toEqual("true");

    sessionStorage.clear();

    saveDataToSessionStorage(userDetails.cardRegistered, {
      ...navigation,
      cancel: { url: "" },
      success: { url: "" },
      error: { url: "error.com" },
    });

    expect(sessionStorage.getItem("cancelUrl")).toBeFalsy();
    expect(sessionStorage.getItem("successUrl")).toBeFalsy();
    expect(sessionStorage.getItem("errorUrl")).toEqual("error.com");
    expect(sessionStorage.getItem("cardRegistered")).toEqual("true");
  });

  test("Redirects to error page when lookup to bceid call fails", async () => {
    sessionStorage.setItem("errorUrl", "error.com");

    mock.onGet(apiRequest).reply(200, {
      userDetails: { ...userDetails, accounts: null },
      navigation,
    });

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
});
