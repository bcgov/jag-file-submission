import React from "react";
import { fireEvent, render } from "@testing-library/react";

import RushDocumentList from "./RushDocumentList";
import { getDocumentsData } from "../../../../modules/test-data/documentTestData";

describe("RushConfirmation Component", () => {
  const documents = getDocumentsData();

  test("Matches the snapshot", () => {
    const { asFragment } = render(<RushDocumentList files={documents} />);

    expect(asFragment()).toMatchSnapshot();
  });

  test("Download button is clickable", () => {
    // TODO: test real functionality when implemented
    // eslint-disable-next-line no-console
    console.log = jest.fn();
    const { getByText } = render(<RushDocumentList files={documents} />);

    const fileDownloadButton = getByText(documents[0].name);
    fireEvent.click(fileDownloadButton);

    // eslint-disable-next-line no-console
    expect(console.log.mock.calls[0][0]).toBe("download");
  });
});
