package ca.bc.gov.open.jagefilingapi.qa.requestbuilders;

import ca.bc.gov.open.jagefilingapi.qa.backend.createcsoaccountpayload.CreateCsoAccountPayload;
import ca.bc.gov.open.jagefilingapi.qa.backendutils.APIResources;
import ca.bc.gov.open.jagefilingapi.qa.backendutils.TestUtil;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.io.IOException;

import static io.restassured.RestAssured.given;

public class CreateCsoAccountRequestBuilders {

    private RequestSpecification request;
    private CreateCsoAccountPayload csoAccountPayloadData;

    public Response requestWithValidRequestBody(String resourceValue) throws IOException {
        csoAccountPayloadData = new CreateCsoAccountPayload();
        APIResources validCreateAccountResourceAPI = APIResources.valueOf(resourceValue);

        request = given().spec(TestUtil.requestSpecification()).body(csoAccountPayloadData.createCsoAccountPayload());
        return request.when().post(validCreateAccountResourceAPI.getResource()).then().spec(TestUtil.createCsoAccountResponseSpecification()).extract().response();
    }

    public Response requestWithIncorrectAccountType(String resourceValue) throws IOException {
        csoAccountPayloadData = new CreateCsoAccountPayload();
        APIResources validCreateAccountResourceAPI = APIResources.valueOf(resourceValue);

        request = given().spec(TestUtil.requestSpecification()).body(csoAccountPayloadData.createIncorrectTypeCsoAccountPayload());
        return request.when().post(validCreateAccountResourceAPI.getResource()).then().spec(TestUtil.createCsoAccountIncorrectTypeErrorResponseSpecification()).extract().response();
    }

    public Response requestWithIncorrectPath(String resourceValue) throws IOException {
        csoAccountPayloadData = new CreateCsoAccountPayload();
        APIResources incorrectPathCreateAccountResourceAPI = APIResources.valueOf(resourceValue);

        request = given().spec(TestUtil.requestSpecification()).body(csoAccountPayloadData.createCsoAccountPayload());
        return request.when().post(incorrectPathCreateAccountResourceAPI.getResource()).then().spec(TestUtil.createCsoAccountIncorrectPathErrorResponseSpecification()).extract().response();
    }
}
