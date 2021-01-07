package stepDefinitions.backendstepdefinitions;

import ca.bc.gov.open.jagefilingapi.qa.backendutils.TestUtil;
import ca.bc.gov.open.jagefilingapi.qa.frontendutils.DriverClass;
import ca.bc.gov.open.jagefilingapi.qa.frontendutils.FrontendTestUtil;
import ca.bc.gov.open.jagefilingapi.qa.frontendutils.JsonDataReader;
import ca.bc.gov.open.jagefilingapi.qa.requestbuilders.GenerateUrlRequestBuilders;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.emptyString;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.*;

public class GenerateUrlAndSubmissionForNonExistingGuidTest extends DriverClass {

    private Response response;
    private GenerateUrlRequestBuilders generateUrlRequestBuilders;
    private String submissionId;
    private JsonPath jsonPath;
    private String nonExistingCSOGuid;
    private static final String CONTENT_TYPE = "application/json";
    private static final String SUBMISSION_ID = "submissionId";
    private static final String TRANSACTION_ID = "transactionId";
    private static final String PATH_PARAM = "/generateUrl";
    private static final String FILE_NAME_PATH = "/data/test-document.pdf";
    private String respUrl;
    private String userToken;

    public Logger log = LogManager.getLogger(GenerateUrlAndSubmissionForNonExistingGuidTest.class);

    @Given("POST http request is made to {string} with non existing CSO account guid and a single pdf file")
    public void postHttpRequestIsMadeToWithNonExistingCSOAccountGuidAndASinglePdfFile(String resource) throws IOException {
        generateUrlRequestBuilders = new GenerateUrlRequestBuilders();
        nonExistingCSOGuid = JsonDataReader.getCsoAccountGuid().getInvalidNoFilingRoleGuid();

        response = generateUrlRequestBuilders.requestWithSinglePdfDocument(resource, nonExistingCSOGuid, FILE_NAME_PATH);
    }

    @When("status code is {int} and content type are verified")
    public void statusCodeIsAndContentTypeAreVerified(Integer statusCode) {
        generateUrlRequestBuilders = new GenerateUrlRequestBuilders();

        assertEquals(200, response.getStatusCode());
        assertEquals(CONTENT_TYPE, response.getContentType());
    }

    @Then("verify submission id and document count are received")
    public void verifySubmissionIdAndDocumentCountAreReceived() {
        jsonPath = new JsonPath(response.asString());

        submissionId = TestUtil.getJsonPath(response, SUBMISSION_ID);
        int receivedCount = jsonPath.get("received");

        assertEquals(1, receivedCount);
        assertNotNull(submissionId);
    }

    @Given("POST http request is made to {string} with client application, court details and navigation urls with non existing guid")
    public void POSTHttpRequestIsMadeToWithClientApplicationCourtDetailsAndNavigationUrlsWithNonExistingGuid(String resource) throws IOException {
        generateUrlRequestBuilders = new GenerateUrlRequestBuilders();
        nonExistingCSOGuid = JsonDataReader.getCsoAccountGuid().getInvalidNoFilingRoleGuid();

        response = generateUrlRequestBuilders.postRequestWithPayload(resource,nonExistingCSOGuid, submissionId, PATH_PARAM );
    }

    @Then("verify expiry date and eFiling url are returned with non existing CSO account guid and submission id")
    public void verifyExpiryDateAndEFilingUrlAreReturnedWithNonExistingCSOAccountGuidAndSubmissionId() throws URISyntaxException {
        jsonPath = new JsonPath(response.asString());

        respUrl = jsonPath.get("efilingUrl");
        Long expiryDate = jsonPath.get("expiryDate");

        generateUrlRequestBuilders = new GenerateUrlRequestBuilders();
        List<String> respId = TestUtil.getSubmissionAndTransId(respUrl, SUBMISSION_ID, TRANSACTION_ID);

        assertEquals(submissionId, respId.get(0));
        assertEquals(nonExistingCSOGuid, respId.get(1));
        assertNotNull(expiryDate);
    }

    @Given("token is retrieved from the frontend")
    public void tokenIsRetrievedFromTheFrontend() throws IOException, InterruptedException {
        generateUrlRequestBuilders = new GenerateUrlRequestBuilders();
        FrontendTestUtil frontendTestUtil = new FrontendTestUtil();
        userToken = frontendTestUtil.getUserJwtToken(respUrl);
    }

    @Given("{string} id is submitted with non existing CSO account GET http request")
    public void idIsSubmittedWithNonExistingCsoAccountGetHttpRequest(String resource) throws IOException, InterruptedException {
        generateUrlRequestBuilders = new GenerateUrlRequestBuilders();
        response = generateUrlRequestBuilders.requestToGetSubmissionConfig(resource, nonExistingCSOGuid,
                                                                                submissionId, userToken);
    }

    @Then("verify clientAppName and csoBaseUrl values are returned")
    public void verifyClientAppNameAndCsoBaseUrlValuesAreReturned() {
        jsonPath = new JsonPath(response.asString());

        assertThat(jsonPath.get("clientAppName"), is(not(emptyString())));
        assertThat(jsonPath.get("csoBaseUrl"), is(not(emptyString())));
        log.info("ClientAppName and csoBaseUrl objects from the response are correct.");
    }

    @And("verify success, error and cancel navigation urls are also returned")
    public void verifySuccessErrorAndCancelNavigationUrlsAreReturned() {
        jsonPath = new JsonPath(response.asString());

        assertNotNull(jsonPath.get("navigationUrls.success"));
        assertNotNull(jsonPath.get("navigationUrls.error"));
        assertNotNull(jsonPath.get("navigationUrls.cancel"));;
    }

    @Then("verify court details and document details are returned")
    public void verifyCourtDetailsAndDocumentDetailsAreReturned() {
        jsonPath = new JsonPath(response.asString());

        assertThat(jsonPath.get("court.location"), is(not(emptyString())));
        assertThat(jsonPath.get("court.level"), is(not(emptyString())));
        assertThat(jsonPath.get("court.class"), is(not(emptyString())));
        assertThat(jsonPath.get("court.division"), is(not(emptyString())));
        assertThat(jsonPath.get("court.fileNumber"), is(not(emptyString())));
        assertThat(jsonPath.get("court.participatingClass"), is(not(emptyString())));
        assertThat(jsonPath.get("court.locationDescription"), is(not(emptyString())));
        assertThat(jsonPath.get("court.levelDescription"), is(not(emptyString())));
        assertThat(jsonPath.get("parties"), is(not(emptyString())));
        Assert.assertEquals(Integer.valueOf(7), jsonPath.get("submissionFeeAmount"));
        log.info("Court fee and document details response have valid values");

        assertFalse(jsonPath.get("documents.name").toString().isEmpty());
        assertFalse(jsonPath.get("documents.type").toString().isEmpty());
        assertFalse(jsonPath.get("documents.subType").toString().isEmpty());
        assertFalse(jsonPath.get("documents.isAmendment").toString().isEmpty());
        assertFalse(jsonPath.get("documents.isSupremeCourtScheduling").toString().isEmpty());
        assertFalse(jsonPath.get("documents.description").toString().isEmpty());
        assertFalse(jsonPath.get("documents.statutoryFeeAmount").toString().isEmpty());
        assertNotNull(jsonPath.get("documents.statutoryFeeAmount"));
        log.info("Account type, description and name objects from the valid CSO account submission response have valid values");
    }

    @Given("{string} id with filing package path is submitted with non existing CSO account GET http request")
    public void idWithFilingPackagePathIsSubmittedWithNonExistingCSOAccountGETHttpRequest(String resource) throws IOException {
        generateUrlRequestBuilders = new GenerateUrlRequestBuilders();
        response = generateUrlRequestBuilders.requestToGetFilingPackage(resource, nonExistingCSOGuid,
                                                                        submissionId, userToken );
    }

    @Given("{string} id with filename path is submitted with non existing CSO account GET http request")
    public void idWithFilenamePathIsSubmittedWithNonExistingCSOAccountGETHttpRequest(String resource) throws IOException {
        generateUrlRequestBuilders = new GenerateUrlRequestBuilders();
        response = generateUrlRequestBuilders.requestToGetDocumentUsingFileName(resource, nonExistingCSOGuid,
                                                                                        submissionId, userToken);
    }

    @Then("Verify status code is {int} and content type is octet-stream")
    public void verifyStatusCodeIsAndContentTypeIsOctetStream(Integer int1) {
        generateUrlRequestBuilders = new GenerateUrlRequestBuilders();
        assertEquals(200, response.getStatusCode());
        assertEquals("application/octet-stream", response.getContentType());
    }

}
