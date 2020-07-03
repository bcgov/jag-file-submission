import React from "react";
import { Input } from "./Input";
import testBasicSnapshot from "../../TestHelper";

describe("Input", () => {
  const input = {
    styling: "editable-white",
    id: "textInputId",
    isReadOnly: false
  };

  const onChange = jest.fn();

  test("field when mandatory and with label matches the snapshot (editable white)", () => {
    const inputElement = (
      <Input
        input={{
          ...input,
          isRequired: true,
          placeholder: "Enter id",
          label: "Submission ID"
        }}
        onChange={onChange}
      />
    );

    testBasicSnapshot(inputElement);
  });

  test("field when not mandatory and with label matches the snapshot (editable white)", () => {
    const inputElement = (
      <Input
        input={{
          ...input,
          isRequired: false,
          placeholder: "Enter id",
          label: "Submission ID"
        }}
        onChange={onChange}
      />
    );

    testBasicSnapshot(inputElement);
  });

  test("field when mandatory and no label matches the snapshot (editable white)", () => {
    const inputElement = (
      <Input
        input={{
          ...input,
          isRequired: true,
          placeholder: "Enter id"
        }}
        onChange={onChange}
      />
    );

    testBasicSnapshot(inputElement);
  });

  test("field when not mandatory and no label matches the snapshot (editable white)", () => {
    const inputElement = (
      <Input
        input={{
          ...input,
          isRequired: false,
          placeholder: "Enter id"
        }}
        onChange={onChange}
      />
    );

    testBasicSnapshot(inputElement);
  });

  test("field matches the snapshot (non-editable-grey)", () => {
    const inputElement = (
      <Input
        input={{
          ...input,
          isRequired: false,
          isReadOnly: true,
          placeholder: "Enter id"
        }}
        onChange={onChange}
      />
    );

    testBasicSnapshot(inputElement);
  });
});
