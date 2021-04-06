import axios from "axios";

export const getSubmissionHistory = async (applicationCode = "") => {
  
  if (applicationCode) {
    await axios.get(`/filingpackages`, {params: {parentApplication: applicationCode}});
  } else {
    await axios.get(`/filingpackages`);
  }
}
