const jwt = require("jsonwebtoken");

export function getJWTData() {
  const token = localStorage.getItem("jwt");

  // get the decoded payload and header
  const decoded = jwt.decode(token, { complete: true });

  if (decoded && decoded.payload) return decoded.payload;
  return null;
}

export function generateJWTToken(payload) {
  return jwt.sign(payload, "secret");
}
