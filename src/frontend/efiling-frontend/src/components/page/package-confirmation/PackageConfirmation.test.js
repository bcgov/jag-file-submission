import React from "react";
import { render } from "@testing-library/react";
import { getTestData } from "../../../modules/confirmationPopupTestData";

import PackageConfirmation from "./PackageConfirmation";

describe("PackageConfirmation Component", () => {
  const confirmationPopup = getTestData();
  const packageConfirmation = { confirmationPopup };
  const csoAccountStatus = { isNew: false };

  test("Matches the existing account snapshot", () => {
    const { asFragment } = render(
      <PackageConfirmation
        packageConfirmation={packageConfirmation}
        csoAccountStatus={csoAccountStatus}
      />
    );

    expect(asFragment()).toMatchSnapshot();
  });

  test("Matches the new account snapshot", () => {
    const { asFragment } = render(
      <PackageConfirmation
        packageConfirmation={packageConfirmation}
        csoAccountStatus={{ ...csoAccountStatus, isNew: true }}
      />
    );

    expect(asFragment()).toMatchSnapshot();
  });
});
