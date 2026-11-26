import renderer from "react-test-renderer";
import { setImmediate as realSetImmediate } from "timers";
import { act } from "@testing-library/react";

export default function testBasicSnapshot(component) {
  const model = renderer.create(component);
  const tree = model.toJSON();

  expect(tree).toMatchSnapshot();
}

// Lets pending promises (mocked axios responses, effects) settle before the next query.
// Uses the real setImmediate from "timers" so it also works under jest.useFakeTimers().
// Replaces the old `await waitFor(() => {})` idiom, which no longer waits with React 18.
export const flushPromises = () =>
  act(async () => {
    await new Promise((resolve) => realSetImmediate(resolve));
  });
