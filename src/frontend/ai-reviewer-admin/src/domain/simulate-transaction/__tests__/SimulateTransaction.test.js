import React from "react";
import api from "AxiosConfig";
import MockAdapter from "axios-mock-adapter";
import { render, waitFor, fireEvent } from "@testing-library/react";
import { configurations } from "domain/documents/_tests_/test-data";
import SimulateTransaction from "../SimulateTransaction";

function dispatchEvt(node, type, data) {
  const event = new Event(type, { bubbles: true });
  Object.assign(event, data);
  fireEvent.drop(node, event);
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

describe("Simulate Transaction Test Suite", () => {
  let mockApi;
  beforeEach(() => {
    console.error = jest.fn();
    mockApi = new MockAdapter(api);
    jest.clearAllMocks();
  });

  const file = new File(["content"], "fakeFile.pdf", {
    type: "application/pdf",
  });
  const data = mockData([file]);

  const resolveExtract = {
    extract: {
      id: "b3184eba-dd42-4a95-b7ec-6efd2af95be7",
      transactionId: "1d4e38ba-0c88-4c92-8367-c8eada8cca19",
    },
    document: {
      documentId: 1234,
      type: "DEVRCC",
      fileName: "fakeFile.pdf",
      size: 134936,
      contentType: "application/pdf",
    },
  };

  const resolveProcessed = {
    extract: {
      id: "b3184eba-dd42-4a95-b7ec-6efd2af95be7",
      transactionId: "1d4e38ba-0c88-4c92-8367-c8eada8cca19",
    },
    document: {
      documentId: null,
      type: "DEVRCC",
      fileName: "fakeFile.pdf",
      size: 134936,
      contentType: "application/pdf",
    },
    validation: [],
    result: {
      document: {
        documentType: "",
        dateFiled: "",
        filedBy: "",
      },
      court: {
        location: "",
        level: "",
        class: "",
        division: "",
        fileNumber: "",
      },
      parties: {
        defendants: "",
        plaintiffs: "",
      },
    },
  };

  test("Get Document Types - success", async () => {
    mockApi.onGet("/documentTypeConfigurations").reply(200, configurations);
    //docService.getDocumentTypeConfigurations = jest.fn(() => Promise.resolve(configurations))
    const { container, getByText } = render(<SimulateTransaction />);
    const dropzone = container.querySelector('[data-testid="dropdownzone"]');

    dispatchEvt(dropzone, "drop", data);
    await waitFor(() => {
      getByText("fakeFile.pdf");
    });

    expect(getByText("fakeFile.pdf")).toBeInTheDocument();
  });

  test("Get Document Types - failure", async () => {
    mockApi.onGet("/documentTypeConfigurations").reply(400);
    const { container, getByText } = render(<SimulateTransaction />);
    const dropzone = container.querySelector('[data-testid="dropdownzone"]');

    dispatchEvt(dropzone, "drop", data);
    await waitFor(() => {
      getByText("Error: Could not load configurations.");
    });

    expect(
      getByText("Error: Could not load configurations.")
    ).toBeInTheDocument();
  });

  test("Submit file for extraction - success", async () => {
    mockApi.onGet("/documentTypeConfigurations").reply(200, configurations);
    mockApi.onPost("/documents/extract").reply(200, resolveExtract);
    mockApi.onGet("/documents/processed/1234").reply(200, resolveProcessed);

    const { container, getByText, getByTestId } = render(
      <SimulateTransaction />
    );
    const dropzone = container.querySelector('[data-testid="dropdownzone"]');

    dispatchEvt(dropzone, "drop", data);
    await waitFor(() => {
      getByText("fakeFile.pdf");
    });

    const dropdown = getByTestId("dropdown");
    fireEvent.change(dropdown, { target: { value: "DEVRCC" } });
    await waitFor(() => {});

    const button = getByText("Submit");
    fireEvent.click(button);
    await waitFor(() => {});

    expect(getByText("Document Processed Successfuly")).toBeInTheDocument();
  });

  test("Submit file for extraction - failure", async () => {
    mockApi.onGet("/documentTypeConfigurations").reply(200, configurations);
    mockApi.onPost("/documents/extract").reply(200, resolveExtract);
    mockApi.onGet("/documents/processed/1234").reply(400);

    const { container, getByText, getByTestId, queryByText } = render(
      <SimulateTransaction />
    );
    const dropzone = container.querySelector('[data-testid="dropdownzone"]');

    dispatchEvt(dropzone, "drop", data);
    await waitFor(() => {
      getByText("fakeFile.pdf");
    });

    const dropdown = getByTestId("dropdown");
    fireEvent.change(dropdown, { target: { value: "DEVRCC" } });
    await waitFor(() => {});

    const button = getByText("Submit");
    fireEvent.click(button);
    await waitFor(() => {});

    expect(
      queryByText("Document Processed Successfuly")
    ).not.toBeInTheDocument();
  });

  test("Get processed document - failure", async () => {
    mockApi.onGet("/documentTypeConfigurations").reply(200, configurations);
    mockApi.onPost("/documents/extract").reply(400);
    mockApi.onGet("/documents/processed/1234").reply(400);

    const { container, getByText, getByTestId, queryByText } = render(
      <SimulateTransaction />
    );
    const dropzone = container.querySelector('[data-testid="dropdownzone"]');

    dispatchEvt(dropzone, "drop", data);
    await waitFor(() => {
      getByText("fakeFile.pdf");
    });

    const dropdown = getByTestId("dropdown");
    fireEvent.change(dropdown, { target: { value: "DEVRCC" } });
    await waitFor(() => {});

    const button = getByText("Submit");
    fireEvent.click(button);
    await waitFor(() => {});

    expect(
      queryByText("Document Processed Successfuly")
    ).not.toBeInTheDocument();
  });
});
