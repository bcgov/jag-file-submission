import React from "react";
import PropTypes from "prop-types";
import axios from "axios";
import Header, { Footer } from "shared-components";
import { Button } from "../../base/button/Button";

import "../page.css";

const generateUrlBody = {
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

export const generateUrl = () => {
  axios
    .post(`/submission/generateUrl`, generateUrlBody)
    .then(response => {
      console.log(response);
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
          <Button onClick={generateUrl} label="With CSO Account" />
          <br />
          <Button onClick={generateUrl} label="Without CSO Account" />
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
