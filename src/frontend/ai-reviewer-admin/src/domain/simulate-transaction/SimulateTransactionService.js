import api from "AxiosConfig";

export const submitFileForExtraction = async (file, transactionId) => {
  const formData = new FormData();
  formData.append("files", file, file.name);

  const response = await api.post(
    "/documents/extract",
     formData,
    {headers: {"Content-Type": "multipart/form-data",
              "X-Transaction-Id": `1d4e38ba-0c88-4c92-8367-c8eada8cca19`,
              "X-Document-Type": `${file.data.type}`,
              "X-Use-Webhook": false}
    }
  )
  return response.data;
}