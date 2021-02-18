import React from "react";
import { render, waitFor } from "@testing-library/react";
import PaymentList from "../PaymentList";

describe("PartyList Component", () => {
  beforeEach(() => {});

  test("Matches snapshot", () => {
    const payments = [
      {
        feeExempt: false,
        paymentCategory: 1,
        processedAmount: 7,
        submittedAmount: 7,
        serviceIdentifier: 0,
        transactionDate: null
      },
      {
        feeExempt: true,
        paymentCategory: 3,
        processedAmount: 7,
        submittedAmount: null,
        serviceIdentifier: 0,
        transactionDate: null
      },
      {
        feeExempt: false,
        paymentCategory: 3,
        processedAmount: 7,
        submittedAmount: 7,
        serviceIdentifier: 0,
        transactionDate: null
      },
      {
        feeExempt: false,
        paymentCategory: 3,
        processedAmount: null,
        submittedAmount: 7,
        serviceIdentifier: 0,
        transactionDate: null
      }
    ];
    const { asFragment } = render(<PaymentList payments={payments} />);
    waitFor(() => {});

    expect(asFragment()).toMatchSnapshot();
  });
});