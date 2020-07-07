import React from "react";
import { action } from "@storybook/addon-actions";
import { Input } from "./Input";

const input = {
  label: "Submission ID",
  id: "textInputId",
  placeholder: "Enter id",
  isReadOnly: false,
  isRequired: false
};

export default {
  title: "Input",
  component: Input
};

export const EditableWhiteMandatory = () => (
  <Input
    input={{
      ...input,
      styling: "editable-white",
      isRequired: true
    }}
    onChange={action("onChange")}
  />
);

export const EditableWhiteMandatoryMobile = () => (
  <Input
    input={{
      ...input,
      styling: "editable-white",
      isRequired: true
    }}
    onChange={action("onChange")}
  />
);

export const EditableWhiteWithLabel = () => (
  <Input
    input={{
      ...input,
      styling: "editable-white"
    }}
    onChange={action("onChange")}
  />
);

export const EditableWhiteWithLabelMobile = () => (
  <Input
    input={{
      ...input,
      styling: "editable-white"
    }}
    onChange={action("onChange")}
  />
);

export const EditableWhiteNoLabel = () => (
  <Input
    input={{
      ...input,
      styling: "editable-white",
      label: ""
    }}
    onChange={action("onChange")}
  />
);

export const EditableWhiteNoLabelMobile = () => (
  <Input
    input={{
      ...input,
      styling: "editable-white",
      label: ""
    }}
    onChange={action("onChange")}
  />
);

export const NonEditableGrey = () => (
  <Input
    input={{
      ...input,
      styling: "non-editable-grey",
      isReadOnly: true
    }}
    onChange={action("onChange")}
  />
);

export const NonEditableGreyMobile = () => (
  <Input
    input={{
      ...input,
      styling: "non-editable-grey",
      isReadOnly: true
    }}
    onChange={action("onChange")}
  />
);

const mobileViewport = {
  parameters: {
    viewport: {
      defaultViewport: "mobile2"
    }
  }
};

EditableWhiteMandatoryMobile.story = mobileViewport;
EditableWhiteNoLabelMobile.story = mobileViewport;
EditableWhiteWithLabelMobile.story = mobileViewport;
NonEditableGreyMobile.story = mobileViewport;
