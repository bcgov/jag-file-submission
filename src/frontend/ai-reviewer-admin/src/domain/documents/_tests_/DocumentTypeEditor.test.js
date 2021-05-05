import React from "react";
import api from "AxiosConfig";
import MockAdapter from "axios-mock-adapter";
import {
  render,
  waitFor,
  fireEvent
} from "@testing-library/react";
import DocumentTypeEditor from "domain/documents/DocumentTypeEditor";
import { configurations } from "domain/documents/_tests_/test-data";
import userEvent from "@testing-library/user-event";

const service = require("domain/documents/DocumentService");

describe("DocumentTypeEditor test suite", () => {
  let mockApi;
  beforeEach(() => {
    console.error = jest.fn();
    mockApi = new MockAdapter(api);
  });

  test("API GET returns 200", async () => {
    // stub out service to return valid response.
    service.getDocumentTypeConfigurations = jest.fn(() =>
      Promise.resolve(configurations)
    );
    const { getAllByText } = render(<DocumentTypeEditor />);
    await waitFor(() => {});

    const sampleData = getAllByText("Response to Civil Claim");
    expect(sampleData.length === 2).toBeTruthy();
  });

  test("API GET returns 401", async () => {
    // stub out service to return valid response.
    service.getDocumentTypeConfigurations = jest.fn(() => Promise.reject());
    const { getByRole, getByTestId } = render(<DocumentTypeEditor />);
    await waitFor(() => {});

    const toast = getByRole("alert");
    expect(toast).toBeInTheDocument();
    // close the error Toast message
    fireEvent.click(getByTestId("toast-close"));
  });

  test("API DELETE returns 200", async () => {
    // stub out service to return valid response.
    service.getDocumentTypeConfigurations = jest.fn(() => Promise.resolve(configurations));
    service.deleteDocumentTypeConfiguration = jest.fn((documentTypeId) => Promise.resolve());
    const { getByText, getByTestId } = render(<DocumentTypeEditor />);
    await waitFor(() => {});

    const deleteIcon = getByTestId("delete-6550866d-754c-9d41-52a5-c229bc849ee3");
    expect(deleteIcon).toBeInTheDocument();

    fireEvent.click(deleteIcon);
    await waitFor(() => {});

    expect(service.deleteDocumentTypeConfiguration).toBeCalledWith("6550866d-754c-9d41-52a5-c229bc849ee3");
  });

  test("API DELETE returns 404", async () => {
    // stub out service to return valid response.
    service.getDocumentTypeConfigurations = jest.fn(() => Promise.resolve(configurations));
    service.deleteDocumentTypeConfiguration = jest.fn((documentTypeId) => Promise.reject());
    const { getByText, getByTestId } = render(<DocumentTypeEditor />);
    await waitFor(() => {});

    const deleteIcon = getByTestId("delete-6550866d-754c-9d41-52a5-c229bc849ee3");
    expect(deleteIcon).toBeInTheDocument();

    fireEvent.click(deleteIcon);
    await waitFor(() => {});

    expect(service.deleteDocumentTypeConfiguration).toBeCalledWith("6550866d-754c-9d41-52a5-c229bc849ee3");
    expect(getByText("Error: Could not delete configuration.")).toBeInTheDocument();
  });

  test("Submit new config - Invalid JSON", async () => {
    service.submitDocumentTypeConfigurations = jest.fn(() => Promise.resolve());
    service.getDocumentTypeConfigurations = jest.fn(() =>
      Promise.resolve(configurations)
    );

    const {
      getByPlaceholderText,
      getByText,
      queryByText,
      getByTestId,
    } = render(<DocumentTypeEditor />);
    await waitFor(() => {});

    const add = getByTestId("add-btn");
    fireEvent.click(add);
    await waitFor(() => {
      getByPlaceholderText("Input a new configuration JSON");
    });

    const textArea = getByPlaceholderText("Input a new configuration JSON");
    const button = getByText("Submit");

    userEvent.type(textArea, "{");
    await waitFor(() => {});

    fireEvent.click(button);
    await waitFor(() => {});

    expect(queryByText("Sorry this JSON is invalid!")).toBeInTheDocument();
  });

  test("Submit new config - Valid JSON", async () => {
    service.submitDocumentTypeConfigurations = jest.fn(() => Promise.resolve());
    service.getDocumentTypeConfigurations = jest.fn(() =>
      Promise.resolve(configurations)
    );

    mockApi.onPost("/documentTypeConfigurations").reply(200);

    const {
      getByPlaceholderText,
      getByText,
      queryByText,
      getByTestId,
    } = render(<DocumentTypeEditor />);
    await waitFor(() => {});

    const add = getByTestId("add-btn");
    fireEvent.click(add);
    await waitFor(() => {
      getByPlaceholderText("Input a new configuration JSON");
    });

    const textArea = getByPlaceholderText("Input a new configuration JSON");
    const button = getByText("Submit");

    userEvent.type(
      textArea,
      JSON.stringify({
        ...configurations[1],
        documentType: { type: "ABC", description: "Description" },
      })
    );
    await waitFor(() => {});

    fireEvent.click(button);
    await waitFor(() => {});

    expect(queryByText("Sorry this JSON is invalid!")).not.toBeInTheDocument();
    expect(service.getDocumentTypeConfigurations).toHaveBeenCalledTimes(2);
  });

  test("Submit new config - API Error", async () => {
    service.getDocumentTypeConfigurations = jest.fn(() => Promise.reject());
    service.submitDocumentTypeConfigurations = jest.fn(() =>
      Promise.reject({ error: { message: "Error" } })
    );

    const { getByPlaceholderText, getByText, getByTestId } = render(
      <DocumentTypeEditor />
    );
    await waitFor(() => {});

    const add = getByTestId("add-btn");
    fireEvent.click(add);
    await waitFor(() => {
      getByPlaceholderText("Input a new configuration JSON");
    });

    const textArea = getByPlaceholderText("Input a new configuration JSON");
    const button = getByText("Submit");

    userEvent.type(
      textArea,
      JSON.stringify({
        ...configurations[1],
        documentType: { type: "ABC", description: "Description" },
      })
    );
    await waitFor(() => {});

    fireEvent.click(button);
    await waitFor(() => {});

    expect(service.getDocumentTypeConfigurations).toHaveBeenCalledTimes(1);
  });

  test("Update existing config - Success", async () => {
    service.getDocumentTypeConfigurations = jest.fn(() =>
      Promise.resolve(configurations)
    );
    mockApi.onPut("/documentTypeConfigurations").reply(200);

    const {
      getByPlaceholderText,
      getByText,
      queryByText,
      getAllByTestId,
    } = render(<DocumentTypeEditor />);
    await waitFor(() => {});

    const update = getAllByTestId("update-btn")[0];
    fireEvent.click(update);
    await waitFor(() => {
      getByPlaceholderText("Input a new configuration JSON");
    });

    const submit = getByText("Submit");

    fireEvent.click(submit);
    await waitFor(() => {});

    expect(queryByText("Sorry this JSON is invalid!")).not.toBeInTheDocument();
    expect(service.getDocumentTypeConfigurations).toHaveBeenCalledTimes(3);
  });

  test("Update existing config - Cancel", async () => {
    service.getDocumentTypeConfigurations = jest.fn(() =>
      Promise.resolve(configurations)
    );

    const {
      getByPlaceholderText,
      getByText,
      getAllByTestId,
    } = render(<DocumentTypeEditor />);
    await waitFor(() => {});

    const update = getAllByTestId("update-btn")[0];
    fireEvent.click(update);
    await waitFor(() => {
      getByPlaceholderText("Input a new configuration JSON");
    });

    const textArea = getByPlaceholderText("Input a new configuration JSON");
    const cancel = getByText("Cancel");
    fireEvent.click(cancel);
    await waitFor(() => {});

    expect(textArea).not.toBeInTheDocument();
  });

  test("Update existing config - Failure", async () => {
    service.getDocumentTypeConfigurations = jest.fn(() =>
      Promise.resolve(configurations)
    );
    mockApi.onPut("/documentTypeConfigurations").reply(400);

    const {
      getByPlaceholderText,
      getByText,
      getAllByTestId,
      getByTestId,
    } = render(<DocumentTypeEditor />);
    await waitFor(() => {});

    const update = getAllByTestId("update-btn")[0];
    fireEvent.click(update);
    await waitFor(() => {
      getByPlaceholderText("Input a new configuration JSON");
    });

    const textArea = getByPlaceholderText("Input a new configuration JSON");
    const button = getByText("Submit");

    fireEvent.click(button);
    await waitFor(() => {});

    expect(getByTestId("submission-error")).toBeInTheDocument();
  });
});
