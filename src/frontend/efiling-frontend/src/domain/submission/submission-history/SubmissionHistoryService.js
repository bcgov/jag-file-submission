import axios from "axios";

export const getSubmissionHistory = (applicationCode = "") => {
  //Temporary fix for issue introduced
  const token = localStorage.getItem("jwt");

  if (applicationCode) {

    return axios.get("/filingpackages", {
      params: { parentApplication: applicationCode },
      headers: {
        Authorization: `Bearer ${token}`,
      },
    });
  }
  return axios.get("/filingpackages", {
    headers: {
      Authorization: `Bearer ${token}`,
    },
  });
};
