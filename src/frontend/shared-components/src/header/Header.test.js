import React from "react";
import { createMemoryHistory } from "history";
import testBasicSnapshot from "../TestHelper";
import { render, fireEvent, getAllByRole } from "@testing-library/react";

import Header, { HeadingTitle, HeaderImage } from "./Header";

describe("Header Component", () => {
  const header = {
    name: "File Submission",
    history: createMemoryHistory()
  };

  test("Header matches the snapshot", () => {
    const headerComponent = <Header header={header} />;

    testBasicSnapshot(headerComponent);
  });

  test("HeadingTitle matches the snapshot", () => {
    const headingTitle = HeadingTitle(header.history, "navbar-brand pointer");

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
    header.history.location.pathname = "/somepageroute";

    const { container } = render(<Header header={header} />);

    fireEvent.click(getAllByRole(container, "button")[0]);

    expect(header.history.location.pathname).toEqual("/");
  });
});
