import axios from "axios";

export const getSubmissionHistory = (applicationCode = "") => {
  if (applicationCode) {
    return axios.get(`/filingpackages`, {
      params: { parentApplication: applicationCode },
    });
  }
  return axios.get(`/filingpackages`);
};
