import React from "react";
import { Switch, Route, Redirect } from "react-router-dom";
import Home from "./components/page/home/Home";

const header = {
  name: "eFiling Demo Client"
};

export default function App() {
  return (
    <div>
      <Switch>
        <Redirect exact from="/" to="/efiling-demo" />
        <Route exact path="/efiling-demo">
          <Home page={{ header }} />
        </Route>
      </Switch>
    </div>
  );
}
