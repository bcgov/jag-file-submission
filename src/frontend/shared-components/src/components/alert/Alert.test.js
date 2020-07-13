import React from "react";
import { MdError, MdCancel, MdCheckBox, MdInfo } from "react-icons/md";

import testBasicSnapshot from "../../TestHelper";
import { Alert } from "./Alert";

describe("Alert Component", () => {
  test("Matches the 'success' snapshot", () => {
    testBasicSnapshot(
      <Alert
        icon={<MdCheckBox size={32} />}
        type="success"
        styling="success-background"
        element="This is a success message!"
      />
    );
  });

  test("Matches the 'warning' snapshot", () => {
    testBasicSnapshot(
      <Alert
        styling="warning-background"
        type="warning"
        element="This is a warning message!"
        icon={<MdError size={32} />}
      />
    );
  });

  test("Matches the 'error' snapshot", () => {
    testBasicSnapshot(
      <Alert
        type="error"
        element="This is an error message!"
        icon={<MdCancel size={32} />}
        styling="error-background"
      />
    );
  });

  test("Matches the 'info' snapshot", () => {
    testBasicSnapshot(
      <Alert
        styling="info-background"
        type="info"
        element="This is an info message!"
        icon={<MdInfo size={32} />}
      />
    );
  });
});
