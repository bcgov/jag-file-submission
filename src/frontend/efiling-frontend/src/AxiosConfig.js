import axios from "axios";
import { NEXT_PUBLIC_API_BASE_URL } from "./EnvConfig";

const api = axios.create({
  baseURL: `${NEXT_PUBLIC_API_BASE_URL}`,
  timeout: 750000,
});

api.interceptors.request.use((request) => {
  const token = localStorage.getItem("jwt");
  request.headers.Authorization = `Bearer ${token}`;
  return request;
});

export default api;
