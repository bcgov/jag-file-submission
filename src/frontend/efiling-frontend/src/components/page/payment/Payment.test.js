import React from "react";
import axios from "axios";
import MockAdapter from "axios-mock-adapter";
import {
  render,
  fireEvent,
  getByText,
  getByRole,
  wait
} from "@testing-library/react";
import { getTestData } from "../../../modules/confirmationPopupTestData";
import { getDocumentsData } from "../../../modules/documentTestData";
import { getCourtData } from "../../../modules/courtTestData";

import Payment from "./Payment";

describe("Payment Component", () => {
  const confirmationPopup = getTestData();
  const submissionId = "abc123";
  const apiRequest = `/submission/${submissionId}/filing-package`;
  const files = getDocumentsData();
  const submissionFee = 25.5;
  const courtData = getCourtData();
  const payment = {
    confirmationPopup,
    submissionId,
    courtData,
    files,
    submissionFee
  };

  let mock;
  beforeEach(() => {
    mock = new MockAdapter(axios);
  });

  test("Matches the snapshot", () => {
    const { asFragment } = render(<Payment payment={payment} />);

    expect(asFragment()).toMatchSnapshot();
  });

  test("Agreeing to conditions toggles button disabled state", () => {
    const { container } = render(<Payment payment={payment} />);

    expect(getByText(container, "Submit").disabled).toBeTruthy();

    fireEvent.click(getByRole(container, "checkbox"));

    expect(getByText(container, "Submit").disabled).toBeFalsy();
  });

  test("On back click, it redirects back to the package confirmation page", async () => {
    mock.onGet(apiRequest).reply(200, {
      documents: files,
      court: courtData,
      submissionFeeAmount: submissionFee
    });

    const { container, asFragment } = render(<Payment payment={payment} />);

    fireEvent.click(getByText(container, "< Back"));

    await wait(() => {
      expect(asFragment()).toMatchSnapshot();
    });
  });
});
