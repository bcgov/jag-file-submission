import React, { useState } from "react";
import PropTypes from "prop-types";
import axios from "axios";
import { Header, Footer, Input, Button } from "shared-components";
import { propTypes } from "../../../types/propTypes";

import "../page.css";

const urlBody = {
  clientApplication: {
    displayName: "Demo App",
    type: "app"
  },
  package: {
    court: {
      location: "string",
      level: "string",
      class: "string",
      division: "string",
      fileNumber: "string",
      participatingClass: "string"
    },
    documents: [
      {
        name: "string",
        description: "string",
        type: "string"
      }
    ]
  },
  navigation: {
    success: {
      url: `${window.location.origin}/efiling-demo/success`
    },
    error: {
      url: `${window.location.origin}/efiling-demo/error`
    },
    cancel: {
      url: `${window.location.origin}/efiling-demo/cancel`
    }
  }
};

const input = {
  label: "Account GUID",
  id: "textInputId",
  styling: "editable-white",
  isRequired: true,
  placeholder: "77da92db-0791-491e-8c58-1a969e67d2fa"
};

const generateUrl = (accountGuid, setErrorExists) => {
  axios
    .post(`/submission/generateUrl`, urlBody, {
      headers: { "X-Auth-UserId": accountGuid }
    })
    .then(({ data: { efilingUrl } }) => {
      window.open(efilingUrl, "_self");
    })
    .catch(() => {
      setErrorExists(true);
    });
};

export default function Home({ page: { header } }) {
  const [errorExists, setErrorExists] = useState(false);
  const [accountGuid, setAccountGuid] = useState(null);

  return (
    <main>
      <Header header={header} />
      <div className="page">
        <div className="content col-md-12">
          <Input input={input} onChange={setAccountGuid} />
          <br />
          <Button
            onClick={() => generateUrl(accountGuid, setErrorExists)}
            label="Generate URL"
            styling="normal-blue btn"
          />
          <br />
          {errorExists && (
            <p className="error">
              An error occurred while generating the URL. Please try again.
            </p>
          )}
        </div>
      </div>
      <Footer />
    </main>
  );
}

Home.propTypes = {
  page: PropTypes.shape({
    header: propTypes.header
  }).isRequired
};
