/* eslint-disable react/function-component-definition */
import "./clientInit";
import React from "react";
import PropTypes from "prop-types";
import queryString from "query-string";
import { useRouter } from "next/router";
import { Header, Footer } from "shared-components";
import AuthenticationGuard from "./domain/authentication/AuthenticationGuard";

/**
 * Layout shared by every page (formerly App.js): header, keycloak guard, footer,
 * plus the bambora redirect bookkeeping that used to run on every route render.
 * Client-only: loaded through next/dynamic with ssr disabled.
 */
export default function AppShell({ children }) {
  const router = useRouter();

  // window.location (not router.query) on purpose: router.query is empty until
  // the router is ready, which would wrongly clear isBamboraRedirect below.
  const queryParams = queryString.parse(window.location.search);
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
    // shared-components' Header calls header.history.push when the logo is clicked
    history: { push: router.push.bind(router) },
  };

  return (
    <main>
      <Header header={header} />
      <AuthenticationGuard>{children}</AuthenticationGuard>
      <Footer />
    </main>
  );
}

AppShell.propTypes = {
  children: PropTypes.node.isRequired,
};
