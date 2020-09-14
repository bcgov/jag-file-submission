package stepDefinitions.backendstepdefinitions;

import ca.bc.gov.open.jagefilingapi.qa.backendutils.TestUtil;
import ca.bc.gov.open.jagefilingapi.qa.frontendutils.JsonDataReader;
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
import static org.junit.Assert.*;

public class CreateCsoAccountTest {

    private CreateCsoAccountRequestBuilders createCsoAccountRequestBuilders;
    private static final String CONTENT_TYPE = "application/json";
    public Logger log = LogManager.getLogger(CreateCsoAccountTest.class);
    private Response response;
    private JsonPath jsonPath;

    @Given("POST http request is made to {string} with a valid request body")
    public void postHttpRequestIsMadeToWithAValidRequestBody(String resource) throws IOException, InterruptedException {
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
            case 200:
                assertEquals(200, response.getStatusCode());
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

    @Then("verify response returns clientId, accountId and internalClientNumber")
    public void verifyResponseReturnsClientIdAccountIdAndInternalClientNumber() {
        jsonPath = new JsonPath(response.asString());

        assertEquals("1", jsonPath.get("clientId"));
        assertEquals("1", jsonPath.get("accountId"));
        assertNull(jsonPath.get("internalClientNumber"));
        assertTrue(jsonPath.get("fileRolePresent"));
    }

    @Given("GET http request is made to {string}")
    public void getHttpRequestIsMadeToCsoAccount(String resource) throws IOException, InterruptedException {
        createCsoAccountRequestBuilders = new CreateCsoAccountRequestBuilders();

        response = createCsoAccountRequestBuilders.requestToGetUserCsoAccount(resource);
    }

    @Given("PUT http request is made to {string} with a valid request body")
    public void putHttpRequestIsMadeToWithAValidRequestBody(String resource) throws IOException, InterruptedException {
        createCsoAccountRequestBuilders = new CreateCsoAccountRequestBuilders();

        response = createCsoAccountRequestBuilders.requestToUpdateUserCsoAccount(resource);
    }

    @Then("verify response returns clientId, accountId and internalClientNumber is updated")
    public void verifyResponseReturnsClientIdAccountIdAndInternalClientNumberIsUpdated() {
        jsonPath = new JsonPath(response.asString());

        assertEquals("1", jsonPath.get("clientId"));
        assertEquals("1", jsonPath.get("accountId"));
        assertEquals("23423", jsonPath.get("internalClientNumber"));
        assertTrue(jsonPath.get("fileRolePresent"));
    }
    @Then("verify response body has error, status and an empty message")
    public void verifyResponseBodyHasErrorStatusAndAnEmptyMessage() {
        jsonPath = new JsonPath(response.asString());
        int status = jsonPath.get("status");

        assertEquals(404, status);
        assertEquals("Not Found", TestUtil.getJsonPath(response, "error"));
        assertEquals("", TestUtil.getJsonPath(response, "message"));

        log.info("Status, error and message response are verified.");
    }

    @Given("GET request is made to {string}")
    public void getHttpRequestIsMadeToBceidAccount(String resource) throws IOException, InterruptedException {
        createCsoAccountRequestBuilders = new CreateCsoAccountRequestBuilders();

        response = createCsoAccountRequestBuilders.requestToGetUserBceidAccount(resource);
    }

    @Then("verify response returns firstName, lastName and middleName")
    public void verifyResponseReturnsFirstNameLastNameAndMiddleName() {
        jsonPath = new JsonPath(response.asString());

        assertEquals("Bob", jsonPath.get("firstName"));
        assertEquals("Ross", jsonPath.get("lastName"));
        assertEquals("Alan", jsonPath.get("middleName"));
    }

    @Given("POST http request is made to {string} with incorrect path value")
    public void postHttpRequestIsMadeToWithIncorrectPathValue(String resource) throws IOException {
        createCsoAccountRequestBuilders = new CreateCsoAccountRequestBuilders();

        response = createCsoAccountRequestBuilders.requestWithIncorrectPath(resource);
    }
}
