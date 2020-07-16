import React from "react";
import {
  Switch,
  Route,
  Redirect,
  useHistory,
  useLocation
} from "react-router-dom";
import queryString from "query-string";
import Home from "./components/page/home/Home";
import Cancel from "./components/page/cancel/Cancel";
import Error from "./components/page/error/Error";
import Success from "./components/page/success/Success";

export default function App() {
  const location = useLocation();
  const queryParams = queryString.parse(location.search);

  const { status, message } = queryParams;

  const header = {
    name: "eFiling Demo Client",
    history: useHistory()
  };

  return (
    <div>
      <Switch>
        <Redirect exact from="/" to="/efiling-demo" />
        <Route exact path="/efiling-demo">
          <Home page={{ header }} />
        </Route>
        <Route exact path="/efiling-demo/cancel">
          <Cancel page={{ header }} />
        </Route>
        <Route exact path="/efiling-demo/error">
          <Error page={{ header, status, message }} />
        </Route>
        <Route exact path="/efiling-demo/success">
          <Success page={{ header }} />
        </Route>
      </Switch>
    </div>
  );
}
