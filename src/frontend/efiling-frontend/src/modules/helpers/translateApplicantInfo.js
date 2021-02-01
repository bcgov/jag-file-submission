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

  if (isIdentityProviderBCeID()) {
    return [
      {
        name: "BCeID:",
        value: bceid,
      },
      {
        name: "Full Name:",
        value: fullName,
      },
      {
        name: "Email Address:",
        value: email,
      },
    ];
  }
  return [
    {
      name: "BCSC:",
      value: null,
    },
    {
      name: "Full Name:",
      value: fullName,
    },
  ];
}
