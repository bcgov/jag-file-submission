import axios from "axios";
import { REACT_APP_AI_REVIEWER_API_URL } from "EnvConfig";

const api = axios.create({
  baseURL: `${REACT_APP_AI_REVIEWER_API_URL}`,
});

export default api;
