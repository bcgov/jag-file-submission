import React from "react";
import { render, fireEvent, getByRole } from "@testing-library/react";
import { Textarea } from "./Textarea";
import testBasicSnapshot from "../../TestHelper";

describe("Textarea Component", () => {
  const label = "some label";
  const onChange = jest.fn();

  test("with label matches the snapshot", () => {
    const textArea = <Textarea id="1" label={label} onChange={onChange} />;

    testBasicSnapshot(textArea);
  });

  test("without label matches the snapshot", () => {
    const textArea = <Textarea id="1" onChange={onChange} />;

    testBasicSnapshot(textArea);
  });

  test("on change updates the value in the textarea", () => {
    const { container } = render(
      <Textarea id="1" label={label} onChange={onChange} />
    );

    fireEvent.change(getByRole(container, "textbox"), {
      target: { value: "examplevalue" }
    });

    expect(getByRole(container, "textbox").value).toBe("examplevalue");
  });
});
