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

const nonEditableGrey = (
  <Input
    input={{
      ...input,
      styling: "non-editable-grey",
      isReadOnly: true
    }}
    onChange={action("onChange")}
  />
);

const editableWhiteMandatory = (
  <Input
    input={{
      ...input,
      styling: "editable-white",
      isRequired: true
    }}
    onChange={action("onChange")}
  />
);

const editableWhiteWithLabel = (
  <Input
    input={{
      ...input,
      styling: "editable-white"
    }}
    onChange={action("onChange")}
  />
);

const editableWhiteNoLabel = (
  <Input
    input={{
      ...input,
      styling: "editable-white",
      label: ""
    }}
    onChange={action("onChange")}
  />
);

export const EditableWhiteMandatory = () => editableWhiteMandatory;

export const EditableWhiteMandatoryMobile = () => editableWhiteMandatory;

export const EditableWhiteWithLabel = () => editableWhiteWithLabel;

export const EditableWhiteWithLabelMobile = () => editableWhiteWithLabel;

export const EditableWhiteNoLabel = () => editableWhiteNoLabel;

export const EditableWhiteNoLabelMobile = () => editableWhiteNoLabel;

export const NonEditableGrey = () => nonEditableGrey;

export const NonEditableGreyMobile = () => nonEditableGrey;

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
