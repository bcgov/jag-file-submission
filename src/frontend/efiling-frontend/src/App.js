/* eslint-disable react/jsx-one-expression-per-line */
import React from "react";
import queryString from "query-string";
import {
  Switch,
  Route,
  Redirect,
  useHistory,
  useLocation,
} from "react-router-dom";
import AuthenticationGuard from "./components/hoc/AuthenticationGuard";

export default function App() {
  const location = useLocation();
  const queryParams = queryString.parse(location.search);
  const { submissionId, transactionId } = queryParams;

  const header = {
    name: "E-File Submission",
    history: useHistory(),
  };

  return (
    <div>
      <Switch>
        <Redirect exact from="/" to="/efiling" />
        <Route exact path="/efiling">
          <AuthenticationGuard page={{ header, submissionId, transactionId }} />
        </Route>
      </Switch>
    </div>
  );
}
