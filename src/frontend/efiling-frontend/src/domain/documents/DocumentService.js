import axios from "axios";
import FileSaver from "file-saver";

/**
 * Retrieves an array of document types
 */
export const getDocumentTypes = async (courtLevel, courtClassification) => {
  const params = {
    courtLevel,
    courtClassification,
  };

  const response = await axios.get("/documents/types", { params });

  return response.data;
};

/**
 * Retrieves the Uploaded Document from the backend API for the given submissionId and filename.
 *
 * @param submissionId the submissionId associated with the package
 * @param file the document to retrieve. Shape must be { mimeType: "string", documentProperties: { name: "string" } }
 */
export const downloadFileByName = async (submissionId, file) => {
  const fileName = file.documentProperties.name;
  const response = await axios.get(
    `/submission/${submissionId}/document/${fileName}`,
    {
      responseType: "blob",
    }
  );
  const fileData = new Blob([response.data], { type: file.mimeType });
  const fileUrl = URL.createObjectURL(fileData);

  FileSaver.saveAs(fileUrl, fileName);
};
