import React from "react";
import { createMemoryHistory } from "history";
import axios from "axios";
import MockAdapter from "axios-mock-adapter";
import { getUserDetails } from "../../../modules/test-data/userDetailsTestData";
import { getDocumentsData } from "../../../modules/test-data/documentTestData";
import { getCourtData } from "../../../modules/test-data/courtTestData";
import { getNavigationData } from "../../../modules/test-data/navigationTestData";
import { generateJWTToken } from "../../../modules/helpers/authentication-helper/authenticationHelper";

import Home from "./Home";

export default {
  title: "Home",
  component: Home,
};

const header = {
  name: "E-File Submission",
  history: createMemoryHistory(),
};
const submissionId = "abc123";
const transactionId = "trans123";
const page = { header, submissionId, transactionId };

const apiRequest = `/submission/${submissionId}`;
const getFilingPackagePath = `/submission/${submissionId}/filing-package`;
const navigation = getNavigationData();
const documents = getDocumentsData();
const court = getCourtData();
const submissionFeeAmount = 25.5;
const userDetails = getUserDetails();
const clientApplication = { displayName: "client app" };

const setRequiredStorage = () => {
  sessionStorage.setItem("errorUrl", "error.com");
  const token = generateJWTToken({
    preferred_username: "username@bceid",
    email: "username@example.com",
    realm_access: {
      roles: ["rush_flag"],
    },
  });
  localStorage.setItem("jwt", token);
};

const LoaderStateData = (props) => {
  setRequiredStorage();
  const mock = new MockAdapter(axios);
  window.open = () => {};
  mock.onGet(apiRequest).reply(400, { message: "There was an error" });
  return props.children({ page });
};

const AccountExistsStateData = (props) => {
  setRequiredStorage();
  const mock = new MockAdapter(axios);
  mock
    .onGet(apiRequest)
    .reply(200, { userDetails, navigation, clientApplication });
  mock
    .onGet(getFilingPackagePath)
    .reply(200, { documents, court, submissionFeeAmount });
  return props.children({ page });
};

const NoAccountExistsStateData = (props) => {
  setRequiredStorage();
  const mock = new MockAdapter(axios);
  mock.onGet(apiRequest).reply(200, {
    userDetails: { ...userDetails, accounts: null },
    navigation,
    clientApplication,
  });
  mock.onGet("/bceidAccount").reply(200, {
    firstName: "User",
    lastName: "Name",
    middleName: "Middle",
  });
  return props.children({ page });
};

const homeComponent = (data) => <Home page={data.page} />;

const loaderComponent = (
  <LoaderStateData>{(data) => homeComponent(data)}</LoaderStateData>
);

const accountExistsComponent = (
  <AccountExistsStateData>
    {(data) => homeComponent(data)}
  </AccountExistsStateData>
);

const noAccountExistsComponent = (
  <NoAccountExistsStateData>
    {(data) => homeComponent(data)}
  </NoAccountExistsStateData>
);

export const Error = () => loaderComponent;

export const ErrorMobile = () => loaderComponent;

export const AccountExists = () => accountExistsComponent;

export const AccountExistsMobile = () => accountExistsComponent;

export const NoAccountExists = () => noAccountExistsComponent;

export const NoAccountExistsMobile = () => noAccountExistsComponent;

const parameters = {
  viewport: {
    defaultViewport: "mobile2",
  },
};

ErrorMobile.parameters = parameters;
AccountExistsMobile.parameters = parameters;
NoAccountExistsMobile.parameters = parameters;
