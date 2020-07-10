import React from "react";
import { render } from "@testing-library/react";
import { getTestData } from "../../../modules/confirmationPopupTestData";

import PackageConfirmation from "./PackageConfirmation";

describe("PackageConfirmation Component", () => {
  const confirmationPopup = getTestData();
  const packageConfirmation = { confirmationPopup };

  test("Matches the snapshot", () => {
    const { asFragment } = render(
      <PackageConfirmation packageConfirmation={packageConfirmation} />
    );

    expect(asFragment()).toMatchSnapshot();
  });
});
