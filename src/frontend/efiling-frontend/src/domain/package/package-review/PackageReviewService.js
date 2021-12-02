import FileSaver from "file-saver";
import api from "../../../AxiosConfig";

/**
 * Retrieves the Filing Package json object from the backend API for the given packageId.
 *
 * @param packageId id associated with the filing package
 */
export const getFilingPackage = (packageId) =>
  api.get(`/filingpackages/${packageId}`);

/**
 * Retrieves the Submission Sheet from the backend API for the given packageId.
 *
 * @param packageId id associated with the filing package
 */
export const downloadSubmissionSheet = async (packageId) => {
  const response = await api.get(
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
  const response = await api.get(
    `/filingpackages/${packageId}/document/${document.identifier}`,
    {
      responseType: "blob",
    }
  );
  const fileData = new Blob([response.data], { type: "application/pdf" });
  const fileUrl = URL.createObjectURL(fileData);
  FileSaver.saveAs(fileUrl, `${document.documentProperties.name}`);
};

/**
 * Withdraws a Submitted Document from the backend API for the given packageId and documentId (sets the status to Withdrawn).
 *
 * @param packageId id associated with the filing package
 * @param document the document to retrieve. Shape must be {identifier: string}
 */
export const withdrawSubmittedDocument = async (packageId, document) =>
  api.delete(`/filingpackages/${packageId}/document/${document.identifier}`);

export const downloadRegistryNotice = async (packageId) => {
  const response = await api.get(
    `/filingpackages/${packageId}/registryNotice`,
    {
      responseType: "blob",
    }
  );
  const fileData = new Blob([response.data], { type: "application/pdf" });
  const fileUrl = URL.createObjectURL(fileData);
  FileSaver.saveAs(fileUrl, "RegistryNotice.pdf");
};

/**
 * Retrieves the Rush Document from the backend API for the given packageId and fileName.
 *
 * @param {*} packageId id associated with the filing package.
 * @param {*} fileName name associated with the document.
 */
export const downloadRushDocument = async (packageId, fileName) => {
  const response = await api.get(
    `/filingpackages/${packageId}/rushDocument/${fileName}`,
    {
      responseType: "blob",
    }
  );
  const fileData = new Blob([response.data], { type: "application/pdf" });
  const fileUrl = URL.createObjectURL(fileData);
  FileSaver.saveAs(fileUrl, fileName);
};
