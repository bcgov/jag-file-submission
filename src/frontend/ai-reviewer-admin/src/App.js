/* eslint-disable react/jsx-one-expression-per-line */
import React from "react";
import { useHistory, Switch, Route, Redirect } from "react-router-dom";
import { Header, Footer } from "shared-components";
import Container from '@material-ui/core/Container';
import DocumentTypeEditor from "domain/documents/DocumentTypeEditor";
import "./App.scss";

function App() {
  const header = {
    name: "AI Reviewer Admin Client",
    history: useHistory(),
  };

  return (
    <>
      <Header header={header} />
      <Container>
        <DocumentTypeEditor />
      </Container>
      <Footer />
    </>
  );
}

export default App;
