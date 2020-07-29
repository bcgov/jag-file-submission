const jwt = require("jsonwebtoken");

export function getJWTData() {
  const token = localStorage.getItem("jwt");

  console.log("token is ", token);

  if (!token) return false;

  // get the decoded payload and header
  const decoded = jwt.decode(token, { complete: true });

  return decoded.payload;
}

export function generateJWTToken(payload) {
  console.log("inside generate jwt", payload);
  return jwt.sign(payload, "something");
}
