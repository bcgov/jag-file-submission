import fetchMock from "fetch-mock-jest";
import {
  getKeycloakBrokerToken,
  getBCSCUserInfoJWT,
  getBCSCUserInfo,
} from "../AuthenticationService";
import {
  generateJWTToken,
  encodeJWT,
} from "../../../modules/helpers/authentication-helper/authenticationHelper";
import { KEYCLOAK, BCSC_USERINFO_URL } from "../AuthenticationGuard";

const brokerURL = `${KEYCLOAK.url}/realms/${KEYCLOAK.realm}/broker/bcsc/token`;
const bcscURL = `${BCSC_USERINFO_URL}`;

beforeEach(() => {
  // IDP is set in the session
  const token = generateJWTToken({
    preferred_username: "username@bceid",
    email: "username@example.com",
    identityProviderAlias: "bcsc",
  });
  localStorage.setItem("jwt", token);

  fetchMock.mockReset();
});

describe("Authentation Service Test Suite", () => {
  test("getKeycloakBrokerToken 200", async () => {
    fetchMock.get(brokerURL, JSON.stringify({ access_token: "12345" }));

    expect(await getKeycloakBrokerToken()).toEqual("12345");
  });

  test("getKeycloakBrokerToken 404", async () => {
    fetchMock.get(brokerURL, 404);

    try {
      await getKeycloakBrokerToken();
    } catch (error) {
      expect(error).toBeInstanceOf(Error);
      expect(error.message).toEqual("HTTP error, status: 404");
    }
  });

  test("getBCSCUserInfoJWT 200", async () => {
    const jwtToken = encodeJWT("jwtToken");
    fetchMock.get(bcscURL, jwtToken);

    const response = await getBCSCUserInfoJWT("broker_token");
    expect(response).toEqual(jwtToken);
  });

  test("getBCSCUserInfoJWT 404", async () => {
    fetchMock.get(bcscURL, 404);

    try {
      await getBCSCUserInfoJWT("broker_token");
    } catch (error) {
      expect(error).toBeInstanceOf(Error);
      expect(error.message).toEqual("HTTP error, status: 404");
    }
  });

  test("getBCSCUserInfo", async () => {
    fetchMock.get(brokerURL, JSON.stringify({ access_token: "12345" }));
    fetchMock.get(
      bcscURL,
      encodeJWT({
        given_names: "Arthur C",
        family_name: "Clark",
      })
    );

    const response = await getBCSCUserInfo();
    expect(response.givenNames).toEqual("Arthur C");
    expect(response.lastName).toEqual("Clark");
  });

  test("getBCSCUserInfo empty", async () => {
    fetchMock.get(brokerURL, JSON.stringify({ access_token: "12345" }));
    fetchMock.get(bcscURL, encodeJWT({}));

    const response = await getBCSCUserInfo();
    expect(response.givenNames).toEqual("");
    expect(response.lastName).toEqual("");
  });
});
