import api from "AxiosConfig";

/** Returns a Promise of a list of document configurations from Diligen. */
export const getDocumentTypeConfigurations = async (documentType) => {
  const params = { documentType };
  const response = await api.get("/documentTypeConfigurations", { params });
  return response.data;
};
