import React from "react";
import ReactDOM from "react-dom";
import { BrowserRouter } from "react-router-dom";
import App from "./App";

ReactDOM.render(
  <BrowserRouter>
    <App />
  </BrowserRouter>,
  document.getElementById("root")
);

import ConfirmationPopup from "./confirmation-popup/ConfirmationPopup";

export { Footer } from "./footer/Footer";
export { Loader } from "./loader/Loader";
export { Input } from "./input/Input";
export { Button } from "./button/Button";
export { Header } from "./header/Header";

export default ConfirmationPopup;
