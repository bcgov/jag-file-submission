import React from "react";
import {
  render,
  waitFor,
  getByText,
  getByRole,
  fireEvent,
} from "@testing-library/react";
import axios from "axios";
import MockAdapter from "axios-mock-adapter";

import { getTestData } from "../../../modules/test-data/confirmationPopupTestData";
import { getApplicantInfo } from "../../../modules/test-data/applicantInfoTestData";
import { generateJWTToken } from "../../../modules/helpers/authentication-helper/authenticationHelper";

import CSOAccount from "./CSOAccount";

describe("CSOAccount Component", () => {
  const confirmationPopup = getTestData();
  const applicantInfo = getApplicantInfo();
  const setCsoAccountStatus = jest.fn();

  window.open = jest.fn();

  const mock = new MockAdapter(axios);
  const API_REQUEST = "/csoAccount";

  const token = generateJWTToken({
    preferred_username: "username@bceid",
  });
  localStorage.setItem("jwt", token);

  test("Matches the snapshot", () => {
    const { asFragment } = render(
      <CSOAccount
        confirmationPopup={confirmationPopup}
        applicantInfo={applicantInfo}
        setCsoAccountStatus={setCsoAccountStatus}
      />
    );

    expect(asFragment()).toMatchSnapshot();
  });

  test("On success, setCsoAccountStatus exists and isNew to true", async () => {
    sessionStorage.setItem("csoAccountId", null);

    mock.onPost(API_REQUEST).reply(201, {
      accounts: [
        { type: "CSO", identifier: "identifier" },
        { type: "notCSO", identifier: "newIdentifier" },
      ],
    });

    const { container } = render(
      <CSOAccount
        confirmationPopup={confirmationPopup}
        applicantInfo={applicantInfo}
        setCsoAccountStatus={setCsoAccountStatus}
      />
    );

    expect(getByText(container, "Create CSO Account").disabled).toBeTruthy();
    fireEvent.click(getByRole(container, "checkbox"));
    expect(getByText(container, "Create CSO Account").disabled).toBeFalsy();

    fireEvent.click(getByText(container, "Create CSO Account"));
    await waitFor(() => {});

    expect(setCsoAccountStatus).toHaveBeenCalled();
    expect(sessionStorage.getItem("csoAccountId")).toEqual("identifier");
  });

  test("On success, if not type of CSO then it does not set the value in session storage", async () => {
    sessionStorage.setItem("csoAccountId", "someId");

    mock.onPost(API_REQUEST).reply(201, {
      accounts: [
        { type: "notCSO", identifier: "identifier" },
        { type: "notCSO", identifier: "newIdentifier" },
      ],
    });

    render(
      <CSOAccount
        confirmationPopup={confirmationPopup}
        applicantInfo={applicantInfo}
        setCsoAccountStatus={setCsoAccountStatus}
      />
    );

    await waitFor(() => {});

    expect(sessionStorage.getItem("csoAccountId")).toEqual("someId");
  });

  test("On failed account creation, should redirect to parent application", async () => {
    mock.onPost(API_REQUEST).reply(400, { message: "There was a problem." });
    sessionStorage.setItem("errorUrl", "error.com");

    const { container } = render(
      <CSOAccount
        confirmationPopup={confirmationPopup}
        applicantInfo={applicantInfo}
        setCsoAccountStatus={setCsoAccountStatus}
      />
    );

    expect(getByText(container, "Create CSO Account").disabled).toBeTruthy();
    fireEvent.click(getByRole(container, "checkbox"));
    expect(getByText(container, "Create CSO Account").disabled).toBeFalsy();

    fireEvent.click(getByText(container, "Create CSO Account"));
    await waitFor(() => {});

    expect(window.open).toHaveBeenCalledWith(
      "error.com?status=400&message=There was a problem.",
      "_self"
    );
  });

  test("Clicking cancel opens confirmation popup and clicking confirm takes user back to client app", async () => {
    const setShow = jest.fn();

    const newConfirmationPopup = {
      ...confirmationPopup,
      mainButton: {
        onClick: setShow,
        label: "Click to open confirmation popup",
        styling: "normal-blue btn",
      },
    };

    const { container } = render(
      <CSOAccount
        confirmationPopup={newConfirmationPopup}
        applicantInfo={applicantInfo}
        setCsoAccountStatus={setCsoAccountStatus}
      />
    );

    await waitFor(() => {});

    fireEvent.click(getByText(container, "Click to open confirmation popup"));

    await waitFor(() => {});

    expect(setShow).toHaveBeenCalled();
  });
});
