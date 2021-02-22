import { isIdentityProviderBCeID } from "./authentication-helper/authenticationHelper";

export function translateApplicantInfo({
  bceid,
  lastName,
  firstName,
  middleName,
  email,
}) {
  const fullName = middleName
    ? `${firstName} ${middleName} ${lastName}`
    : `${firstName} ${lastName}`;
  const isBCeID = isIdentityProviderBCeID();

  const data = [];
  if (isBCeID) {
    data.push({
      name: "BCeID:",
      value: bceid,
    });
  } else {
    data.push({
      name: "BCSC:",
      value: "",
    });
  }

  data.push({
    name: "Full Name:",
    value: fullName,
  });

  // if BCSC, don't show missing email, we'll show input fields instead.
  if (!isBCeID && email) {
    data.push({
      name: "Email Address:",
      value: email,
    });
  }
  return data;
}
