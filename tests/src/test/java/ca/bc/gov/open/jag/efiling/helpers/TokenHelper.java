package ca.bc.gov.open.jag.efiling.helpers;

import ca.bc.gov.open.jagefilingapi.qa.backendutils.TestUtil;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.apache.http.client.utils.URIBuilder;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.text.MessageFormat;
import java.util.Base64;

public class TokenHelper {

    private static final String CLIENT_ID = "client_id";
    private static final String GRANT_TYPE = "grant_type";
    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";

    public static Response getUserAccessToken(String keycloakHost, String keycloakRealm, String username, String password, String clientId) {

        try {
            URIBuilder uriBuilder = new URIBuilder(keycloakHost);

            uriBuilder.setPath(MessageFormat.format("/auth/realms/{0}/protocol/openid-connect/token", keycloakRealm));

            RequestSpecification request = RestAssured.given()
                    .formParam(CLIENT_ID, clientId)
                    .formParam(GRANT_TYPE, "password")
                    .formParam(USERNAME, username)
                    .formParam(PASSWORD, password);

            return request.when().post(uriBuilder.build().toURL().toString()).then()
                    .spec(TestUtil.validResponseSpecification())
                    .extract().response();


        } catch (MalformedURLException | URISyntaxException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }

    }

    public static String decodeTokenToJsonString(String accessToken) {
        String[] tokenParts = accessToken.split("\\.");

        if (tokenParts.length != 3) {
            throw new RuntimeException("invalid token string");
        }

        return new String(Base64.getUrlDecoder().decode(tokenParts[1]));
    }

}
