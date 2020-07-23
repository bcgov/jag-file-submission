import React from "react";
import axios from "axios";
import MockAdapter from "axios-mock-adapter";
import { render, fireEvent, getByText, wait } from "@testing-library/react";
import { getTestData } from "../../../modules/confirmationPopupTestData";
import { getDocumentsData } from "../../../modules/documentTestData";
import { getCourtData } from "../../../modules/courtTestData";

import Upload from "./Upload";

describe("Upload Component", () => {
  const confirmationPopup = getTestData();
  const submissionId = "abc123";
  const documents = getDocumentsData();
  const court = getCourtData();
  const submissionFeeAmount = 25.5;

  const upload = {
    confirmationPopup,
    submissionId
  };

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

    await wait(() => {
      expect(asFragment()).toMatchSnapshot();
    });
  });
});
