import React from "react";
import PropTypes from "prop-types";

const Toast = ({ content, setShow, colorClass }) => (
  <div className={`alert alert-dismissible fade show ${colorClass}`} role="alert">
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
  colorClass: PropTypes.string
};

Toast.defaultProps = {
  colorClass: "alert-danger"
}
