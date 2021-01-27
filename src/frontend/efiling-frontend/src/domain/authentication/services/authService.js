// import { decodeJWT } from "../../../modules/helpers/authentication-helper/authenticationHelper";
// import {
//   KEYCLOAK,
//   BCSC_USERINFO_URL,
// } from "../../../components/hoc/AuthenticationGuard";

/**
 * Using the current OIDC token, request a broker token directly from Keycloak
 */
export const getKeycloakBrokerToken = () => {
  // const oidcToken = localStorage.getItem("jwt");

  // const response = await fetch(
  //   `${KEYCLOAK.url}/realms/${KEYCLOAK.realm}/broker/bcsc/token`,
  //   {
  //     method: "GET",
  //     headers: new Headers({
  //       "Content-type": "application/json",
  //       Authorization: `Bearer ${oidcToken}`,
  //     }),
  //   }
  // );
  // if (!response.ok) {
  //   throw new Error(`HTTP error, status: ${response.status}`);
  // }
  // return response.json().then((data) => data.access_token);

  console.log("whynot");

  return "yeah";
};

/**
 * Using the brokerToken, fetch the userinfo JWT object from bcsc
 */
export const getBCSCUserInfoJWT = (brokerToken) => {
  console.log(brokerToken);

  // const response = await fetch(`${BCSC_USERINFO_URL}`, {
  //   method: "GET",
  //   headers: new Headers({
  //     Authorization: `Bearer ${brokerToken}`,
  //   }),
  // });
  // if (!response.ok) {
  //   throw new Error(`HTTP error, status: ${response.status}`);
  // }

  console.log("whynot");

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
  // const brokerToken = await getKeycloakBrokerToken();
  // const bcscToken = await getBCSCUserInfoJWT(brokerToken);

  // const userInfo = decodeJWT(bcscToken);
  // const firstName = userInfo.given_name ? userInfo.given_name : "";
  // let middleName = userInfo.given_names ? userInfo.given_names : "";
  // middleName = middleName.replace(firstName, "").trim();
  // const lastName = userInfo.family_name ? userInfo.family_name : "";

  console.log("whynot");

  return { one: "yeah", 2: "yo", 3: "eheheh" };
};
