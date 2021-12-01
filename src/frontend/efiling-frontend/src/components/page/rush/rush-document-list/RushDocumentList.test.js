import React from "react";
import { fireEvent, render } from "@testing-library/react";

import RushDocumentList from "./RushDocumentList";
import { getDocumentsData } from "../../../../modules/test-data/documentTestData";

describe("RushConfirmation Component", () => {
  const documents = getDocumentsData();

  test("Matches the snapshot", () => {
    const onDeleteFile = jest.fn();

    const { asFragment } = render(
      <RushDocumentList files={documents} onDeleteFile={onDeleteFile} />
    );

    expect(asFragment()).toMatchSnapshot();
  });

  test("Download button is clickable", () => {
    // TODO: test real functionality when implemented
    // eslint-disable-next-line no-console
    console.log = jest.fn();
    const onDeleteFile = jest.fn();
    const { getByText } = render(
      <RushDocumentList files={documents} onDeleteFile={onDeleteFile} />
    );

    const fileDownloadButton = getByText(documents[0].name);
    fireEvent.click(fileDownloadButton);

    // eslint-disable-next-line no-console
    expect(console.log.mock.calls[0][0]).toBe("download");
  });

  test("Remove link onClick", () => {
    const setFiles = jest.fn();
    const { getAllByText } = render(
      <RushDocumentList files={documents} setFiles={setFiles} />
    );

    const links = getAllByText("Remove");
    expect(links.length).toBeGreaterThan(0);

    fireEvent.click(links[0]);
    expect(setFiles).toBeCalled();
  });

  test("Remove link onKeyDown Enter", () => {
    const setFiles = jest.fn();
    const { getAllByText } = render(
      <RushDocumentList files={documents} setFiles={setFiles} />
    );

    const links = getAllByText("Remove");
    expect(links.length).toBeGreaterThan(0);

    fireEvent.keyDown(links[0], {
      key: "Enter",
      keyCode: "13",
    });
    expect(setFiles).toBeCalled();
  });

  test("Remove link onKeyDown Tab", () => {
    const onDeleteFile = jest.fn();
    const { getAllByText } = render(
      <RushDocumentList files={documents} onDeleteFile={onDeleteFile} />
    );

    const links = getAllByText("Remove");
    expect(links.length).toBeGreaterThan(0);

    fireEvent.keyDown(links[0], {
      key: "Tab",
      keyCode: "9",
    });
    expect(onDeleteFile).not.toBeCalled();
  });
});
