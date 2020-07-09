import React from "react";
import testBasicSnapshot from "../../TestHelper";

import { Button } from "./Button";

describe("Button Component", () => {
  const onClick = jest.fn();
  const label = "Button label";

  test("Matches the 'primary' snapshot", () => {
    const buttonComponent = (
      <Button onClick={onClick} label={label} styling="normal-blue btn" />
    );

    testBasicSnapshot(buttonComponent);
  });

  test("Matches the 'secondary' snapshot", () => {
    const buttonComponent = (
      <Button onClick={onClick} label={label} styling="normal-white btn" />
    );

    testBasicSnapshot(buttonComponent);
  });

  test("Matches the 'disabled' snapshot", () => {
    const buttonComponent = (
      <Button
        onClick={onClick}
        label={label}
        styling="normal-blue btn"
        disabled
      />
    );

    testBasicSnapshot(buttonComponent);
  });
});
