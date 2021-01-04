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

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.emptyString;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.*;

public class UpdateAndDeleteDocumentTest extends DriverClass {

    private Response response;
    private GenerateUrlRequestBuilders generateUrlRequestBuilders;
    private String submissionId;
    private JsonPath jsonPath;
    private String validExistingCSOGuid;
    private static final String CONTENT_TYPE = "application/json";
    private static final String SUBMISSION_ID = "submissionId";
    private static final String TRANSACTION_ID = "transactionId";
    private static final String GENERATE_URL_PATH_PARAM = "/generateUrl";
    private static final String FIRST_FILE_NAME_PATH = "/data/test-document.pdf";
    private static final String SECOND_FILE_NAME_PATH = "/test-document-2.pdf";
    private String respUrl;
    private String userToken;

    public Logger log = LogManager.getLogger(UpdateAndDeleteDocumentTest.class);

    @Given("initial document is posted to {string} with valid existing CSO account guid and a single pdf file")
    public void initialDocumentIsPostedToWithValidExistingCsoAccountGuidAndASinglePdfFile(String resource) throws IOException {
        generateUrlRequestBuilders = new GenerateUrlRequestBuilders();
        validExistingCSOGuid = JsonDataReader.getCsoAccountGuid().getValidExistingCSOGuid();

        response = generateUrlRequestBuilders.requestWithSinglePdfDocument(resource,validExistingCSOGuid, FIRST_FILE_NAME_PATH);
    }

    @When("validated status code is {int} and content type")
    public void validateStatusCodeIsAndContentType(Integer statusCode) {
        generateUrlRequestBuilders = new GenerateUrlRequestBuilders();

        statusCode = response.getStatusCode();
        switch (statusCode) {
            case 200:
                assertEquals(200, response.getStatusCode());
                assertEquals(CONTENT_TYPE, response.getContentType());
                break;
            case 404:
                assertEquals(404, response.getStatusCode());
                assertEquals(CONTENT_TYPE, response.getContentType());
                break;
            default:
                log.info("Status code and content type did not match.");
        }

        assertEquals(CONTENT_TYPE, response.getContentType());
    }

    @When("validated status code is {int}")
    public void validateStatusCode(Integer statusCode) {
        generateUrlRequestBuilders = new GenerateUrlRequestBuilders();

        assertEquals(200, response.getStatusCode());
    }

    @Then("verify submission id and document count is returned")
    public void verifySubmissionIdAndDocumentCountIsReturned() {
        jsonPath = new JsonPath(response.asString());

        submissionId = TestUtil.getJsonPath(response, SUBMISSION_ID);
        int receivedCount = jsonPath.get("received");

        assertEquals(1, receivedCount);
        assertNotNull(submissionId);
    }

    @Given("POST request is made to {string} with client application, court details and navigation urls")
    public void POSTRequestIsMadeToWithClientApplicationCourtDetailsAndNavigationUrls(String resource) throws IOException {
        generateUrlRequestBuilders = new GenerateUrlRequestBuilders();
        validExistingCSOGuid = JsonDataReader.getCsoAccountGuid().getValidExistingCSOGuid();

        response = generateUrlRequestBuilders.postRequestWithPayload(resource,validExistingCSOGuid,
                                                                        submissionId, GENERATE_URL_PATH_PARAM);
    }

    @Then("verify expiry date and eFiling url are returned with the submission id")
    public void verifyExpiryDateAndEfilingUrlAreReturnedWithTheSubmissionId() throws URISyntaxException, IOException {
        jsonPath = new JsonPath(response.asString());
        validExistingCSOGuid = JsonDataReader.getCsoAccountGuid().getValidExistingCSOGuid();

         respUrl = jsonPath.get("efilingUrl");
        Long expiryDate = jsonPath.get("expiryDate");

        List<String> respId = TestUtil.getSubmissionAndTransId(respUrl, SUBMISSION_ID, TRANSACTION_ID);

        assertEquals(submissionId, respId.get(0));
        assertEquals(validExistingCSOGuid, respId.get(1));
        assertNotNull(expiryDate);
    }

    @Given("retrieve jwt from the frontend")
    public void retrieveJwtFromTheFrontend() throws IOException, InterruptedException {
        generateUrlRequestBuilders = new GenerateUrlRequestBuilders();
        FrontendTestUtil frontendTestUtil = new FrontendTestUtil();
        userToken = frontendTestUtil.getUserJwtToken(respUrl);
    }

    @Given("{string} id is submitted with GET request")
    public void idIsSubmittedWithGetRequest(String resource) throws IOException {
        generateUrlRequestBuilders = new GenerateUrlRequestBuilders();

        response = generateUrlRequestBuilders.requestToGetSubmissionConfig(resource, validExistingCSOGuid,
                                                                                submissionId, userToken);
    }

    @Then("ClientAppName and csoBaseUrl values are verified")
    public void clientAppNameAndCsoBaseUrlValuesAreVerified() {
        jsonPath = new JsonPath(response.asString());

        assertThat(jsonPath.get("clientAppName"), is(not(emptyString())));
        assertThat(jsonPath.get("csoBaseUrl"), is(not(emptyString())));
        log.info("ClientAppName and csoBaseUrl objects from the response are correct.");
    }

    @Given("second document is posted to {string}")
    public void secondDocumentIsPostedToDocumentSubmission(String resource) throws IOException {
        generateUrlRequestBuilders = new GenerateUrlRequestBuilders();
        validExistingCSOGuid = JsonDataReader.getCsoAccountGuid().getValidExistingCSOGuid();

        response = generateUrlRequestBuilders.requestWithSinglePdfDocument(resource,validExistingCSOGuid, SECOND_FILE_NAME_PATH);
    }

    @And("verify navigation urls are returned")
    public void verifyNavigationUrlsAreReturned() {
        jsonPath = new JsonPath(response.asString());
        assertNotNull(jsonPath.get("navigationUrls.success"));
        assertNotNull(jsonPath.get("navigationUrls.error"));
        assertNotNull(jsonPath.get("navigationUrls.cancel"));
    }

    @Given("{string} id with filename is submitted with GET http request")
    public void idWithFilenameIsSubmittedWithGETHttpRequest(String resource) throws IOException {
        generateUrlRequestBuilders = new GenerateUrlRequestBuilders();
        response = generateUrlRequestBuilders.requestToGetDocumentUsingSecondFileName(resource,validExistingCSOGuid,
                                                                submissionId, userToken);
    }

    @Then("validated status code is {int} and content type is not json")
    public void verifyStatusCodeIsAndContentTypeIsNotJson(Integer int1) {
        assertEquals(200, response.getStatusCode());
        assertEquals("application/octet-stream", response.getContentType());
    }

    @Given("{string} id with payload is submitted to update the document properties")
    public void idWithPayloadIsSubmittedToUpdateTheDocumentProperties(String resource) throws IOException {
        generateUrlRequestBuilders = new GenerateUrlRequestBuilders();
        response = generateUrlRequestBuilders.requestToUpdateDocumentProperties(resource,validExistingCSOGuid,
                                                submissionId, userToken);
    }

    @Then("verify document properties are updated")
    public void verifyDocumentPropertiesAreUpdated() {
        jsonPath = new JsonPath(response.asString());
        int statutoryFee = jsonPath.get("documents.statutoryFeeAmount");

        assertFalse(jsonPath.get("documents.name").toString().isEmpty());
        assertFalse(jsonPath.get("documents.type").toString().isEmpty());
        assertTrue(jsonPath.get("documents.isAmendment"));
        assertTrue(jsonPath.get("documents.isSupremeCourtScheduling"));
        assertFalse(jsonPath.get("documents.description").toString().isEmpty());
        assertEquals(0 , statutoryFee);
        assertFalse(jsonPath.get("documents.mimeType").toString().isEmpty());
        log.info("Document properties are updated");
    }

    @Given("{string} id is submitted with DELETE http request")
    public void idIsSubmittedWithDeleteHttpRequest(String resource) throws IOException {
        generateUrlRequestBuilders = new GenerateUrlRequestBuilders();
        response = generateUrlRequestBuilders.requestToDeleteDocuments(resource, validExistingCSOGuid, submissionId, userToken);
    }
}
