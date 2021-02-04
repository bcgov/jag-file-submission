import axios from "axios";
import FileSaver from "file-saver";
import {
  getFilingPackage,
  getSubmissionSheet,
  getSubmittedDocument,
} from "./PackageReviewService";
import { getCourtData } from "../../../modules/test-data/courtTestData";

jest.mock("axios");
FileSaver.saveAs = jest.fn();

describe("PackageReviewService TestSuite", () => {
  const packageId = "1";
  const document = {
    identifier: "1",
    type: "AFF",
    name: "test-document.pdf",
    status: {
      description: "Submitted",
    },
  };
  const courtData = getCourtData();
  const submittedDate = new Date("2021-01-14T18:57:43.602Z").toISOString();
  const submittedBy = { firstName: "Han", lastName: "Solo" };
  const filingComments =
    "Lorem ipsum dolor sit amet.<script>alert('Hi');</script>\n\nDuis aute irure dolor.";
  const filingPackagesURL = `/filingpackages/${packageId}`;
  const submissionSheetURL = `/filingpackages/${packageId}/submissionSheet`;
  const submittedDocumentURL = `/filingpackages/${packageId}/document/${document.identifier}`;
  const data = {
    packageNumber: packageId,
    court: courtData,
    submittedBy,
    submittedDate,
    filingComments,
  };
  global.URL.createObjectURL = jest.fn();
  global.URL.createObjectURL.mockReturnValueOnce("SubmissionSheet.pdf");

  beforeEach(() => {
    axios.get.mockReset();
    FileSaver.saveAs.mockReset();
  });

  test("getFilingPackage success", async () => {
    axios.get.mockImplementationOnce(() => Promise.resolve(data));
    await expect(getFilingPackage(packageId)).resolves.toEqual(data);
    expect(axios.get).toHaveBeenCalledWith(filingPackagesURL);
  });

  test("getFilingPackage fail", async () => {
    const errorMessage = "Network Error";
    axios.get.mockImplementationOnce(() =>
      Promise.reject(new Error(errorMessage))
    );
    await expect(getFilingPackage(packageId)).rejects.toThrow(errorMessage);
  });

  test("getSubmissionSheet success", async () => {
    axios.get.mockImplementationOnce(() =>
      Promise.resolve({ status: 200, data: "blob_data" })
    );

    await expect(getSubmissionSheet(packageId)).resolves;
    expect(axios.get).toHaveBeenCalledWith(submissionSheetURL, {
      responseType: "blob",
    });
    expect(FileSaver.saveAs).toHaveBeenCalled();
  });

  test("getSubmissionSheet fail", async () => {
    const errorMessage = "Network Error";
    axios.get.mockImplementationOnce(() =>
      Promise.reject(new Error(errorMessage))
    );
    await expect(getSubmissionSheet(packageId)).rejects.toThrow(errorMessage);
    expect(FileSaver.saveAs).not.toHaveBeenCalled();
  });

  test("getSubmittedDocument success", async () => {
    axios.get.mockImplementationOnce(() =>
      Promise.resolve({ status: 200, data: "blob_data" })
    );

    await expect(getSubmittedDocument(packageId, document)).resolves;
    expect(axios.get).toHaveBeenCalledWith(submittedDocumentURL, {
      responseType: "blob",
    });
    expect(FileSaver.saveAs).toHaveBeenCalled();
  });
});
