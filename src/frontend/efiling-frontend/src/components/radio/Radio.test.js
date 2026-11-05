import React from "react";
import { render, fireEvent, getByRole } from "@testing-library/react";
import { Radio } from "./Radio";
import testBasicSnapshot from "../../TestHelper";

describe("Radio Component", () => {
  const id = "yes";
  const name = "yes";
  const label = "Yes";
  const onSelect = jest.fn();

  test("matches the snapshot", () => {
    testBasicSnapshot(
      <Radio id={id} name={name} label={label} onSelect={onSelect} />
    );
  });

  test("on change updates the selected value", () => {
    const { container } = render(
      <Radio id={id} name={name} label={label} onSelect={onSelect} />
    );

    fireEvent.click(getByRole(container, "radio"));

    expect(onSelect).toHaveBeenCalledWith(id);
  });
});
