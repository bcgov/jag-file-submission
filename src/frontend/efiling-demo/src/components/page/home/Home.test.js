import React from "react";
import { createMemoryHistory } from "history";
import axios from "axios";
import MockAdapter from "axios-mock-adapter";
import {
  render,
  waitFor,
  getByText,
  fireEvent,
  getAllByRole,
} from "@testing-library/react";
import Home from "./Home";
import { generateJWTToken } from "../../../modules/authentication-helper/authenticationHelper";

const header = {
  name: "eFiling Demo Client",
  history: createMemoryHistory(),
};

const page = { header };

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

describe("Home", () => {
  let mock;
  const files = [
    {
      file: {
        name: "ping.json",
        type: "json",
      },
    },
  ];
  const submissionId = "123";
  const efilingUrl = "example.com";
  const filingPackage = {
    documents: [files[0].file],
  };

  sessionStorage.setItem("apiKeycloakUrl", "apikeycloakexample.com");
  sessionStorage.setItem("apiKeycloakRealm", "apiRealm");

  const token = generateJWTToken({
    "universal-id": "123",
  });
  localStorage.setItem("jwt", token);

  const file = new File([JSON.stringify({ ping: true })], "ping.json", {
    type: "application/json",
  });
  const data = mockData([file]);
  const ui = <Home page={page} />;

  beforeEach(() => {
    mock = new MockAdapter(axios);
    window.open = jest.fn();
  });

  test("Component matches the snapshot", async () => {
    mock
      .onPost(
        "apikeycloakexample.com/realms/apiRealm/protocol/openid-connect/token"
      )
      .reply(200, { access_token: token });

    const { asFragment } = render(ui);

    await waitFor(() => {});

    expect(asFragment()).toMatchSnapshot();
  });

  test("displays an error message on page on failure of generateUrl call", async () => {
    mock
      .onPost(
        "apikeycloakexample.com/realms/apiRealm/protocol/openid-connect/token"
      )
      .reply(200, { access_token: token });
    mock.onPost("/submission/documents").reply(200, { submissionId });
    mock.onPost("/submission/generateUrl").reply(400);

    const { container } = render(ui);
    const dropzone = container.querySelector('[data-testid="dropdownzone"]');

    dispatchEvt(dropzone, "drop", data);

    await waitFor(() => {});
    await flushPromises(ui, container);

    const button = getByText(container, "E-File my Package");

    const textbox = getAllByRole(container, "textbox");

    fireEvent.change(textbox[0], {
      target: { value: JSON.stringify(filingPackage) },
    });

    fireEvent.click(button);

    await waitFor(() => {});

    const error = getByText(
      container,
      "An error occurred while eFiling your package. Please make sure you upload at least one file and try again."
    );

    expect(error).toBeInTheDocument();
  });

  test("displays an error message on page on failure of upload documents call", async () => {
    mock
      .onPost(
        "apikeycloakexample.com/realms/apiRealm/protocol/openid-connect/token"
      )
      .reply(200, { access_token: token });
    mock.onPost("/submission/documents").reply(400);
    mock.onPost("/submission/generateUrl").reply(200, { efilingUrl });

    const { container } = render(ui);
    const dropzone = container.querySelector('[data-testid="dropdownzone"]');

    dispatchEvt(dropzone, "drop", data);

    await waitFor(() => {});
    await flushPromises(ui, container);

    const button = getByText(container, "E-File my Package");

    const textbox = getAllByRole(container, "textbox");

    fireEvent.change(textbox[0], {
      target: { value: JSON.stringify(filingPackage) },
    });

    fireEvent.click(button);

    await waitFor(() => {});

    const error = getByText(
      container,
      "An error occurred while eFiling your package. Please make sure you upload at least one file and try again."
    );

    expect(error).toBeInTheDocument();
  });

  test("invoke onDrop when drop event occurs and efile successfully", async () => {
    mock
      .onPost(
        "apikeycloakexample.com/realms/apiRealm/protocol/openid-connect/token"
      )
      .reply(200, { access_token: token });
    mock.onPost("/submission/documents").reply(200, { submissionId });
    mock
      .onPost(`/submission/${submissionId}/generateUrl`)
      .reply(200, { efilingUrl });

    const { container } = render(ui);
    const dropzone = container.querySelector('[data-testid="dropdownzone"]');

    dispatchEvt(dropzone, "drop", data);

    await waitFor(() => {});
    await flushPromises(ui, container);

    const button = getByText(container, "E-File my Package");

    const textbox = getAllByRole(container, "textbox");

    fireEvent.change(textbox[0], {
      target: { value: JSON.stringify(filingPackage) },
    });

    fireEvent.click(button);

    await waitFor(() => {});

    expect(window.open).toHaveBeenCalledWith("example.com", "_self");
  });

  // This test is not relevant anymore as document names are no longer required in the filing package (JSON)
  /* test("uploading document with incorrect name matching filing package sets error", async () => {
    mock
      .onPost(
        "apikeycloakexample.com/realms/apiRealm/protocol/openid-connect/token"
      )
      .reply(200, { access_token: token });
    mock.onPost("/submission/documents").reply(200, { submissionId });
    mock
      .onPost(`/submission/${submissionId}/generateUrl`)
      .reply(200, { efilingUrl });

    const wrongFile = new File(
      [JSON.stringify({ ping: true })],
      "wrongping.json",
      {
        type: "application/json",
      }
    );
    const wrongData = mockData([wrongFile]);

    const { container } = render(ui);
    const dropzone = container.querySelector('[data-testid="dropdownzone"]');

    dispatchEvt(dropzone, "drop", wrongData);

    await waitFor(() => {});
    await flushPromises(ui, container);

    const button = getByText(container, "E-File my Package");

    const textbox = getAllByRole(container, "textbox");

    fireEvent.change(textbox[0], {
      target: { value: JSON.stringify(filingPackage) },
    });

    fireEvent.click(button);

    await waitFor(() => {});

    const error = getByText(
      container,
      "An error occurred while eFiling your package. Please make sure you upload at least one file and try again."
    );

    expect(error).toBeInTheDocument();
  }); */
});
