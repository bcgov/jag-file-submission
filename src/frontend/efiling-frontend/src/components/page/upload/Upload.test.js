import React from "react";
import axios from "axios";
import MockAdapter from "axios-mock-adapter";
import { render, fireEvent, getByText, waitFor } from "@testing-library/react";
import { getTestData } from "../../../modules/confirmationPopupTestData";
import { getDocumentsData } from "../../../modules/documentTestData";
import { getCourtData } from "../../../modules/courtTestData";
import { generateJWTToken } from "../../../modules/authenticationHelper";

import Upload from "./Upload";

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
  const confirmationPopup = getTestData();
  const submissionId = "abc123";
  const documents = getDocumentsData();
  const court = getCourtData();
  const submissionFeeAmount = 25.5;

  const upload = {
    confirmationPopup,
    submissionId,
  };

  const token = generateJWTToken({ preferred_username: "username@bceid" });
  localStorage.setItem("jwt", token);

  const apiRequest = `/submission/${submissionId}/filing-package`;
  let mock;
  beforeEach(() => {
    mock = new MockAdapter(axios);
    window.open = jest.fn();
  });

  test("Matches the snapshot", () => {
    const { asFragment } = render(<Upload upload={upload} />);

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

    expect(asFragment()).toMatchSnapshot();
  });
});
