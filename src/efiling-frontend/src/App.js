import React from "react";
import { Switch, Route, Redirect } from "react-router-dom";

export default function App() {
  return (
    <div>
      <Switch>
        <Redirect exact from="/" to="/efiling" />
        <Route exact path="/efiling">
          <p>Testing</p>
        </Route>
      </Switch>
    </div>
  );
}
