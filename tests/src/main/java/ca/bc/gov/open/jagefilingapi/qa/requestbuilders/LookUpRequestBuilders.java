package ca.bc.gov.open.jagefilingapi.qa.requestbuilders;

import ca.bc.gov.open.jagefilingapi.qa.backendutils.APIResources;
import ca.bc.gov.open.jagefilingapi.qa.backendutils.TestUtil;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.io.IOException;

import static io.restassured.RestAssured.given;

public class LookUpRequestBuilders {

    public Response requestToGetDocumentTypes(String resourceValue, String userJwt) throws IOException {
        APIResources validCreateAccountResourceAPI = APIResources.valueOf(resourceValue);

        RequestSpecification request = given().spec(TestUtil.requestSpecification())
                .auth().preemptive().oauth2(userJwt);

        return request.when()
                .get(validCreateAccountResourceAPI.getResource() + "/P" + "/F")
                .then().spec(TestUtil.validResponseSpecification())
                .extract().response();
    }
}
