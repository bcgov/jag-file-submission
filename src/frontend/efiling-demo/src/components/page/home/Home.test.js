import React from "react";
import { createMemoryHistory } from "history";
import axios from "axios";
import {
  render,
  wait,
  getByText,
  fireEvent,
  getByRole
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

    eFilePackage(files, accountGuid, setErrorExists);

    await wait(() => {
      expect(setErrorExists).toHaveBeenCalledWith(true);
    });
  });

  test("eFilePackage function displays an error message on page on failure of uploadDocuments call", async () => {
    mock.onPost("/submission/documents").reply(400);

    eFilePackage(files, accountGuid, setErrorExists);

    await wait(() => {
      expect(setErrorExists).toHaveBeenCalledWith(true);
    });
  });

  test("eFilePackage function generates the proper documentData for the updated url body and redirects to frontend app on success", async () => {
    const efilingUrl = "example.com";

    mock.onPost("/submission/documents").reply(200, { submissionId });
    mock
      .onPost(`/submission/${submissionId}/generateUrl`)
      .reply(200, { efilingUrl });

    eFilePackage(files, accountGuid, setErrorExists);

    await wait(() => {
      expect(window.open).toHaveBeenCalledTimes(1);
      expect(window.open).toHaveBeenCalledWith(efilingUrl, "_self");
    });
  });

  test("eFilePackage functions returns error when no files uploaded", async () => {
    const { container } = render(<Home page={page} />);

    fireEvent.change(getByRole(container, "textbox"), {
      target: { value: "" }
    });

    fireEvent.click(getByText(container, "E-File my Package"));

    await wait(() => {
      expect(
        getByText(
          container,
          "An error occurred while eFiling your package. Please make sure you upload at least one file and try again."
        )
      ).toBeInTheDocument();
      expect(setErrorExists).toHaveBeenCalledWith(true);
    });
  });
});
