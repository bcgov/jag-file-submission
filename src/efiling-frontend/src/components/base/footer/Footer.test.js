import React from "react";
import { create } from "react-test-renderer";

import Footer from "./Footer";

describe("Footer Component", () => {
  test("Matches the snapshot", () => {
    const footer = create(<Footer />);
    expect(footer.toJSON()).toMatchSnapshot();
  });
});
