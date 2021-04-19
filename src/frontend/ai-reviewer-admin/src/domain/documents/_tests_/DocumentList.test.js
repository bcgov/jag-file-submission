import React from "react";
import { render, fireEvent } from "@testing-library/react";
import DocumentList from "domain/documents/DocumentList";
import { configurations } from "domain/documents/_tests_/test-data";

describe("DocumentList test suite", () => {
  test("Render empty configurations", () => {
    const empty = [];
    const { container } = render(<DocumentList configurations={empty} />);
    // The only <li> should be the new entry row
    expect(container.getElementsByTagName("li").length).toEqual(1);
  });
  
  test("Render populated configurations", () => {
    const { container } = render(<DocumentList configurations={configurations} />);
    // should be the new entry row + both test data rows
    expect(container.getElementsByTagName("li").length).toEqual(3);
  });
});
