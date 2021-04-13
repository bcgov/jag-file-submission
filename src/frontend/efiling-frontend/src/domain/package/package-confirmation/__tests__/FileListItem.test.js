import React from "react";
import { render, waitFor, fireEvent } from "@testing-library/react";
import FileListItem from "../FileListItem";
import { getDocumentsData } from "../../../../modules/test-data/documentTestData";

const service = require("../../../documents/DocumentService");
const error = require("../../../../modules/helpers/errorRedirect");

describe("FileListItem Testsuite", () => {
  const submissionId = "0345a4e0-3095-4ce4-9e47-b7eb9c991448";
  const files = getDocumentsData();
  const toastMessage =
    "Something went wrong while trying to download your file.";

  beforeEach(() => {
    error.errorRedirect = jest.fn();
  });

  test("download document success (on click)", async () => {
    // stub out service to return valid response.
    service.downloadFileByName = jest.fn(() => Promise.resolve());

    const { getByText, queryByText } = render(
      <FileListItem submissionId={submissionId} file={files[0]} />
    );
    await waitFor(() => {});

    const fileLink = getByText(files[0].documentProperties.name);
    fireEvent.click(fileLink);
    await waitFor(() => {});

    expect(service.downloadFileByName).toHaveBeenCalled();
    expect(queryByText(toastMessage)).not.toBeInTheDocument();
  });

  test("download document failure (on click)", async () => {
    // stub out service to return valid response.
    service.downloadFileByName = jest.fn(() => Promise.reject());

    const { getByText, queryByText } = render(
      <FileListItem submissionId={submissionId} file={files[0]} />
    );
    await waitFor(() => {});

    const fileLink = getByText(files[0].documentProperties.name);
    fireEvent.click(fileLink);
    await waitFor(() => {});

    expect(service.downloadFileByName).toHaveBeenCalled();
    expect(queryByText(toastMessage)).toBeInTheDocument();
  });

  test("download document success (on keypress Enter)", async () => {
    // stub out service to return valid response.
    service.downloadFileByName = jest.fn(() => Promise.resolve());

    const { getByText, queryByText } = render(
      <FileListItem submissionId={submissionId} file={files[0]} />
    );
    await waitFor(() => {});

    const fileLink = getByText(files[0].documentProperties.name);
    fireEvent.keyDown(fileLink, {
      key: "Enter",
      keyCode: "13",
    });
    await waitFor(() => {});

    expect(service.downloadFileByName).toHaveBeenCalled();
    expect(queryByText(toastMessage)).not.toBeInTheDocument();
  });

  test("download document success (on keypress Tab)", async () => {
    // stub out service to return valid response.
    service.downloadFileByName = jest.fn(() => Promise.resolve());

    const { getByText, queryByText } = render(
      <FileListItem submissionId={submissionId} file={files[0]} />
    );
    await waitFor(() => {});

    const fileLink = getByText(files[0].documentProperties.name);
    fireEvent.keyDown(fileLink, {
      key: "Enter",
      keyCode: "9",
    });
    await waitFor(() => {});

    expect(service.downloadFileByName).not.toHaveBeenCalled();
    expect(queryByText(toastMessage)).not.toBeInTheDocument();
  });

  test("download document failure (on keypress Enter)", async () => {
    // stub out service to return valid response.
    service.downloadFileByName = jest.fn(() => Promise.reject());

    const { getByText, queryByText } = render(
      <FileListItem submissionId={submissionId} file={files[0]} />
    );
    await waitFor(() => {});

    const fileLink = getByText(files[0].documentProperties.name);
    fireEvent.keyDown(fileLink, {
      key: "Enter",
      keyCode: "13",
    });
    await waitFor(() => {});

    expect(service.downloadFileByName).toHaveBeenCalled();
    expect(queryByText(toastMessage)).toBeInTheDocument();
  });
});
