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
    expect(asFragment).toMatchSpecificSnapshot(filename);
  });
};

initStoryshots({
  asyncJest: true,
  snapshotSerializers: [createSerializer()],
  test: ({ story, context, done }) => {
    runTest(story, context).then(done);
  }
});
