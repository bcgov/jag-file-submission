import React from "react";
import { Switch, Route, useHistory, useLocation } from "react-router-dom";
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
    history: useHistory(),
  };

  return (
    <div>
      <Switch>
        <Route exact path="/">
          <AuthenticationGuard page={{ header }} />
        </Route>
        <Route exact path="/cancel">
          <Cancel page={{ header }} />
        </Route>
        <Route exact path="/error">
          <Error page={{ header, status, message }} />
        </Route>
        <Route exact path="/success">
          <Success page={{ header, packageRef }} />
        </Route>
        <Route exact path="/updatecard">
          <UpdateCreditCard page={{ header }} />
        </Route>
      </Switch>
    </div>
  );
}
