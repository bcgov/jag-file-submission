package ca.bc.gov.open.jagefilingapi.qa.requestbuilders;

import ca.bc.gov.open.jagefilingapi.qa.backend.createcsoaccountpayload.CreateCsoAccountPayload;
import ca.bc.gov.open.jagefilingapi.qa.backendutils.APIResources;
import ca.bc.gov.open.jagefilingapi.qa.backendutils.TestUtil;
import ca.bc.gov.open.jagefilingapi.qa.frontendutils.JsonDataReader;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.io.IOException;

import static io.restassured.RestAssured.given;

public class CreateCsoAccountRequestBuilders {

    private RequestSpecification request;
    private CreateCsoAccountPayload csoAccountPayloadData;
    private static final String X_AUTH_USER_ID = "X-Auth-UserId";
    private String validExistingCSOGuid;

    public Response requestWithValidRequestBody(String resourceValue) throws IOException {
        csoAccountPayloadData = new CreateCsoAccountPayload();
        APIResources validCreateAccountResourceAPI = APIResources.valueOf(resourceValue);
        validExistingCSOGuid = JsonDataReader.getCsoAccountGuid().getValidExistingCSOGuid();

        request = given().spec(TestUtil.requestSpecification())
                .header(X_AUTH_USER_ID, validExistingCSOGuid)
                .body(csoAccountPayloadData.createCsoAccountPayload());

        return request.when()
                .post(validCreateAccountResourceAPI.getResource())
                .then().spec(TestUtil.createCsoAccountResponseSpecification())
                .extract().response();
    }

    public Response requestWithIncorrectAccountType(String resourceValue) throws IOException {
        csoAccountPayloadData = new CreateCsoAccountPayload();
        APIResources validCreateAccountResourceAPI = APIResources.valueOf(resourceValue);
        validExistingCSOGuid = JsonDataReader.getCsoAccountGuid().getValidExistingCSOGuid();

        request = given().spec(TestUtil.requestSpecification())
                .header(X_AUTH_USER_ID, validExistingCSOGuid)
                .body(csoAccountPayloadData.createIncorrectTypeCsoAccountPayload());

        return request.when()
                .post(validCreateAccountResourceAPI.getResource())
                .then().spec(TestUtil.createCsoAccountIncorrectTypeErrorResponseSpecification())
                .extract().response();
    }

    public Response requestWithIncorrectPath(String resourceValue) throws IOException {
        csoAccountPayloadData = new CreateCsoAccountPayload();
        APIResources incorrectPathCreateAccountResourceAPI = APIResources.valueOf(resourceValue);

        request = given().spec(TestUtil.requestSpecification())
                .body(csoAccountPayloadData.createCsoAccountPayload());

        return request.when()
                .post(incorrectPathCreateAccountResourceAPI.getResource())
                .then()
                .spec(TestUtil.createCsoAccountIncorrectPathErrorResponseSpecification())
                .extract().response();
    }
}
