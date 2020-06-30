import React from "react";
import testBasicSnapshot from "../../TestHelper";
import { getTestData } from "../../modules/termsOfUseTestData";

import TermsOfUse from "./TermsOfUse";

describe("TermsOfUse", () => {
  const onScroll = jest.fn();

  const acceptTerms = jest.fn();

  const content = getTestData();

  const heading = "Terms of Use";

  const confirmText = "I accept these terms and conditions";

  test("Matches the snapshot", () => {
    const termsOfUse = (
      <TermsOfUse
        onScroll={onScroll}
        acceptTerms={acceptTerms}
        content={content}
        heading={heading}
        confirmText={confirmText}
      />
    );

    testBasicSnapshot(termsOfUse);
  });
});
