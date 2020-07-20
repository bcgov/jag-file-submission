import React from "react";
import axios from "axios";
import MockAdapter from "axios-mock-adapter";
import { render, wait } from "@testing-library/react";
import { getTestData } from "../../../modules/confirmationPopupTestData";
import { getDocumentsData } from "../../../modules/documentTestData";

import PackageConfirmation from "./PackageConfirmation";

describe("PackageConfirmation Component", () => {
  const confirmationPopup = getTestData();
  const submissionId = "abc123";
  const apiRequest = `/submission/${submissionId}/filing-package`;
  const packageConfirmation = { confirmationPopup, submissionId };
  const csoAccountStatus = { isNew: false };
  const documents = getDocumentsData();

  sessionStorage.setItem("csoAccountId", "123");

  window.open = jest.fn();

  let mock;
  beforeEach(() => {
    mock = new MockAdapter(axios);
  });

  test("Matches the existing account snapshot", async () => {
    mock.onGet(apiRequest).reply(200, { documents });

    const { asFragment } = render(
      <PackageConfirmation
        packageConfirmation={packageConfirmation}
        csoAccountStatus={csoAccountStatus}
      />
    );

    await wait(() => {
      expect(asFragment()).toMatchSnapshot();
    });
  });

  test("Matches the new account snapshot", async () => {
    mock.onGet(apiRequest).reply(200, { documents });

    const { asFragment } = render(
      <PackageConfirmation
        packageConfirmation={packageConfirmation}
        csoAccountStatus={{ ...csoAccountStatus, isNew: true }}
      />
    );

    await wait(() => {
      expect(asFragment()).toMatchSnapshot();
    });
  });

  test("When call to retrieve filing package fails, redirects to error page of client app", async () => {
    sessionStorage.setItem("errorUrl", "error.com");

    mock.onGet(apiRequest).reply(400);

    render(
      <PackageConfirmation
        packageConfirmation={packageConfirmation}
        csoAccountStatus={csoAccountStatus}
      />
    );

    await wait(() => {
      expect(window.open).toHaveBeenCalledWith("error.com", "_self");
    });
  });
});
