package stepDefinitions.backendstepdefinitions;

import ca.bc.gov.open.jagefilingapi.qa.backend.generateurlpayload.GenerateUrlPayload;
import ca.bc.gov.open.jagefilingapi.qa.backendutils.APIResources;
import ca.bc.gov.open.jagefilingapi.qa.backendutils.TestUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.io.IOException;
import java.net.URL;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class GenerateEfilingUrl extends TestUtil {

    private static Response response;
    private static RequestSpecification request;
    private static GenerateUrlPayload payloadData;
    private static String firstParamPath;

    @Given("user calls {string} with POST http request")
    public static void user_calls_with_POST_http_request(String resource) throws IOException {
        payloadData = new GenerateUrlPayload();

        APIResources resourceAPI = APIResources.valueOf(resource);

        request = given().spec(requestSpecification()).body(payloadData.generateUrlFinalPayload());
        responseSpecification();
        response = request.when().post(resourceAPI.getResource()).then().spec(responseSpecification()).extract().response();
    }

    @When("status code is {int} and content type is verified")
    public static void status_code_is_and_content_type_is_verified(Integer statusCode) {
        assertEquals(response.getStatusCode(), 200);
        assertEquals(response.getContentType(), "application/json");
    }

    @Then("verify response returns {string} and expiry date")
    public static void the_response_returns_and_expiry_date(String resource) {
        APIResources resourceUrl = APIResources.valueOf(resource);
        JsonPath jsPath = new JsonPath(response.asString());

        String respUrl1 = getJsonPath(response, "efilingUrl");
        String respUrl2 = getJsonPath(response, "eFilingUrl");
        Long respExpDate = jsPath.get("expiryDate");

        firstParamPath = null;
        String secondParamPath = null;
        try {
            URL url = new URL(respUrl1);
            firstParamPath = url.getPath();

            url = new URL(respUrl2);
            secondParamPath = url.getPath();
        } catch (Exception e) {
            e.printStackTrace();
        }

        assertEquals(respUrl1, resourceUrl.getResource() + firstParamPath);
        assertEquals(respUrl2, resourceUrl.getResource() + secondParamPath);
        assertEquals(respUrl1, respUrl2);
        assertNotNull(respExpDate);
    }

    @Given("user calls {string} with GET http request")
    public static void user_calls_with_GET_http_request(String resource) throws IOException {
        APIResources resourceGet = APIResources.valueOf(resource);

        firstParamPath = firstParamPath.replace("//", "/");

        request = given().spec(requestSpecification());
        response = request.when().get(resourceGet.getResource() + firstParamPath).then().spec(responseSpecification()).extract().response();
    }

    @Then("verify response body matches the POST request")
    public static void verify_response_body_matches_the_POST_request() throws JsonProcessingException {
        payloadData = new GenerateUrlPayload();

        String actualResp = response.asString();
        String expResp = payloadData.generateUrlFinalPayload();

        assertEquals(actualResp, expResp);
    }

    @Given("user calls incorrect {string} with POST http request")
    public static void user_calls_incorrect_with_POST_http_request(String resource) throws IOException {
        payloadData = new GenerateUrlPayload();
        APIResources resourceInvalid = APIResources.valueOf(resource);

        request = given().spec(requestSpecification()).body(payloadData.generateUrlFinalPayload());
        response = request.when().post(resourceInvalid.getResource() + "s").then().extract().response();
    }

    @When("status is {int} and content type is verified")
    public static void status_is_and_content_type_is_verified(Integer int1) {
        assertEquals(response.getStatusCode(), 404);
        assertEquals(response.getContentType(), "application/json");
    }

    @Then("verify response payload objects are correct")
    public static void verify_response_payload_objects_are_correct() {
        String error = getJsonPath(response, "error");
        String message = getJsonPath(response, "message");

        assertEquals("Not Found", error);
        assertEquals("", message);
    }
}

