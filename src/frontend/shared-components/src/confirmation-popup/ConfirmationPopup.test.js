import React from "react";
import { mount } from "enzyme";
import EnzymeToJson from "enzyme-to-json";

import ConfirmationPopup from "./ConfirmationPopup";

describe("ConfirmationPopup Component", () => {
  const body = () => (
    <>
      <p>Your files will not be submitted.</p>
      <p>
        You will be returned to:
        <br />
        <b>Original</b> website
      </p>
    </>
  );

  const modal = {
    show: true,
    handleShow: jest.fn(),
    handleClose: jest.fn(),
    handleConfirm: jest.fn(),
    title: "Cancel process?",
    body
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

  const component = (
    <ConfirmationPopup
      modal={modal}
      mainButton={mainButton}
      confirmButton={confirmButton}
      cancelButton={cancelButton}
    />
  );

  test("Matches the snapshot", () => {
    const subject = mount(component);
    expect(EnzymeToJson(subject)).toMatchSnapshot();
  });
});
