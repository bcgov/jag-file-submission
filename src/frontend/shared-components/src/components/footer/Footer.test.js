import React from "react";
import testBasicSnapshot from "../../TestHelper";

import { Footer, LinkElement } from "./Footer";

describe("Footer Component", () => {
  test("Matches the snapshot", () => {
    testBasicSnapshot(<Footer />);
  });

  test("Link element renders the link and matches the snapshot", () => {
    const linkElement = LinkElement("example.com", "Example");

    testBasicSnapshot(linkElement);
  });
});
