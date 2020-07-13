export function translateApplicantInfo({
  bceid,
  firstName,
  middleName,
  lastName,
  email
}) {
  const fullName = [firstName, middleName, lastName];
  return [
    {
      name: "BCeID:",
      value: bceid
    },
    {
      name: "Full Name:",
      value: fullName.join(" ")
    },
    {
      name: "Email Address:",
      value: email
    }
  ];
}
