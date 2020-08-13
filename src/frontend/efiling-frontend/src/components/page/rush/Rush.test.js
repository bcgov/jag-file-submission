// TODO: remove ignoring Rush.js from coverage report
import React from "react";
import { render } from "@testing-library/react";
import { getDocumentsData } from "../../../modules/test-data/documentTestData";
import { getTestData } from "../../../modules/test-data/confirmationPopupTestData";
import { getCourtData } from "../../../modules/test-data/courtTestData";

import Rush from "./Rush";

describe("Rush Component", () => {
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

  test("Matches the snapshot", () => {
    const { asFragment } = render(<Rush payment={payment} />);

    expect(asFragment()).toMatchSnapshot();
  });
});
