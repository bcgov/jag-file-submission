import { decodeJWT } from "../../modules/helpers/authentication-helper/authenticationHelper";
import { KEYCLOAK, BCSC_USERINFO_URL } from "./AuthenticationGuard";

/**
 * Using the current OIDC token, request a broker token directly from Keycloak
 */
export async function getKeycloakBrokerToken() {
  const oidcToken = localStorage.getItem("jwt");

  const response = await fetch(
    `${KEYCLOAK.url}/realms/${KEYCLOAK.realm}/broker/bcsc/token`,
    {
      method: "GET",
      headers: new Headers({
        "Content-type": "application/json",
        Authorization: `Bearer ${oidcToken}`,
      }),
    }
  );
  if (!response.ok) {
    throw new Error(`HTTP error, status: ${response.status}`);
  }
  return response.json().then((data) => data.access_token);
}

/**
 * Using the brokerToken, fetch the userinfo JWT object from bcsc
 */
export async function getBCSCUserInfoJWT(brokerToken) {
  const response = await fetch(`${BCSC_USERINFO_URL}`, {
    method: "GET",
    headers: new Headers({
      Authorization: `Bearer ${brokerToken}`,
    }),
  });
  if (!response.ok) {
    throw new Error(`HTTP error, status: ${response.status}`);
  }
  return response.text().then((data) => data);
}

/**
 * Retieves a userInfo object directly from BC Services:
 * {
 *   id: string,
 *   givenNames: string,
 *   lastName: string
 * }
 */
export async function getBCSCUserInfo() {
  const brokerToken = await getKeycloakBrokerToken();
  const bcscToken = await getBCSCUserInfoJWT(brokerToken);

  const userInfo = decodeJWT(bcscToken);
  const givenNames = userInfo.given_names ? userInfo.given_names : "";
  const lastName = userInfo.family_name ? userInfo.family_name : "";

  return { givenNames, lastName };
}
