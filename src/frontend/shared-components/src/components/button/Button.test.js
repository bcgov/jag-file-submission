import React from "react";
import testBasicSnapshot from "../../TestHelper";

import { Button } from "./Button";

describe("Button Component", () => {
  const onClick = jest.fn();
  const label = "Button label";
  const styling = "normal-blue btn";

  test("Button matches the snapshot", () => {
    const buttonComponent = (
      <Button onClick={onClick} label={label} styling={styling} />
    );

    testBasicSnapshot(buttonComponent);
  });
});
