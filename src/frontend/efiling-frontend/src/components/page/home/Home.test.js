import React from "react";
import { createMemoryHistory } from "history";
import { MemoryRouter } from "react-router-dom";
import axios from "axios";
import {
  render,
  getByText,
  wait,
  fireEvent,
  getByRole
} from "@testing-library/react";
import Home from "./Home";

const MockAdapter = require("axios-mock-adapter");

const header = {
  name: "eFiling Frontend",
  history: createMemoryHistory()
};

const page = { header };

describe("Home", () => {
  const submissionId = "abc123";
  let mock;

  beforeEach(() => {
    mock = new MockAdapter(axios);
  });

  test("Component matches the snapshot when user cso account exists", async () => {
    mock
      .onGet(`/submission/${submissionId}/userDetail`)
      .reply(200, { csoAccountExists: true });

    const { asFragment } = render(
      <MemoryRouter initialEntries={[`?submissionId=${submissionId}`]}>
        <Home page={page} />
      </MemoryRouter>
    );

    await wait(() => {
      expect(asFragment()).toMatchSnapshot();
    });
  });

  test("Component matches the snapshot when user cso account does not exist", async () => {
    mock
      .onGet(`/submission/${submissionId}/userDetail`)
      .reply(200, { csoAccountExists: false });

    const { asFragment } = render(
      <MemoryRouter initialEntries={[`?submissionId=${submissionId}`]}>
        <Home page={page} />
      </MemoryRouter>
    );

    await wait(() => {
      expect(asFragment()).toMatchSnapshot();
    });
  });

  test("Component matches the snapshot when still loading", async () => {
    mock.onGet(`/submission/${submissionId}/userDetail`).reply(400);

    const { asFragment } = render(
      <MemoryRouter initialEntries={[`?submissionId=${submissionId}`]}>
        <Home page={page} />
      </MemoryRouter>
    );

    await wait(() => {
      expect(asFragment()).toMatchSnapshot();
    });
  });
});
