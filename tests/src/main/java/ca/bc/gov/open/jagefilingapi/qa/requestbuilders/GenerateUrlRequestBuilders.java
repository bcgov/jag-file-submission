package ca.bc.gov.open.jagefilingapi.qa.requestbuilders;

import ca.bc.gov.open.jagefilingapi.qa.backend.generateurlpayload.GenerateUrlPayload;
import ca.bc.gov.open.jagefilingapi.qa.backendutils.APIResources;
import ca.bc.gov.open.jagefilingapi.qa.backendutils.TestUtil;
import ca.bc.gov.open.jagefilingapi.qa.frontendutils.JsonDataReader;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import java.io.IOException;
import static io.restassured.RestAssured.given;

public class GenerateUrlRequestBuilders {

    private  RequestSpecification request;
    private static final String X_AUTH_USER_ID = "X-Auth-UserId";

    private  GenerateUrlPayload payloadData;

    public Response requestWithValidCSOAccountGuid(String resourceValue) throws IOException {
        payloadData = new GenerateUrlPayload();
        APIResources resourceAPI = APIResources.valueOf(resourceValue);

        String validExistingCSOGuid = JsonDataReader.getCsoAccountGuid().getValidExistingCSOGuid();

        request = given().spec(TestUtil.requestSpecification()).header(X_AUTH_USER_ID,validExistingCSOGuid).body(payloadData.validGenerateUrlPayload());
        return request.when().post(resourceAPI.getResource() + validExistingCSOGuid + "/generateUrl").then().spec(TestUtil.responseSpecification()).extract().response();
    }

    public Response requestWithNonExistingCSOAccountGuid(String resourceValue) throws IOException {
        payloadData = new GenerateUrlPayload();
        APIResources resourceAPI = APIResources.valueOf(resourceValue);

        String nonExistingCSOGuid = JsonDataReader.getCsoAccountGuid().getNonExistingCSOGuid();

        request = given().spec(TestUtil.requestSpecification()).header(X_AUTH_USER_ID,nonExistingCSOGuid).body(payloadData.validGenerateUrlPayload());
        return request.when().post(resourceAPI.getResource() + nonExistingCSOGuid + "/generateUrl") .then().spec(TestUtil.responseSpecification()).extract().response();
    }

    public Response requestWithInvalidCSOAccountGuid(String resourceValue) throws IOException {
        payloadData = new GenerateUrlPayload();

        APIResources resourceAPI = APIResources.valueOf(resourceValue);
        String invalidNoFilingRoleGuid = JsonDataReader.getCsoAccountGuid().getInvalidNoFilingRoleGuid();

        request = given().spec(TestUtil.requestSpecification()).header(X_AUTH_USER_ID,invalidNoFilingRoleGuid).body(payloadData.validGenerateUrlPayload());
        return request.when().post(resourceAPI.getResource() + invalidNoFilingRoleGuid + "/generateUrl").then().spec(TestUtil.errorResponseSpecification()).extract().response();
    }

    public Response requestWithIncorrectPath(String resourceValue) throws IOException {
        payloadData = new GenerateUrlPayload();
        APIResources resourceIncorrect = APIResources.valueOf(resourceValue);

        String validExistingCSOGuid = JsonDataReader.getCsoAccountGuid().getValidExistingCSOGuid();

        request = given().spec(TestUtil.requestSpecification()).header(X_AUTH_USER_ID, validExistingCSOGuid).body(payloadData.validGenerateUrlPayload());
        return request.when().post(resourceIncorrect.getResource() + validExistingCSOGuid + "/generateUrl").then().extract().response();
    }

    public Response requestWithInvalidPath(String resourceValue) throws IOException {
        payloadData = new GenerateUrlPayload();
        APIResources resourceInvalid = APIResources.valueOf(resourceValue);

        String validExistingCSOGuid = JsonDataReader.getCsoAccountGuid().getValidExistingCSOGuid();

        request = given().spec(TestUtil.requestSpecification()).header(X_AUTH_USER_ID,validExistingCSOGuid).body(payloadData.validGenerateUrlPayload());
        return request.when().post(resourceInvalid.getResource()  + validExistingCSOGuid + "/generateUrs").then().extract().response();
    }

    public Response requestWithInvalidNoIdAndIncorrectPath(String resourceValue) throws IOException {
        payloadData = new GenerateUrlPayload();
        APIResources resourceInvalid = APIResources.valueOf(resourceValue);

        String validExistingCSOGuid = JsonDataReader.getCsoAccountGuid().getValidExistingCSOGuid();

        request = given().spec(TestUtil.requestSpecification()).header(X_AUTH_USER_ID,validExistingCSOGuid).body(payloadData.validGenerateUrlPayload());
        return request.when().post(resourceInvalid.getResource() + "/generateUrs").then().extract().response();
    }
}
