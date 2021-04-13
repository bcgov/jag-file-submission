/* eslint-disable react/jsx-one-expression-per-line */
import React from "react";
import { useHistory } from "react-router-dom";
import { Header, Footer } from "shared-components";
import DocumentTypeEditor from "domain/documents/DocumentTypeEditor";
import "./App.scss";

function App() {
  const header = {
    name: "AI Reviewer Admin Client",
    history: useHistory(),
  };

  return (
    <main>
      <Header header={header} />
      <DocumentTypeEditor />
      <Footer />
    </main>
  );
}

export default App;
