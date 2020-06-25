package stepDefinitions.backendstepdefinitions;

import ca.bc.gov.open.jagefilingapi.qa.backend.generateurlpayload.GenerateUrlPayload;
import ca.bc.gov.open.jagefilingapi.qa.backendutils.APIResources;
import ca.bc.gov.open.jagefilingapi.qa.backendutils.TestUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.junit.Assert.*;

public class GenerateEfilingUrl {

    private static Response response;
    private static RequestSpecification request;
    private static GenerateUrlPayload payloadData;
    private static String submissionId;
    private static String submissionIdQuery;
    private static JsonPath jsonPath;
    private static final String CONTENT_TYPE = "application/json";

    private GenerateEfilingUrl() {
        throw new IllegalStateException("Step definition class");
    }

    @Given("user calls {string} with POST http request")
    public static void userCallsWithPOSTHttpRequest(String resource) throws IOException {
        payloadData = new GenerateUrlPayload();

        APIResources resourceAPI = APIResources.valueOf(resource);

        request = given().spec(TestUtil.requestSpecification()).body(payloadData.generateUrlFinalPayload());
        TestUtil.responseSpecification();
        response = request.when().post(resourceAPI.getResource()).then().spec(TestUtil.responseSpecification()).extract().response();
    }

    @When("status code is {int} and content type is verified")
    public static void statusCodeIsAndContentTypeIsVerified(Integer statusCode) {
        assertEquals(200, response.getStatusCode());
        assertEquals(CONTENT_TYPE, response.getContentType());
    }

    @Then("verify response returns {string} and expiry date")
    public static void theResponseReturnsAndExpiryDate(String resource) {
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

        assertEquals(respUrl, resourceUrl.getResource().concat("?") + submissionIdQuery);
        assertNotNull(respExpDate);
    }

    @Given("user calls {string} with GET http request")
    public static void userCallsWithGETHttpRequest(String resource) throws IOException {
        APIResources resourceGet = APIResources.valueOf(resource);

        request = given().spec(TestUtil.requestSpecification());
        response = request.when().get(resourceGet.getResource() + submissionId).then().spec(TestUtil.responseSpecification()).extract().response();
    }

    @Then("verify response body has account and redirect Urls")
    public static void verifyResponseBodyHasAccountAndRedirectUrls() throws JsonProcessingException {
        payloadData = new GenerateUrlPayload();
        jsonPath = new JsonPath(response.asString());

        Map<String, String> respNavigation = jsonPath.getMap("navigation");
        ObjectMapper objMap = new ObjectMapper();
        String actualResponse = objMap.writeValueAsString(respNavigation);
        String expectedResponse = payloadData.getNavigationData();

        boolean csoAccountExists = jsonPath.get("csoAccountExists");

        assertEquals(expectedResponse,actualResponse);
        assertFalse(csoAccountExists);
    }

    @Given("user calls incorrect {string} with POST http request")
    public static void userCallsIncorrectWithPOSTHttpRequest(String resource) throws IOException {
        payloadData = new GenerateUrlPayload();
        APIResources resourceInvalid = APIResources.valueOf(resource);

        request = given().spec(TestUtil.requestSpecification()).body(payloadData.generateUrlFinalPayload());
        response = request.when().post(resourceInvalid.getResource()).then().extract().response();
    }

    @When("status is {int} and content type is verified")
    public static void statusIsAndContentTypeIsVerified(Integer int1) {
        if(int1 == 404) {
            assertEquals(404, response.getStatusCode());
            assertEquals(CONTENT_TYPE, response.getContentType());
        } else if(int1 == 405) {
            assertEquals(405, response.getStatusCode());
            assertEquals(CONTENT_TYPE, response.getContentType());
        }
    }

    @Then("verify error message is present and message has no value")
    public static void verifyErrorMessageIsPresentAndMessageHasNoValue() {
        jsonPath = new JsonPath(response.asString());

        String error = TestUtil.getJsonPath(response, "error");
        String message = TestUtil.getJsonPath(response, "message");
        int status = jsonPath.get("status");

        if(status == 404) {
            assertEquals("Not Found", error);
            assertEquals("", message);
        } else if(status == 405) {
            assertEquals("Method Not Allowed", error);
            assertEquals("", message);
        }
    }

    @Given("user calls invalid {string} with POST http request")
    public static void userCallsInvalidWithPOSTHttpRequest(String resource) throws IOException {
        payloadData = new GenerateUrlPayload();
        APIResources resourceInvalid = APIResources.valueOf(resource);

        request = given().spec(TestUtil.requestSpecification()).body(payloadData.generateUrlFinalPayload());
        response = request.when().post(resourceInvalid.getResource() + "s").then().extract().response();
    }
}
