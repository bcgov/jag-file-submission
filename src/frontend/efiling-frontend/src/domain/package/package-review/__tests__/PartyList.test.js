import React from "react";
import { render, waitFor } from "@testing-library/react";
import PartyList from "../PartyList";

describe("PartyList Component", () => {
  beforeEach(() => {});

  test("Matches snapshot", () => {
    const parties = [
      {
        partyType: "IND",
        roleType: "APP",
        firstName: "Bob",
        middleName: "Q",
        lastName: "Ross",
        roleDescription: "Applicant",
        partyDescription: "Individual",
      },
    ];
    const { asFragment } = render(<PartyList parties={parties} />);
    waitFor(() => {});

    expect(asFragment()).toMatchSnapshot();
  });
});
