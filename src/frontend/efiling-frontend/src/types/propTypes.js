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
      body: PropTypes.func,
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
    bceid: PropTypes.string,
    firstName: PropTypes.string.isRequired,
    middleName: PropTypes.string,
    lastName: PropTypes.string.isRequired,
    email: PropTypes.string,
  }).isRequired,
  setState: PropTypes.func.isRequired,
  file: PropTypes.shape({
    description: PropTypes.string.isRequired,
    documentProperties: PropTypes.shape({
      name: PropTypes.string.isRequired,
      type: PropTypes.string.isRequired,
    }),
    mimeType: PropTypes.string.isRequired,
    statutoryFeeAmount: PropTypes.number,
  }),
};
