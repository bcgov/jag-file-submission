const elements = [
  { name: "BCeID", value: "bobross42" },
  {
    name: "Last Name",
    value: "Ross"
  },
  { name: "First Name", value: "Bob" },
  { name: "Email Address", value: "bob.ross@example.com" }
];

const table = {
  heading: "BCeID Info",
  elements
};

export function getTableTestData() {
  return table;
}
