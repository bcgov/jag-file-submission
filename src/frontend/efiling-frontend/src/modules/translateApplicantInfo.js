export function translateApplicantInfo({
  bceid,
  firstName,
  middleName,
  lastName,
  email,
}) {
  let fullName = middleName
    ? [firstName, middleName, lastName]
    : [firstName, lastName];
  return [
    {
      name: "BCeID:",
      value: bceid,
    },
    {
      name: "Full Name:",
      value: fullName.join(" "),
    },
    {
      name: "Email Address:",
      value: email,
    },
  ];
}
