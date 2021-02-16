import axios from "axios";

/**
 * Retrieves an array of document types
 */
export const getDocumentTypes = async (courtLevel, courtClassification) => {
  const params = {
    courtLevel,
    courtClassification,
  };

  const response = await axios.get(`/documents/types`, { params });

  return response.data;
};
