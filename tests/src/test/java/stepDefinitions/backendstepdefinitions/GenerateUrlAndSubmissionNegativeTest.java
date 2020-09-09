package stepDefinitions.backendstepdefinitions;

import ca.bc.gov.open.jagefilingapi.qa.backendutils.TestUtil;
import ca.bc.gov.open.jagefilingapi.qa.frontendutils.DriverClass;
import ca.bc.gov.open.jagefilingapi.qa.requestbuilders.GenerateUrlRequestBuilders;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import static org.junit.Assert.assertEquals;

public class GenerateUrlAndSubmissionNegativeTest extends DriverClass {

    private Response response;
    private GenerateUrlRequestBuilders generateUrlRequestBuilders;
    private JsonPath jsonPath;
    private static final String CONTENT_TYPE = "application/json";
    private static final String ERROR = "error";
    private static final String MESSAGE = "message";

    public Logger log = LogManager.getLogger(GenerateUrlAndSubmissionNegativeTest.class);

    @When("error status code is {int} and content type is verified")
    public void errorStatusCodeIsAndContentTypeIsVerified(Integer statusCode) {
        generateUrlRequestBuilders = new GenerateUrlRequestBuilders();

        switch (statusCode) {
            case 403:
                assertEquals(403, response.getStatusCode());
                assertEquals(CONTENT_TYPE, response.getContentType());
                break;
            case 404:
                assertEquals(404, response.getStatusCode());
                assertEquals(CONTENT_TYPE, response.getContentType());
                break;
            case 405:
                assertEquals(405, response.getStatusCode());
                assertEquals(CONTENT_TYPE, response.getContentType());
                break;
            case 415:
                assertEquals(415, response.getStatusCode());
                assertEquals(CONTENT_TYPE, response.getContentType());
                break;
            default:
                log.info("Expected status code did not match.");
        }
    }

    @Given("POST http request is made to {string} with invalid CSO account guid in the header")
    public void postHttpRequestIsMadeToWithInvalidCsoAccountGuidInHeader(String resource) throws IOException {
        generateUrlRequestBuilders = new GenerateUrlRequestBuilders();

        response = generateUrlRequestBuilders.requestWithInvalidCSOAccountGuid(resource);
    }

    @Given("POST http request is made to {string} with invalid file type and a single image file")
    public void postHttpRequestIsMadeToWithInvalidFileTypeAndASingleImageFile(String resource) throws IOException {
        generateUrlRequestBuilders = new GenerateUrlRequestBuilders();

        response = generateUrlRequestBuilders.requestWithIncorrectFileType(resource);
    }

    @When("status code is {int} and content type is not json")
    public void statusCodeIsAndContentTypeIsNotJson(Integer int1) {
        assertEquals(400, response.getStatusCode());
        assertEquals(CONTENT_TYPE, response.getContentType());
    }

    @Then("verify response returns document required error and message")
    public void verifyResponseReturnsDocumentRequiredErrorAndMessage() {
        jsonPath = new JsonPath(response.asString());
        String error = jsonPath.get(ERROR);
        String message = jsonPath.get(MESSAGE);

        assertEquals("DOCUMENT_REQUIRED", error);
        assertEquals("At least one document is required.", message);
    }

    @Then("verify response returns invalid role error and message")
    public void verifyResponseReturnsInvalidRoleErrorAndMessage() {
        jsonPath = new JsonPath(response.asString());

        String error = jsonPath.get(ERROR);
        String message = jsonPath.get(MESSAGE);

        assertEquals("Unsupported Media Type", error);
        assertEquals("", message);
    }

    @Given("POST http request is made to {string} with incorrect path")
    public void PostHttpRequestIsMadeToWithIncorrectPath(String resource) throws IOException {
        generateUrlRequestBuilders = new GenerateUrlRequestBuilders();

        response = generateUrlRequestBuilders.requestWithIncorrectPath(resource);
    }

    @Then("verify error message is present and message has no value")
    public void verifyErrorMessageIsPresentAndMessageHasNoValue() {
        jsonPath = new JsonPath(response.asString());

        String error = TestUtil.getJsonPath(response, ERROR);
        String message = TestUtil.getJsonPath(response, MESSAGE);
        int statusCode = jsonPath.get("status");

        switch (statusCode) {
            case 404:
                assertEquals("Not Found", error);
                assertEquals("", message);
                break;
            case 405:
                assertEquals("Method Not Allowed", error);
                assertEquals("", message);
                break;
            default:
                log.info("Status, error and message response for incorrect and or invalid request is verified.");
        }
    }

    @Given("POST http request is made to {string} with invalid path")
    public void PostHttpRequestIsMadeToWithInvalidPath(String resource) throws IOException {
        generateUrlRequestBuilders = new GenerateUrlRequestBuilders();

        response = generateUrlRequestBuilders.requestWithInvalidPath(resource);
    }

    @Given("POST http request is made to {string} without id in the path")
    public void POSTHttpRequestIsMadeToWithoutIdInThePath(String resource) throws IOException {
        generateUrlRequestBuilders = new GenerateUrlRequestBuilders();

        response = generateUrlRequestBuilders.requestWithoutIdInThePath(resource);
    }
}
