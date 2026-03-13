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

import { getTestData } from "../../../../modules/test-data/confirmationPopupTestData";
import { getApplicantInfo } from "../../../../modules/test-data/applicantInfoTestData";
import { generateJWTToken } from "../../../../modules/helpers/authentication-helper/authenticationHelper";

import CSOAccount from "../CSOAccount";

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

  test("Initial rendering: with no email should display email input form", async () => {
    const mockApplicantInfo = {
      bceid: "bobross42",
      firstName: "Bob",
      lastName: "Ross",
    };

    const { container } = render(
      <CSOAccount
        confirmationPopup={confirmationPopup}
        applicantInfo={mockApplicantInfo}
        setCsoAccountStatus={setCsoAccountStatus}
      />
    );

    const emailInput = getByTestId(container, "email");
    const emailConfInput = getByTestId(container, "conf-email");

    expect(emailInput).not.toBeNull();
    expect(emailConfInput).not.toBeNull();
  });

  test("Initial rendering: with email should not display email input form", async () => {
    const mockApplicantInfo = {
      bceid: "bobross42",
      firstName: "Bob",
      lastName: "Ross",
      email: "bob.ross@paintit.com",
    };

    const { container } = render(
      <CSOAccount
        confirmationPopup={confirmationPopup}
        applicantInfo={mockApplicantInfo}
        setCsoAccountStatus={setCsoAccountStatus}
      />
    );

    const emailInput = queryByTestId(container, "email");
    const emailConfInput = queryByTestId(container, "conf-email");

    expect(emailInput).toBeNull();
    expect(emailConfInput).toBeNull();
  });

  test("Validation: invalid email should render client error", async () => {
    const mockApplicantInfo = {
      firstName: "Bob",
      middleName: "Painter",
      lastName: "Ross",
      email: "",
    };

    const { container } = render(
      <CSOAccount
        confirmationPopup={confirmationPopup}
        applicantInfo={mockApplicantInfo}
        setCsoAccountStatus={setCsoAccountStatus}
      />
    );

    // click on the acceptTerms checkbox
    fireEvent.click(getByRole(container, "checkbox"));

    await waitFor(() => {});

    const emailInput = getByTestId(container, "email");

    fireEvent.change(emailInput, { target: { value: "not-an-email" } });

    await waitFor(() => {});

    const emailError = getByTestId(container, "email-error");
    expect(emailError.innerHTML).toEqual("Must be a valid email.");
  });

  test("Validation: different email inputs should render client error", async () => {
    const mockApplicantInfo = {
      firstName: "Bob",
      middleName: "Painter",
      lastName: "Ross",
    };

    const { container } = render(
      <CSOAccount
        confirmationPopup={confirmationPopup}
        applicantInfo={mockApplicantInfo}
        setCsoAccountStatus={setCsoAccountStatus}
      />
    );

    const emailInput = getByTestId(container, "email");
    const confEmailInput = getByTestId(container, "conf-email");

    // Assert confirmation email must match
    fireEvent.change(emailInput, { target: { value: "cso@cso.com" } });
    await waitFor(() => {});
    fireEvent.change(confEmailInput, { target: { value: "other@cso.com" } });
    await waitFor(() => {});
    const confEmailError = getByTestId(container, "conf-email-error");
    expect(confEmailError.innerHTML).toEqual(
      "Email and confirmation email must match."
    );
  });

  test("Validation: valid emails should not render any error", async () => {
    const mockApplicantInfo = {
      firstName: "Bob",
      middleName: "Painter",
      lastName: "Ross",
      email: "",
    };

    const { container } = render(
      <CSOAccount
        confirmationPopup={confirmationPopup}
        applicantInfo={mockApplicantInfo}
        setCsoAccountStatus={setCsoAccountStatus}
      />
    );

    const emailInput = getByTestId(container, "email");
    const confEmailInput = getByTestId(container, "conf-email");

    // happy path, both valid emails, both match
    fireEvent.change(emailInput, { target: { value: "cso@cso.com" } });
    await waitFor(() => {});
    fireEvent.change(confEmailInput, { target: { value: "cso@cso.com" } });
    await waitFor(() => {});

    const emailError = getByTestId(container, "email-error");
    expect(emailError.innerHTML).toEqual("");
    const confEmailError = getByTestId(container, "conf-email-error");
    expect(confEmailError.innerHTML).toEqual("");
  });

  test("Success Account Creation: setCsoAccountStatus exists and isNew to true", async () => {
    const mockApplicantInfo = {
      firstName: "Bob",
      middleName: "Painter",
      lastName: "Ross",
      email: "bob.ross@paintit.com",
    };

    sessionStorage.setItem("csoAccountId", null);
    sessionStorage.setItem("paymentProfileId", null);

    mock.onPost(API_REQUEST).reply(201, {
      internalClientNumber: "ABC123",
      clientId: "123",
    });

    const { container } = render(
      <CSOAccount
        confirmationPopup={confirmationPopup}
        applicantInfo={mockApplicantInfo}
        setCsoAccountStatus={setCsoAccountStatus}
      />
    );

    fireEvent.click(getByRole(container, "checkbox"));
    await waitFor(() => {});

    expect(getByText(container, "Create CSO Account").disabled).toBeFalsy();

    fireEvent.click(getByText(container, "Create CSO Account"));
    await waitFor(() => {});

    expect(setCsoAccountStatus).toHaveBeenCalled();
    expect(sessionStorage.getItem("csoAccountId")).toEqual("123");
    expect(sessionStorage.getItem("paymentProfileId")).toEqual("ABC123");
  });

  test("Failed Account Creation: should create toast alert", async () => {
    const mockApplicantInfo = {
      firstName: "Bob",
      lastName: "Ross",
      email: "bob.ross@paintit.com",
    };

    mock.onPost(API_REQUEST).reply(400, { message: "There was a problem." });
    sessionStorage.setItem("errorUrl", "error.com");

    const { container, queryByText } = render(
      <CSOAccount
        confirmationPopup={confirmationPopup}
        applicantInfo={mockApplicantInfo}
        setCsoAccountStatus={setCsoAccountStatus}
      />
    );

    expect(getByText(container, "Create CSO Account").disabled).toBeTruthy();
    fireEvent.click(getByRole(container, "checkbox"));

    expect(getByText(container, "Create CSO Account").disabled).toBeFalsy();

    fireEvent.click(getByText(container, "Create CSO Account"));
    await waitFor(() => {});

    expect(
      queryByText(
        "Something went wrong while trying to create your CSO Account."
      )
    ).toBeInTheDocument();
  });

  test("email fields should appear if email is blank", async () => {
    const mockApplicantInfo = {
      firstName: "Bob",
      middleName: "Painter",
      lastName: "Ross",
      email: "",
    };

    const { container } = render(
      <CSOAccount
        confirmationPopup={confirmationPopup}
        applicantInfo={mockApplicantInfo}
        setCsoAccountStatus={setCsoAccountStatus}
      />
    );

    const emailInput = queryByTestId(container, "email");
    expect(emailInput).not.toBeNull();
  });
});
