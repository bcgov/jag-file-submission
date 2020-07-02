import React from "react";
import {
  Switch,
  Route,
  Redirect,
  useHistory,
  useLocation
} from "react-router-dom";
import Home from "./components/page/home/Home";
import Cancel from "./components/page/cancel/Cancel";
import Error from "./components/page/error/Error";

export default function App() {
  const location = useLocation();
  const queryParams = queryString.parse(location.search);

  const error = queryParams.error;

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
          <Error page={{ header, error }} />
        </Route>
      </Switch>
    </div>
  );
}
