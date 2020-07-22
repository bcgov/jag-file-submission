import React from "react";
import Dinero from "dinero.js";
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
const fileSummaryData = [
  {
    name: "Number of Documents in Package:",
    value: "5",
    isValueBold: true
  },
  {
    name: "Statutory Fees:",
    value: "$150.00",
    isValueBold: true
  },
  {
    name: "Submission Fee:",
    value: "$7.00",
    isValueBold: true
  }
];
const overallTotalFeeData = Dinero({ amount: 157 * 100 });

const payment = {
  confirmationPopup,
  submissionId,
  courtData,
  fileSummaryData,
  overallTotalFeeData
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
