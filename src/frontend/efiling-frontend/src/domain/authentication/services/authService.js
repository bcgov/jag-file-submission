// import { decodeJWT } from "../../../modules/helpers/authentication-helper/authenticationHelper";
// import {
//   KEYCLOAK,
//   BCSC_USERINFO_URL,
// } from "../../../components/hoc/AuthenticationGuard";

/**
 * Using the current OIDC token, request a broker token directly from Keycloak
 */
export const getKeycloakBrokerToken = () => {
  console.log("whynot");
  return "yeah";
};

/**
 * Using the brokerToken, fetch the userinfo JWT object from bcsc
 */
export const getBCSCUserInfoJWT = (brokerToken) => {
  console.log(brokerToken);
  return "yahooo";
};

/**
 * Retieves a userInfo object directly from BC Services:
 * {
 *   id: string,
 *   firstName: string,
 *   middleName: string,
 *   lastName: string
 * }
 */
export const getBCSCUserInfo = () => {
  console.log("whynot");
  return { one: "yeah", 2: "yo", 3: "eheheh" };
};
