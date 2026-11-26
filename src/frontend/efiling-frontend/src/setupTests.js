// jest-dom adds custom jest matchers for asserting on DOM nodes.
// allows you to do things like:
// expect(element).toHaveTextContent(/react/i)
// learn more: https://github.com/testing-library/jest-dom
import "@testing-library/jest-dom/extend-expect";
import { configure } from "enzyme";
import Adapter from "enzyme-adapter-react-16";
import "regenerator-runtime/runtime";
import MockAdapter from "axios-mock-adapter";

configure({ adapter: new Adapter() });

// to address Error: Not implemented: HTMLCanvasElement.prototype.getContext
window.HTMLCanvasElement.prototype.getContext = () => {};

// axios >= 0.34 hands adapters a request config whose `headers` object has a
// null prototype. axios-mock-adapter 1.x reads `config.headers.constructor.name`,
// which throws a TypeError, so every mocked request rejected before replying.
// Give the adapter a plain-object copy of the headers until the mock adapter
// handles null-prototype headers itself.
const createMockAdapter = MockAdapter.prototype.adapter;
MockAdapter.prototype.adapter = function adapterWithPlainHeaders(...args) {
  const mockAdapter = createMockAdapter.apply(this, args);
  return (config) => {
    if (config.headers && Object.getPrototypeOf(config.headers) === null) {
      // eslint-disable-next-line no-param-reassign
      config.headers = { ...config.headers };
    }
    return mockAdapter(config);
  };
};
