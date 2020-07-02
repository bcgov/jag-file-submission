import React from "react";
import { render } from "@testing-library/react";
import { getTestData } from "../../../modules/confirmationPopupTestData";

import CSOAccount from "./CSOAccount";

describe("CSOAccount Component", () => {
  const confirmationPopup = getTestData();

  test("Matches the snapshot", () => {
    const { asFragment } = render(
      <CSOAccount confirmationPopup={confirmationPopup} />
    );

    expect(asFragment()).toMatchSnapshot();
  });
});
