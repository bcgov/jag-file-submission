import initStoryshots from "@storybook/addon-storyshots";
import { mount } from "enzyme";
import { createSerializer } from "enzyme-to-json";

initStoryshots({
  /* configuration options */
  // renderer: mount,
  // snapshotSerializers: [createSerializer()]
  storyKindRegex: /^((?!.*?ConfirmationPopup).)*$/
});
