import React from "react";
import { createMemoryHistory } from "history";
import { MemoryRouter } from "react-router-dom";
import axios from "axios";
import { render, wait } from "@testing-library/react";
import MockAdapter from "axios-mock-adapter";

import Home, { saveNavigationToSession } from "./Home";
import { getTestData } from "../../../modules/confirmationPopupTestData";
import { getUserDetails } from "../../../modules/userDetails";

const header = {
  name: "eFiling Frontend",
  history: createMemoryHistory()
};

const confirmationPopup = getTestData();
const page = { header, confirmationPopup };

describe("Home", () => {
  const submissionId = "abc123";
  const apiRequest = `/submission/${submissionId}`;
  const navigation = {
    cancel: {
      url: "cancelurl.com"
    },
    success: {
      url: "successurl.com"
    },
    error: {
      url: ""
    }
  };
  const userDetails = getUserDetails();

  window.open = jest.fn();

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

    const { asFragment } = render(component);

    await wait(() => {
      expect(asFragment()).toMatchSnapshot();
      expect(sessionStorage.getItem("cancelUrl")).toEqual("cancelurl.com");
    });
  });

  test("Component matches the snapshot when user cso account does not exist", async () => {
    mock.onGet(apiRequest).reply(200, {
      userDetails: { ...userDetails, accounts: null },
      navigation
    });

    const { asFragment } = render(component);

    await wait(() => {
      expect(asFragment()).toMatchSnapshot();
      expect(sessionStorage.getItem("cancelUrl")).toEqual("cancelurl.com");
    });
  });

  test("Component matches the snapshot when still loading", async () => {
    mock.onGet(apiRequest).reply(400, { message: "There was an error" });

    const { asFragment } = render(component);

    await wait(() => {
      expect(asFragment()).toMatchSnapshot();
      expect(sessionStorage.getItem("cancelUrl")).toBeFalsy();
    });

    expect(window.open).toHaveBeenCalledWith(
      "error.com?status=400&message=There was a problem.",
      "_self"
    );
  });

  test("saveNavigationToSession saves urls to session storage when url values are truthy", () => {
    expect(sessionStorage.getItem("cancelUrl")).toBeFalsy();
    expect(sessionStorage.getItem("successUrl")).toBeFalsy();
    expect(sessionStorage.getItem("errorUrl")).toBeFalsy();

    saveNavigationToSession(navigation);

    expect(sessionStorage.getItem("cancelUrl")).toEqual("cancelurl.com");
    expect(sessionStorage.getItem("successUrl")).toEqual("successurl.com");
    expect(sessionStorage.getItem("errorUrl")).toBeFalsy();
  });
});
