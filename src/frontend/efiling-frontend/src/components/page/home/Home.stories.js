import React from "react";
import { MemoryRouter } from "react-router-dom";
import { createMemoryHistory } from "history";
import axios from "axios";
import MockAdapter from "axios-mock-adapter";

import Home from "./Home";

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

const LoaderStateData = props => {
  const mock = new MockAdapter(axios);
  const apiRequest = `/submission/${submissionId}/userDetail`;

  mock.onGet(apiRequest).reply(400);

  return props.children({ page });
};

const loaderComponent = (
  <LoaderStateData>
    {data => (
      <MemoryRouter initialEntries={[`/?submissionId=${submissionId}`]}>
        <Home page={data.page} />
      </MemoryRouter>
    )}
  </LoaderStateData>
);

export const Loader = () => loaderComponent;

export const LoaderMobile = () => loaderComponent;

const mobileViewport = {
  parameters: {
    viewport: {
      defaultViewport: "mobile2"
    }
  }
};

LoaderMobile.story = mobileViewport;
