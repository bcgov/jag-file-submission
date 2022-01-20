/* eslint-disable react/function-component-definition, import/no-named-as-default, import/no-named-as-default-member, */
import React, { useState } from "react";
import { MdHelp } from "react-icons/md";
import SafetyCheckHelp from "./SafetyCheckHelp";

import "./SafetyCheck.scss";

export default function SafetyCheck() {
  const [showModal, setShowModal] = useState(false);
  const handleOnShow = () => setShowModal(true);
  const handleOnHide = () => setShowModal(false);
  return (
    <div className="alert alert-success safety-check row">
      <div className="row justify-content-center text-warning">
        <p className="bg-primary py-0 px-2 mt-2 title">SAFETY CHECK</p>
      </div>
      <p>
        By clicking on the document name, a PDF version of the application will
        download or open. Depending on your browser settings, your PDF might
        save the form to your computer or it will open in a new tab or window.
        For more information about opening and saving PDF forms, click on{" "}
        <span
          role="button"
          tabIndex={-1}
          className="file-href"
          onClick={handleOnShow}
          onKeyDown={handleOnShow}
          data-testid="help-link"
        >
          <MdHelp
            style={{
              fontSize: "20px",
              marginRight: "3px",
              verticalAlign: "text-bottom",
              color: "#38598a",
            }}
          />
          Get help opening and saving PDF forms
        </span>{" "}
        below. If you are concerned about having a copy saved to your computer,
        may want to review and print from a safe computer, tablet or device, for
        example a computer, table or device of a trusted friend, at work, a
        library, school or an internet café.
      </p>
      <SafetyCheckHelp showModal={showModal} onHide={handleOnHide} />
    </div>
  );
}
