const jwt = require("jsonwebtoken");

export function getJWTData() {
  const token = localStorage.getItem("jwt");
  if (!token) return false;

  // get the decoded payload and header
  const decoded = jwt.decode(token, { complete: true });
  return decoded.payload;
}

export function generateJWTToken(payload) {
  return jwt.sign(payload, "something");
}
