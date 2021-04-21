import React from "react";
import { render, screen } from "@testing-library/react";
import App from "./App";

describe("App test suite", () => {

  jest.mock("react-router-dom", () => ({
    useHistory: () => ({
      push: jest.fn(),
    }),
  }));

  test("renders App", () => {
    render(<App />);
    const titleInHeader = screen.getByText(/AI Reviewer Admin Client/i);
    expect(titleInHeader).toBeInTheDocument();
  });

})

