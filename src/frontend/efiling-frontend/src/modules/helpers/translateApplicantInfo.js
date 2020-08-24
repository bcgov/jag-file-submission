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
