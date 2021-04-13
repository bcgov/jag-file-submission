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

import Payment from "../Payment";

describe("Payment Component", () => {
  let realDate;
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

  const noFeePayment = {
    ...payment,
    submissionFee: 0,
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

  let mock;
  beforeEach(() => {
    mock = new MockAdapter(axios);
    window.open = jest.fn();
    sessionStorage.setItem("internalClientNumber", "ABC123");
    sessionStorage.setItem("bamboraRedirectUrl", "efilinghub.com");

    mock
      .onPost("/payment/generate-update-card")
      .reply(200, { bamboraUrl: "bambora.com" });
  });

  test("Matches the snapshot with existing credit card", () => {
    const { asFragment } = render(<Payment payment={payment} />);

    expect(asFragment()).toMatchSnapshot();
  });

  test("Matches the snapshot with no credit card", () => {
    sessionStorage.setItem("internalClientNumber", null);

    const { asFragment } = render(<Payment payment={payment} />);

    expect(asFragment()).toMatchSnapshot();
  });

  test("Allows submission with no submission fee", () => {
    sessionStorage.setItem("internalClientNumber", null);
    const { container } = render(<Payment payment={noFeePayment} />);

    expect(getByText(container, "Submit").disabled).toBeTruthy();

    fireEvent.click(getByRole(container, "checkbox"));

    expect(getByText(container, "Submit").disabled).toBeFalsy();
  });

  test("Click on register a credit card when no card exists takes user to bambora", async () => {
    sessionStorage.setItem("internalClientNumber", null);

    const { container } = render(<Payment payment={payment} />);

    fireEvent.click(getByText(container, "Register a Credit Card now"));

    await waitFor(() => {});

    expect(window.open).toHaveBeenCalledWith("bambora.com", "_self");
  });

  test("Keydown on register a credit card when no card exists and bambora error exists takes user to bambora, enter", async () => {
    sessionStorage.setItem("internalClientNumber", null);

    const { container } = render(<Payment payment={payment} />);

    fireEvent.keyDown(getByText(container, "Register a Credit Card now"), {
      key: "Enter",
      keyCode: "13",
    });

    await waitFor(() => {});

    expect(window.open).toHaveBeenCalledWith("bambora.com", "_self");
  });

  test("Keydown on register a credit card when no card exists and bambora error exists takes user to bambora, tab", async () => {
    sessionStorage.setItem("internalClientNumber", null);

    const { container } = render(<Payment payment={payment} />);

    fireEvent.keyDown(getByText(container, "Register a Credit Card now"), {
      key: "Tab",
      keyCode: "9",
    });

    await waitFor(() => {});

    expect(window.open).not.toHaveBeenCalledWith("bambora.com", "_self");
  });

  test("Click on register a new credit card when card exists takes user to bambora", async () => {
    sessionStorage.setItem("internalClientNumber", "ABC123");
    sessionStorage.setItem("bamboraErrorExists", false);

    const { container } = render(<Payment payment={payment} />);

    fireEvent.click(getByText(container, "Register a new Credit Card."));

    await waitFor(() => {});

    expect(window.open).toHaveBeenCalledWith("bambora.com", "_self");
  });

  test("Keydown on register a new credit card when card exists takes user to bambora, enter", async () => {
    sessionStorage.setItem("internalClientNumber", "ABC123");
    sessionStorage.setItem("bamboraErrorExists", false);

    const { container } = render(<Payment payment={payment} />);

    fireEvent.keyDown(getByText(container, "Register a new Credit Card."), {
      key: "Enter",
      keyCode: "13",
    });

    await waitFor(() => {});

    expect(window.open).toHaveBeenCalledWith("bambora.com", "_self");
  });

  test("Keydown on register a new credit card when card exists takes user to bambora, tab", async () => {
    sessionStorage.setItem("internalClientNumber", "ABC123");
    sessionStorage.setItem("bamboraErrorExists", false);

    const { container } = render(<Payment payment={payment} />);

    fireEvent.keyDown(getByText(container, "Register a new Credit Card."), {
      key: "Tab",
      keyCode: "9",
    });

    await waitFor(() => {});

    expect(window.open).not.toHaveBeenCalledWith("bambora.com", "_self");
  });

  test("Click on register a new credit card on failed update takes user to bambora", async () => {
    sessionStorage.setItem("internalClientNumber", "ABC123");
    sessionStorage.setItem("bamboraErrorExists", true);

    const { container } = render(<Payment payment={payment} />);

    fireEvent.click(getByText(container, "Register a new Credit Card."));

    await waitFor(() => {});

    expect(window.open).toHaveBeenCalledWith("bambora.com", "_self");
  });

  test("Keydown on register a new credit card on failed update takes user to bambora, enter", async () => {
    sessionStorage.setItem("internalClientNumber", "ABC123");
    sessionStorage.setItem("bamboraErrorExists", true);

    const { container } = render(<Payment payment={payment} />);

    fireEvent.keyDown(getByText(container, "Register a new Credit Card."), {
      key: "Enter",
      keyCode: "13",
    });

    await waitFor(() => {});

    expect(window.open).toHaveBeenCalledWith("bambora.com", "_self");
  });

  test("Keydown on register a new credit card on failed update takes user to bambora, tab", async () => {
    sessionStorage.setItem("internalClientNumber", "ABC123");
    sessionStorage.setItem("bamboraErrorExists", true);

    const { container } = render(<Payment payment={payment} />);

    fireEvent.keyDown(getByText(container, "Register a new Credit Card."), {
      key: "Tab",
      keyCode: "9",
    });

    await waitFor(() => {});

    expect(window.open).not.toHaveBeenCalledWith("bambora.com", "_self");
  });

  test("Click on register a credit card takes user to error page when call fails", async () => {
    sessionStorage.setItem("errorUrl", "error.com");

    mock
      .onPost("/payment/generate-update-card")
      .reply(400, { message: "There was an error." });

    const { container } = render(<Payment payment={payment} />);

    fireEvent.keyDown(getByText(container, "Register a new Credit Card."), {
      key: "Enter",
      keyCode: "13",
    });

    await waitFor(() => {});

    expect(window.open).toHaveBeenCalledWith(
      "error.com?status=400&message=There was an error.",
      "_self"
    );
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
      .reply(200, { packageRef: "packageRef" });

    const { container } = render(<Payment payment={payment} />);

    fireEvent.click(getByRole(container, "checkbox"));
    fireEvent.click(getByText(container, "Submit"));

    await waitFor(() => {});

    expect(window.open).toHaveBeenCalledWith(
      "success.com?packageRef=packageRef",
      "_self"
    );
  });

  test("Submit on error generates toast message", async () => {
    sessionStorage.setItem("errorUrl", "error.com");

    mock
      .onPost(`/submission/${submissionId}/submit`)
      .reply(400, { message: "There was an error." });

    const { container, queryByText } = render(<Payment payment={payment} />);

    fireEvent.click(getByRole(container, "checkbox"));
    fireEvent.click(getByText(container, "Submit"));

    await waitFor(() => {});

    expect(
      queryByText("Something went wrong while trying to submit your package.")
    ).toBeInTheDocument();
  });

  test("Click on request rush submission opens rush submission page", async () => {
    const currentDate = new Date("2019-05-14T11:01:58.135Z");
    realDate = Date;
    global.Date = class extends Date {
      constructor() {
        return currentDate;
      }
    };

    mock.onGet(apiRequest).reply(200, {
      documents: files,
      court: courtData,
      submissionFeeAmount: submissionFee,
    });

    const { container, asFragment } = render(<Payment payment={payment} />);

    fireEvent.click(getByText(container, "Request rush submission"));

    await waitFor(() => {});

    expect(asFragment()).toMatchSnapshot();

    global.Date = realDate;
  });

  test("when coming from a successful bambora card registration, and a successful call to set-bambora-cso, set the internal client number", async () => {
    sessionStorage.setItem("internalClientNumber", null);
    sessionStorage.setItem("bamboraSuccess", "1234");

    mock.onPut("csoAccount").reply(200, { internalClientNumber: "1234" });

    render(<Payment payment={payment} />);

    await waitFor(() => {});

    expect(sessionStorage.getItem("internalClientNumber")).toEqual("1234");
  });

  test("when coming from a successful bambora card registration, and a failed call to set-bambora-cso, redirects to error page", async () => {
    sessionStorage.setItem("internalClientNumber", null);
    sessionStorage.setItem("bamboraSuccess", "1234");

    mock.onPut("csoAccount").reply(400, {
      message: "There was an error.",
    });

    render(<Payment payment={payment} />);

    await waitFor(() => {});

    expect(window.open).toHaveBeenCalledWith(
      "error.com?status=400&message=There was an error.",
      "_self"
    );
  });
});
