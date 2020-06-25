import React from "react";
import { createMemoryHistory } from "history";
import { Router } from "react-router-dom";
import { render, fireEvent, getAllByRole } from "@testing-library/react";
import testBasicSnapshot from "../TestHelper";
import Header, { HeadingTitle, HeaderImage } from "./Header";

describe("Header Component", () => {
  const header = {
    name: "File Submission"
  };
  const history = createMemoryHistory();

  const headerComponent = (
    <Router history={history}>
      <Header header={header} />
    </Router>
  );

  test("Header matches the snapshot", () => {
    testBasicSnapshot(headerComponent);
  });

  test("HeadingTitle matches the snapshot", () => {
    const headingTitle = HeadingTitle(history, "navbar-brand pointer");

    testBasicSnapshot(headingTitle);
  });

  test("HeaderImage matches the snapshot", () => {
    const headingImage = HeaderImage(
      "img-fluid d-none d-md-block",
      181,
      "bcid-logo-rev-en.svg"
    );

    testBasicSnapshot(headingImage);
  });

  test("Clicking HeadingTitle takes you back to home", () => {
    history.location.pathname = "/somepageroute";

    const { container } = render(headerComponent);

    fireEvent.click(getAllByRole(container, "button")[0]);

    expect(history.location.pathname).toEqual("/");
  });

  test("Keydown on HeadingTitle takes you back to home", () => {
    history.location.pathname = "/somepageroute";

    const { container } = render(headerComponent);

    fireEvent.keyDown(getAllByRole(container, "button")[0]);

    expect(history.location.pathname).toEqual("/");
  });
});
