import React, { useState } from "react";
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

const generateUrl = (urlBody, setErrorExists) => {
  axios
    .post(`/submission/generateUrl`, urlBody)
    .then(({ data: { efilingUrl } }) => {
      window.open(efilingUrl, "_self");
    })
    .catch(() => {
      setErrorExists(true);
    });
};

export default function Home({ page: { header } }) {
  const [errorExists, setErrorExists] = useState(false);

  return (
    <main>
      <Header header={header} />
      <div className="page">
        <div className="content col-md-10">
          <Button
            onClick={() => generateUrl(urlBodyWithCSO, setErrorExists)}
            label="With CSO Account"
          />
          <br />
          <Button
            onClick={() => generateUrl(urlBodyWithoutCSO, setErrorExists)}
            label="Without CSO Account"
          />
          {errorExists && (
            <p>An error occurred while generating the URL. Please try again.</p>
          )}
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
