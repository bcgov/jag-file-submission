import PropTypes from "prop-types";

export const propTypes = {
  header: PropTypes.shape({
    name: PropTypes.string.isRequired,
    history: PropTypes.func.isRequired,
  }).isRequired,
};
