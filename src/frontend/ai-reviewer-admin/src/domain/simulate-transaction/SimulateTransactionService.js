import api from "AxiosConfig";

export const submitFileForExtraction = async (file, transactionId) => {
  const formData = new FormData();
  formData.append("file", file);

  const response = await api.post("/documents/extract", formData, {
    headers: {
      "Content-Type": "multipart/form-data",
      "X-Transaction-Id": `${transactionId}`,
      "X-Document-Type": `${file.data.type}`,
      "X-Use-Webhook": false,
    },
  });
  return response.data;
};

export const getProcessedDocument = async (documentId, transactionId) => {
  const response = await api.get(`/documents/processed/${documentId}`, {
    headers: { "X-Transaction-Id": `${transactionId}` },
  });

  return response.data;
};
