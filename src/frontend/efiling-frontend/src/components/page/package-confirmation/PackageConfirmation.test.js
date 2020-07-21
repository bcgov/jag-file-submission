import React from "react";
import axios from "axios";
import MockAdapter from "axios-mock-adapter";
import { render, wait, fireEvent, getByText } from "@testing-library/react";
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

  let mock;
  beforeEach(() => {
    mock = new MockAdapter(axios);
    window.open = jest.fn();
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

  test("On click of continue to payment button, it redirects to the payment page", async () => {
    mock.onGet(apiRequest).reply(200, { documents });

    const { container, asFragment } = render(
      <PackageConfirmation
        packageConfirmation={packageConfirmation}
        csoAccountStatus={csoAccountStatus}
      />
    );

    fireEvent.click(getByText(container, "Continue to Payment"));

    await wait(() => {
      expect(asFragment()).toMatchSnapshot();
    });
  });

  test("Successfully opens the file in new window when get document call succeeds", async () => {
    const blob = new Blob(["foo", "bar"]);
    const file = {
      name: "file name 1",
      description: "file description 1",
      type: "file type",
      statutoryFeeAmount: 40
    };

    global.URL.createObjectURL = jest.fn();
    global.URL.createObjectURL.mockReturnValueOnce("fileurl.com");

    mock.onGet(apiRequest).reply(200, { documents });
    mock.onGet(`/submission/${submissionId}/document/${file.name}`).reply(200, {
      blob
    });

    const { container } = render(
      <PackageConfirmation
        packageConfirmation={packageConfirmation}
        csoAccountStatus={csoAccountStatus}
      />
    );

    await wait(() => {});

    fireEvent.click(getByText(container, file.name));

    await wait(() => {
      expect(window.open).toHaveBeenCalledWith("fileurl.com");
    });
  });

  test("Fails to open the file in new window when get document call fails", async () => {
    const file = {
      name: "file name 1",
      description: "file description 1",
      type: "file type",
      statutoryFeeAmount: 40
    };

    sessionStorage.setItem("errorUrl", "error.com");

    mock.onGet(apiRequest).reply(200, { documents });
    mock
      .onGet(`/submission/${submissionId}/document/${file.name}`)
      .reply(400, { message: "There was an error." });

    const { container } = render(
      <PackageConfirmation
        packageConfirmation={packageConfirmation}
        csoAccountStatus={csoAccountStatus}
      />
    );

    await wait(() => {});

    fireEvent.click(getByText(container, file.name));

    await wait(() => {
      expect(window.open).toHaveBeenCalledWith(
        "error.com?status=400&message=There was an error.",
        "_self"
      );
    });
  });
});
