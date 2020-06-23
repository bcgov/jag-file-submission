import React from "react";
import { Input } from "./Input";
import testBasicSnapshot from "../TestHelper";

describe("Input", () => {
  const input = {
    styling: "editable_white",
    id: "textInputId"
  };

  const onChange = jest.fn();

  test("Input field when mandatory and with label matches the snapshot", () => {
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

  test("Input field when not mandatory and with label matches the snapshot", () => {
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

  test("Input field when mandatory and no label matches the snapshot", () => {
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

  test("Input field when not mandatory and no label matches the snapshot", () => {
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
});
