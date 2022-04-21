/* eslint-disable no-promise-executor-return, import/no-named-as-default, import/no-named-as-default-member */
import React from "react";
import axios from "axios";
import { setImmediate } from "timers";
import MockAdapter from "axios-mock-adapter";
import {
  render,
  fireEvent,
  getByText,
  waitFor,
  getByTestId,
  getAllByRole,
  queryByText,
  getAllByTestId,
} from "@testing-library/react";
import { getCourtData } from "../../../modules/test-data/courtTestData";
import {
  getDocumentsData,
  getJsonDocumentsData,
} from "../../../modules/test-data/documentTestData";
import { generateJWTToken } from "../../../modules/helpers/authentication-helper/authenticationHelper";

import Upload, { uploadDocuments } from "./Upload";

jest.mock("../../../domain/documents/DocumentService");

function flushPromises(ui, container) {
  return new Promise((resolve) =>
    setImmediate(() => {
      render(ui, { container });
      resolve(container);
    })
  );
}

function dispatchEvt(node, type, data) {
  const event = new Event(type, { bubbles: true });
  Object.assign(event, data);
  fireEvent(node, event);
}

function mockData(files) {
  return {
    dataTransfer: {
      files,
      items: files.map((file) => ({
        kind: "file",
        type: file.type,
        getAsFile: () => file,
      })),
      types: ["Files"],
    },
  };
}

describe("Upload Component", () => {
  const submissionId = "abc123";
  const documents = getDocumentsData();
  const court = getCourtData();
  const submissionFeeAmount = 25.5;
  const setShowLoader = jest.fn();
  const setShowUpload = jest.fn();
  const files = getJsonDocumentsData();

  const upload = {
    submissionId,
    courtData: court,
    setShowUpload,
    files,
  };

  const token = generateJWTToken({ preferred_username: "username@bceid" });
  localStorage.setItem("jwt", token);

  const apiRequest = `/submission/${submissionId}/filing-package`;
  let mock;
  beforeEach(() => {
    mock = new MockAdapter(axios);

    window.open = jest.fn();
    global.URL.createObjectURL = jest.fn();
    global.URL.createObjectURL.mockReturnValueOnce("fileurl.com");
    sessionStorage.setItem("errorUrl", "errorexample.com");
  });

  test("Matches the snapshot", async () => {
    const { asFragment } = render(<Upload upload={upload} />);

    await waitFor(() => {});

    expect(asFragment()).toMatchSnapshot();
  });

  test("Radio buttons working as expected", async () => {
    mock.onPost(`/submission/${submissionId}/documents`).reply(200);

    mock.onPost(`/submission/${submissionId}/update-documents`).reply(200);

    const updatedDocuments = [
      ...documents,
      {
        description: "file description ping",
        statutoryFeeAmount: 0,
        isAmendment: true,
        isSupremeCourtScheduling: true,
        mimeType: "application/pdf",
        documentProperties: {
          name: "ping.json",
          type: "file type ping",
        },
      },
      {
        description: "file description ping2",
        statutoryFeeAmount: 0,
        isAmendment: true,
        isSupremeCourtScheduling: true,
        mimeType: "application/pdf",
        documentProperties: {
          name: "ping2.json",
          type: "file type ping2",
        },
      },
    ];

    mock.onGet(`/submission/${submissionId}/filing-package`).reply(200, {
      documents: updatedDocuments,
      court,
      submissionFeeAmount,
    });

    const fileList = [];
    const file1 = new File([JSON.stringify({ ping: true })], "ping.json", {
      type: "application/json",
    });
    const file2 = new File([JSON.stringify({ ping: true })], "ping2.json", {
      type: "application/json",
    });

    fileList.push(file1);
    fileList.push(file2);

    const data = mockData(fileList);
    const ui = <Upload upload={upload} />;
    const { container, asFragment } = render(ui);
    const dropzone = container.querySelector('[data-testid="dropdownzone"]');

    dispatchEvt(dropzone, "drop", data);

    await waitFor(() => {});
    await flushPromises(ui, container);

    // test opening file link in new tab on click
    const fileLink = getByTestId(container, "file-link-ping.json");
    fireEvent.click(fileLink);

    expect(window.open).toHaveBeenCalled();

    const radio = getAllByRole(container, "radio");
    const button = getByText(container, "Continue");
    const dropdown = getAllByTestId(container, "dropdown");

    expect(button).toBeEnabled();

    fireEvent.change(dropdown[0], {
      target: { value: "Case Conference Brief" },
    });

    fireEvent.click(radio[1]);
    fireEvent.click(radio[0]);
    fireEvent.click(radio[3]);
    fireEvent.click(radio[5]);
    fireEvent.click(radio[6]);

    expect(button).not.toBeDisabled();

    fireEvent.click(button);

    await waitFor(() => {});

    expect(asFragment()).toMatchSnapshot();
  });

  test("On cancel upload button click, it redirects to the package confirmation page", async () => {
    mock
      .onGet(apiRequest)
      .reply(200, { documents, court, submissionFeeAmount });

    const { container, asFragment } = render(<Upload upload={upload} />);

    fireEvent.click(getByText(container, "Cancel Upload"));

    await waitFor(() => {});

    expect(asFragment()).toMatchSnapshot();
  });

  test("invoke onDrop when drop event occurs", async () => {
    const file = new File([JSON.stringify({ ping: true })], "ping.json", {
      type: "application/json",
    });
    const data = mockData([file]);

    const ui = <Upload upload={upload} />;
    const { container, asFragment } = render(ui);
    const dropzone = container.querySelector('[data-testid="dropdownzone"]');

    dispatchEvt(dropzone, "drop", data);

    await waitFor(() => {});
    await flushPromises(ui, container);

    // test opening file link in new tab on keydown
    const fileLink = getByTestId(container, "file-link-ping.json");
    fireEvent.keyDown(fileLink);

    expect(window.open).toHaveBeenCalled();
    expect(asFragment()).toMatchSnapshot();
  });

  test("redirects to error page if fail to generate dropdown elements", async () => {
    const court2 = {
      locationDescription: "Court location",
      fileNumber: "Court file number",
      levelDescription: "Level",
      classDescription: "Class",
      level: "A",
      courtClass: "A",
      location: "Kelowna Law Courts",
    };

    const upload2 = {
      submissionId,
      courtData: court2,
    };

    render(<Upload upload={upload2} />);

    await waitFor(() => {});

    expect(window.open).toHaveBeenCalledWith(
      "errorexample.com?status=400&message=There was an error.",
      "_self"
    );
  });

  test("failed uploadDocuments call to /submission/submissionId/documents opens error page", async () => {
    mock
      .onPost(`/submission/${submissionId}/documents`)
      .reply(400, { message: "There was an error." });

    uploadDocuments(submissionId, [], jest.fn(), setShowLoader, jest.fn());

    await waitFor(() => {});

    expect(setShowLoader).toHaveBeenCalledWith(false);
    expect(window.open).toHaveBeenCalledWith(
      "errorexample.com?status=400&message=There was an error.",
      "_self"
    );
  });

  test("failed uploadDocuments call to /submission/submissionId/update-documents opens error page", async () => {
    mock.onPost(`/submission/${submissionId}/documents`).reply(200);

    mock
      .onPost(`/submission/${submissionId}/update-documents`)
      .reply(400, { message: "There was an error." });

    uploadDocuments(submissionId, [], jest.fn(), setShowLoader, jest.fn());

    await waitFor(() => {});

    expect(setShowLoader).toHaveBeenCalledWith(false);
    expect(window.open).toHaveBeenCalledWith(
      "errorexample.com?status=400&message=There was an error.",
      "_self"
    );
  });

  test("successful document upload works as expected", async () => {
    mock.onPost(`/submission/${submissionId}/documents`).reply(200);

    mock.onPost(`/submission/${submissionId}/update-documents`).reply(200);

    const updatedDocuments = [
      ...documents,
      {
        description: "file description ping",
        statutoryFeeAmount: 0,
        isAmendment: true,
        isSupremeCourtScheduling: true,
        mimeType: "application/pdf",
        documentProperties: {
          name: "ping.json",
          type: "file type ping",
        },
      },
      {
        description: "file description ping2",
        statutoryFeeAmount: 0,
        isAmendment: true,
        isSupremeCourtScheduling: true,
        mimeType: "application/pdf",
        documentProperties: {
          name: "ping2.json",
          type: "file type ping2",
        },
      },
    ];

    mock.onGet(`/submission/${submissionId}/filing-package`).reply(200, {
      documents: updatedDocuments,
      court,
      submissionFeeAmount,
    });

    const fileList = [];
    const file1 = new File([JSON.stringify({ ping: true })], "ping.json", {
      type: "application/json",
    });
    const file2 = new File([JSON.stringify({ ping: true })], "ping2.json", {
      type: "application/json",
    });

    fileList.push(file1);
    fileList.push(file2);

    const data = mockData(fileList);
    const ui = <Upload upload={upload} />;
    const { container, asFragment } = render(ui);
    const dropzone = container.querySelector('[data-testid="dropdownzone"]');

    dispatchEvt(dropzone, "drop", data);

    await waitFor(() => {});
    await flushPromises(ui, container);

    // test opening file link in new tab on click
    const fileLink = getByTestId(container, "file-link-ping.json");
    fireEvent.click(fileLink);

    expect(window.open).toHaveBeenCalled();

    const radio = getAllByRole(container, "radio");
    const button = getByText(container, "Continue");
    const dropdown = getAllByTestId(container, "dropdown");

    fireEvent.change(dropdown[0], {
      target: { value: "Case Conference Brief" },
    });
    fireEvent.click(radio[0]);
    fireEvent.click(radio[2]);
    fireEvent.click(radio[5]);
    fireEvent.click(radio[7]);

    expect(button).not.toBeDisabled();

    fireEvent.click(button);

    await waitFor(() => {});

    expect(asFragment()).toMatchSnapshot();
  });

  test("removing uploaded file works as expected", async () => {
    const file = new File([JSON.stringify({ ping: true })], "ping.json", {
      type: "application/json",
    });
    const data = mockData([file]);

    const ui = <Upload upload={upload} />;
    const { container } = render(ui);
    const dropzone = container.querySelector('[data-testid="dropdownzone"]');

    dispatchEvt(dropzone, "drop", data);

    await waitFor(() => {});
    await flushPromises(ui, container);

    expect(getByText(container, "ping.json")).toBeInTheDocument();

    const removeIcon = getByTestId(container, "remove-icon");

    fireEvent.click(removeIcon);

    expect(queryByText(container, "ping.json")).not.toBeInTheDocument();
  });

  test("Continue button is only enabled if there is more than one document", async () => {
    const ui = <Upload upload={upload} />;
    const { container } = render(ui);
    const dropzone = container.querySelector('[data-testid="dropdownzone"]');

    const file = new File([JSON.stringify({ ping: true })], "ping.json", {
      type: "application/json",
    });
    const data = mockData([file]);

    await waitFor(() => {});
    await flushPromises(ui, container);

    const button = getByText(container, "Continue");

    expect(button).toBeDisabled();

    dispatchEvt(dropzone, "drop", data);

    await waitFor(() => {});
    await flushPromises(ui, container);

    expect(button).toBeEnabled();

    const removeIcon = getByTestId(container, "remove-icon");

    fireEvent.click(removeIcon);

    expect(button).toBeDisabled();
  });

  test("files with same name (duplicates) uploaded shows error message", async () => {
    const ui = <Upload upload={upload} />;
    const { container } = render(ui);
    const dropzone = container.querySelector('[data-testid="dropdownzone"]');

    const file = new File([JSON.stringify({ ping: true })], "ping.json", {
      type: "application/json",
    });
    const data = mockData([file]);

    dispatchEvt(dropzone, "drop", data);

    await waitFor(() => {});
    await flushPromises(ui, container);

    expect(
      queryByText(
        container,
        "You cannot upload multiple files with the same name."
      )
    ).not.toBeInTheDocument();

    dispatchEvt(dropzone, "drop", data);

    await waitFor(() => {});
    await flushPromises(ui, container);

    expect(
      getByText(
        container,
        "You cannot upload multiple files with the same name."
      )
    ).toBeInTheDocument();

    const newFile = new File([JSON.stringify({ ping: true })], "ping2.json", {
      type: "application/json",
    });
    const newData = mockData([newFile]);

    dispatchEvt(dropzone, "drop", newData);

    await waitFor(() => {});
    await flushPromises(ui, container);

    expect(
      queryByText(
        container,
        "You cannot upload multiple files with the same name."
      )
    ).not.toBeInTheDocument();
  });

  test("files uploaded with same name as filing package files shows error message", async () => {
    const ui = <Upload upload={upload} />;
    const { container } = render(ui);
    const dropzone = container.querySelector('[data-testid="dropdownzone"]');

    const file = new File([JSON.stringify({ ping: true })], "ping3.json", {
      type: "application/json",
    });
    const data = mockData([file]);

    dispatchEvt(dropzone, "drop", data);

    await waitFor(() => {});
    await flushPromises(ui, container);

    expect(
      getByText(
        container,
        "You cannot upload multiple files with the same name."
      )
    ).toBeInTheDocument();
  });
});
