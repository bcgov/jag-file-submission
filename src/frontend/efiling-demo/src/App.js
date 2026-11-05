import React from "react";
import { Routes, Route, useNavigate, useLocation } from "react-router-dom";
import queryString from "query-string";
import AuthenticationGuard from "./components/hoc/AuthenticationGuard";
import Cancel from "./components/page/cancel/Cancel";
import Error from "./components/page/error/Error";
import Success from "./components/page/success/Success";
import UpdateCreditCard from "./components/page/payment/creditcard/UpdateCreditCard";

export default function App() {
  const location = useLocation();
  const queryParams = queryString.parse(location.search);

  const { status, message, packageRef } = queryParams;

  const header = {
    name: "eFiling Demo Client",
    history: useNavigate(),
  };

  return (
    <div>
      <Routes>
        <Route
          exact
          path="/"
          element={<AuthenticationGuard page={{ header }} />}
        />
        <Route exact path="/cancel" element={<Cancel page={{ header }} />} />
        <Route
          exact
          path="/error"
          element={<Error page={{ header, status, message }} />}
        />
        <Route
          exact
          path="/success"
          element={<Success page={{ header, packageRef }} />}
        />
        <Route
          exact
          path="/updatecard"
          element={<UpdateCreditCard page={{ header }} />}
        />
      </Routes>
    </div>
  );
}
