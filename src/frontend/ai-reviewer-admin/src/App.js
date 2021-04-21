/* eslint-disable react/jsx-one-expression-per-line */
import React from "react";
import { useHistory, Switch, Route, BrowserRouter as Router } from "react-router-dom";
import { Header, Footer } from "shared-components";
import Container from '@material-ui/core/Container';
import NavDrawer from "components/nav-drawer/NavDrawer";
import DocumentTypeEditor from "domain/documents/DocumentTypeEditor";
import "./App.scss";

function App() {
  const header = {
    name: "AI Reviewer Admin Client",
    history: useHistory(),
  };

  return (
    <>
        <Router>
        <div className="fixed-top header-div">
          <Header header={header}/>
        </div>

        <Container className="content">
          <NavDrawer variant="permanent" />
          <NavDrawer variant="temporary" />

          <Switch>
            <Route exact path="/">
            <DocumentTypeEditor />
            </Route>
          </Switch>
        </Container>

        <div className="header-div">
          <Footer />
        </div>
      </Router>
    </>
  );
}

export default App;
