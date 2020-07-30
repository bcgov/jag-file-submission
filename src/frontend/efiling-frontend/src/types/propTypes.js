import PropTypes from "prop-types";

export const propTypes = {
  header: PropTypes.shape({
    name: PropTypes.string.isRequired,
    history: PropTypes.object.isRequired,
  }).isRequired,
  confirmationPopup: PropTypes.shape({
    modal: PropTypes.shape({
      show: PropTypes.bool.isRequired,
      title: PropTypes.string.isRequired,
      body: PropTypes.func.isRequired,
    }).isRequired,
    mainButton: PropTypes.shape({
      label: PropTypes.string.isRequired,
      styling: PropTypes.string.isRequired,
      onClick: PropTypes.func.isRequired,
    }).isRequired,
    confirmButton: PropTypes.shape({
      label: PropTypes.string.isRequired,
      styling: PropTypes.string.isRequired,
      onClick: PropTypes.func.isRequired,
    }).isRequired,
    cancelButton: PropTypes.shape({
      label: PropTypes.string.isRequired,
      styling: PropTypes.string.isRequired,
      onClick: PropTypes.func.isRequired,
    }).isRequired,
  }).isRequired,
  applicantInfo: PropTypes.shape({
    bceid: PropTypes.string.isRequired,
    name: PropTypes.string.isRequired,
    email: PropTypes.string.isRequired,
  }).isRequired,
  setState: PropTypes.func.isRequired,
};
