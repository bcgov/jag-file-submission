package stepDefinitions.backendstepdefinitions;

import ca.bc.gov.open.jagefilingapi.qa.backend.generateurlpayload.GenerateUrlPayload;
import ca.bc.gov.open.jagefilingapi.qa.backendutils.APIResources;
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
import io.restassured.specification.RequestSpecification;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;

public class GenerateUrlAndSubmissionForNonExistingGuidTest extends DriverClass {

    private Response response;
    private GenerateUrlRequestBuilders generateUrlRequestBuilders;
    private String submissionId;
    private JsonPath jsonPath;
    private String nonExistingCSOGuid;
    private static final String CONTENT_TYPE = "application/json";
    private static final String X_TRANSACTION_ID = "X-Transaction-Id";
    private static final String X_USER_ID = "X-User-Id";
    private static final String SUBMISSION_ID = "submissionId";
    private static final String TRANSACTION_ID = "transactionId";
    private String userToken;

    public Logger log = LogManager.getLogger(GenerateUrlAndSubmissionForNonExistingGuidTest.class);

    @Given("POST http request is made to {string} with non existing CSO account guid and a single pdf file")
    public void postHttpRequestIsMadeToWithNonExistingCSOAccountGuidAndASinglePdfFile(String resource) throws IOException {
        generateUrlRequestBuilders = new GenerateUrlRequestBuilders();

        response = generateUrlRequestBuilders.requestWithNonExistingCSOAccountGuid(resource);
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
        GenerateUrlPayload payloadData = new GenerateUrlPayload();
        APIResources resourceGet = APIResources.valueOf(resource);

        nonExistingCSOGuid = JsonDataReader.getCsoAccountGuid().getNonExistingCSOGuid();
        String validUserid = JsonDataReader.getCsoAccountGuid().getValidUserId();

        generateUrlRequestBuilders = new GenerateUrlRequestBuilders();
        response = generateUrlRequestBuilders.getBearerToken();
        jsonPath = new JsonPath(response.asString());
        String accessToken = jsonPath.get("access_token");

        RequestSpecification request = given().auth().preemptive().oauth2(accessToken)
                .spec(TestUtil.requestSpecification())
                .header(X_TRANSACTION_ID, nonExistingCSOGuid)
                .header(X_USER_ID,validUserid )
                .body(payloadData.validGenerateUrlPayload());
        response = request.when().post(resourceGet.getResource() + submissionId + "/generateUrl")
                .then()
                .spec(TestUtil.responseSpecification())
                .extract().response();
    }

    @Then("verify expiry date and eFiling url are returned with non existing CSO account guid and submission id")
    public void verifyExpiryDateAndEFilingUrlAreReturnedWithNonExistingCSOAccountGuidAndSubmissionId() throws URISyntaxException {
        jsonPath = new JsonPath(response.asString());

        String respUrl = jsonPath.get("efilingUrl");
        Long expiryDate = jsonPath.get("expiryDate");

        List<NameValuePair> params = URLEncodedUtils.parse(new URI(respUrl), StandardCharsets.UTF_8);
        String respSubId = null;
        String respTransId = null;

        for (NameValuePair param : params) {
            if (param.getName().equals(SUBMISSION_ID)) {
                respSubId = param.getValue();
            } else if (param.getName().equals(TRANSACTION_ID)) {
                respTransId = param.getValue();
            }
        }
        assertEquals(submissionId, respSubId);
        assertEquals(nonExistingCSOGuid, respTransId);
        assertNotNull(expiryDate);
    }

    @Given("{string} id is submitted with non existing CSO account GET http request")
    public void idIsSubmittedWithNonExistingCsoAccountGetHttpRequest(String resource) throws IOException {
        APIResources resourceGet = APIResources.valueOf(resource);
        nonExistingCSOGuid = JsonDataReader.getCsoAccountGuid().getNonExistingCSOGuid();

        generateUrlRequestBuilders = new GenerateUrlRequestBuilders();
        userToken = generateUrlRequestBuilders.getUserJwtToken();

        RequestSpecification request = given().auth().preemptive().oauth2(userToken)
                .spec(TestUtil.requestSpecification())
                .header(X_TRANSACTION_ID, nonExistingCSOGuid);
        response = request.when().get(resourceGet.getResource() + submissionId)
                .then()
                .spec(TestUtil.responseSpecification())
                .extract().response();
    }

    @Then("verify universal id, account type and identifier values are returned")
    public void verifyUniversalIdAccountTypeAndIdentifierValuesAreReturned() {
        jsonPath = new JsonPath(response.asString());

        String universalId = jsonPath.get("userDetails.universalId");
        String displayName = jsonPath.get("clientApplication.displayName");
        String clientAppType = jsonPath.get("clientApplication.type");

        List<String> type = jsonPath.get("userDetails.accounts.type");
        List<String> identifier = jsonPath.get("userDetails.accounts.identifier");

        assertThat(universalId, is(not(emptyString())));
        assertThat(displayName, is(not(emptyString())));
        assertThat(clientAppType, is(not(emptyString())));
        log.info("Universal id, display name and type for non existing CSO account submission response have valid values");

        assertFalse(type.isEmpty());
        assertFalse(identifier.isEmpty());
        log.info("Account type and identifier objects from the valid CSO account submission response have valid values");
    }

    @And("verify success, error and cancel navigation urls are also returned")
    public void verifySuccessErrorAndCancelNavigationUrlsAreReturned() {
        jsonPath = new JsonPath(response.asString());

        String successUrl = jsonPath.get("navigation.success.url");
        String errorUrl = jsonPath.get("navigation.error.url");
        String cancelUrl = jsonPath.get("navigation.cancel.url");

        assertNotNull(successUrl);
        assertNotNull(errorUrl);
        assertNotNull(cancelUrl);
    }

    @Then("verify court details and document details are returned")
    public void verifyCourtDetailsAndDocumentDetailsAreReturned() {
        jsonPath = new JsonPath(response.asString());

        String location = jsonPath.get("court.location");
        String level = jsonPath.get("court.level");
        String courtClass = jsonPath.get("court.class");
        String division = jsonPath.get("court.division");
        String fileNumber = jsonPath.get("court.fileNumber");
        String participatingClass = jsonPath.get("court.participatingClass");
        String locationDescription = jsonPath.get("court.locationDescription");
        String levelDescription = jsonPath.get("court.levelDescription");
        float submissionFeeAmount = jsonPath.get("submissionFeeAmount");
        String parties = jsonPath.get("parties");

        List<String> name = jsonPath.get("documents.name");
        List<String> type = jsonPath.get("documents.type");
        List<String> subType = jsonPath.get("documents.subType");
        List<String> isAmendment = jsonPath.get("documents.isAmendment");
        List<String> isSupremeCourtScheduling = jsonPath.get("documents.isSupremeCourtScheduling");
        List<String> description = jsonPath.get("documents.description");
        List<String> statutoryFeeAmount = jsonPath.get("documents.statutoryFeeAmount");
        List<String> mimeType = jsonPath.get("documents.mimeType");

        assertThat(location, is(not(emptyString())));
        assertThat(level, is(not(emptyString())));
        assertThat(courtClass, is(not(emptyString())));
        assertThat(division, is(not(emptyString())));
        assertThat(fileNumber, is(not(emptyString())));
        assertThat(participatingClass, is(not(emptyString())));
        assertThat(locationDescription, is(not(emptyString())));
        assertThat(levelDescription, is(not(emptyString())));
        assertThat(parties, is(not(emptyString())));
        assertEquals(7.00, submissionFeeAmount, 0);
        log.info("Court fee and document details response have valid values");

        assertFalse(name.isEmpty());
        assertFalse(type.isEmpty());
        assertFalse(subType.isEmpty());
        assertFalse(isAmendment.isEmpty());
        assertFalse(isSupremeCourtScheduling.isEmpty());
        assertFalse(description.isEmpty());
        assertFalse(mimeType.isEmpty());
        assertNotNull(statutoryFeeAmount);
        log.info("Account type, description and name objects from the valid CSO account submission response have valid values");
    }

    @Given("{string} id with filing package path is submitted with non existing CSO account GET http request")
    public void idWithFilingPackagePathIsSubmittedWithNonExistingCSOAccountGETHttpRequest(String resource) throws IOException {
        APIResources resourceGet = APIResources.valueOf(resource);

        generateUrlRequestBuilders = new GenerateUrlRequestBuilders();

        RequestSpecification request = given().auth().preemptive().oauth2(userToken)
                .spec(TestUtil.requestSpecification())
                .header(X_TRANSACTION_ID, nonExistingCSOGuid);

        response = request.when().get(resourceGet.getResource() + submissionId + "/filing-package")
                .then()
                .spec(TestUtil.responseSpecification())
                .extract().response();
    }

    @Given("{string} id with filename path is submitted with non existing CSO account GET http request")
    public void idWithFilenamePathIsSubmittedWithNonExistingCSOAccountGETHttpRequest(String resource) throws IOException {
        APIResources resourceGet = APIResources.valueOf(resource);

        RequestSpecification request = given().auth().preemptive().oauth2(userToken).spec(TestUtil.requestSpecification())
                .header(X_TRANSACTION_ID, nonExistingCSOGuid);

        response = request.when().get(resourceGet.getResource() + submissionId + "/document" + "/test-document.pdf")
                .then()
               // .spec(TestUtil.documentValidResponseSpecification())
                .extract().response();
    }
}
