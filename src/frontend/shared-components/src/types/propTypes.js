import PropTypes from "prop-types";

export const propTypes = {
  button: PropTypes.shape({
    label: PropTypes.string.isRequired,
    styling: PropTypes.string.isRequired,
    onClick: PropTypes.func.isRequired,
    disabled: PropTypes.bool
  }).isRequired
};
