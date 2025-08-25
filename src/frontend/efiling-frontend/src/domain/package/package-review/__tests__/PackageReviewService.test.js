import MockAdapter from "axios-mock-adapter";
import FileSaver from "file-saver";
import api from "../../../../AxiosConfig";
import {
  getFilingPackage,
  downloadSubmissionSheet,
  downloadSubmittedDocument,
  withdrawSubmittedDocument,
} from "../PackageReviewService";
import { getCourtData } from "../../../../modules/test-data/courtTestData";

FileSaver.saveAs = jest.fn();

describe("PackageReviewService TestSuite", () => {
  const packageId = "1";
  const document = {
    identifier: "1",
    documentProperties: {
      type: "AFF",
      name: "test-document.pdf",
    },
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

  let mockApi;
  beforeEach(() => {
    mockApi = new MockAdapter(api);
    FileSaver.saveAs.mockReset();
  });

  test("getFilingPackage success", async () => {
    mockApi.onGet(filingPackagesURL).reply(200, data);
    const response = await getFilingPackage(packageId);
    expect(response.data).toMatchObject(data);
  });

  test("getFilingPackage fail", async () => {
    mockApi.onGet(filingPackagesURL).reply(404);
    expect(getFilingPackage(packageId)).rejects.toThrowError();
  });

  test("downloadSubmissionSheet success", async () => {
    mockApi.onGet(submissionSheetURL).reply(200, "blob_data");
    await downloadSubmissionSheet(packageId);
    expect(FileSaver.saveAs).toHaveBeenCalled();
  });

  test("downloadSubmissionSheet fail", async () => {
    mockApi.onGet(submissionSheetURL).reply(404);
    expect(downloadSubmissionSheet(packageId)).rejects.toThrowError();
    expect(FileSaver.saveAs).not.toHaveBeenCalled();
  });

  test("downloadSubmittedDocument success", async () => {
    mockApi.onGet(submittedDocumentURL).reply(200, "blob_data");

    await downloadSubmittedDocument(packageId, document);
    expect(FileSaver.saveAs).toHaveBeenCalled();
  });

  test("withdrawSubmittedDocument success", async () => {
    mockApi.onDelete(submittedDocumentURL).reply(200);
    const response = await withdrawSubmittedDocument(packageId, document);
    expect(response.status).toEqual(200);
  });

  test("withdrawSubmittedDocument fail", async () => {
    mockApi.onDelete(submittedDocumentURL).reply(404);

    expect(
      withdrawSubmittedDocument(packageId, document)
    ).rejects.toThrowError();
  });
});
