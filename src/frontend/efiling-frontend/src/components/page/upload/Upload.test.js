import React from "react";
import { render } from "@testing-library/react";
import { getTestData } from "../../../modules/confirmationPopupTestData";

import Upload from "./Upload";

describe("Upload Component", () => {
  const confirmationPopup = getTestData();
  const submissionId = "abc123";
  const upload = {
    confirmationPopup,
    submissionId
  };

  test("Matches the snapshot", () => {
    const { asFragment } = render(<Upload upload={upload} />);

    expect(asFragment()).toMatchSnapshot();
  });
});
