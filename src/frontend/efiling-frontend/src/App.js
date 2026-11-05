/* eslint-disable react/function-component-definition, import/no-named-as-default, import/no-named-as-default-member */

import React from "react";
import queryString from "query-string";
import {
  Routes,
  Route,
  useNavigate,
  useLocation,
  Navigate,
} from "react-router-dom";
import { Header, Footer } from "shared-components";
import AuthenticationGuard from "./domain/authentication/AuthenticationGuard";
import Home from "./components/page/home/Home";
import PackageReview from "./domain/package/package-review/PackageReview";
import SubmissionHistory from "./domain/submission/submission-history/SubmissionHistory";

export default function App() {
  const location = useLocation();
  const queryParams = queryString.parse(location.search);
  const { submissionId, transactionId, responseCode, customerCode } =
    queryParams;

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
    history: useNavigate(),
  };

  return (
    <main>
      <Header header={header} />
      <AuthenticationGuard>
        <Routes>
          <Route
            path="/"
            element={<Navigate exact from="/" to="/efilinghub" />}
          />
          <Route exact path="/efilinghub" element={<Home />} />
          <Route
            path="/efilinghub/packagereview/:packageId"
            element={<PackageReview />}
          />
          <Route
            path="/efilinghub/submissionhistory"
            element={<SubmissionHistory />}
          />
        </Routes>
      </AuthenticationGuard>
      <Footer />
    </main>
  );
}
