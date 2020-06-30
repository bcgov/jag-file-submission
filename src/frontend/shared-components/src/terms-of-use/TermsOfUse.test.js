import React from "react";
import testBasicSnapshot from "../TestHelper";
import { getTestData } from "../modules/termsOfUseTestData";

import TermsOfUse from "./TermsOfUse";

describe("TermsOfUse", () => {
  const onScroll = jest.fn();

  const acceptTerms = jest.fn();

  const continueButton = {
    label: "Continue",
    styling: "normal-blue btn",
    onClick: jest.fn()
  };

  const cancelButton = {
    label: "Cancel",
    styling: "normal-white btn",
    onClick: jest.fn()
  };

  const content = getTestData();

  test("Matches the snapshot", () => {
    const termsOfUse = (
      <TermsOfUse
        onScroll={onScroll}
        acceptTerms={acceptTerms}
        continueButton={continueButton}
        cancelButton={cancelButton}
        content={content}
      />
    );

    testBasicSnapshot(termsOfUse);
  });
});
