package ca.bc.gov.open.jag.efiling.helpers;

import ca.bc.gov.open.jag.efiling.error.EfilingTestException;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.apache.http.client.utils.URIBuilder;

import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.text.MessageFormat;
import java.util.Base64;

public class TokenHelper {

    private TokenHelper() {
    }

    public static final String CLIENT_ID = "client_id";
    public static final String GRANT_TYPE = "grant_type";

    public static Response getUserAccessToken(String keycloakHost, String keycloakRealm, String username, String password, String clientId) {

        try {
            URIBuilder uriBuilder = new URIBuilder(keycloakHost);

            uriBuilder.setPath(MessageFormat.format("/auth/realms/{0}/protocol/openid-connect/token", keycloakRealm));

            RequestSpecification request = RestAssured.given()
                    .formParam(CLIENT_ID, clientId)
                    .formParam(GRANT_TYPE, "password")
                    .formParam("username", username)
                    .formParam("password", password);

            return request.when().post(uriBuilder.build().toURL().toString()).then()
                    .extract().response();


        } catch (MalformedURLException | URISyntaxException e) {
            e.printStackTrace();
            throw new EfilingTestException("Could not build access token url", e);
        }

    }

    public static String decodeTokenToJsonString(String accessToken) {
        String[] tokenParts = accessToken.split("\\.");

        if (tokenParts.length != 3) {
            throw new EfilingTestException("invalid token string");
        }

        return new String(Base64.getUrlDecoder().decode(tokenParts[1]));
    }

}
