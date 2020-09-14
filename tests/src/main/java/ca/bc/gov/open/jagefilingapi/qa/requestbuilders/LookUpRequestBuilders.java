package ca.bc.gov.open.jagefilingapi.qa.requestbuilders;

import ca.bc.gov.open.jagefilingapi.qa.backendutils.APIResources;
import ca.bc.gov.open.jagefilingapi.qa.backendutils.TestUtil;
import ca.bc.gov.open.jagefilingapi.qa.frontendutils.FrontendTestUtil;
import ca.bc.gov.open.jagefilingapi.qa.frontendutils.JsonDataReader;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.io.IOException;

import static io.restassured.RestAssured.given;

public class LookUpRequestBuilders {

    private RequestSpecification request;

    public Response requestToGetDocumentTypes(String resourceValue) throws IOException, InterruptedException {
        APIResources validCreateAccountResourceAPI = APIResources.valueOf(resourceValue);

        FrontendTestUtil frontendTestUtil = new FrontendTestUtil();
        String userToken = frontendTestUtil.getUserJwtToken();

        request = given().spec(TestUtil.requestSpecification())
                .auth().preemptive().oauth2(userToken);

        return request.when()
                .get(validCreateAccountResourceAPI.getResource() + "/P" + "/F")
                .then().spec(TestUtil.validResponseSpecification())
                .extract().response();
    }
}
