/**
 * Formats a name to be "lastName, firstName middleName"
 * @param firstName if specified, the given name of an individual
 * @param middleName if specified, the middle name of an individual
 * @param lastName if specified, the family name of an individual
 */
export const formatFullName = ({ firstName, middleName, lastName }) => {
  let fullName = lastName || "";
  if (firstName || middleName) {
    if (fullName.length > 0) {
      fullName += ",";
    }
    if (firstName) {
      fullName += " " + firstName;
    }
    if (middleName) {
      fullName += " " + middleName;
    }
  }
  return fullName.trim();
};
