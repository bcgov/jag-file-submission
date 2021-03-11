import React from "react";
import { render, waitFor, fireEvent } from "@testing-library/react";
import MockAdapter from "axios-mock-adapter";
import axios from "axios";
import FileSaver from "file-saver";
import PaymentList from "../PaymentList";
import { generateJWTToken } from "../../../../modules/helpers/authentication-helper/authenticationHelper";
import { payments } from "../../../../modules/test-data/paymentListData";

jest.mock("react-router-dom", () => ({
  ...jest.requireActual("react-router-dom"),
  useParams: jest.fn().mockReturnValue({ packageId: 1 }),
}));

describe("PaymentList Component", () => {
  const packageId = 1;

  FileSaver.saveAs = jest.fn();

  let mock;
  beforeEach(() => {
    jest.clearAllMocks();
    // IDP is set in the session
    const token = generateJWTToken({
      preferred_username: "user@bceid",
      email: "user@example.com",
      identityProviderAlias: "bceid",
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

    fireEvent.keyDown(getByText("View Receipt"), {
      key: "Enter",
      keyCode: "13",
    });
    await waitFor(() => {});

    expect(FileSaver.saveAs).toHaveBeenCalled();
  });

  test("View Payment Receipt (on keydown) - successful - not Enter", async () => {
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

    fireEvent.keyDown(getByText("View Receipt"), {
      key: "Enter",
      keyCode: "9",
    });
    await waitFor(() => {});

    expect(FileSaver.saveAs).not.toHaveBeenCalled();
  });

  test("View Payment Receipt (on keydown) - unsuccessful", async () => {
    sessionStorage.setItem("errorUrl", "error.com");

    mock
      .onGet(`/filingpackages/${packageId}/paymentReceipt`)
      .reply(400, { message: "There was an error." });

    const { getByText } = render(
      <PaymentList payments={payments} packageId={packageId} />
    );
    await waitFor(() => {});

    fireEvent.keyDown(getByText("View Receipt"), {
      key: "Enter",
      keyCode: "13",
    });
    await waitFor(() => {});

    expect(FileSaver.saveAs).not.toHaveBeenCalled();
  });

  test("View Payment Receipt (on click) - unsuccessful", async () => {
    sessionStorage.setItem("errorUrl", "error.com");

    mock
      .onGet(`/filingpackages/${packageId}/paymentReceipt`)
      .reply(400, { message: "There was an error." });

    const { getByText } = render(
      <PaymentList payments={payments} packageId={packageId} />
    );
    await waitFor(() => {});

    fireEvent.click(getByText("View Receipt"));
    await waitFor(() => {});

    expect(FileSaver.saveAs).not.toHaveBeenCalled();
  });
});
