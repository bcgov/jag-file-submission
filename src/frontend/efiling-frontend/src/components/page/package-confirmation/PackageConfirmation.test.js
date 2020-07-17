import React from "react";
import axios from "axios";
import MockAdapter from "axios-mock-adapter";
import { render, wait } from "@testing-library/react";
import { getTestData } from "../../../modules/confirmationPopupTestData";

import PackageConfirmation from "./PackageConfirmation";

describe("PackageConfirmation Component", () => {
  const confirmationPopup = getTestData();
  const submissionId = "abc123";
  const apiRequest = `/submission/${submissionId}/filing-package`;
  const packageConfirmation = { confirmationPopup, submissionId };
  const csoAccountStatus = { isNew: false };
  const documents = [
    {
      name: "file name 1",
      description: "file description 1",
      type: "file type",
      statutoryFeeAmount: 40
    },
    {
      name: "file name 2",
      description: "file description 2",
      type: "file type",
      statutoryFeeAmount: 0
    }
  ];

  sessionStorage.setItem("csoAccountId", "123");

  let mock;
  beforeEach(() => {
    mock = new MockAdapter(axios);
  });

  test("Matches the existing account snapshot", async () => {
    mock.onGet(apiRequest).reply(200, { documents: documents });

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
    mock.onGet(apiRequest).reply(200, { documents: documents });

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
});
