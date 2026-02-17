import { addDecorator } from "@storybook/react";
import React, { createElement } from "react";
import "@bcgov/bootstrap-theme/dist/css/bootstrap-theme.min.css";

// Added as workaround for storyshots breaking with stories that declare hooks
addDecorator(createElement);
