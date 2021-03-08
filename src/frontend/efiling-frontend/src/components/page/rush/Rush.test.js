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
  });
  localStorage.setItem("jwt", token);
  sessionStorage.setItem("csoBaseUrl", "https://dev.justice.gov.bc.ca/cso");
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

    const button = getByText(container, "Cancel Request");

    fireEvent.click(button);

    expect(asFragment()).toMatchSnapshot();
  });
});
