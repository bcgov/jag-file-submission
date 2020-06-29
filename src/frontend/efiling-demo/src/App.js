import React from "react";
import { Switch, Route, Redirect, useHistory } from "react-router-dom";
import Home from "./components/page/home/Home";
import Cancel from "./components/page/cancel/Cancel";

export default function App() {
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
      </Switch>
    </div>
  );
}
