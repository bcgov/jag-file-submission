import React from "react";
import { Switch, Route, Redirect, useHistory } from "react-router-dom";
import Home from "./components/page/home/Home";

export default function App() {
  const header = {
    name: "eFiling Demo Client",
    history: useHistory()
  };

  return (
    <div>
      <Switch>
        <Redirect exact from="/" to="/efiling" />
        <Route exact path="/efiling">
          <Home page={{ header }} />
        </Route>
      </Switch>
    </div>
  );
}
