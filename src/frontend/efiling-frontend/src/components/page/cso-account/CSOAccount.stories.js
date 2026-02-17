import React from "react";
import { action } from "@storybook/addon-actions";
import axios from "axios";
import MockAdapter from "axios-mock-adapter";

import { getTestData } from "../../../modules/test-data/confirmationPopupTestData";
import { getApplicantInfo } from "../../../modules/test-data/applicantInfoTestData";

import CSOAccount from "./CSOAccount";

export default {
  title: "CSOAccount",
  component: CSOAccount,
};

const confirmationPopup = getTestData();
const applicantInfo = getApplicantInfo();
const setCsoAccountStatus = action("setCso");

const mock = new MockAdapter(axios);
const API_REQUEST = "/csoAccount";

const CreateAccount = (props) => {
  mock.onPost(API_REQUEST).reply(201, {
    internalClientNumber: "ABC123",
    clientId: "123",
  });

  return props.children({
    confirmationPopup,
    applicantInfo,
    setCsoAccountStatus,
  });
};

const baseComponent = (data) => (
  <CSOAccount
    confirmationPopup={data.confirmationPopup}
    applicantInfo={data.applicantInfo}
    setCsoAccountStatus={data.setCsoAccountStatus}
  />
);

const defaultCreateAccount = (
  <CreateAccount>{(data) => baseComponent(data)}</CreateAccount>
);

export function Default() {
  return defaultCreateAccount;
}

export function Mobile() {
  return defaultCreateAccount;
}

Mobile.parameters = {
  viewport: {
    defaultViewport: "mobile2",
  },
};
