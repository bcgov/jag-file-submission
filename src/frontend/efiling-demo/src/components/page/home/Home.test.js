import React from "react";
import { createMemoryHistory } from "history";
import axios from "axios";
import {
  render,
  waitFor,
  getByText,
  fireEvent,
  getAllByRole
} from "@testing-library/react";
import Home, { eFilePackage } from "./Home";

const MockAdapter = require("axios-mock-adapter");

window.open = jest.fn();

const header = {
  name: "eFiling Demo Client",
  history: createMemoryHistory()
};

const page = { header };

describe("Home", () => {
  let mock;
  const setErrorExists = jest.fn();
  const files = [
    {
      file: {
        name: "filename",
        type: "filetype"
      }
    }
  ];
  const accountGuid = "guid";
  const submissionId = "123";
  const filingPackage = {
    documents: [files[0].file]
  };

  beforeEach(() => {
    mock = new MockAdapter(axios);
  });

  test("Component matches the snapshot", () => {
    const { asFragment } = render(<Home page={page} />);

    expect(asFragment()).toMatchSnapshot();
  });

  test("eFilePackage function displays an error message on page on failure of generateUrl call", async () => {
    mock.onPost("/submission/documents").reply(200, { submissionId });
    mock.onPost("/submission/generateUrl").reply(400);

    eFilePackage(files, accountGuid, setErrorExists, filingPackage);

    await waitFor(() => {});

    expect(setErrorExists).toHaveBeenCalledWith(true);
  });

  test("eFilePackage function displays an error message on page on failure of uploadDocuments call", async () => {
    mock.onPost("/submission/documents").reply(400);

    eFilePackage(files, accountGuid, setErrorExists, filingPackage);

    await waitFor(() => {});

    expect(setErrorExists).toHaveBeenCalledWith(true);
  });

  test("eFilePackage function generates the proper documentData for the updated url body and redirects to frontend app on success", async () => {
    const efilingUrl = "example.com";

    mock.onPost("/submission/documents").reply(200, { submissionId });
    mock
      .onPost(`/submission/${submissionId}/generateUrl`)
      .reply(200, { efilingUrl });

    eFilePackage(files, accountGuid, setErrorExists, filingPackage);

    await waitFor(() => {});

    expect(window.open).toHaveBeenCalledTimes(1);
    expect(window.open).toHaveBeenCalledWith(efilingUrl, "_self");
  });

  test("eFilePackage functions returns error when no files uploaded", async () => {
    const { container } = render(<Home page={page} />);

    const textbox = getAllByRole(container, "textbox");

    fireEvent.change(textbox[0], {
      target: { value: "" }
    });

    fireEvent.change(textbox[1], {
      target: { value: JSON.stringify(filingPackage) }
    });

    fireEvent.click(getByText(container, "E-File my Package"));

    await waitFor(() => {});

    expect(
      getByText(
        container,
        "An error occurred while eFiling your package. Please make sure you upload at least one file and try again."
      )
    ).toBeInTheDocument();
    expect(setErrorExists).toHaveBeenCalledWith(true);
  });

  test("eFilePackage does not make axios call when no formdata present (due to incorrect filingPackage data)", () => {
    const improperFilingPackage = {
      documents: [{ name: "wrongname", type: "type" }]
    };

    eFilePackage(files, accountGuid, setErrorExists, improperFilingPackage);

    expect(setErrorExists).toHaveBeenCalledWith(true);
  });
});
