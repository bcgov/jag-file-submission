import React from "react";
import axios from "axios";
import MockAdapter from "axios-mock-adapter";
import {
  render,
  fireEvent,
  getByText,
  getByRole,
  waitFor,
} from "@testing-library/react";
import { getTestData } from "../../../modules/test-data/confirmationPopupTestData";
import { getDocumentsData } from "../../../modules/test-data/documentTestData";
import { getCourtData } from "../../../modules/test-data/courtTestData";
import { generateJWTToken } from "../../../modules/helpers/authentication-helper/authenticationHelper";

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
    submissionFee,
  };

  sessionStorage.setItem("cardRegistered", true);
  const token = generateJWTToken({
    preferred_username: "username@bceid",
    realm_access: {
      roles: ["rush_flag"],
    },
  });
  localStorage.setItem("jwt", token);

  let mock;
  beforeEach(() => {
    mock = new MockAdapter(axios);
    window.open = jest.fn();
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

    fireEvent.click(getByRole(container, "checkbox"));

    expect(getByText(container, "Submit").disabled).toBeTruthy();
  });

  test("On back click, it redirects back to the package confirmation page", async () => {
    mock.onGet(apiRequest).reply(200, {
      documents: files,
      court: courtData,
      submissionFeeAmount: submissionFee,
    });

    const { container, asFragment } = render(<Payment payment={payment} />);

    fireEvent.click(getByText(container, "< Back"));

    await waitFor(() => {});

    expect(asFragment()).toMatchSnapshot();
  });

  test("Submit on success redirects to success page", async () => {
    sessionStorage.setItem("successUrl", "success.com");

    mock
      .onPost(`/submission/${submissionId}/submit`)
      .reply(200, { transactionId: 1 });

    const { container } = render(<Payment payment={payment} />);

    fireEvent.click(getByRole(container, "checkbox"));
    fireEvent.click(getByText(container, "Submit"));

    await waitFor(() => {});

    expect(window.open).toHaveBeenCalledWith("success.com", "_self");
  });

  test("Submit on error redirects to error page", async () => {
    sessionStorage.setItem("errorUrl", "error.com");

    mock
      .onPost(`/submission/${submissionId}/submit`)
      .reply(400, { message: "There was an error." });

    const { container } = render(<Payment payment={payment} />);

    fireEvent.click(getByRole(container, "checkbox"));
    fireEvent.click(getByText(container, "Submit"));

    await waitFor(() => {});

    expect(window.open).toHaveBeenCalledWith(
      "error.com?status=400&message=There was an error.",
      "_self"
    );
  });

  test("Click on request rush submission opens rush submission page", async () => {
    mock.onGet(apiRequest).reply(200, {
      documents: files,
      court: courtData,
      submissionFeeAmount: submissionFee,
    });

    const { container, asFragment } = render(<Payment payment={payment} />);

    fireEvent.click(getByText(container, "Request rush submission"));

    await waitFor(() => {});

    expect(asFragment()).toMatchSnapshot();
  });
});
