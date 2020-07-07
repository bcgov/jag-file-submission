export function translateApplicantInfo({
  bceID,
  firstName,
  middleName,
  lastName,
  emailAddress
}) {
  const fullName = [firstName, middleName, lastName];
  return [
    {
      name: "BCeID:",
      value: bceID
    },
    {
      name: "Full Name:",
      value: fullName.join(" ")
    },
    {
      name: "Email Address:",
      value: emailAddress
    }
  ];
}
