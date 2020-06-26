import React from "react";
import { Switch, Route, Redirect } from "react-router-dom";
import Home from "./home/Home";

export default function App() {
  return (
    <div>
      <Switch>
        <Redirect exact from="/" to="/shared" />
        <Route exact path="/shared">
          <Home />
        </Route>
      </Switch>
    </div>
  );
}
