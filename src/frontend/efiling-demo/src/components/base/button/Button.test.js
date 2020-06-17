import React from "react";
import testBasicSnapshot from "../../../TestHelper";

import { Button } from "./Button";

describe("Button Component", () => {
  const onClick = jest.fn();
  const label = "button label";

  test("Button matches the snapshot", () => {
    const buttonComponent = <Button onClick={onClick} label={label} />;

    testBasicSnapshot(buttonComponent);
  });
});
