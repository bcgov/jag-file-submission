package stepDefinitions.backendstepdefinitions;

import ca.bc.gov.open.jagefilingapi.qa.backendutils.TestUtil;
import ca.bc.gov.open.jagefilingapi.qa.requestbuilders.CreateCsoAccountRequestBuilders;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.emptyString;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertEquals;

public class CreateCsoAccountTest {

    private CreateCsoAccountRequestBuilders createCsoAccountRequestBuilders;
    private static final String CONTENT_TYPE = "application/json";
    public Logger log = LogManager.getLogger(CreateCsoAccountTest.class);
    private Response response;
    private JsonPath jsonPath;

    @Given("POST http request is made to {string} with a valid request body")
    public void postHttpRequestIsMadeToWithAValidRequestBody(String resource) throws IOException {
        createCsoAccountRequestBuilders = new CreateCsoAccountRequestBuilders();

        response = createCsoAccountRequestBuilders.requestWithValidRequestBody(resource);
    }

    @When("status is {int} and content type is verified")
    public void statusIsAndContentTypeIsVerified(Integer status) {
        createCsoAccountRequestBuilders = new CreateCsoAccountRequestBuilders();

        switch (status) {
            case 201:
                assertEquals(201, response.getStatusCode());
                assertEquals(CONTENT_TYPE, response.getContentType());
                break;
            case 400:
                assertEquals(400, response.getStatusCode());
                assertEquals(CONTENT_TYPE, response.getContentType());
                break;
            case 404:
                assertEquals(404, response.getStatusCode());
                assertEquals(CONTENT_TYPE, response.getContentType());
                break;
            default:
                log.info("Expected status code did not match with the provided codes.");
        }
    }

    @Then("verify response returns names, email and accounts with type and identifiers")
    public void verifyResponseReturnsNamesEmailAndAccountsWithTypeAndIdentifiers() {
        jsonPath = new JsonPath(response.asString());

        String respFirstName = jsonPath.get("firstName");
        String respLastName = jsonPath.get("lastName");
        String respMiddleName = jsonPath.get("middleName");
        String respEmail = jsonPath.get("email");

        List<String> respTypes = jsonPath.get("accounts.type");
        String requestType = respTypes.get(0);
        String responseType = respTypes.get(1);

        List<String> respIdentifiers = jsonPath.get("accounts.identifier");
        String requestId = respIdentifiers.get(0);
        String responseId = respIdentifiers.get(1);

        assertThat(respFirstName, is(not(emptyString())));
        assertThat(respLastName, is(not(emptyString())));
        assertThat(respMiddleName, is(not(emptyString())));
        assertThat(respEmail, is(not(emptyString())));

        assertEquals("BCEID", requestType);
        assertEquals("CSO", responseType);

        assertEquals("string", requestId);
        assertEquals("id", responseId);
    }

    @Given("POST http request is made to {string} with incorrect account type")
    public void postHttpRequestIsMadeToWithIncorrectAccountType(String resource) throws IOException {
        createCsoAccountRequestBuilders = new CreateCsoAccountRequestBuilders();

        response = createCsoAccountRequestBuilders.requestWithIncorrectAccountType(resource);
    }

    @Then("verify response body has error, status and an empty message")
    public void verifyResponseBodyHasErrorStatusAndAnEmptyMessage() {
        jsonPath = new JsonPath(response.asString());

        String respError = TestUtil.getJsonPath(response, "error");
        String respMessage = TestUtil.getJsonPath(response, "message");
        int status = jsonPath.get("status");

        switch (status) {
            case 400:
                assertEquals("Bad Request", respError);
                assertEquals("", respMessage);
                break;
            case 404:
                assertEquals("Not Found", respError);
                assertEquals("", respMessage);
                break;
            default:
                log.info("Status, error and message response for incorrect and or invalid request is verified.");
        }
    }

    @Given("POST http request is made to {string} with incorrect path value")
    public void postHttpRequestIsMadeToWithIncorrectPathValue(String resource) throws IOException {
        createCsoAccountRequestBuilders = new CreateCsoAccountRequestBuilders();

        response = createCsoAccountRequestBuilders.requestWithIncorrectPath(resource);
    }
}
