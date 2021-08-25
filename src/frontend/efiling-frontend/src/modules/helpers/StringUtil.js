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

/**
 * Formats a phone number to be "555-555-5555". Also removes any non-digit characters passed in
 * @param {*} phoneNumber a string containing any characters
 * @returns formatted phone number
 */
export const formatPhoneNumber = (phoneNumber) => {
  // Case for empty or null value
  if (!phoneNumber) {
    return phoneNumber;
  }

  const phoneNumberDigits = phoneNumber.replace(/[^\d]/g, "");

  if (phoneNumberDigits.length < 4) {
    return phoneNumberDigits;
  }

  if (phoneNumberDigits.length < 7) {
    return `${phoneNumberDigits.slice(0, 3)}-${phoneNumberDigits.slice(3)}`;
  }

  return `${phoneNumberDigits.slice(0, 3)}-${phoneNumberDigits.slice(
    3,
    6
  )}-${phoneNumberDigits.slice(6, 10)}`;
};
