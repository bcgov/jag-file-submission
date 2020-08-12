export function translateApplicantInfo({ bceid, name, email }) {
  return [
    {
      name: "BCeID:",
      value: bceid,
    },
    {
      name: "Full Name:",
      value: name,
    },
    {
      name: "Email Address:",
      value: email,
    },
  ];
}
