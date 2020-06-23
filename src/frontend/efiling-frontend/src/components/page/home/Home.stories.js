import React from "react";
import { MemoryRouter } from "react-router-dom";
import { createMemoryHistory } from "history";
import axios from "axios";
import MockAdapter from "axios-mock-adapter";

import Home from "./Home";

console.error = () => {}; // for async storyshot errors, due to lack of support

export default {
  title: "Home",
  component: Home
};

const header = {
  name: "eFiling Demo Client",
  history: createMemoryHistory()
};
const page = { header };

const submissionId = "abc123";
const mock = new MockAdapter(axios);
const apiRequest = `/submission/${submissionId}/userDetail`;

const LoaderStateData = props => {
  mock.onGet(apiRequest).reply(400);
  return props.children({ page });
};

const AccountExistsStateData = props => {
  mock.onGet(apiRequest).reply(200, { csoAccountExists: true });
  return props.children({ page });
};

const NoAccountExistsStateData = props => {
  mock.onGet(apiRequest).reply(200, { csoAccountExists: false });
  return props.children({ page });
};

const homeComponent = data => (
  <MemoryRouter initialEntries={[`?submissionId=${submissionId}`]}>
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

export const Loader = () => loaderComponent;

export const LoaderMobile = () => loaderComponent;

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

LoaderMobile.story = mobileViewport;
AccountExistsMobile.story = mobileViewport;
NoAccountExistsMobile.story = mobileViewport;
