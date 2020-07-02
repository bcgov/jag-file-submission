/* eslint-disable react/jsx-one-expression-per-line */
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
    title: "Cancel process?",
    body
  };

  const onButtonClick = jest.fn();

  const mainButton = {
    label: "main label",
    styling: "normal-blue btn",
    onClick: onButtonClick
  };

  const confirmButton = {
    label: "Yes, cancel my process please",
    styling: "normal-blue btn consistent-width",
    onClick: onButtonClick
  };

  const cancelButton = {
    label: "No, resume my process please",
    styling: "normal-white btn consistent-width",
    onClick: onButtonClick
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
