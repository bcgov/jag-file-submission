import api from "AxiosConfig";

/** Returns a Promise of a list of document configurations from Diligen. */
export const getDocumentTypeConfigurations = async (documentType) => {
  const params = { documentType };
  const response = await api.get("/documentTypeConfigurations", { params });
  if (!response.data || !response.data.map) {
    throw new Error("Unexpected response from Diligen API");
  }
  return response.data;
};

export const submitNewDocumentTypeConfigurations = async (configJson) => {
  const response = await api.post("/documentTypeConfigurations", JSON.parse(configJson));
  return response.data;
}

export const submitUpdatedDocumentTypeConfigurations = async (configJson) => {
  const response = await api.put("/documentTypeConfigurations", JSON.parse(configJson));
  return response.data;
}