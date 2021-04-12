import React from "react";
import { useHistory } from "react-router-dom";
import { Header, Footer } from "shared-components";
import "./App.css";

function App() {
  const header = {
    name: "AI Reviewer Admin Client",
    history: useHistory(),
  };

  return (
    <main>
      <Header header={header} />
      Hello World!
      <Footer />
    </main>
  );
}

export default App;
