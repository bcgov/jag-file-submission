import React from "react";
import { render } from "@testing-library/react";
import { getTestData } from "../../../modules/confirmationPopupTestData";

import CSOStatus from "./CSOStatus";

describe("CSOStatus Component", () => {
  const confirmationPopup = getTestData();

  test("Matches the snapshot when account exists", () => {
    const { asFragment } = render(
      <CSOStatus csoStatus={{ accountExists: true, confirmationPopup }} />
    );

    expect(asFragment()).toMatchSnapshot();
  });

  test("Matches the snapshot when account does not exist", () => {
    const { asFragment } = render(
      <CSOStatus csoStatus={{ accountExists: false, confirmationPopup }} />
    );

    expect(asFragment()).toMatchSnapshot();
  });
});
