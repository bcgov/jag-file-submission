/* eslint-disable no-constructor-return, import/no-named-as-default, import/no-named-as-default-member, prefer-destructuring, no-shadow */
import React from "react";
import axios from "axios";
import MockAdapter from "axios-mock-adapter";
import { render, fireEvent, waitFor } from "@testing-library/react";
import userEvent from "@testing-library/user-event";
import { getDocumentsData } from "../../../modules/test-data/documentTestData";
import { getTestData } from "../../../modules/test-data/confirmationPopupTestData";
import { getCourtData } from "../../../modules/test-data/courtTestData";
import { generateJWTToken } from "../../../modules/helpers/authentication-helper/authenticationHelper";

import Rush from "./Rush";

describe("Rush Component", () => {
  let realDate;
  const confirmationPopup = getTestData();
  const submissionId = "abc123";
  const courtData = getCourtData();
  const files = getDocumentsData();
  const submissionFee = 25.5;
  const countries = [
    { code: "1", description: "Canada" },
    { code: "1", description: "United States" },
    { code: "34", description: "Spain" },
  ];

  let mock;
  beforeEach(() => {
    jest.clearAllMocks();
    mock = new MockAdapter(axios);
    mock.onGet("/countries").reply(200, countries);
  });

  const moreFiles = [
    {
      description: "file description 3",
      documentProperties: {
        name: "file name 3",
        type: "file type",
      },
      name: "file name 3",
      isAmendment: null,
      isSupremeCourtScheduling: null,
      mimeType: "application/pdf",
      statutoryFeeAmount: 40,
    },
    {
      description: "file description 4",
      documentProperties: {
        name: "file name 4",
        type: "file type",
      },
      name: "file name 4",
      isAmendment: null,
      isSupremeCourtScheduling: null,
      mimeType: "application/pdf",
      statutoryFeeAmount: 40,
    },
    {
      description: "file description 5",
      documentProperties: {
        name: "file name 5",
        type: "file type",
      },
      name: "file name 5",
      isAmendment: null,
      isSupremeCourtScheduling: null,
      mimeType: "application/pdf",
      statutoryFeeAmount: 40,
    },
    {
      description: "file description 6",
      documentProperties: {
        name: "file name 6",
        type: "file type",
      },
      name: "file name 6",
      isAmendment: null,
      isSupremeCourtScheduling: null,
      mimeType: "application/pdf",
      statutoryFeeAmount: 40,
    },
  ];

  const singleFile = [
    {
      description: "file description 7",
      documentProperties: {
        name: "file name 7",
        type: "file type",
      },
      name: "file name 7",
      isAmendment: null,
      isSupremeCourtScheduling: null,
      mimeType: "application/pdf",
      statutoryFeeAmount: 40,
    },
  ];

  function mockData(files) {
    return {
      dataTransfer: {
        files,
        items: files.map((file) => ({
          kind: "file",
          type: file.type,
          data: file.data,
          getAsFile: () => file,
        })),
        types: ["Files"],
      },
    };
  }
  const nonDuplicateData = mockData(singleFile);
  const tooManyData = mockData([...files, ...moreFiles]);

  function dispatchEvt(node, type, data) {
    const event = new Event(type, { bubbles: true });
    Object.assign(event, data);
    fireEvent(node, event);
  }

  const payment = {
    confirmationPopup,
    submissionId,
    courtData,
    files,
    submissionFee,
  };

  const radio1Label =
    "The attached application is made under Rule 8-5 (1) SCR.";
  const radio2Label =
    "The court directed that the order be processed on an urgent basis.";
  const radio3Label = "Other (please explain).";

  const duplicateError =
    "You cannot upload two supporting documents with the same filename.";
  const numDocumentsError =
    "You cannot upload more than five supporting documents.";

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
  window.scrollTo = jest.fn();

  test("Matches the snapshot", () => {
    mock.onGet("/countries").reply(200, countries);
    const currentDate = new Date("2019-05-14T11:01:58.135Z");
    realDate = Date;
    global.Date = class extends Date {
      constructor() {
        return currentDate;
      }
    };

    const { asFragment } = render(
      <Rush payment={payment} setIsRush={() => {}} setShowRush={() => {}} />
    );

    expect(asFragment()).toMatchSnapshot();

    global.Date = realDate;
  });

  test("Radio Buttons render correct contact", async () => {
    mock.onGet("/countries").reply(200, countries);
    const { getByLabelText, queryByTestId } = render(
      <Rush payment={payment} />
    );
    const radio1 = getByLabelText(radio1Label);
    const radio2 = getByLabelText(radio2Label);
    const radio3 = getByLabelText(radio3Label);

    fireEvent.click(radio1);
    await waitFor(() =>
      expect(queryByTestId("dropdownzone")).toBeInTheDocument()
    );
    fireEvent.click(radio2);
    await waitFor(() =>
      expect(getByLabelText("Clear and detailed reason(s)")).toBeInTheDocument()
    );
    fireEvent.click(radio3);
    await waitFor(() =>
      expect(getByLabelText("Clear and detailed reason(s)")).toBeInTheDocument()
    );
  });

  test("Correctly display duplicate error", async () => {
    mock.onGet("/countries").reply(200, countries);
    const { getByLabelText, queryByTestId, getByText } = render(
      <Rush payment={payment} setIsRush={() => {}} setShowRush={() => {}} />
    );
    const radio1 = getByLabelText(radio1Label);

    fireEvent.click(radio1);
    const dropzone = queryByTestId("dropdownzone");
    await waitFor(() => expect(dropzone).toBeInTheDocument());

    dispatchEvt(dropzone, "drop", nonDuplicateData);
    await waitFor(() => expect(getByText("file name 7")).toBeInTheDocument());
    const firstFile = getByText("file name 7");
    await waitFor(() => expect(firstFile).toBeInTheDocument());

    dispatchEvt(dropzone, "drop", nonDuplicateData);
    await waitFor(() => expect(getByText(duplicateError)).toBeInTheDocument());

    fireEvent.click(firstFile);
    fireEvent.keyDown(firstFile);
  });

  test("Correctly display document number error", async () => {
    mock.onGet("/countries").reply(200, countries);
    const { getByLabelText, queryByTestId, getByText } = render(
      <Rush payment={payment} setIsRush={() => {}} setShowRush={() => {}} />
    );
    const radio1 = getByLabelText(radio1Label);

    fireEvent.click(radio1);
    const dropzone = queryByTestId("dropdownzone");
    await waitFor(() => expect(dropzone).toBeInTheDocument());

    dispatchEvt(dropzone, "drop", tooManyData);

    await waitFor(() =>
      expect(getByText(numDocumentsError)).toBeInTheDocument()
    );
  });

  test("Text fields change based on dropdown", async () => {
    mock.onGet("/countries").reply(200, countries);
    const { getByLabelText, getAllByTestId, getAllByText, getByText } = render(
      <Rush payment={payment} setIsRush={() => {}} setShowRush={() => {}} />
    );
    const radio3 = getByLabelText(radio3Label);

    fireEvent.click(radio3);
    const dropdown = getAllByTestId("dropdown");

    expect(getAllByText("Phone Number").length).toBe(1);
    fireEvent.change(dropdown[1], { target: { value: "Phone Number" } });
    await waitFor(() => expect(getAllByText("Phone Number").length).toBe(2));

    let input = getAllByTestId("input-test")[3];

    fireEvent.change(dropdown[0], { target: { value: "Spain" } });
    await waitFor(() => expect(getByText("Spain")));

    userEvent.type(input, "555-555-5555");
    await waitFor(() => expect(input.value).toBe("555-555-5555"));
    userEvent.type(input, "5555");

    fireEvent.change(input, { target: { value: "" } });
    await waitFor(() => expect(input.value).toBe(""));

    expect(getAllByText("Email").length).toBe(1);
    fireEvent.change(dropdown[1], { target: { value: "Email" } });
    await waitFor(() => expect(getAllByText("Email").length).toBe(2));

    input = getAllByTestId("input-test")[3];
    expect(input.value).toBe("");

    userEvent.type(input, "not-an-email");
    expect(input.value).toBe("not-an-email");

    userEvent.type(input, "@email.com");
    expect(input.value).toBe("not-an-email@email.com");
  });

  test("Input validation works as expected", async () => {
    mock.onGet("/countries").reply(200, countries);
    const { getByLabelText, getAllByTestId, getByText } = render(
      <Rush payment={payment} setIsRush={() => {}} setShowRush={() => {}} />
    );
    const radio2 = getByLabelText(radio2Label);

    fireEvent.click(radio2);

    const input = getAllByTestId("input-test")[0];

    userEvent.clear(input);
    await waitFor(() => expect(getByText("Invalid name")).toBeInTheDocument());
  });

  test("Document can be successfully deleted", async () => {
    mock.onGet("/countries").reply(200, countries);
    const { getByLabelText, queryByTestId, getAllByText } = render(
      <Rush payment={payment} setIsRush={() => {}} setShowRush={() => {}} />
    );
    const radio3 = getByLabelText(radio3Label);

    fireEvent.click(radio3);

    const dropzone = queryByTestId("dropdownzone");
    await waitFor(() => expect(dropzone).toBeInTheDocument());

    dispatchEvt(dropzone, "drop", nonDuplicateData);
    await waitFor(() => expect(getAllByText("Remove")[0]).toBeInTheDocument());
    expect(files.length).toBe(2);

    // fireEvent.click(getAllByText("Remove")[0])
  });

  test("Contact Country dropdown works", async () => {
    mock.onGet("/countries").reply(200, countries);
    const { getByLabelText, getAllByTestId, getByText } = render(
      <Rush payment={payment} setIsRush={() => {}} setShowRush={() => {}} />
    );
    const radio3 = getByLabelText(radio3Label);

    fireEvent.click(radio3);
    const dropdown = getAllByTestId("dropdown");

    fireEvent.change(dropdown[0], { target: { value: "Spain" } });
    await waitFor(() => expect(getByText("Spain")).toBeInTheDocument());
  });

  test("Field inputs work correctly", async () => {
    mock.onGet("/countries").reply(200, countries);
    mock.onPost(`submission/${submissionId}/rushProcessing`).reply(201);
    mock
      .onPost(`submission/${submissionId}/rushDocuments`)
      .reply(201, { submissionId, received: 1 });

    const { getByLabelText, getAllByTestId, getByText, queryByTestId } = render(
      <Rush payment={payment} setIsRush={() => {}} setShowRush={() => {}} />
    );
    const radio2 = getByLabelText(radio2Label);

    fireEvent.click(radio2);

    const dropzone = queryByTestId("dropdownzone");
    await waitFor(() => expect(dropzone).toBeInTheDocument());
    dispatchEvt(dropzone, "drop", nonDuplicateData);

    const surnameInput = getAllByTestId("input-test")[0];
    const firstNameInput = getAllByTestId("input-test")[1];
    const orgInput = getAllByTestId("input-test")[2];

    const reasonsInput = getByLabelText("Clear and detailed reason(s)");
    userEvent.type(reasonsInput, "I'm in a rush");
    await waitFor(() => expect(reasonsInput.value).toBe("I'm in a rush"));

    const dropdown = getAllByTestId("dropdown");

    fireEvent.change(dropdown[0], { target: { value: "Spain" } });
    await waitFor(() => expect(getByText("Spain")).toBeInTheDocument());

    const defaultSurnameInput = surnameInput.value;
    const defaultFirstNameInput = firstNameInput.value;
    const defaultOrgInput = orgInput.value;

    userEvent.type(surnameInput, "test");
    await waitFor(() =>
      expect(surnameInput.value).toBe(`${defaultSurnameInput}test`)
    );

    userEvent.type(firstNameInput, "test");
    await waitFor(() =>
      expect(firstNameInput.value).toBe(`${defaultFirstNameInput}test`)
    );

    userEvent.type(orgInput, "test");
    await waitFor(() => expect(orgInput.value).toBe(`${defaultOrgInput}test`));

    fireEvent.click(getByText("Continue"));
  });

  test("Surname field is limited to 30 characters", () => {
    // 40 characters long
    const shortString = "asdfbasdfbasdfbasdfbasdfbasdfbasdfbasdfb";

    // 30 characters long
    const shortStringTruncated = "asdfbasdfbasdfbasdfbasdfbasdfb";

    const { getByLabelText, getByDisplayValue } = render(
      <Rush payment={payment} setIsRush={() => {}} setShowRush={() => {}} />
    );

    const radioButton1 = getByLabelText(
      "The attached application is made under Rule 8-5 (1) SCR."
    );
    expect(radioButton1).toBeInTheDocument();

    fireEvent.click(radioButton1);

    const surnameInput = getByDisplayValue("ross");

    expect(surnameInput).toBeInTheDocument();

    fireEvent.change(surnameInput, { target: { value: shortString } });
    expect(surnameInput.value).toBe(shortStringTruncated);
  });

  test("First name field is limited to 30 characters", () => {
    // 40 characters long
    const shortString = "asdfbasdfbasdfbasdfbasdfbasdfbasdfbasdfb";

    // 30 characters long
    const shortStringTruncated = "asdfbasdfbasdfbasdfbasdfbasdfb";

    const { getByLabelText, getByDisplayValue } = render(
      <Rush payment={payment} setIsRush={() => {}} setShowRush={() => {}} />
    );

    const radioButton1 = getByLabelText(
      "The attached application is made under Rule 8-5 (1) SCR."
    );
    expect(radioButton1).toBeInTheDocument();

    fireEvent.click(radioButton1);

    const firstNameInput = getByDisplayValue("bob");

    expect(firstNameInput).toBeInTheDocument();

    fireEvent.change(firstNameInput, { target: { value: shortString } });
    expect(firstNameInput.value).toBe(shortStringTruncated);
  });

  test("Organization field is limited to 150 characters", () => {
    // 165 characters long
    const longString =
      "asdfbasdfbasdfbasdfbasdfbasdfbasdfbasdfbasdfbasdfbasdfbasdfbasdfbasdfbasdfbasdfbasdfbasdfbasdfbasdfbasdfbasdfbasdfbasdfbasdfbasdfbasdfbasdfbasdfbasdfbasdfbasdfbasdfb";

    // 150 characters long
    const longStringTruncated =
      "asdfbasdfbasdfbasdfbasdfbasdfbasdfbasdfbasdfbasdfbasdfbasdfbasdfbasdfbasdfbasdfbasdfbasdfbasdfbasdfbasdfbasdfbasdfbasdfbasdfbasdfbasdfbasdfbasdfbasdfb";

    const { getByLabelText } = render(
      <Rush payment={payment} setIsRush={() => {}} setShowRush={() => {}} />
    );

    const radioButton1 = getByLabelText(
      "The attached application is made under Rule 8-5 (1) SCR."
    );
    expect(radioButton1).toBeInTheDocument();

    fireEvent.click(radioButton1);

    const orgInput = getByLabelText("Organization");

    expect(orgInput).toBeInTheDocument();

    fireEvent.change(orgInput, { target: { value: longString } });
    expect(orgInput.value).toBe(longStringTruncated);
  });

  test("Fields are prepopulated from JWT", () => {
    const { getByLabelText, getByDisplayValue } = render(
      <Rush payment={payment} setIsRush={() => {}} setShowRush={() => {}} />
    );

    const radioButton1 = getByLabelText(
      "The attached application is made under Rule 8-5 (1) SCR."
    );
    expect(radioButton1).toBeInTheDocument();

    fireEvent.click(radioButton1);

    expect(getByDisplayValue("bob")).toBeInTheDocument();
    expect(getByDisplayValue("ross")).toBeInTheDocument();
    expect(getByDisplayValue("bobross@paintit.com")).toBeInTheDocument();
  });

  test("rushDocument endpoint fail", async () => {
    mock.onGet("/countries").reply(200, countries);
    mock.onPost(`submission/${submissionId}/rushDocuments`).reply(400);
    mock.onPost(`submission/${submissionId}/rushProcessing`).reply(200);

    const { getByLabelText, getAllByTestId, getByText, queryByTestId } = render(
      <Rush payment={payment} setIsRush={() => {}} setShowRush={() => {}} />
    );
    const radio1 = getByLabelText(radio1Label);

    fireEvent.click(radio1);

    const dropzone = queryByTestId("dropdownzone");
    await waitFor(() => expect(dropzone).toBeInTheDocument());
    dispatchEvt(dropzone, "drop", nonDuplicateData);

    const dropdown = getAllByTestId("dropdown");

    fireEvent.change(dropdown[0], { target: { value: "Spain" } });
    await waitFor(() => expect(getByText("Spain")).toBeInTheDocument());

    const surnameInput = getAllByTestId("input-test")[0];
    const firstNameInput = getAllByTestId("input-test")[1];
    const orgInput = getAllByTestId("input-test")[2];

    const defaultSurnameInput = surnameInput.value;
    const defaultFirstNameInput = firstNameInput.value;
    const defaultOrgInput = orgInput.value;

    userEvent.type(surnameInput, "test");
    await waitFor(() =>
      expect(surnameInput.value).toBe(`${defaultSurnameInput}test`)
    );

    userEvent.type(firstNameInput, "test");
    await waitFor(() =>
      expect(firstNameInput.value).toBe(`${defaultFirstNameInput}test`)
    );

    userEvent.type(orgInput, "test");
    await waitFor(() => expect(orgInput.value).toBe(`${defaultOrgInput}test`));

    fireEvent.click(getByText("Continue"));
    await waitFor(() =>
      expect(
        getByText(
          "Something went wrong while trying to submit your document(s)"
        )
      ).toBeInTheDocument()
    );
  });

  test("rushProcessing endpoint fail", async () => {
    mock.onGet("/countries").reply(200, countries);
    mock.onPost(`submission/${submissionId}/rushDocuments`).reply(200);
    mock.onPost(`submission/${submissionId}/rushProcessing`).reply(400);

    const { getByLabelText, getAllByTestId, getByText, queryByTestId } = render(
      <Rush payment={payment} setIsRush={() => {}} setShowRush={() => {}} />
    );
    const radio1 = getByLabelText(radio1Label);

    fireEvent.click(radio1);

    const dropzone = queryByTestId("dropdownzone");
    await waitFor(() => expect(dropzone).toBeInTheDocument());
    dispatchEvt(dropzone, "drop", nonDuplicateData);

    const dropdown = getAllByTestId("dropdown");

    fireEvent.change(dropdown[0], { target: { value: "Spain" } });
    await waitFor(() => expect(getByText("Spain")).toBeInTheDocument());

    const surnameInput = getAllByTestId("input-test")[0];
    const firstNameInput = getAllByTestId("input-test")[1];
    const orgInput = getAllByTestId("input-test")[2];

    const defaultSurnameInput = surnameInput.value;
    const defaultFirstNameInput = firstNameInput.value;
    const defaultOrgInput = orgInput.value;

    userEvent.type(surnameInput, "test");
    await waitFor(() =>
      expect(surnameInput.value).toBe(`${defaultSurnameInput}test`)
    );

    userEvent.type(firstNameInput, "test");
    await waitFor(() =>
      expect(firstNameInput.value).toBe(`${defaultFirstNameInput}test`)
    );

    userEvent.type(orgInput, "test");
    await waitFor(() => expect(orgInput.value).toBe(`${defaultOrgInput}test`));

    fireEvent.click(getByText("Continue"));
    await waitFor(() =>
      expect(
        getByText(
          "Something went wrong while trying to process your submission"
        )
      ).toBeInTheDocument()
    );
  });

  test("Sidecard displays rush info", async () => {
    const { getByText } = render(
      <Rush payment={payment} setIsRush={() => {}} setShowRush={() => {}} />
    );

    const rushCardBtn = getByText("Learn more about rush processing.");
    fireEvent.click(rushCardBtn);

    expect(getByText(/An application made under Rule/i)).toBeInTheDocument();
  });
});
