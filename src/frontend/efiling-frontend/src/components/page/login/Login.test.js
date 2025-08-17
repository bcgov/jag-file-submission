import React from "react";
import { render, waitFor } from "@testing-library/react";

import Login from "./Login";

const keycloak = jest.fn();

describe("Login Component", () => {
  test("BCeID button tries to log in with BCeID", async () => {
    const { asFragment } = render(<Login keycloak={keycloak} />);

    await waitFor(() => {});

    expect(asFragment()).toMatchSnapshot();
  });
});
