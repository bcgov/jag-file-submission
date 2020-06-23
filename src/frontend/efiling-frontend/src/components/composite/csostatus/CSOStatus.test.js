import React from "react";
import testBasicSnapshot from "../../../TestHelper";

import CSOStatus from "./CSOStatus";

describe("CSOStatus Component", () => {
  test("Matches the snapshot when account exists", () => {
    testBasicSnapshot(<CSOStatus accountExists />);
  });

  test("Matches the snapshot when account does not exist", () => {
    testBasicSnapshot(<CSOStatus accountExists={false} />);
  });
});
