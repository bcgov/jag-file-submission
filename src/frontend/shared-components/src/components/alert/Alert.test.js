import React from "react";

import testBasicSnapshot from "../../TestHelper";
import { Alert } from "./Alert";

describe("Alert Component", () => {
  test("Matches the 'success' snapshot", () => {
    testBasicSnapshot(
      <Alert type="success" element="This is a success message!" />
    );
  });

  test("Matches the 'warning' snapshot", () => {
    testBasicSnapshot(
      <Alert type="warning" element="This is a warning message!" />
    );
  });

  test("Matches the 'danger' snapshot", () => {
    testBasicSnapshot(
      <Alert type="danger" element="This is a danger message!" />
    );
  });
});
