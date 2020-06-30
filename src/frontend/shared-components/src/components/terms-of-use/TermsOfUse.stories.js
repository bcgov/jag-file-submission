import React from "react";
import { action } from "@storybook/addon-actions";
import { getTestData } from "../../modules/termsOfUseTestData";

import TermsOfUse from "./TermsOfUse";

export default {
  title: "Terms of Use",
  component: TermsOfUse
};

const continueButton = {
  label: "Continue",
  styling: "normal-blue btn",
  onClick: () => action("on click continue")
};

const cancelButton = {
  label: "Cancel",
  styling: "normal-white btn",
  onClick: () => action("on click cancel")
};

const content = getTestData();

const heading = "Terms of Use";

const confirmText = "I accept these terms and conditions";

export const Default = () => (
  <TermsOfUse
    onScroll={action("terms of use scroll")}
    acceptTerms={action("accept terms of use")}
    continueButton={continueButton}
    cancelButton={cancelButton}
    content={content}
    heading={heading}
    confirmText={confirmText}
  />
);

export const Mobile = () => (
  <TermsOfUse
    onScroll={action("terms of use scroll")}
    acceptTerms={action("accept terms of use")}
    continueButton={continueButton}
    cancelButton={cancelButton}
    content={content}
    heading={heading}
    confirmText={confirmText}
  />
);

Mobile.story = {
  parameters: {
    viewport: {
      defaultViewport: "mobile2"
    }
  }
};
