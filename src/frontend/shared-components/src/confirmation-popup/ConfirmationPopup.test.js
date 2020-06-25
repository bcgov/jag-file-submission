import React from "react";
import { render } from "@testing-library/react";

import { ConfirmationPopup } from "./ConfirmationPopup";

describe("ConfirmationPopup Component", () => {
  const modal = {
    show: true,
    handleShow: jest.fn(),
    handleClose: jest.fn(),
    title: "Cancel process?",
    body1: "Your process will end.",
    body2: "You will be redirected back to the original application."
  };
  const mainButton = {
    mainLabel: "main label",
    mainStyling: "normal-blue btn"
  };
  const confirmButton = {
    confirmLabel: "Yes, cancel my process please",
    confirmStyling: "normal-blue btn consistent-width"
  };
  const cancelButton = {
    cancelLabel: "No, resume my process please",
    cancelStyling: "normal-white btn consistent-width"
  };

  test("Matches the snapshot", () => {
    const component = (
      <ConfirmationPopup
        modal={modal}
        mainButton={mainButton}
        confirmButton={confirmButton}
        cancelButton={cancelButton}
      />
    );

    const { asFragment } = render(component);

    expect(asFragment()).toMatchSnapshot();
  });
});
