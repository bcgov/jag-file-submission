import axios from "axios";
import FileSaver from "file-saver";

/**
 * Retrieves the Filing Package json object from the backend API for the given packageId.
 *
 * @param packageId id associated with the filing package
 */
export const getFilingPackage = (packageId) =>
  axios.get(`/filingpackages/${packageId}`);

/**
 * Retrieves the Submission Sheet from the backend API for the given packageId.
 *
 * @param packageId id associated with the filing package
 */
export const getSubmissionSheet = async (packageId) => {
  const response = await axios.get(
    `/filingpackages/${packageId}/submissionSheet`,
    {
      responseType: "blob",
    }
  );
  const fileData = new Blob([response.data], { type: "application/pdf" });
  const fileUrl = URL.createObjectURL(fileData);
  FileSaver.saveAs(fileUrl, "SubmissionSheet.pdf");
};

/**
 * Retrieves the Submitted Document from the backend API for the given packageId and documentId.
 *
 * @param packageId id associated with the filing package
 * @param document the document to retrieve. Shape must be {identifier: string, name: string}
 */
export const downloadSubmittedDocument = async (packageId, document) => {
  const response = await axios.get(
    `/filingpackages/${packageId}/document/${document.identifier}`,
    {
      responseType: "blob",
    }
  );
  const fileData = new Blob([response.data], { type: "application/pdf" });
  const fileUrl = URL.createObjectURL(fileData);
  FileSaver.saveAs(fileUrl, `${document.name}`);
};
