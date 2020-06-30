import React from "react";

import testBasicSnapshot from "../TestHelper";
import InfoDisplay from "./InfoDisplay";

describe("InfoDisplay component", () => {
  test("Matches the snapshot", () => {
    const infoDisplayCOmponent = <InfoDisplay />;

    testBasicSnapshot(infoDisplayCOmponent);
  });
});
