import renderer from "react-test-renderer";

export default function testBasicSnapshot(component) {
  const model = renderer.create(component);
  const tree = model.toJSON();

  expect(tree).toMatchSnapshot();
}
