import React, { useState } from "react";
import PropTypes from "prop-types";
import axios from "axios";
import ConfirmationPopup, {
  Header,
  Footer,
  Input,
  Button
} from "shared-components";

import "../page.css";

const urlBody = {
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
      url: "http://localhost:3001/efiling-demo"
    }
  }
};

const input = {
  label: "Account GUID",
  id: "textInputId",
  styling: "editable_white",
  isRequired: true,
  placeholder: "77da92db-0791-491e-8c58-1a969e67d2fa"
};

const generateUrl = (accountGuid, setErrorExists) => {
  const updatedUrlBody = { ...urlBody, userId: accountGuid };

  axios
    .post(`/submission/generateUrl`, updatedUrlBody)
    .then(({ data: { efilingUrl } }) => {
      window.open(efilingUrl, "_self");
    })
    .catch(() => {
      setErrorExists(true);
    });
};

export default function Home({ page: { header } }) {
  //TESTING
  const [show, setShow] = useState(false);

  const handleClose = () => setShow(false);
  const handleShow = () => setShow(true);

  const modal = {
    show,
    handleShow,
    handleClose,
    handleConfirm: () => {
      console.log("confirmed");
    },
    title: "Cancel process?",
    body1: "Your process will end.",
    body2: "You will be redirected back to the original application."
  };
  const mainButton = {
    mainLabel: "main label",
    mainStyling: "normal-blue btn"
  };
  const confirmButton = {
    confirmLabel: "Yes, cancel my process please",
    confirmStyling: "normal-blue btn consistent-width"
  };
  const cancelButton = {
    cancelLabel: "No, resume my process please",
    cancelStyling: "normal-white btn consistent-width"
  };
  // END TESTING

  const [errorExists, setErrorExists] = useState(false);
  const [accountGuid, setAccountGuid] = useState(null);

  return (
    <main>
      <Header header={header} />
      <div className="page">
        <div className="content col-md-10">
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

        <ConfirmationPopup
          modal={modal}
          mainButton={mainButton}
          confirmButton={confirmButton}
          cancelButton={cancelButton}
        />
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
