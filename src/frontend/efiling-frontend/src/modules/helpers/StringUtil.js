/**
 * Formats a name to be "lastName, firstName middleName"
 * @param firstName if specified, the given name of an individual
 * @param middleName if specified, the middle name of an individual
 * @param lastName if specified, the family name of an individual
 */
export const formatFullName = ({ firstName, middleName, lastName }) => {
  const fn = firstName || "";
  const mn = middleName || "";
  const ln = lastName || "";
  let fullName = `${ln}, ${fn} ${mn}`;
  fullName = fullName.replace(/^\s*,/, ""); // leading comma
  fullName = fullName.replace(/,\s*$/, ""); // trailing comma
  fullName = fullName.replace(/\s+/g, " "); // extra whitespace
  return fullName.trim();
};
