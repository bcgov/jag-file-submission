/* eslint-disable react/jsx-one-expression-per-line */
import React from "react";
import { useHistory, Switch, Route, Redirect } from "react-router-dom";
import { Header, Footer } from "shared-components";
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
      <div className="content">
        <DocumentTypeEditor />
      </div>
      <Footer />
    </>
  );
}

export default App;
