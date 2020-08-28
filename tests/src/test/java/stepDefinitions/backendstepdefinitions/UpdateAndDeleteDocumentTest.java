package stepDefinitions.backendstepdefinitions;

import ca.bc.gov.open.jagefilingapi.qa.backendutils.TestUtil;
import ca.bc.gov.open.jagefilingapi.qa.frontendutils.DriverClass;
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
import static org.hamcrest.CoreMatchers.nullValue;
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
    private static final String FIRST_FILE_NAME_PATH = "/test-document.pdf";
    private static final String SECOND_FILE_NAME_PATH = "/test-document-2.pdf";
    private static final String DOCUMENT_PATH_PARAM = "/document";
    private static final String DOCUMENTS_PATH_PARAM = "/documents";
    private static final String UPDATE_DOCUMENTS_PATH_PARAM = "/update-documents";


    public Logger log = LogManager.getLogger(UpdateAndDeleteDocumentTest.class);

    @Given("initial document is posted to {string} with valid existing CSO account guid and a single pdf file")
    public void initialDocumentIsPostedToWithValidExistingCsoAccountGuidAndASinglePdfFile(String resource) throws IOException {
        generateUrlRequestBuilders = new GenerateUrlRequestBuilders();
        validExistingCSOGuid = JsonDataReader.getCsoAccountGuid().getValidExistingCSOGuid();

        response = generateUrlRequestBuilders.requestWithSinglePdfDocument(resource,validExistingCSOGuid, FIRST_FILE_NAME_PATH,
                                                                            null, null);
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

        assertEquals(200, response.getStatusCode());
        assertEquals(CONTENT_TYPE, response.getContentType());
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

        String respUrl = jsonPath.get("efilingUrl");
        Long expiryDate = jsonPath.get("expiryDate");

        List<String> respId = TestUtil.getSubmissionAndTransId(respUrl, SUBMISSION_ID, TRANSACTION_ID);

        assertEquals(submissionId, respId.get(0));
        assertEquals(validExistingCSOGuid, respId.get(1));
        assertNotNull(expiryDate);
    }

    @Given("{string} id is submitted with GET request")
    public void idIsSubmittedWithGetRequest(String resource) throws IOException {
        generateUrlRequestBuilders = new GenerateUrlRequestBuilders();
        response = generateUrlRequestBuilders.requestToGetSubmissionAndDocuments(resource, validExistingCSOGuid, submissionId,
                                                                                    null, null);
    }

    @Then("verify universal id, user details and account type values are returned and not empty")
    public void verifyUniversalIdUserDetailsAndAccountTypeValuesAreReturnedAndNotEmpty() {
        jsonPath = new JsonPath(response.asString());

        String universalId = jsonPath.get("userDetails.universalId");
        String displayName = jsonPath.get("clientApplication.displayName");
        String clientAppType = jsonPath.get("clientApplication.type");

        List<String> type = jsonPath.get("userDetails.accounts.type");
        List<String> identifier = jsonPath.get("userDetails.accounts.identifier");

        assertThat(universalId, is(not(emptyString())));
        assertThat(displayName, is(not(emptyString())));
        assertThat(clientAppType, is(not(emptyString())));
        log.info("Names and email objects from the valid CSO account submission response have valid values");

        assertFalse(type.isEmpty());
        assertFalse(identifier.isEmpty());
        log.info("Account type and identifier objects from the valid CSO account submission response have valid values");
    }

    @Given("second document is posted to {string}")
    public void secondDocumentIsPostedToDocumentSubmission(String resource) throws IOException {
        generateUrlRequestBuilders = new GenerateUrlRequestBuilders();
        validExistingCSOGuid = JsonDataReader.getCsoAccountGuid().getValidExistingCSOGuid();

        response = generateUrlRequestBuilders.requestWithSinglePdfDocument(resource,validExistingCSOGuid, SECOND_FILE_NAME_PATH,
                                                                            submissionId, DOCUMENTS_PATH_PARAM);
    }

    @And("verify navigation urls are returned")
    public void verifyNavigationUrlsAreReturned() {
        jsonPath = new JsonPath(response.asString());

        String successUrl = jsonPath.get("navigation.success.url");
        String errorUrl = jsonPath.get("navigation.error.url");
        String cancelUrl = jsonPath.get("navigation.cancel.url");

        assertNotNull(successUrl);
        assertNotNull(errorUrl);
        assertNotNull(cancelUrl);
    }

    @Given("{string} id with filename is submitted with GET http request")
    public void idWithFilenameIsSubmittedWithGETHttpRequest(String resource) throws IOException {
        generateUrlRequestBuilders = new GenerateUrlRequestBuilders();
        response = generateUrlRequestBuilders.requestToUpdateDocumentProperties(resource,validExistingCSOGuid,
                submissionId, DOCUMENT_PATH_PARAM);
    }

    @Then("validated status code is {int} and content type is not json")
    public void verifyStatusCodeIsAndContentTypeIsNotJson(Integer int1) {
        assertEquals(200, response.getStatusCode());
        assertEquals("application/octet-stream", response.getContentType());
    }

    @Given("{string} id with payload is submitted to upload the document properties")
    public void idWithPayloadIsSubmittedToUploadTheDocumentProperties(String resource) throws IOException {
        generateUrlRequestBuilders = new GenerateUrlRequestBuilders();
        response = generateUrlRequestBuilders.requestToUpdateDocumentProperties(resource,validExistingCSOGuid,
                                                                                    submissionId, UPDATE_DOCUMENTS_PATH_PARAM);
    }

    @Then("verify document properties are updated")
    public void verifyDocumentPropertiesAreUpdated() {
        jsonPath = new JsonPath(response.asString());

        assertFalse(jsonPath.get("documents.name").toString().isEmpty());
        assertFalse(jsonPath.get("documents.type").toString().isEmpty());
        assertFalse(jsonPath.get("documents.subType").toString().isEmpty());
        assertFalse(jsonPath.get("documents.isAmendment").toString().isEmpty());
        assertFalse(jsonPath.get("documents.isSupremeCourtScheduling").toString().isEmpty());
        assertFalse(jsonPath.get("documents.data").toString().isEmpty());
        assertFalse(jsonPath.get("documents.description").toString().isEmpty());
        assertFalse(jsonPath.get("documents.statutoryFeeAmount").toString().isEmpty());
        assertFalse(jsonPath.get("documents.mimeType").toString().isEmpty());
        log.info("Document properties are updated");

    }

    @Given("{string} id is submitted with DELETE http request")
    public void idIsSubmittedWithDeleteHttpRequest(String resource) throws IOException {
        generateUrlRequestBuilders = new GenerateUrlRequestBuilders();
        response = generateUrlRequestBuilders.requestToDeleteDocuments(resource, validExistingCSOGuid, submissionId);
    }
}
