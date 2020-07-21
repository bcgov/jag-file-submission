import React from "react";
import {
  render,
  fireEvent,
  getByText,
  getByRole
} from "@testing-library/react";
import { getTestData } from "../../../modules/confirmationPopupTestData";

import Payment from "./Payment";

describe("Payment Component", () => {
  const confirmationPopup = getTestData();
  const submissionId = "abc123";

  test("Matches the snapshot", () => {
    const { asFragment } = render(
      <Payment payment={{ confirmationPopup, submissionId }} />
    );

    expect(asFragment()).toMatchSnapshot();
  });

  test("Agreeing to conditions toggles button disabled state", () => {
    const { container } = render(
      <Payment payment={{ confirmationPopup, submissionId }} />
    );

    expect(getByText(container, "Submit").disabled).toBeTruthy();

    fireEvent.click(getByRole(container, "checkbox"));

    expect(getByText(container, "Submit").disabled).toBeFalsy();
  });

  test("On back click, it redirects back to the package confirmation page", () => {
    const { container, asFragment } = render(
      <Payment payment={{ confirmationPopup, submissionId }} />
    );

    fireEvent.click(getByText(container, "< Back"));

    expect(asFragment()).toMatchSnapshot();
  });
});
