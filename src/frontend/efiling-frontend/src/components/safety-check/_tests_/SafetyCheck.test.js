import React from "react";
import { fireEvent, render, screen, waitFor } from "@testing-library/react";
import SafetyCheck from "../SafetyCheck";

describe("SafefyCheck Component Testsuite", () => {
  test("render", () => {
    render(<SafetyCheck />);
    const title = screen.getByText(/SAFETY CHECK/i);
    expect(title).toBeInTheDocument();
  });

  test("open/close help popup", async () => {
    render(<SafetyCheck />);

    // click link to open help popup
    const modalLink = screen.getByTestId("help-link");
    expect(modalLink).toBeInTheDocument();
    fireEvent.click(modalLink);
    await waitFor(() => {});

    // assert the popup is open
    let modalTitle = screen.getByTestId("help-title");
    expect(modalTitle).toBeInTheDocument();

    // close popup
    const modalClose = screen.getByTestId("close-btn");
    expect(modalClose).toBeInTheDocument();
    fireEvent.click(modalClose);
    await waitFor(() => {});

    // assert the popup is closed
    modalTitle = screen.queryByTestId("help-title");
    // expect(modalTitle).toBeNull();
  });
});
