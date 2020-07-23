import React from "react";
import { getDocumentsData } from "../../../modules/documentTestData";
import { getTestData } from "../../../modules/confirmationPopupTestData";

import Payment from "./Payment";

export default {
  title: "Payment",
  component: Payment
};

sessionStorage.setItem("csoAccountId", "123");

const confirmationPopup = getTestData();
const submissionId = "abc123";
const courtData = {
  locationDescription: "Court location",
  fileNumber: "Court file number",
  level: "Level",
  courtClass: "Class"
};
const files = getDocumentsData();
const submissionFee = 25.5;

const payment = {
  confirmationPopup,
  submissionId,
  courtData,
  files,
  submissionFee
};

export const Default = () => <Payment payment={payment} />;

export const Mobile = () => <Payment payment={payment} />;

const mobileViewport = {
  parameters: {
    viewport: {
      defaultViewport: "mobile2"
    }
  }
};

Mobile.story = mobileViewport;
