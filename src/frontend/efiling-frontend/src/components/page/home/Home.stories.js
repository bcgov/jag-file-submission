import React from "react";
import { MemoryRouter } from "react-router-dom";
import { createMemoryHistory } from "history";
import axios from "axios";
import MockAdapter from "axios-mock-adapter";
import { getTestData } from "../../../modules/confirmationPopupTestData";
import { getUserDetails } from "../../../modules/userDetails";

import Home from "./Home";

export default {
  title: "Home",
  component: Home
};

const header = {
  name: "E-File Submission",
  history: createMemoryHistory()
};
const confirmationPopup = getTestData();
const page = { header, confirmationPopup };

const submissionId = "abc123";
const mock = new MockAdapter(axios);
const apiRequest = `/submission/${submissionId}`;
const navigation = {
  cancel: {
    url: "cancelurl.com"
  },
  success: {
    url: "successurl.com"
  },
  error: {
    url: ""
  }
};
const userDetails = getUserDetails();

const LoaderStateData = props => {
  mock.onGet(apiRequest).reply(400, { message: "There was an error" });
  return props.children({ page });
};

const AccountExistsStateData = props => {
  mock.onGet(apiRequest).reply(200, { userDetails, navigation });
  return props.children({ page });
};

const NoAccountExistsStateData = props => {
  mock.onGet(apiRequest).reply(200, {
    userDetails: { ...userDetails, accounts: null },
    navigation
  });
  return props.children({ page });
};

const homeComponent = data => (
  <MemoryRouter
    initialEntries={[
      { search: `?submissionId=${submissionId}`, key: "testKey" }
    ]}
  >
    <Home page={data.page} />
  </MemoryRouter>
);

const loaderComponent = (
  <LoaderStateData>{data => homeComponent(data)}</LoaderStateData>
);

const accountExistsComponent = (
  <AccountExistsStateData>{data => homeComponent(data)}</AccountExistsStateData>
);

const noAccountExistsComponent = (
  <NoAccountExistsStateData>
    {data => homeComponent(data)}
  </NoAccountExistsStateData>
);

export const Error = () => loaderComponent;

export const ErrorMobile = () => loaderComponent;

export const AccountExists = () => accountExistsComponent;

export const AccountExistsMobile = () => accountExistsComponent;

export const NoAccountExists = () => noAccountExistsComponent;

export const NoAccountExistsMobile = () => noAccountExistsComponent;

const mobileViewport = {
  parameters: {
    viewport: {
      defaultViewport: "mobile2"
    }
  }
};

ErrorMobile.story = mobileViewport;
AccountExistsMobile.story = mobileViewport;
NoAccountExistsMobile.story = mobileViewport;
