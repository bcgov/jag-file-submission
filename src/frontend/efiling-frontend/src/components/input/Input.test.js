import React from "react";
import { render, fireEvent, getByRole } from "@testing-library/react";
import { Input } from "./Input";
import testBasicSnapshot from "../../TestHelper";

describe("Input Component", () => {
  const input = {
    styling: "bcgov-editable-white",
    id: "textInputId",
    isReadOnly: false,
  };

  const onChange = jest.fn();

  test("when mandatory and with label matches the snapshot (editable white)", () => {
    const inputElement = (
      <Input
        input={{
          ...input,
          isRequired: true,
          placeholder: "Enter id",
          label: "Submission ID",
        }}
        onChange={onChange}
      />
    );

    testBasicSnapshot(inputElement);
  });

  test("when not mandatory and with label matches the snapshot (editable white)", () => {
    const inputElement = (
      <Input
        input={{
          ...input,
          isRequired: false,
          placeholder: "Enter id",
          label: "Submission ID",
        }}
        onChange={onChange}
      />
    );

    testBasicSnapshot(inputElement);
  });

  test("when mandatory and no label matches the snapshot (editable white)", () => {
    const inputElement = (
      <Input
        input={{
          ...input,
          isRequired: true,
          placeholder: "Enter id",
        }}
        onChange={onChange}
      />
    );

    testBasicSnapshot(inputElement);
  });

  test("when not mandatory and no label matches the snapshot (editable white)", () => {
    const inputElement = (
      <Input
        input={{
          ...input,
          isRequired: false,
          placeholder: "Enter id",
        }}
        onChange={onChange}
      />
    );

    testBasicSnapshot(inputElement);
  });

  test("matches the snapshot (non-editable-grey)", () => {
    const inputElement = (
      <Input
        input={{
          ...input,
          isRequired: false,
          isReadOnly: true,
          placeholder: "Enter id",
        }}
        onChange={onChange}
      />
    );

    testBasicSnapshot(inputElement);
  });

  test("on change updates the value in the input field", () => {
    const { container } = render(
      <Input
        input={{
          ...input,
          isRequired: false,
          placeholder: "Enter id",
        }}
        onChange={onChange}
      />
    );

    fireEvent.change(getByRole(container, "textbox"), {
      target: { value: "examplevalue" },
    });

    expect(getByRole(container, "textbox").value).toBe("examplevalue");
  });
});
