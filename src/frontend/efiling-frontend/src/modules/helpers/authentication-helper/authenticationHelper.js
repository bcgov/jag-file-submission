const jwt = require("jsonwebtoken");

/**
 * Encodes/signs a payload with JWT into a JSON Web Token string.
 * @param payload Payload to sign, could be an literal, buffer or string
 */
export function encodeJWT(payload) {
  return jwt.sign(payload, "secret");
}

/**
 * Decodes a JWT token, returning the payload, or null if no payload can be found.
 * @param token a JWT token string to decode
 */
export function decodeJWT(token) {
  const decoded = jwt.decode(token, { complete: true });

  if (decoded && decoded.payload) return decoded.payload;
  return null;
}

export function getJWTData() {
  const token = localStorage.getItem("jwt");

  // get the decoded payload and header
  return decodeJWT(token);
}

export function generateJWTToken(payload) {
  return jwt.sign(payload, "secret");
}

/**
 * Returns the identity provider of the current user.
 */
export function getIdentityProviderAlias() {
  const token = getJWTData();
  return token.identityProviderAlias;
}
