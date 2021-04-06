import axios from "axios";

export const getSubmissionHistory = () => axios.get("/filingpackages");
