import React from "react";
import { getTestData } from "../../../modules/confirmationPopupTestData";

import Upload from "./Upload";

export default {
  title: "Upload",
  component: Upload,
};

const confirmationPopup = getTestData();
const submissionId = "abc123";
const upload = { confirmationPopup, submissionId };

export const Default = () => <Upload upload={upload} />;

export const Mobile = () => <Upload upload={upload} />;

const mobileViewport = {
  parameters: {
    viewport: {
      defaultViewport: "mobile2",
    },
  },
};

Mobile.story = mobileViewport;
