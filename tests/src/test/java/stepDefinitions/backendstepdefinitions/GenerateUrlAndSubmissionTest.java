package stepDefinitions.backendstepdefinitions;

import ca.bc.gov.open.jagefilingapi.qa.backend.generateurlpayload.GenerateUrlPayload;
import ca.bc.gov.open.jagefilingapi.qa.backendutils.APIResources;
import ca.bc.gov.open.jagefilingapi.qa.backendutils.TestUtil;
import ca.bc.gov.open.jagefilingapi.qa.frontendutils.JsonDataReader;
import ca.bc.gov.open.jagefilingapi.qa.requestbuilders.GenerateUrlRequestBuilders;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.URL;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

public class GenerateUrlAndSubmissionTest {

    private Response response;
    private GenerateUrlRequestBuilders generateUrlRequestBuilders;
    private String submissionId;
    private String submissionIdQuery;
    private JsonPath jsonPath;
    private String universalId;
    private String firstName;
    private String lastName;
    private String middleName;
    private String email;
    private static final String CONTENT_TYPE = "application/json";
    public Logger log = LogManager.getLogger(GenerateUrlAndSubmissionTest.class);

    @Given("POST http request is made to {string} with valid existing CSO account guid in header")
    public void postHttpRequestIsMadeToWithValidExistingCsoAccountGuidInHeader(String resource) throws IOException {
        generateUrlRequestBuilders = new GenerateUrlRequestBuilders();

        response = generateUrlRequestBuilders.requestWithValidCSOAccountGuid(resource);
    }

    @When("status code is {int} and content type is verified")
    public void statusCodeIsAndContentTypeIsVerified(Integer statusCode) {
        generateUrlRequestBuilders = new GenerateUrlRequestBuilders();

        switch (statusCode) {
            case 200:
                assertEquals(200, response.getStatusCode());
                assertEquals(CONTENT_TYPE, response.getContentType());
                break;
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
            default:
                log.info("Expected status code did not match.");
        }
    }

    @Then("verify response returns {string} and expiry date")
    public void theResponseReturnsAndExpiryDate(String resource) {
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

    @Given("POST http request is made to {string} with non existing CSO account guid in the header")
    public void postHttpRequestIsMadeToWithNonExistingCsoAccountGuidInHeader(String resource) throws IOException {
        generateUrlRequestBuilders = new GenerateUrlRequestBuilders();

        response = generateUrlRequestBuilders.requestWithNonExistingCSOAccountGuid(resource);
    }

    @Given("POST http request is made to {string} with invalid CSO account guid in the header")
    public void postHttpRequestIsMadeToWithInvalidCsoAccountGuidInHeader(String resource) throws IOException {
        generateUrlRequestBuilders = new GenerateUrlRequestBuilders();

        response = generateUrlRequestBuilders.requestWithInvalidCSOAccountGuid(resource);
    }

    @When("{string} id is submitted with GET http request")
    public void idIsSubmittedWithGetHttpRequest(String resource) throws IOException {
        APIResources resourceGet = APIResources.valueOf(resource);

        RequestSpecification request = given().spec(TestUtil.requestSpecification());
        response = request.when().get(resourceGet.getResource() + submissionId).then().spec(TestUtil.responseSpecification()).extract().response();
    }

    @Given("{string} id with filing package path is submitted with GET http request")
    public void idWithFilingPackagePathIsSubmittedWithGETHttpRequest(String resource) throws IOException {
        APIResources resourceGet = APIResources.valueOf(resource);

        RequestSpecification request = given().spec(TestUtil.requestSpecification());
        response = request.when().get(resourceGet.getResource() + submissionId + "/filing-package").then().spec(TestUtil.responseSpecification()).extract().response();
    }

    @Then("verify court details and document details are returned and not empty")
    public void verifyCourtDetailsAndDocumentDetailsAreReturnedAndNotEmpty() {
        jsonPath = new JsonPath(response.asString());

        String location = jsonPath.get("court.location");
        String level = jsonPath.get("court.level");
        String courtClass = jsonPath.get("court.class");
        String division = jsonPath.get("court.division");
        String fileNumber = jsonPath.get("court.fileNumber");
        String participatingClass = jsonPath.get("court.participatingClass");

        List<String> name = jsonPath.get("documents.name");
        List<String> description = jsonPath.get("documents.description");
        List<String> type = jsonPath.get("documents.type");

        assertThat(location, is(not(emptyString())));
        assertThat(level, is(not(emptyString())));
        assertThat(courtClass, is(not(emptyString())));
        assertThat(division, is(not(emptyString())));
        assertThat(fileNumber, is(not(emptyString())));
        assertThat(participatingClass, is(not(emptyString())));
        log.info("Court and document details response have valid values");

        assertFalse(type.isEmpty());
        assertFalse(name.isEmpty());
        assertFalse(description.isEmpty());
        log.info("Account type, description and name objects from the valid CSO account submission response have valid values");
    }


    @Then("verify universal id, user details, account type and identifier values are returned and not empty")
    public void verifyUniversalIdUserDetailsAccountTypeAndIdentifierValuesAreReturnedAndNotEmpty() throws IOException {
        jsonPath = new JsonPath(response.asString());

        String validExistingCSOGuid = JsonDataReader.getCsoAccountGuid().getValidExistingCSOGuid();

        universalId = jsonPath.get("userDetails.universalId");
        firstName = jsonPath.get("userDetails.firstName");
        lastName = jsonPath.get("userDetails.lastName");
        middleName = jsonPath.get("userDetails.middleName");
        email = jsonPath.get("userDetails.email");

        List<String> type = jsonPath.get( "userDetails.accounts.type");
        List<String> identifier = jsonPath.get( "userDetails.accounts.identifier");

        List<String> feeAmt = jsonPath.get( "fees.feeAmt");
        List<String> serviceTypeCd = jsonPath.get( "fees.serviceTypeCd");

        assertThat(universalId, is(equalToIgnoringCase(validExistingCSOGuid)));
        assertThat(firstName, is(not(emptyString())));
        assertThat(lastName, is(not(emptyString())));
        assertThat(middleName, is(not(emptyString())));
        assertThat(email, is(not(emptyString())));
        log.info("Names and email objects from the valid CSO account submission response does not have empty values");

        assertFalse(type.isEmpty());
        assertFalse(identifier.isEmpty());
        assertFalse(feeAmt.isEmpty());
        assertFalse(serviceTypeCd.isEmpty());
        log.info("Account type, identifier, fee and service objects from the valid CSO account submission response have valid values");
    }

    @And("verify success, error and cancel navigation urls are returned")
    public void verifySuccessErrorAndCancelNavigationUrlsAreReturned() {
        jsonPath = new JsonPath(response.asString());

        String successUrl = jsonPath.get("navigation.success.url");
        String errorUrl = jsonPath.get("navigation.error.url");
        String cancelUrl = jsonPath.get("navigation.cancel.url");

        assertNotNull(successUrl);
        assertNotNull(errorUrl);
        assertNotNull(cancelUrl);
    }

    @Then("verify accounts value is null but names and email details are returned")
    public void verifyAccountsValueIsNullButNamesAndEmailDetailsAreReturned() throws IOException {
        jsonPath = new JsonPath(response.asString());

        String nonExistingCSOGuid = JsonDataReader.getCsoAccountGuid().getNonExistingCSOGuid();

        universalId = jsonPath.get("userDetails.universalId");
        firstName = jsonPath.get("userDetails.firstName");
        lastName = jsonPath.get("userDetails.lastName");
        middleName = jsonPath.get("userDetails.middleName");
        email = jsonPath.get("userDetails.email");

        String accounts = jsonPath.get("accounts");

        List<String> feeAmt = jsonPath.get( "fees.feeAmt");
        List<String> serviceTypeCd = jsonPath.get( "fees.serviceTypeCd");

        assertThat(universalId, is(equalToIgnoringCase(nonExistingCSOGuid)));
        assertThat(firstName, is(not(emptyString())));
        assertThat(lastName, is(not(emptyString())));
        assertThat(email, is(not(emptyString())));
        log.info("Names and email objects from the valid CSO account submission response have valid values.");

        assertNull(accounts);
        assertFalse(feeAmt.isEmpty());
        assertFalse(serviceTypeCd.isEmpty());
        log.info("Accounts object value from the valid CSO account submission response is null but fee amount and .");
    }

    @Then("verify response returns invalid role error and message")
    public void verifyResponseReturnsInvalidRoleErrorAndMessage() {
        jsonPath = new JsonPath(response.asString());

        String error = jsonPath.get("error");
        String message = jsonPath.get("message");

        assertEquals("INVALIDROLE", error);
        assertEquals("User does not have a valid role for this request.", message);
    }

    @Given("POST http request is made to {string} with incorrect path")
    public void PostHttpRequestIsMadeToWithIncorrectPath(String resource) throws IOException {
        generateUrlRequestBuilders = new GenerateUrlRequestBuilders();

        response = generateUrlRequestBuilders.requestWithIncorrectPath(resource);
    }

    @Then("verify error message is present and message has no value")
    public void verifyErrorMessageIsPresentAndMessageHasNoValue() {
        jsonPath = new JsonPath(response.asString());

        String error = TestUtil.getJsonPath(response, "error");
        String message = TestUtil.getJsonPath(response, "message");
        int statusCode = jsonPath.get("status");

        switch(statusCode) {
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
