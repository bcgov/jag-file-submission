import React from "react";
import { Router } from "react-router-dom";
import { createMemoryHistory } from "history";
import testBasicSnapshot from "../../../TestHelper";

import Header, { HeadingTitle, HeaderImage } from "./Header";

describe("Header Component", () => {
  const header = {
    name: "File Submission"
  };

  test("Header matches the snapshot", () => {
    const history = createMemoryHistory();

    const headerComponent = (
      <Router history={history}>
        <Header header={header} />
      </Router>
    );

    testBasicSnapshot(headerComponent);
  });

  test("HeadingTitle matches the snapshot", () => {
    const history = createMemoryHistory();

    const headingTitle = HeadingTitle(history, "navbar-brand pointer");

    testBasicSnapshot(headingTitle);
  });

  test("HeaderImage matches the snapshot", () => {
    const history = createMemoryHistory();

    const headingImage = HeaderImage(
      "img-fluid d-none d-md-block",
      181,
      "bcid-logo-rev-en.svg"
    );

    testBasicSnapshot(headingImage);
  });
});
