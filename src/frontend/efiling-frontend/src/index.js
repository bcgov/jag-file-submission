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

const bamboraRedirectUrl = window.env
  ? window.env.REACT_APP_BAMBORA_REDIRECT_URL
  : process.env.REACT_APP_BAMBORA_REDIRECT_URL;

sessionStorage.setItem("bamboraRedirectUrl", bamboraRedirectUrl);

// prevent user from leaving site and losing saved data
window.addEventListener("beforeunload", (e) => {
  if (sessionStorage.getItem("validExit")) return false;

  const confirmationMessage = "";
  (e || window.event).returnValue = confirmationMessage;
  return confirmationMessage;
});

window.addEventListener("unload", () => {
  sessionStorage.removeItem("listenerExists");
  sessionStorage.removeItem("validExit");
});

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
