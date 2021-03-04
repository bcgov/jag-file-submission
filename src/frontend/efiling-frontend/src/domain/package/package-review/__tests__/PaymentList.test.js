import React from "react";
import { render, waitFor, fireEvent } from "@testing-library/react";
import MockAdapter from "axios-mock-adapter";
import axios from "axios";
import FileSaver from "file-saver";
import PaymentList from "../PaymentList";
import { generateJWTToken } from "../../../../modules/helpers/authentication-helper/authenticationHelper";

jest.mock("react-router-dom", () => ({
  ...jest.requireActual("react-router-dom"),
  useParams: jest.fn().mockReturnValue({ packageId: 1 }),
}));

describe("PartyList Component", () => {
  const payments = [
    {
      feeExempt: false,
      paymentCategory: 1,
      processedAmount: 7,
      submittedAmount: 7,
      serviceIdentifier: 0,
      transactionDate: null,
    },
    {
      feeExempt: true,
      paymentCategory: 3,
      processedAmount: 7,
      submittedAmount: null,
      serviceIdentifier: 0,
      transactionDate: null,
    },
    {
      feeExempt: false,
      paymentCategory: 3,
      processedAmount: 7,
      submittedAmount: 7,
      serviceIdentifier: 0,
      transactionDate: null,
    },
    {
      feeExempt: false,
      paymentCategory: 3,
      processedAmount: null,
      submittedAmount: 7,
      serviceIdentifier: 0,
      transactionDate: null,
    },
  ];

  const packageId = "1";

  FileSaver.saveAs = jest.fn();

  let mock;
  beforeEach(() => {
    // IDP is set in the session
    const token = generateJWTToken({
      preferred_username: "username@bceid",
      email: "username@example.com",
      identityProviderAlias: "bcsc",
    });
    localStorage.setItem("jwt", token);

    mock = new MockAdapter(axios);
    window.open = jest.fn();
  });

  test("Matches snapshot", () => {
    const { asFragment } = render(
      <PaymentList payments={payments} packageId={packageId} />
    );
    waitFor(() => {});

    expect(asFragment()).toMatchSnapshot();
  });

  test("View Payment Receipt (on click) - successful", async () => {
    const blob = new Blob(["foo", "bar"]);

    global.URL.createObjectURL = jest.fn();
    global.URL.createObjectURL.mockReturnValueOnce("fileurl.com");

    mock
      .onGet(`/filingpackages/${packageId}/paymentReceipt`)
      .reply(200, { blob });

    const { getByText } = render(
      <PaymentList payments={payments} packageId={packageId} />
    );
    await waitFor(() => {});

    fireEvent.click(getByText("View Receipt"));
    await waitFor(() => {});

    expect(FileSaver.saveAs).toHaveBeenCalled();
  });

  test("View Payment Receipt (on keydown) - successful", async () => {
    const blob = new Blob(["foo", "bar"]);

    global.URL.createObjectURL = jest.fn();
    global.URL.createObjectURL.mockReturnValueOnce("fileurl.com");

    mock
      .onGet(`/filingpackages/${packageId}/paymentReceipt`)
      .reply(200, { blob });

    const { getByText } = render(
      <PaymentList payments={payments} packageId={packageId} />
    );
    await waitFor(() => {});

    fireEvent.keyDown(getByText("View Receipt"));
    await waitFor(() => {});

    expect(FileSaver.saveAs).toHaveBeenCalled();
  });

  test("View Payment Receipt (on keydown) - unsuccessful", async () => {
    sessionStorage.setItem("errorUrl", "error.com");

    global.URL.createObjectURL = jest.fn();
    global.URL.createObjectURL.mockReturnValueOnce("fileurl.com");

    mock
      .onGet(`/filingpackages/${packageId}/paymentReceipt`)
      .reply(404, { message: "There was an error." });

    const { getByText } = render(
      <PaymentList payments={payments} packageId={packageId} />
    );
    await waitFor(() => {});

    fireEvent.keyDown(getByText("View Receipt"), {
      key: "Enter",
      keyCode: "13",
    });
    await waitFor(() => {});
  });

  test("View Payment Receipt (on click) - unsuccessful", async () => {
    sessionStorage.setItem("errorUrl", "error.com");

    global.URL.createObjectURL = jest.fn();
    global.URL.createObjectURL.mockReturnValueOnce("fileurl.com");

    mock
      .onGet(`/filingpackages/${packageId}/paymentReceipt`)
      .reply(404, { message: "There was an error." });

    const { getByText } = render(
      <PaymentList payments={payments} packageId={packageId} />
    );
    await waitFor(() => {});

    fireEvent.click(getByText("View Receipt"));
    await waitFor(() => {});
  });
});
