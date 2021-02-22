import React from "react";
import { getDocumentsData } from "../../modules/test-data/documentTestData";
import { getTestData } from "../../modules/test-data/confirmationPopupTestData";
import { getCourtData } from "../../modules/test-data/courtTestData";

import Payment from "./Payment";

export default {
  title: "Payment",
  component: Payment,
};

sessionStorage.setItem("csoAccountId", "123");

const confirmationPopup = getTestData();
const submissionId = "abc123";
const courtData = getCourtData();
const files = getDocumentsData();
const submissionFee = 25.5;
const noFeeFile = [
  {
    name: "file name 1",
    description: "file description 1",
    type: "file type",
    statutoryFeeAmount: 0,
    isAmendment: null,
    isSupremeCourtScheduling: null,
  },
];

const payment = {
  confirmationPopup,
  submissionId,
  courtData,
  files,
  submissionFee,
};

export const WithFees = () => <Payment payment={payment} />;

export const WithoutFees = () => (
  <Payment payment={{ ...payment, submissionFee: 0, files: noFeeFile }} />
);

export const Mobile = () => <Payment payment={payment} />;

Mobile.parameters = {
  viewport: {
    defaultViewport: "mobile2",
  },
};
