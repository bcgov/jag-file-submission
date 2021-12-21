import axios from "axios";

export const getCountries = () => axios.get("/countries");

export const submitRush = async (submissionId, rushPackage) => {
  const res = await axios.post(
    `submission/${submissionId}/rushProcessing`,
    rushPackage
  );
  return res;
};

export const submitRushDocuments = async (submissionId, files) => {
  const res = await axios.post(
    `submission/${submissionId}/rushDocuments`,
    files,
    {
      headers: { "Content-Type": "multipart/form-data" },
    }
  );
  return res;
};
