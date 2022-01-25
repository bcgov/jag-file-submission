import React from "react";
import PropTypes from "prop-types";

export const Toast = function ({ content, setShow, testId }) {
  return (
    <div
      className="alert alert-danger alert-dismissible fade show"
      role="alert"
      data-testId={testId}
    >
      {content}
      <button
        type="button"
        data-testid="toast-close"
        className="close"
        data-dismiss="alert"
        aria-label="Close"
        onClick={() => setShow(false)}
      >
        <span aria-hidden="true">&times;</span>
      </button>
    </div>
  );
};

Toast.propTypes = {
  content: PropTypes.string.isRequired,
  setShow: PropTypes.func.isRequired,
  testId: PropTypes.string,
};

Toast.defaultProps = {
  testId: "",
};
