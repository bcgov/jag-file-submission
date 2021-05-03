import api from "AxiosConfig";

export const submitFileForExtraction = async (file, transactionId) => {
  const formData = new FormData();
  formData.append("files", file);

  const response = await api.post(
    "/documents/extract",
     formData,
    {headers: {"Content-Type": "multipart/form-data",
              "X-Transaction-Id": `${transactionId}`,
              "X-Document-Type": `${file.data.type}`,
              "X-Use-Webhook": false}
    }
  )
  return response.data;
}