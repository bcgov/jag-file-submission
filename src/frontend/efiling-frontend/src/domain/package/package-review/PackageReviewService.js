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
