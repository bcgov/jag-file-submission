/* eslint-disable import/no-named-as-default, import/no-named-as-default-member, */
import React from "react";
import axios from "axios";
import MockAdapter from "axios-mock-adapter";
import { getTestData } from "../../../modules/test-data/confirmationPopupTestData";
import { getDocumentsData } from "../../../modules/test-data/documentTestData";
import { getCourtData } from "../../../modules/test-data/courtTestData";
import { generateJWTToken } from "../../../modules/helpers/authentication-helper/authenticationHelper";

import PackageConfirmation from "./PackageConfirmation";

export default {
  title: "PackageConfirmation",
  component: PackageConfirmation,
};

const submissionId = "abc123";
const apiRequest = `/submission/${submissionId}/filing-package`;
const confirmationPopup = getTestData();
const packageConfirmation = { confirmationPopup, submissionId };
const csoAccountStatus = { isNew: false };
const documents = getDocumentsData();
const courtData = getCourtData();
const submissionFeeAmount = 25.5;

sessionStorage.setItem("csoAccountId", "123");
const token = generateJWTToken({ preferred_username: "username@bceid" });
localStorage.setItem("jwt", token);

const LoadData = (props) => {
  const mock = new MockAdapter(axios);
  mock
    .onGet(apiRequest)
    .reply(200, { documents, court: courtData, submissionFeeAmount });
  return props.children({ packageConfirmation, csoAccountStatus });
};

export const ExistingAccount = function () {
  return (
    <LoadData>
      {(data) => (
        <PackageConfirmation
          packageConfirmation={data.packageConfirmation}
          csoAccountStatus={data.csoAccountStatus}
        />
      )}
    </LoadData>
  );
};

export const NewAccount = function () {
  return (
    <LoadData>
      {(data) => (
        <PackageConfirmation
          packageConfirmation={data.packageConfirmation}
          csoAccountStatus={{ ...data.csoAccountStatus, isNew: true }}
        />
      )}
    </LoadData>
  );
};

export const Mobile = function () {
  return (
    <LoadData>
      {(data) => (
        <PackageConfirmation
          packageConfirmation={data.packageConfirmation}
          csoAccountStatus={data.csoAccountStatus}
        />
      )}
    </LoadData>
  );
};

Mobile.parameters = {
  viewport: {
    defaultViewport: "mobile2",
  },
};
