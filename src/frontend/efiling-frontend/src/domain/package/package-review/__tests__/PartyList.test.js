import React from "react";
import { render, waitFor } from "@testing-library/react";
import PartyList from "../PartyList";
import * as packageReviewTestData from "../../../../modules/test-data/packageReviewTestData";

describe("PartyList Component", () => {
  beforeEach(() => {});

  test("Matches snapshot", () => {
    const { parties, organizationParties } = packageReviewTestData;
    const { asFragment } = render(
      <PartyList parties={parties} organizationParties={organizationParties} />
    );
    waitFor(() => {});

    expect(asFragment()).toMatchSnapshot();
  });
});
