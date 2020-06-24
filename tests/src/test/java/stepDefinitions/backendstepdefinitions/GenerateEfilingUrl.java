package stepDefinitions.backendstepdefinitions;

import ca.bc.gov.open.jagefilingapi.qa.backend.generateurlpayload.GenerateUrlPayload;
import ca.bc.gov.open.jagefilingapi.qa.backendutils.APIResources;
import ca.bc.gov.open.jagefilingapi.qa.backendutils.TestUtil;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.io.IOException;
import java.net.URL;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.*;

public class GenerateEfilingUrl {

    private static Response response;
    private static RequestSpecification request;
    private static GenerateUrlPayload payloadData;
    private static String submissionId;
    private static String submissionIdQuery;
    private static JsonPath jsonPath;

    private GenerateEfilingUrl() {
        throw new IllegalStateException("Step definition class");
    }

    @Given("user calls {string} with POST http request")
    public static void user_calls_with_POST_http_request(String resource) throws IOException {
        payloadData = new GenerateUrlPayload();

        APIResources resourceAPI = APIResources.valueOf(resource);

        request = given().spec(TestUtil.requestSpecification()).body(payloadData.generateUrlFinalPayload());
        TestUtil.responseSpecification();
        response = request.when().post(resourceAPI.getResource()).then().spec(TestUtil.responseSpecification()).extract().response();
    }

    @When("status code is {int} and content type is verified")
    public static void status_code_is_and_content_type_is_verified(Integer statusCode) {
        assertEquals(response.getStatusCode(), 200);
        assertEquals(response.getContentType(), "application/json");
    }

    @Then("verify response returns {string} and expiry date")
    public static void the_response_returns_and_expiry_date(String resource) {
        APIResources resourceUrl = APIResources.valueOf(resource);
        jsonPath = new JsonPath(response.asString());

        String respUrl = TestUtil.getJsonPath(response, "efilingUrl");
        Long respExpDate = jsonPath.get("expiryDate");

        submissionId = null;
        try {
            URL url = new URL(respUrl);
            submissionIdQuery = url.getQuery();
            submissionId = submissionIdQuery.split("=")[1];
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertEquals(respUrl, resourceUrl.getResource() + submissionIdQuery);
        assertNotNull(respExpDate);
    }

    @Given("user calls {string} with GET http request")
    public static void user_calls_with_GET_http_request(String resource) throws IOException {
        APIResources resourceGet = APIResources.valueOf(resource);

        request = given().spec(TestUtil.requestSpecification());
        response = request.when().get(resourceGet.getResource() + submissionId + "/userDetail").then().spec(TestUtil.responseSpecification()).extract().response();
    }

    @Then("verify response body has account and role information")
    public static void verify_response_body_has_account_and_role_information() {
        jsonPath = new JsonPath(response.asString());

        boolean csoAccountExists = jsonPath.get("csoAccountExists");
        boolean hasEfilingRole = jsonPath.get("hasEfilingRole");

        assertFalse(csoAccountExists);
        assertFalse(hasEfilingRole);
    }

    @Given("user calls incorrect {string} with POST http request")
    public static void user_calls_incorrect_with_POST_http_request(String resource) throws IOException {
        payloadData = new GenerateUrlPayload();
        APIResources resourceInvalid = APIResources.valueOf(resource);

        request = given().spec(TestUtil.requestSpecification()).body(payloadData.generateUrlFinalPayload());
        response = request.when().post(resourceInvalid.getResource() + "s").then().extract().response();
    }

    @When("status is {int} and content type is verified")
    public static void status_is_and_content_type_is_verified(Integer int1) {
        assertEquals(response.getStatusCode(), 404);
        assertEquals(response.getContentType(), "application/json");
    }

    @Then("verify error message is present with empty message")
    public static void verify_fferror_message_is_present_with_empty_message() {
        String error = TestUtil.getJsonPath(response, "error");
        String message = TestUtil.getJsonPath(response, "message");

        assertEquals("Not Found", error);
        assertEquals("", message);
    }
}


