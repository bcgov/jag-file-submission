/* eslint-disable vars-on-top, import/no-named-as-default, import/no-named-as-default-member, no-constructor-return */
import React from "react";
import { getDocumentsData } from "../../../modules/test-data/documentTestData";
import { getTestData } from "../../../modules/test-data/confirmationPopupTestData";
import { getCourtData } from "../../../modules/test-data/courtTestData";
import Rush from "./Rush";

export default {
  title: "Rush",
  component: Rush,
};

const confirmationPopup = getTestData();
const submissionId = "abc123";
const courtData = getCourtData();
const files = getDocumentsData();
const submissionFee = 25.5;
const currentDate = new Date("2019-05-14T11:01:58.135Z");

global.Date = class extends Date {
  constructor() {
    return currentDate;
  }
};

const payment = {
  confirmationPopup,
  submissionId,
  courtData,
  files,
  submissionFee,
};

export const Default = function () {
  return <Rush payment={payment} />;
};

export const Mobile = function () {
  return <Rush payment={payment} />;
};

Mobile.parameters = {
  viewport: {
    defaultViewport: "mobile2",
  },
};
