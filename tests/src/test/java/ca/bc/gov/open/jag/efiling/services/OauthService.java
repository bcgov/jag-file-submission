package ca.bc.gov.open.jag.efiling.services;

import ca.bc.gov.open.jag.efiling.error.EfilingTestException;
import ca.bc.gov.open.jag.efiling.helpers.TokenHelper;
import ca.bc.gov.open.jag.efiling.models.UserIdentity;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;


public class OauthService {

    @Value("${KEYCLOAK_HOST:http://localhost:8081}")
    private String keycloakHost;

    @Value("${KEYCLOAK_REALM:Efiling-Hub}")
    private String keycloakRealm;

    @Value("${USERNAME_KEYCLOAK:bobross}")
    private String username;

    @Value("${PASSWORD_KEYCLOAK:changeme}")
    private String password;

    private final Logger logger = LoggerFactory.getLogger(OauthService.class);

    public UserIdentity getUserIdentity() {

        logger.info("Requesting bearer token from {} issuer", keycloakHost);

        Response response = TokenHelper.getUserAccessToken(keycloakHost, keycloakRealm, username, password, "efiling-frontend");

        logger.info("Issuer respond with http status: {}", Integer.valueOf(response.getStatusCode()));

        JsonPath oidcTokenJsonPath = new JsonPath(response.asString());

        if (oidcTokenJsonPath.get("access_token") == null)
            throw new EfilingTestException("access_token not present in response");

        String actualUserToken = oidcTokenJsonPath.get("access_token");
        JsonPath tokenJsonPath = new JsonPath(TokenHelper.decodeTokenToJsonString(actualUserToken));

        if (tokenJsonPath.get("universal-id") == null)
            throw new EfilingTestException("universal-id not present in response");


        return new UserIdentity(actualUserToken, tokenJsonPath.get("universal-id"));

    }
}
