// TODO: remove ignoring Rush.js from coverage report
import React from "react";
import { render, fireEvent, getByText } from "@testing-library/react";
import { getDocumentsData } from "../../../modules/test-data/documentTestData";
import { getTestData } from "../../../modules/test-data/confirmationPopupTestData";
import { getCourtData } from "../../../modules/test-data/courtTestData";
import { generateJWTToken } from "../../../modules/helpers/authentication-helper/authenticationHelper";

import Rush from "./Rush";

describe("Rush Component", () => {
  let realDate;
  const confirmationPopup = getTestData();
  const submissionId = "abc123";
  const courtData = getCourtData();
  const files = getDocumentsData();
  const submissionFee = 25.5;

  const payment = {
    confirmationPopup,
    submissionId,
    courtData,
    files,
    submissionFee,
  };

  const token = generateJWTToken({
    preferred_username: "username@bceid",
    realm_access: {
      roles: ["rush_flag"],
    },
    email: "bobross@paintit.com",
    family_name: "ross",
    given_name: "bob",
  });
  localStorage.setItem("jwt", token);
  window.scrollTo = jest.fn();

  test("Matches the snapshot", () => {
    const currentDate = new Date("2019-05-14T11:01:58.135Z");
    realDate = Date;
    global.Date = class extends Date {
      constructor() {
        return currentDate;
      }
    };

    const { asFragment } = render(<Rush payment={payment} />);

    expect(asFragment()).toMatchSnapshot();

    global.Date = realDate;
  });

  test("Clicking on cancel request takes user back to payment page", () => {
    const { container, asFragment } = render(<Rush payment={payment} />);

    const button = getByText(container, "Cancel");

    fireEvent.click(button);

    expect(asFragment()).toMatchSnapshot();
  });

  test("Surname field is limited to 30 characters", () => {
    // 40 characters long
    const shortString = "asdfbasdfbasdfbasdfbasdfbasdfbasdfbasdfb";

    // 30 characters long
    const shortStringTruncated = "asdfbasdfbasdfbasdfbasdfbasdfb";

    const { getByLabelText, getByDisplayValue } = render(
      <Rush payment={payment} />
    );

    const radioButton1 = getByLabelText(
      "The attached application is made under Rule 8-5 (1) SCR."
    );
    expect(radioButton1).toBeInTheDocument();

    fireEvent.click(radioButton1);

    const surnameInput = getByDisplayValue("ross");

    expect(surnameInput).toBeInTheDocument();

    fireEvent.change(surnameInput, { target: { value: shortString } });
    expect(surnameInput.value).toBe(shortStringTruncated);
  });

  test("First name field is limited to 30 characters", () => {
    // 40 characters long
    const shortString = "asdfbasdfbasdfbasdfbasdfbasdfbasdfbasdfb";

    // 30 characters long
    const shortStringTruncated = "asdfbasdfbasdfbasdfbasdfbasdfb";

    const { getByLabelText, getByDisplayValue } = render(
      <Rush payment={payment} />
    );

    const radioButton1 = getByLabelText(
      "The attached application is made under Rule 8-5 (1) SCR."
    );
    expect(radioButton1).toBeInTheDocument();

    fireEvent.click(radioButton1);

    const firstNameInput = getByDisplayValue("bob");

    expect(firstNameInput).toBeInTheDocument();

    fireEvent.change(firstNameInput, { target: { value: shortString } });
    expect(firstNameInput.value).toBe(shortStringTruncated);
  });

  test("Organization field is limited to 150 characters", () => {
    // 165 characters long
    const longString =
      "asdfbasdfbasdfbasdfbasdfbasdfbasdfbasdfbasdfbasdfbasdfbasdfbasdfbasdfbasdfbasdfbasdfbasdfbasdfbasdfbasdfbasdfbasdfbasdfbasdfbasdfbasdfbasdfbasdfbasdfbasdfbasdfbasdfb";

    // 150 characters long
    const longStringTruncated =
      "asdfbasdfbasdfbasdfbasdfbasdfbasdfbasdfbasdfbasdfbasdfbasdfbasdfbasdfbasdfbasdfbasdfbasdfbasdfbasdfbasdfbasdfbasdfbasdfbasdfbasdfbasdfbasdfbasdfbasdfb";

    const { getByLabelText } = render(<Rush payment={payment} />);

    const radioButton1 = getByLabelText(
      "The attached application is made under Rule 8-5 (1) SCR."
    );
    expect(radioButton1).toBeInTheDocument();

    fireEvent.click(radioButton1);

    const orgInput = getByLabelText("Organization");

    expect(orgInput).toBeInTheDocument();

    fireEvent.change(orgInput, { target: { value: longString } });
    expect(orgInput.value).toBe(longStringTruncated);
  });
});
