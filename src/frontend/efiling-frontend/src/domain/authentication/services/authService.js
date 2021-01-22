import { decodeJWT } from "../../../modules/helpers/authentication-helper/authenticationHelper";
import {
  KEYCLOAK,
  BCSC_USERINFO_URL,
} from "../../../components/hoc/AuthenticationGuard";

/**
 * Using the current OIDC token, request a broker token directly from Keycloak
 */
async function getKeycloakBrokerToken() {
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
  return response
    .json()
    .then((data) => data.access_token)
    .catch(() => {
      throw new Error("Error obtaining Keycloak broker token.");
    });
}

/**
 * Using the brokerToken, fetch the userinfo JWT object from bcsc
 */
async function getBCSCUserInfoJWT(brokerToken) {
  const response = await fetch(`${BCSC_USERINFO_URL}`, {
    method: "GET",
    headers: new Headers({
      Authorization: `Bearer ${brokerToken}`,
    }),
  });
  if (!response.ok) {
    throw new Error(
      `HTTP error, status: ${response.status}, type: ${response.type}`
    );
  }
  return response
    .text()
    .then((data) => data)
    .catch(() => {
      throw new Error("Could not retreive user information from BCSC.");
    });
}

/**
 * Retieves a userInfo object directly from BC Services:
 * {
 *   id: string,
 *   firstName: string,
 *   middleName: string,
 *   lastName: string
 * }
 */
export async function getBCSCUserInfo() {
  const brokerToken = await getKeycloakBrokerToken();
  const bcscToken = await getBCSCUserInfoJWT(brokerToken);

  const userInfo = decodeJWT(bcscToken);
  const id = userInfo.sub;
  const firstName = userInfo.given_name;
  let middleName = userInfo.given_names;
  middleName = middleName.replace(firstName, "").trim();
  const lastName = userInfo.family_name;

  return {
    id,
    firstName,
    middleName,
    lastName,
  };
}
