import React from "react";
import { render, waitFor } from "@testing-library/react";
import PaymentList from "../PaymentList";

describe("PartyList Component", () => {
  beforeEach(() => {});

  test("Matches snapshot", () => {
    const payments = [
      
    ];
    const { asFragment } = render(<PaymentList payments={payments} />);
    waitFor(() => {});

    expect(asFragment()).toMatchSnapshot();
  });
});