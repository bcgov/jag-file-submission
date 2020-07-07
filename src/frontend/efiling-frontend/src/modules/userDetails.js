const userDetails = {
  bceid: "bobross42",
  firstName: "Bob",
  middleName: "Norman",
  lastName: "Ross",
  email: "bob.ross@example.com",
  accounts: [
    {
      type: "CSO"
    }
  ]
};

export function getUserDetails() {
  return userDetails;
}
