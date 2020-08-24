import React from "react";
import { getTestData } from "../../../modules/test-data/confirmationPopupTestData";
import { getCourtData } from "../../../modules/test-data/courtTestData";

import Upload from "./Upload";

export default {
  title: "Upload",
  component: Upload,
};

const confirmationPopup = getTestData();
const courtData = getCourtData();
const submissionId = "abc123";
const upload = { confirmationPopup, submissionId, courtData };

export const Default = () => <Upload upload={upload} />;

export const Mobile = () => <Upload upload={upload} />;

Mobile.parameters = {
  viewport: {
    defaultViewport: "mobile2",
  },
};
