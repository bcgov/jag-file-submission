/* eslint-disable no-shadow, import/no-named-as-default, import/no-named-as-default-member */
import React from "react";
import axios from "axios";
import FileSaver from "file-saver";
import MockAdapter from "axios-mock-adapter";
import { act } from "react-dom/test-utils";
import { render, waitFor, fireEvent, getByText } from "@testing-library/react";
import { getTestData } from "../../../../modules/test-data/confirmationPopupTestData";
import {
  getDocumentsData,
  getDuplicateDocumentsData,
  getPorDocumentsData,
} from "../../../../modules/test-data/documentTestData";
import { getCourtData } from "../../../../modules/test-data/courtTestData";
import { generateJWTToken } from "../../../../modules/helpers/authentication-helper/authenticationHelper";

import PackageConfirmation from "../PackageConfirmation";

describe("PackageConfirmation Component", () => {
  const confirmationPopup = getTestData();
  const submissionId = "abc123";
  const apiRequest = `/submission/${submissionId}/filing-package`;
  const packageConfirmation = { confirmationPopup, submissionId };
  const csoAccountStatus = { isNew: false };
  const documents = getDocumentsData();
  const duplicateDocuments = getDuplicateDocumentsData();
  const porDocuments = getPorDocumentsData();

  const file = {
    documentProperties: {
      name: "file name 1",
      type: "file type",
    },
    description: "file description 1",
    statutoryFeeAmount: 40,
    mimeType: "application/pdf",
  };
  const court = getCourtData();
  const submissionFeeAmount = 25.5;

  sessionStorage.setItem("listenerExists", true);
  sessionStorage.setItem("csoAccountId", "123");
  window.scrollTo = jest.fn();

  sessionStorage.setItem("validRushExit", "false");

  let mock;
  beforeEach(() => {
    process.env.REACT_APP_RUSH_TAB_FEATURE_FLAG = "true";
    mock = new MockAdapter(axios);
    window.open = jest.fn();
    FileSaver.saveAs = jest.fn();

    const token = generateJWTToken({
      preferred_username: "username@bceid",
      realm_access: {
        roles: ["rush_flag"],
      },
      email: "bobross@paintit.com",
      family_name: "ross",
      given_name: "bob",
    });
    localStorage.setItem("jwt", token);
  });

  test("Matches the existing account snapshot", async () => {
    mock
      .onGet(apiRequest)
      .reply(200, { documents, court, submissionFeeAmount });

    const { asFragment } = render(
      <PackageConfirmation
        packageConfirmation={packageConfirmation}
        csoAccountStatus={csoAccountStatus}
      />
    );

    await waitFor(() => {});

    expect(asFragment()).toMatchSnapshot();
  });

  test("Matches the new account snapshot", async () => {
    mock
      .onGet(apiRequest)
      .reply(200, { documents, court, submissionFeeAmount });

    const { asFragment } = render(
      <PackageConfirmation
        packageConfirmation={packageConfirmation}
        csoAccountStatus={{ ...csoAccountStatus, isNew: true }}
      />
    );

    await waitFor(() => {});

    expect(asFragment()).toMatchSnapshot();
  });

  test("Rush radio buttons update content succesfully", async () => {
    mock
      .onGet(apiRequest)
      .reply(200, { documents, court, submissionFeeAmount });

    const { getByLabelText, getByText, queryByText, getByRole, queryByTestId } =
      render(
        <PackageConfirmation
          packageConfirmation={packageConfirmation}
          csoAccountStatus={csoAccountStatus}
        />
      );

    const rushNo = getByLabelText("No");
    const rushYes = getByLabelText("Yes");
    const continueBtn = getByText("Continue");

    fireEvent.click(rushNo);
    expect(queryByText("About Rush Documents")).not.toBeInTheDocument();

    fireEvent.click(rushYes);
    await waitFor(() =>
      expect(queryByText("About Rush Documents")).toBeInTheDocument()
    );

    fireEvent.click(rushNo);
    fireEvent.click(rushYes);

    fireEvent.click(continueBtn);
    fireEvent.click(getByRole("dialog"));
    await waitFor(() =>
      expect(queryByText("Rush Details")).toBeInTheDocument()
    );

    const radio1 = getByLabelText(
      "The attached application is made under Rule 8-5 (1) SCR."
    );
    const cancel = getByText("Cancel");

    fireEvent.click(radio1);
    await waitFor(() =>
      expect(queryByTestId("dropdownzone")).toBeInTheDocument()
    );

    fireEvent.click(cancel);
    await waitFor(() =>
      expect(getByText("Package Confirmation")).toBeInTheDocument()
    );
  });

  test("When call to retrieve filing package fails, generate toast error", async () => {
    sessionStorage.setItem("errorUrl", "error.com");

    mock.onGet(apiRequest).reply(400, { message: "There was an error." });

    const { queryByText } = render(
      <PackageConfirmation
        packageConfirmation={packageConfirmation}
        csoAccountStatus={csoAccountStatus}
      />
    );

    await waitFor(() => {});

    expect(
      queryByText(
        "Something went wrong while trying to retrieve your filing package."
      )
    ).toBeInTheDocument();
  });

  test("When filing package contains multiple documents with the same name, display a toast error and disable continuing", async () => {
    mock.onGet(apiRequest).reply(200, {
      documents: duplicateDocuments,
      court,
      submissionFeeAmount,
    });

    const { container } = render(
      <PackageConfirmation
        packageConfirmation={packageConfirmation}
        csoAccountStatus={csoAccountStatus}
      />
    );

    await waitFor(() => expect(mock.history.get.length).toBe(1));

    expect(getByText(container, "Continue")).toBeDisabled();
    expect(
      getByText(container, /This package contains duplicate file names/i)
    ).toBeInTheDocument();
  });

  test("On click of Continue button, it redirects to the payment page", async () => {
    mock
      .onGet(apiRequest)
      .reply(200, { documents, court, submissionFeeAmount });

    const { container, asFragment } = render(
      <PackageConfirmation
        packageConfirmation={packageConfirmation}
        csoAccountStatus={csoAccountStatus}
      />
    );

    await waitFor(() => {});

    fireEvent.click(getByText(container, "Continue"));

    expect(asFragment()).toMatchSnapshot();
  });

  test("On click of Upload them now text, it redirects to the upload page", async () => {
    mock
      .onGet(apiRequest)
      .reply(200, { documents, court, submissionFeeAmount });

    const { container, asFragment } = render(
      <PackageConfirmation
        packageConfirmation={packageConfirmation}
        csoAccountStatus={csoAccountStatus}
      />
    );

    await waitFor(() => {});

    fireEvent.click(getByText(container, "Upload them now."));

    expect(asFragment()).toMatchSnapshot();
  });

  test("On keydown of Upload them now text, it redirects to the upload page", async () => {
    mock
      .onGet(apiRequest)
      .reply(200, { documents, court, submissionFeeAmount });

    const { container, asFragment } = render(
      <PackageConfirmation
        packageConfirmation={packageConfirmation}
        csoAccountStatus={csoAccountStatus}
      />
    );

    await waitFor(() => {});

    fireEvent.keyDown(getByText(container, "Upload them now."));

    expect(asFragment()).toMatchSnapshot();
  });

  test("Succeeds to open the file in new window when get document call", async () => {
    global.URL.createObjectURL = jest.fn();
    global.URL.createObjectURL.mockReturnValueOnce("fileurl.com");
    sessionStorage.setItem("errorUrl", "error.com");

    mock
      .onGet(apiRequest)
      .reply(200, { documents, court, submissionFeeAmount });
    mock
      .onGet(
        `/submission/${submissionId}/document/${file.documentProperties.name}`
      )
      .reply(200);

    const { container } = render(
      <PackageConfirmation
        packageConfirmation={packageConfirmation}
        csoAccountStatus={csoAccountStatus}
      />
    );

    await waitFor(() => {});

    const fileLink = getByText(container, file.documentProperties.name);
    fireEvent.click(fileLink);
    await waitFor(() => {});

    expect(FileSaver.saveAs).toHaveBeenCalled();
  });

  test("Fails to open the file in new window when get document call fails", async () => {
    global.URL.createObjectURL = jest.fn();
    global.URL.createObjectURL.mockReturnValueOnce("fileurl.com");
    sessionStorage.setItem("errorUrl", "error.com");

    mock
      .onGet(apiRequest)
      .reply(200, { documents, court, submissionFeeAmount });
    mock
      .onGet(
        `/submission/${submissionId}/document/${file.documentProperties.name}`
      )
      .reply(400, { message: "There was an error." });

    const { container, queryByText } = render(
      <PackageConfirmation
        packageConfirmation={packageConfirmation}
        csoAccountStatus={csoAccountStatus}
      />
    );

    await waitFor(() => {});

    const fileLink = getByText(container, file.documentProperties.name);
    fireEvent.click(fileLink);

    await waitFor(() => {});

    expect(
      queryByText("Something went wrong while trying to download your file.")
    ).toBeInTheDocument();
  });

  test("popstate event should take us back to package confirmation page", async () => {
    sessionStorage.removeItem("listenerExists");

    const event = { stopPropagation: () => {} };
    const mockAddEventListener = jest.fn();
    window.addEventListener = mockAddEventListener;

    mock
      .onGet(apiRequest)
      .reply(200, { documents, court, submissionFeeAmount });

    const { container } = render(
      <PackageConfirmation
        packageConfirmation={packageConfirmation}
        csoAccountStatus={csoAccountStatus}
      />
    );
    await waitFor(() => {});

    fireEvent.click(getByText(container, "Upload them now."));
    await waitFor(() => {});

    // here the [6] of calls denotes the popstate call of the mock
    // the [0] denotes the index of arguments passed to that call (popstate function)
    expect(mockAddEventListener.mock.calls[6][0]).toBe("popstate");

    await waitFor(() => {
      // trigger our callback by calling [1] index
      mockAddEventListener.mock.calls[6][1](event);
    });

    // assert here what was being done in callback
    expect(getByText(container, "Package Confirmation")).toBeInTheDocument();
  });

  test("Rush is set to yes and radio buttons are hidden when a POR document is in the package", async () => {
    const promise = Promise.resolve();
    mock
      .onGet(apiRequest)
      .reply(200, { documents: porDocuments, court, submissionFeeAmount });

    const { getByText, queryByTestId } = render(
      <PackageConfirmation
        packageConfirmation={packageConfirmation}
        csoAccountStatus={csoAccountStatus}
      />
    );

    await act(() => promise);

    const rushRadioButtons = queryByTestId("rushRadioOpts");

    const rushStatus = getByText("Yes");

    expect(rushRadioButtons).not.toBeInTheDocument();
    expect(rushStatus).toBeInTheDocument();
  });

  test("When package has rush, the screen updates accordingly", async () => {
    const rush = {
      rushType: "test",
      supportingDocuments: [
        {
          fileName: "test-support1.pdf",
          identifier: null,
        },
        {
          fileName: "test-support2.pdf",
          identifier: null,
        },
      ],
    };

    mock.onGet(apiRequest).reply(200, {
      documents,
      court,
      submissionFeeAmount,
      rush,
    });

    const { getByText, queryByTestId } = render(
      <PackageConfirmation
        packageConfirmation={packageConfirmation}
        csoAccountStatus={csoAccountStatus}
      />
    );

    await waitFor(() => {});

    expect(getByText("Yes")).toBeInTheDocument();

    expect(queryByTestId("rushRadioOpts")).not.toBeInTheDocument();
  });

  test("Sidecard displays rush info", async () => {
    const rush = {
      rushType: "test",
      supportingDocuments: [
        {
          fileName: "test-support1.pdf",
          identifier: null,
        },
        {
          fileName: "test-support2.pdf",
          identifier: null,
        },
      ],
    };

    mock.onGet(apiRequest).reply(200, {
      documents,
      court,
      submissionFeeAmount,
      rush,
    });

    const { getByText, getByLabelText } = render(
      <PackageConfirmation
        packageConfirmation={packageConfirmation}
        csoAccountStatus={csoAccountStatus}
      />
    );

    const rushYes = getByLabelText("Yes");

    fireEvent.click(rushYes);
    expect(getByText("About Rush Documents")).toBeInTheDocument();

    const rushCardBtn = getByText("Learn more about rush processing.");
    fireEvent.click(rushCardBtn);

    expect(getByText(/An application made under Rule/i)).toBeInTheDocument();
  });

  test("take user directly to payment page when coming from bambora redirect", async () => {
    sessionStorage.setItem("isBamboraRedirect", true);

    mock
      .onGet(apiRequest)
      .reply(200, { documents, court, submissionFeeAmount });

    const { asFragment } = render(
      <PackageConfirmation
        packageConfirmation={packageConfirmation}
        csoAccountStatus={csoAccountStatus}
      />
    );

    await waitFor(() => {});

    expect(asFragment()).toMatchSnapshot();
  });

  test("Rush flag is set to false", async () => {
    mock
      .onGet(apiRequest)
      .reply(200, { documents, court, submissionFeeAmount });
    process.env.REACT_APP_RUSH_TAB_FEATURE_FLAG = "false";

    const { queryByText } = render(
      <PackageConfirmation
        packageConfirmation={packageConfirmation}
        csoAccountStatus={csoAccountStatus}
      />
    );

    await waitFor(() => {});

    expect(queryByText("Rush")).not.toBeInTheDocument();
  });

  test("Sidecard displays BCeID info", async () => {
    const promise = Promise.resolve();
    mock.onGet(apiRequest).reply(200, {
      documents,
      court,
      submissionFeeAmount,
    });

    const { getByText } = render(
      <PackageConfirmation
        packageConfirmation={packageConfirmation}
        csoAccountStatus={csoAccountStatus}
      />
    );

    await act(() => promise);

    expect(getByText(/BCeID/i)).toBeInTheDocument();
  });

  test("Sidecard displays BCSC info", async () => {
    const promise = Promise.resolve();
    mock.onGet(apiRequest).reply(200, {
      documents,
      court,
      submissionFeeAmount,
    });

    const token = generateJWTToken({
      preferred_username: "username@bceid",
      email: "username@example.com",
      identityProviderAlias: "bcsc",
    });
    localStorage.setItem("jwt", token);

    const { getByText } = render(
      <PackageConfirmation
        packageConfirmation={packageConfirmation}
        csoAccountStatus={csoAccountStatus}
      />
    );

    await act(() => promise);

    expect(getByText(/BC Services Card/i)).toBeInTheDocument();
  });
});
