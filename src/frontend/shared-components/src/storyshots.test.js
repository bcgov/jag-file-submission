import initStoryshots, {
  Stories2SnapsConverter
} from "@storybook/addon-storyshots";
import { wait } from "@testing-library/react";
import { mount } from "enzyme";
import { createSerializer } from "enzyme-to-json";

const converter = new Stories2SnapsConverter();

const runTest = async (story, context) => {
  const filename = converter.getSnapshotFileName(context);

  if (!filename) {
    return;
  }

  const storyElement = story.render();

  const asFragment = mount(storyElement);

  await wait(() => {
    if (
      filename ===
      "components/confirmation-popup/__snapshots__/ConfirmationPopup.stories.storyshot"
    ) {
      setTimeout(() => {
        expect(asFragment).toMatchSpecificSnapshot(filename);
      }, 2000);
    } else {
      expect(asFragment).toMatchSpecificSnapshot(filename);
    }
  });
};

initStoryshots({
  asyncJest: true,
  snapshotSerializers: [createSerializer()],
  test: ({ story, context, done }) => {
    runTest(story, context).then(done);
  }
});
