import "react-app-polyfill/ie11";
import "react-app-polyfill/stable";
import React from "react";
import axios from "axios";
import ReactDOM from "react-dom";
import { BrowserRouter } from "react-router-dom";
import "./index.css";
import "@bcgov/bootstrap-theme/dist/css/bootstrap-theme.min.css";
import App from "./App";
import * as serviceWorker from "./serviceWorker";

axios.defaults.baseURL = window.env
  ? window.env.REACT_APP_API_BASE_URL
  : process.env.REACT_APP_API_BASE_URL;

ReactDOM.render(
  <BrowserRouter>
    <App />
  </BrowserRouter>,
  document.getElementById("root")
);

// If you want your app to work offline and load faster, you can change
// unregister() to register() below. Note this comes with some pitfalls.
// Learn more about service workers: https://bit.ly/CRA-PWA
serviceWorker.unregister();
