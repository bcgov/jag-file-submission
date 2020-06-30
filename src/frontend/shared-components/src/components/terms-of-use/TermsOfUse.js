import React from "react";
import PropTypes from "prop-types";
import { FaPrint } from "react-icons/fa";
import { Button } from "../button/Button";
import { propTypes } from "../../types/propTypes";

import "./TermsOfUse.css";

export default function TermsOfUse({
  onScroll,
  acceptTerms,
  continueButton,
  cancelButton,
  content,
  heading,
  confirmText
}) {
  return (
    <div>
      <div style={{ width: "100%" }}>
        <span role="button" className="print-page print" tabIndex={0}>
          Print
          <FaPrint style={{ marginLeft: "10px" }} />
        </span>

        <h1>{heading}</h1>
      </div>

      <section className="scroll-box" onScroll={onScroll}>
        {content}
      </section>

      <section className="pt-2">
        <label htmlFor="acceptTerms">
          <input id="acceptTerms" type="checkbox" onClick={acceptTerms} />{" "}
          <b>{confirmText}</b>
          <span id="asterisk" className="mandatory">
            *
          </span>
        </label>
      </section>

      <section className="buttons pt-4">
        <Button
          label={cancelButton.label}
          onClick={cancelButton.onClick}
          styling={cancelButton.styling}
        />
        <Button
          label={continueButton.label}
          onClick={continueButton.onClick}
          styling={continueButton.styling}
        />
      </section>
    </div>
  );
}

TermsOfUse.propTypes = {
  onScroll: PropTypes.func.isRequired,
  acceptTerms: PropTypes.func.isRequired,
  continueButton: propTypes.button,
  cancelButton: propTypes.button,
  content: PropTypes.object.isRequired,
  heading: PropTypes.string.isRequired,
  confirmText: PropTypes.string.isRequired
};
