import React from "react";
import {
  render,
  waitFor,
  getByText,
  getByRole,
  fireEvent,
  getByTestId,
  queryByTestId,
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
  sessionStorage.setItem("csoBaseUrl", "https://dev.justice.gov.bc.ca/cso");

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
    // IDP is set to bcsc
    const jwtToken = generateJWTToken({
      preferred_username: "username@bcsc",
      identityProviderAlias: "bcsc",
    });
    localStorage.setItem("jwt", jwtToken);
    const bcscInfo = {
      bceid: "",
      firstName: "Bob",
      lastName: "Ross",
    };
    sessionStorage.setItem("csoAccountId", null);
    sessionStorage.setItem("internalClientNumber", null);

    mock.onPost(API_REQUEST).reply(201, {
      internalClientNumber: "ABC123",
      clientId: "123",
    });

    const { container } = render(
      <CSOAccount
        confirmationPopup={confirmationPopup}
        applicantInfo={bcscInfo}
        setCsoAccountStatus={setCsoAccountStatus}
      />
    );

    expect(getByText(container, "Create CSO Account").disabled).toBeTruthy();
    fireEvent.click(getByRole(container, "checkbox"));

    const emailInput = getByTestId(container, "email");
    const confEmailInput = getByTestId(container, "conf-email");

    // Assert email validation
    fireEvent.change(emailInput, { target: { value: "not-an-email" } });
    await waitFor(() => {});
    fireEvent.change(confEmailInput, { target: { value: "cso@cso.com" } });
    await waitFor(() => {});
    const emailError = getByTestId(container, "email-error");
    expect(emailError.innerHTML).toEqual("Must be a valid email.");

    // Assert confirmation email must match
    fireEvent.change(emailInput, { target: { value: "cso@cso.com" } });
    await waitFor(() => {});
    fireEvent.change(confEmailInput, { target: { value: "other@cso.com" } });
    await waitFor(() => {});
    const confEmailError = getByTestId(container, "conf-email-error");
    expect(confEmailError.innerHTML).toEqual(
      "Email and confirmation email must match."
    );

    // happy path, both valid emails, both match
    fireEvent.change(emailInput, { target: { value: "cso@cso.com" } });
    await waitFor(() => {});
    fireEvent.change(confEmailInput, { target: { value: "cso@cso.com" } });
    await waitFor(() => {});

    expect(getByText(container, "Create CSO Account").disabled).toBeFalsy();

    fireEvent.click(getByText(container, "Create CSO Account"));
    await waitFor(() => {});

    expect(setCsoAccountStatus).toHaveBeenCalled();
    expect(sessionStorage.getItem("csoAccountId")).toEqual("123");
    expect(sessionStorage.getItem("internalClientNumber")).toEqual("ABC123");
  });

  test("On failed account creation, should redirect to parent application", async () => {
    // IDP is set to bcsc
    const jwtToken = generateJWTToken({
      preferred_username: "username@bcsc",
      identityProviderAlias: "bcsc",
    });
    localStorage.setItem("jwt", jwtToken);
    const bcscInfo = {
      bceid: "",
      firstName: "Bob",
      lastName: "Ross",
    };

    mock.onPost(API_REQUEST).reply(400, { message: "There was a problem." });
    sessionStorage.setItem("errorUrl", "error.com");

    const { container } = render(
      <CSOAccount
        confirmationPopup={confirmationPopup}
        applicantInfo={bcscInfo}
        setCsoAccountStatus={setCsoAccountStatus}
      />
    );

    expect(getByText(container, "Create CSO Account").disabled).toBeTruthy();
    fireEvent.click(getByRole(container, "checkbox"));

    const emailInput = getByTestId(container, "email");
    const confEmailInput = getByTestId(container, "conf-email");

    fireEvent.change(emailInput, { target: { value: "cso@cso.com" } });
    await waitFor(() => {});
    fireEvent.change(confEmailInput, { target: { value: "cso@cso.com" } });
    await waitFor(() => {});

    expect(getByText(container, "Create CSO Account").disabled).toBeFalsy();

    fireEvent.click(getByText(container, "Create CSO Account"));
    await waitFor(() => {});

    expect(window.open).toHaveBeenCalledWith(
      "error.com?status=400&message=There was a problem.",
      "_self"
    );
  });

  test("email fields should not appear for BCeID accounts", async () => {
    // IDP is set to bceid
    const jwtToken = generateJWTToken({
      preferred_username: "username@bceid",
      identityProviderAlias: "bceid",
    });
    localStorage.setItem("jwt", jwtToken);
    const bceidInfo = {
      bceid: "bobross42",
      firstName: "Bob",
      lastName: "Ross",
      email: "bob.ross@example.com",
    };

    mock.onPost(API_REQUEST).reply(201, {
      internalClientNumber: "ABC123",
      clientId: "123",
    });

    const { container } = render(
      <CSOAccount
        confirmationPopup={confirmationPopup}
        applicantInfo={bceidInfo}
        setCsoAccountStatus={setCsoAccountStatus}
      />
    );

    const emailInput = queryByTestId(container, "email");
    expect(emailInput).toBeNull();
  });
});
