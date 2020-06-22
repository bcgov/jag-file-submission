import React from "react";
import { action } from "@storybook/addon-actions";
import { Input } from "./Input";

const input = {
  label: "Submission ID",
  id: "textInputId"
};

const actionData = {
  onChange: action("onChange")
};

export default {
  title: "Input",
  component: Input
};

export const EditableWhite = () => (
  <Input
    input={{
      ...input,
      styling: "editable_white",
      isRequired: true,
      placeholder: "Enter id"
    }}
    {...actionData}
  />
);

export const EditableWhiteMobile = () => (
  <Input
    input={{
      ...input,
      styling: "editable_white",
      placeholder: "Enter id",
      isRequired: true
    }}
    {...actionData}
  />
);

EditableWhiteMobile.story = {
  parameters: {
    viewport: {
      defaultViewport: "mobile2"
    }
  }
};
