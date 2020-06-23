import React from "react";
import { Input } from "./Input";
import testBasicSnapshot from "../TestHelper";

describe("Input", () => {
  const input = {
    label: "Submission ID",
    id: "textInputId"
  };

  const onChange = jest.fn();

  test("Editable white input field matches the snapshot", () => {
    const inputElement = (
      <Input
        input={{
          ...input,
          styling: "editable_white",
          isRequired: true,
          placeholder: "Enter id"
        }}
        onChange={onChange}
      />
    );

    testBasicSnapshot(inputElement);
  });
});
