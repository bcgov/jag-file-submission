import React from "react";
import queryString from "query-string";
import {
  Switch,
  Route,
  Redirect,
  useHistory,
  useLocation,
} from "react-router-dom";
import { Header, Footer } from "shared-components";
import AuthenticationGuard from "./domain/authentication/AuthenticationGuard";
import Home from "./components/page/home/Home";
import PackageReview from "./domain/package/package-review/PackageReview";
import SubmissionHistory from "./domain/submission/submission-history/SubmissionHistory";

export default function App() {
  const location = useLocation();
  const queryParams = queryString.parse(location.search);
  const {
    submissionId,
    transactionId,
    responseCode,
    customerCode,
  } = queryParams;

  if (responseCode === "19" || responseCode === "17")
    sessionStorage.setItem("bamboraErrorExists", true);
  if (responseCode === "1") {
    sessionStorage.setItem("bamboraSuccess", customerCode);
    sessionStorage.setItem("bamboraErrorExists", false);
  }

  if (typeof customerCode === "undefined")
    sessionStorage.removeItem("isBamboraRedirect");

  if (submissionId && transactionId) {
    sessionStorage.setItem("submissionId", submissionId);
    sessionStorage.setItem("transactionId", transactionId);
  }

  const header = {
    name: "E-File Submission",
    history: useHistory(),
  };

  return (
    <main>
      <Header header={header} />
      <AuthenticationGuard>
        <Switch>
          <Redirect exact from="/" to="/efilinghub" />
          <Route exact path="/efilinghub">
            <Home />
          </Route>
          <Route path="/efilinghub/packagereview/:packageId">
            <PackageReview />
          </Route>
          <Route path="/efilinghub/submissionhistory">
            <SubmissionHistory />
          </Route>
        </Switch>
      </AuthenticationGuard>
      <Footer />
    </main>
  );
}
