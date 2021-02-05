import React from "react";
import { render, waitFor, fireEvent, getByText } from "@testing-library/react";
import DocumentList from "./DocumentList";

const pkgRvwService = require("./PackageReviewService");

describe("DocumentList Component Testsuite", () => {
  const packageId = "1";
  const documents = [
    {
      identifier: "1",
      type: "AFF",
      name: "test-document.pdf",
      status: {
        description: "Submitted",
      },
    },
  ];

  beforeEach(() => {
    window.open = jest.fn();
  });

  test("Matches the snapshot", async () => {
    const { asFragment } = render(
      <DocumentList packageId={packageId} documents={documents} />
    );
    await waitFor(() => {});
    expect(asFragment()).toMatchSnapshot();
  });

  test("Download document (click) success", async () => {
    // stub out service to return valid response.
    pkgRvwService.downloadSubmittedDocument = jest.fn(() => Promise.resolve());

    const { container } = render(
      <DocumentList packageId={packageId} documents={documents} />
    );
    await waitFor(() => {});

    // get the span wrapping the file link, click it.
    fireEvent.click(getByText(container, `${documents[0].name}`));
    await waitFor(() => {});

    expect.assertions(1);
    expect(pkgRvwService.downloadSubmittedDocument()).resolves.not.toThrow();
  });

  test("Download document (click) fail", async () => {
    const errorMessage = "Network Error";
    // stub out service to return failed response.
    pkgRvwService.downloadSubmittedDocument = jest.fn(() =>
      Promise.reject(new Error(errorMessage))
    );

    const { container } = render(
      <DocumentList packageId={packageId} documents={documents} />
    );
    await waitFor(() => {});

    // get the span wrapping the file link, click it.
    fireEvent.click(getByText(container, `${documents[0].name}`));
    await waitFor(() => {});

    expect.assertions(1);
    expect(pkgRvwService.downloadSubmittedDocument()).rejects.toThrow(
      errorMessage
    );
  });

  test("Download document (keyDown) success keyCode=13", async () => {
    // stub out service to return valid response.
    pkgRvwService.downloadSubmittedDocument = jest.fn(() => Promise.resolve());

    const { container } = render(
      <DocumentList packageId={packageId} documents={documents} />
    );
    await waitFor(() => {});

    // get the span wrapping the file link, click it.
    fireEvent.keyDown(getByText(container, `${documents[0].name}`), {
      key: "Enter",
      keyCode: "13",
    });
    await waitFor(() => {});

    expect.assertions(1);
    expect(pkgRvwService.downloadSubmittedDocument()).resolves.not.toThrow();
  });

  test("Download document (keyDown) success keyCode=32", async () => {
    // stub out service to return valid response.
    pkgRvwService.downloadSubmittedDocument = jest.fn(() => Promise.resolve());

    const { container } = render(
      <DocumentList packageId={packageId} documents={documents} />
    );
    await waitFor(() => {});

    // get the span wrapping the file link, click it.
    fireEvent.keyDown(getByText(container, `${documents[0].name}`), {
      key: "Space",
      keyCode: "32",
    });
    await waitFor(() => {});

    expect.assertions(1);
    expect(pkgRvwService.downloadSubmittedDocument).not.toHaveBeenCalled();
  });

  test("Download document (click) fail", async () => {
    const errorMessage = "Network Error";
    // stub out service to return failed response.
    pkgRvwService.downloadSubmittedDocument = jest.fn(() =>
      Promise.reject(new Error(errorMessage))
    );

    const { container } = render(
      <DocumentList packageId={packageId} documents={documents} />
    );
    await waitFor(() => {});

    // get the span wrapping the file link, click it.
    fireEvent.keyDown(getByText(container, `${documents[0].name}`), {
      key: "Enter",
      keyCode: "13",
    });
    await waitFor(() => {});

    expect.assertions(1);
    expect(pkgRvwService.downloadSubmittedDocument()).rejects.toThrow(
      errorMessage
    );
  });
});
