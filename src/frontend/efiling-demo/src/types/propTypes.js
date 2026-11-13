import PropTypes from "prop-types";

export const propTypes = {
  header: PropTypes.shape({
    name: PropTypes.string.isRequired,
    navigate: PropTypes.func.isRequired,
  }).isRequired,
};
