import React from "react";
import testBasicSnapshot from "../../TestHelper";

import { Loader } from "./Loader";

describe("Loader Component", () => {
  test("Button version matches the snapshot", () => {
    testBasicSnapshot(<Loader />);
  });

  test("Page version matches the snapshot", () => {
    testBasicSnapshot(<Loader page />);
  });
});
