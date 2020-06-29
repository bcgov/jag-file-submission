import React from "react";
import { render } from "@testing-library/react";
import { getTestData } from "../../../modules/confirmationPopupTestData";

import CSOStatus from "./CSOStatus";

describe("CSOStatus Component", () => {
  const confirmationPopup = getTestData();

  test("Matches the snapshot when account exists", () => {
    const { asFragment } = render(
      <CSOStatus accountExists confirmationPopup={confirmationPopup} />
    );

    expect(asFragment()).toMatchSnapshot();
  });

  test("Matches the snapshot when account does not exist", () => {
    const { asFragment } = render(
      <CSOStatus accountExists={false} confirmationPopup={confirmationPopup} />
    );

    expect(asFragment()).toMatchSnapshot();
  });
});
