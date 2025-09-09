/* eslint-disable react/function-component-definition */
import React from "react";
import PropTypes from "prop-types";
import { UncontrolledTooltip } from "reactstrap";
import "./Tooltip.scss";

export default function BcGovTooltip({ content, styling, target }) {
  return (
    <div className={`bcgov-withdraw-tooltip ${styling}`}>
      <UncontrolledTooltip
        placement="right"
        target={target}
        className="ct-tooltip"
      >
        {content}
      </UncontrolledTooltip>
    </div>
  );
}

BcGovTooltip.propTypes = {
  content: PropTypes.element.isRequired,
  target: PropTypes.string.isRequired,
  styling: PropTypes.string,
};

BcGovTooltip.defaultProps = {
  styling: "",
};
