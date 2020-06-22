import React from "react";
import PropTypes from "prop-types";
import axios from "axios";
import Header, { Footer } from "shared-components";
import { Button } from "../../base/button/Button";

import "../page.css";

const urlBodyWithCSO = {
  userId: "77da92db-0791-491e-8c58-1a969e67d2fa",
  documentProperties: {
    type: "string",
    subType: "string",
    submissionAccess: {
      url: "string",
      verb: "GET",
      headers: {
        additionalProp1: "string",
        additionalProp2: "string",
        additionalProp3: "string"
      }
    }
  },
  navigation: {
    success: {
      url: "string"
    },
    error: {
      url: "string"
    },
    cancel: {
      url: "string"
    }
  }
};

const urlBodyWithoutCSO = {
  ...urlBodyWithCSO,
  userId: "77da92db-0791-491e-8c58-1a969e67d2f4"
};

export const generateUrl = urlBody => {
  axios
    .post(`/submission/generateUrl`, urlBody)
    .then(({ data: { efilingUrl } }) => {
      window.open(efilingUrl, "_self");
    })
    .catch(() => {
      throw new Error(
        "An error occurred with generating the url. Please try again."
      );
    });
};

export default function Home({ page: { header } }) {
  return (
    <main>
      <Header header={header} />
      <div className="page">
        <div className="content col-md-10">
          <Button
            onClick={() => generateUrl(urlBodyWithCSO)}
            label="With CSO Account"
          />
          <br />
          <Button
            onClick={() => generateUrl(urlBodyWithoutCSO)}
            label="Without CSO Account"
          />
        </div>
      </div>
      <Footer />
    </main>
  );
}

Home.propTypes = {
  page: PropTypes.shape({
    header: PropTypes.shape({
      name: PropTypes.string.isRequired
    }).isRequired
  }).isRequired
};
