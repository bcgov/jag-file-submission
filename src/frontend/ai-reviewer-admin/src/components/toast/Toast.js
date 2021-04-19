import React from "react";
import PropTypes from "prop-types";

const Toast = ({ content, setShow }) => (
  <div className="alert alert-danger alert-dismissible fade show" role="alert">
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
export default Toast;

Toast.propTypes = {
  content: PropTypes.string.isRequired,
  setShow: PropTypes.func.isRequired,
};
