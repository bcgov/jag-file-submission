import axios from "axios";

export const getCountries = () => axios.get(`/countries`);
