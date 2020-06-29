import PropTypes from "prop-types";

export const propTypes = {
  header: PropTypes.shape({
    name: PropTypes.string.isRequired,
    history: PropTypes.object.isRequired
  }).isRequired,
  confirmationPopup: PropTypes.shape({
    modal: PropTypes.shape({
      show: PropTypes.bool.isRequired,
      handleShow: PropTypes.func.isRequired,
      handleClose: PropTypes.func.isRequired,
      handleConfirm: PropTypes.func.isRequired,
      title: PropTypes.string.isRequired,
      body: PropTypes.func.isRequired
    }).isRequired,
    mainButton: PropTypes.shape({
      mainLabel: PropTypes.string.isRequired,
      mainStyling: PropTypes.string.isRequired
    }).isRequired,
    confirmButton: PropTypes.shape({
      confirmLabel: PropTypes.string.isRequired,
      confirmStyling: PropTypes.string.isRequired
    }).isRequired,
    cancelButton: PropTypes.shape({
      cancelLabel: PropTypes.string.isRequired,
      cancelStyling: PropTypes.string.isRequired
    }).isRequired
  }).isRequired
};
