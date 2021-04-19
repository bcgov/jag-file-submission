import React from "react";
import { render, waitFor, fireEvent } from "@testing-library/react";
import DocumentTypeEditor from "domain/documents/DocumentTypeEditor";
import { configurations } from "domain/documents/_tests_/test-data";

const service = require("domain/documents/DocumentService");

describe("DocumentTypeEditor test suite", () => {
  beforeEach(() => {
    console.error = jest.fn();
  });

  test("API returns 200", async () => {
    // stub out service to return valid response.
    service.getDocumentTypeConfigurations = jest.fn(() =>
      Promise.resolve(configurations)
    );
    const { getByText } = render(<DocumentTypeEditor />);
    await waitFor(() => {});

    const sampleData = getByText("Response to Civil Claim");
    expect(sampleData).toBeInTheDocument();
  });

  test("API returns 401", async () => {
    // stub out service to return valid response.
    service.getDocumentTypeConfigurations = jest.fn(() => Promise.reject());
    const { getByRole, getByTestId } = render(<DocumentTypeEditor />);
    await waitFor(() => {});

    const toast = getByRole("alert");
    expect(toast).toBeInTheDocument();
    // close the error Toast message
    fireEvent.click(getByTestId("toast-close"));
  });
});
