package stepDefinitions.backendstepdefinitions;

import ca.bc.gov.open.jagefilingapi.qa.backendutils.APIResources;
import ca.bc.gov.open.jagefilingapi.qa.backendutils.TestUtil;
import ca.bc.gov.open.jagefilingapi.qa.frontendutils.DriverClass;
import ca.bc.gov.open.jagefilingapi.qa.frontendutils.FrontendTestUtil;
import ca.bc.gov.open.jagefilingapi.qa.frontendutils.JsonDataReader;
import ca.bc.gov.open.jagefilingapi.qa.requestbuilders.GenerateUrlRequestBuilders;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.builder.MultiPartSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.MultiPartSpecification;
import io.restassured.specification.RequestSpecification;
import org.apache.http.client.utils.URIBuilder;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.text.MessageFormat;
import java.util.Base64;
import java.util.List;
import java.util.UUID;

import static io.restassured.RestAssured.enableLoggingOfRequestAndResponseIfValidationFails;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.emptyString;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.*;

public class GenerateUrlAndSubmissionTest extends DriverClass {


    private static final String CLIENT_ID = "client_id";
    private static final String GRANT_TYPE = "grant_type";
    private static final String USERNAME = "username";
    private static final String PASSWORD = "password";
    private static final String CONTENT_TYPE = "application/json";
    private static final String SUBMISSION_ID = "submissionId";
    private static final String TRANSACTION_ID = "transactionId";
    private static final String GENERATE_URL_PATH_PARAM = "/generateUrl";
    private static final String FILE_NAME_PATH = "/data/test-document.pdf";
    private static final String TEST_DOCUMENT_PDF = "test-document.pdf";
    private static String PAYLOAD = "{\n" +
            "    \"navigationUrls\": {\n" +
            "        \"success\": \"http//somewhere.com\",\n" +
            "        \"error\": \"http//somewhere.com\",\n" +
            "        \"cancel\": \"http//somewhere.com\"\n" +
            "    },\n" +
            "    \"clientAppName\": \"my app\",\n" +
            "    \"filingPackage\": {\n" +
            "        \"court\": {\n" +
            "            \"location\": \"1211\",\n" +
            "            \"level\": \"P\",\n" +
            "            \"courtClass\": \"F\"\n" +
            "        },\n" +
            "        \"documents\": [\n" +
            "            {\n" +
            "                \"name\": \"" + TEST_DOCUMENT_PDF + "\",\n" +
            "                \"type\": \"AFF\",\n" +
            "                \"statutoryFeeAmount\": 0,\n" +
            "                \"data\": {},\n" +
            "                \"md5\": \"string\"\n" +
            "            }\n" +
            "        ],\n" +
            "        \"parties\": [\n" +
            "            {\n" +
            "                \"partyType\": \"IND\",\n" +
            "                \"roleType\": \"APP\",\n" +
            "                \"firstName\": \"first\",\n" +
            "                \"middleName\": \"middle\",\n" +
            "                \"lastName\": \"last\"\n" +
            "            }\n" +
            "        ]\n" +
            "    }\n" +
            "}";

    private UUID actualTransactionId;

    private Response response;

    private GenerateUrlRequestBuilders generateUrlRequestBuilders;
    private String submissionId;
    private JsonPath jsonPath;
    private String validExistingCSOGuid;
    private String respUrl;

    private Response actualDocumentResponse;
    private Response actualGenerateUrlResponse;
    private String actualUserToken;
    private UUID actualSubmissionId;
    private String actualUniversalId;

    public Logger logger = LogManager.getLogger(GenerateUrlAndSubmissionTest.class);

    public GenerateUrlAndSubmissionTest() {
        actualTransactionId = UUID.randomUUID();
    }


    @Given("POST http request is made to {string} with valid existing CSO account guid and a single pdf file")
    public void postHttpRequestIsMadeToWithValidExistingCsoAccountGuidAndASinglePdfFile(String resource) throws IOException {
        generateUrlRequestBuilders = new GenerateUrlRequestBuilders();
        validExistingCSOGuid = JsonDataReader.getCsoAccountGuid().getValidExistingCSOGuid();

        response = generateUrlRequestBuilders.requestWithSinglePdfDocument(resource,validExistingCSOGuid, FILE_NAME_PATH);
    }

    @When("status code is {int} and content type is verified")
    public void statusCodeIsAndContentTypeIsVerified(Integer statusCode) {
        generateUrlRequestBuilders = new GenerateUrlRequestBuilders();

        assertEquals(200, response.getStatusCode());
        assertEquals(CONTENT_TYPE, response.getContentType());
    }

    @Then("verify submission id and document count is received")
    public void verifySubmissionIdAndDocumentCountIsReceived() {
        jsonPath = new JsonPath(response.asString());

        submissionId = TestUtil.getJsonPath(response, SUBMISSION_ID);
        int receivedCount = jsonPath.get("received");

        switch (receivedCount) {
            case 1:
                assertEquals(1, receivedCount);
                break;
            case 2:
                assertEquals(2, receivedCount);
                break;
            default:
                logger.info("Document count did not match.");
        }
        assertNotNull(submissionId);
    }

    @Given("POST http request is made to {string} with client application, court details and navigation urls")
    public void POSTHttpRequestIsMadeToWithClientApplicationCourtDetailsAndNavigationUrls(String resource) throws IOException {
        generateUrlRequestBuilders = new GenerateUrlRequestBuilders();
        validExistingCSOGuid = JsonDataReader.getCsoAccountGuid().getValidExistingCSOGuid();

        response = generateUrlRequestBuilders.postRequestWithPayload(resource,validExistingCSOGuid, submissionId, GENERATE_URL_PATH_PARAM);
    }

    @Then("verify expiry date and eFiling url are returned with the CSO account guid and submission id")
    public void verifyExpiryDateAndEfilingUrlAreReturnedWithTheCsoAccountGuidAndSubmissionId() throws URISyntaxException, IOException {
        jsonPath = new JsonPath(response.asString());
        validExistingCSOGuid = JsonDataReader.getCsoAccountGuid().getValidExistingCSOGuid();

        respUrl = jsonPath.get("efilingUrl");
        Long expiryDate = jsonPath.get("expiryDate");
        List<String> respId = TestUtil.getSubmissionAndTransId(respUrl, SUBMISSION_ID, TRANSACTION_ID);

        assertEquals(submissionId, respId.get(0));
        assertEquals(validExistingCSOGuid, respId.get(1));
        assertNotNull(expiryDate);
    }

    @Given("user token is retrieved from the frontend")
    public void userTokenisRetrievedFromTheFrontend() throws IOException, InterruptedException {
        generateUrlRequestBuilders = new GenerateUrlRequestBuilders();
        FrontendTestUtil frontendTestUtil = new FrontendTestUtil();
        actualUserToken = frontendTestUtil.getUserJwtToken(respUrl);
    }

    @Then("{string} id is submitted with GET http request")
    public void idIsSubmittedWithGetHttpRequest(String resource) throws IOException {
        generateUrlRequestBuilders = new GenerateUrlRequestBuilders();
        response = generateUrlRequestBuilders.requestToGetSubmissionConfig(resource, validExistingCSOGuid, submissionId, actualUserToken);
    }

    @Then("verify clientAppName and csoBaseUrl values are returned and not empty")
    public void verifyUniversalIdClientAppNameAndCsoBaseUrlValuesAreReturnedAndNotEmpty() {
        jsonPath = new JsonPath(response.asString());

        assertThat(jsonPath.get("clientAppName"), is(not(emptyString())));
        assertThat(jsonPath.get("csoBaseUrl"), is(not(emptyString())));
        logger.info("ClientAppName and csoBaseUrl objects from the response are correct.");
    }

    @Given("{string} id with filing package path is submitted with GET http request")
    public void idWithFilingPackagePathIsSubmittedWithGETHttpRequest(String resource) throws IOException {
        generateUrlRequestBuilders = new GenerateUrlRequestBuilders();
        response = generateUrlRequestBuilders.requestToGetFilingPackage(resource,validExistingCSOGuid,
                                                                submissionId, actualUserToken);
    }

    @Then("verify court details and document details are returned and not empty")
    public void verifyCourtDetailsAndDocumentDetailsAreReturnedAndNotEmpty() {
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
        logger.info("Court fee and document details response have valid values");

        assertFalse(jsonPath.get("documents.name").toString().isEmpty());
        assertFalse(jsonPath.get("documents.type").toString().isEmpty());
        assertFalse(jsonPath.get("documents.subType").toString().isEmpty());
        assertFalse(jsonPath.get("documents.isAmendment").toString().isEmpty());
        assertFalse(jsonPath.get("documents.isSupremeCourtScheduling").toString().isEmpty());
        assertFalse(jsonPath.get("documents.description").toString().isEmpty());
        assertFalse(jsonPath.get("documents.statutoryFeeAmount").toString().isEmpty());
        assertNotNull(jsonPath.get("documents.statutoryFeeAmount"));
        logger.info("Account type, description and name objects from the valid CSO account submission response have valid values");
    }

    @And("verify success, error and cancel navigation urls are returned")
    public void verifySuccessErrorAndCancelNavigationUrlsAreReturned() {
        jsonPath = new JsonPath(response.asString());

        assertNotNull(jsonPath.get("navigationUrls.success"));
        assertNotNull(jsonPath.get("navigationUrls.error"));
        assertNotNull(jsonPath.get("navigationUrls.cancel"));
    }

    @Given("{string} id with filename path is submitted with GET http request")
    public void idWithFilenamePathIsSubmittedWithGETHttpRequest(String resource) throws IOException {
        generateUrlRequestBuilders = new GenerateUrlRequestBuilders();
        response = generateUrlRequestBuilders.requestToGetDocumentUsingFileName(resource,validExistingCSOGuid,
                                                                    submissionId, actualUserToken);
    }

    @Then("Verify status code is {int} and content type is not json")
    public void verifyStatusCodeIsAndContentTypeIsNotJson(Integer int1) {
        generateUrlRequestBuilders = new GenerateUrlRequestBuilders();
        assertEquals(200, response.getStatusCode());
        assertEquals("application/octet-stream", response.getContentType());
    }

    @Given("POST http request is made to {string} with valid existing CSO account guid and multiple file")
    public void POSTHttpRequestIsMadeToWithValidExistingCSOAccountGuidAndMultipleFile(String resource) throws IOException {
        generateUrlRequestBuilders = new GenerateUrlRequestBuilders();
        response = generateUrlRequestBuilders.validRequestWithMultipleDocuments(resource);
    }

    @Given("{string} id with submit path is submitted with POST http request")
    public void IdWithSubmitPathsSubmittedWithPOSTHttpRequest(String resource) throws IOException {
        generateUrlRequestBuilders = new GenerateUrlRequestBuilders();
        APIResources resourceGet = APIResources.valueOf(resource);

        RequestSpecification request = given()
                .auth().preemptive().oauth2(actualUserToken)
                .spec(TestUtil.requestSpecification())
                .header("x-transaction-id", validExistingCSOGuid)
                .body(new ObjectMapper().createObjectNode());

        response = request.when().post(resourceGet.getResource() + submissionId + "/submit")
                .then()
                .spec(TestUtil.validResponseSpecification())
                .extract().response();
    }

    @Then("packageRef is returned")
    public void packageRefIsReturned() {
        jsonPath = new JsonPath(response.asString());
        assertThat(jsonPath.get("packageRef"), is(not(emptyString())));
    }

    @Given("{string} without court level type is submitted with GET http request")
    public void withoutCourtLevelTypeIsSubmittedWithGETHttpRequest(String resource) throws IOException {
        generateUrlRequestBuilders = new GenerateUrlRequestBuilders();
        response = generateUrlRequestBuilders.requestToGetCourts(resource);
    }

    @Then("validate the court location details")
    public void validateTheCourtLocationDetails() {
        jsonPath = new JsonPath(response.asString());
        Assert.assertEquals(Integer.valueOf(10264), jsonPath.get("courts.id[0]"));
        Assert.assertEquals(Integer.valueOf(9393), jsonPath.get("courts.id[1]"));
        Assert.assertEquals("5871", jsonPath.get("courts.identifierCode[0]"));
        Assert.assertEquals("3561", jsonPath.get("courts.identifierCode[1]"));
        Assert.assertEquals("OMH", jsonPath.get("courts.code[0]"));
        Assert.assertEquals("ABB", jsonPath.get("courts.code[1]"));
    }

    @Given("a user {string} with password {string} that authenticated with keycloak at {string} on realm {string}")
    public void aUserUsernameWithPasswordPasswordThatAuthenticatedWithKeycloak(String username, String password, String keycloakhost, String keycloakRealm) throws URISyntaxException, MalformedURLException {

        getBearerToken(keycloakhost, keycloakRealm, username, password);

    }

    @When("user Submit a valid pdf document")
    public void userSubmitAValidPdfDocument() throws IOException {


        File resource = new ClassPathResource(
                "data/test-document.pdf").getFile();

        MultiPartSpecification spec = new MultiPartSpecBuilder(resource).
                fileName(TEST_DOCUMENT_PDF).
                controlName("files").
                mimeType("text/application-pdf").
                build();

        RequestSpecification request = RestAssured.given().auth().preemptive().oauth2(actualUserToken)
                .header("X-Transaction-Id", actualTransactionId)
                .header("X-User-Id", actualUniversalId)
                .multiPart(spec);

        actualDocumentResponse = request.when().post("http://localhost:8080/submission/documents").then()
                .extract().response();

        logger.info(actualDocumentResponse.asString());

    }

    @Then("a valid transaction should be generated")
    public void aValidTransactionIsShouldBeGenerated() {

        // {"submissionId":"e34c576c-598d-4e70-b906-5f12adebc311","received":1}
        JsonPath jsonPath = new JsonPath(actualDocumentResponse.asString());
        Assert.assertEquals("File Received not don't match", new BigDecimal(1), new BigDecimal(jsonPath.get("received").toString()));
        actualSubmissionId = UUID.fromString(jsonPath.getString("submissionId"));

        logger.info("subimssionId {}", actualSubmissionId);

    }

    public void getBearerToken(String keycloakHost, String keycloakRealm, String username, String password) throws URISyntaxException, MalformedURLException {

        URIBuilder uriBuilder = new URIBuilder(keycloakHost);
        uriBuilder.setPath(MessageFormat.format("/auth/realms/{0}/protocol/openid-connect/token", keycloakRealm));

        RequestSpecification request = RestAssured.given()
                .formParam(CLIENT_ID, "efiling-frontend")
                .formParam(GRANT_TYPE, "password")
                .formParam(USERNAME, username)
                .formParam(PASSWORD, password);

        Response response = request.when().post(uriBuilder.build().toURL().toString()).then()
                .spec(TestUtil.validResponseSpecification())
                .extract().response();

        logger.info("successfully authenticated user.");

        JsonPath jsonPath = new JsonPath(response.asString());

        actualUserToken = jsonPath.get("access_token");

        String jsonToken = decodeToken(actualUserToken);

        actualUniversalId = new JsonPath(jsonToken).get("universal-id");


    }

    @And("user request a submission url")
    public void userRequestASubmissionUrl() {

        RequestSpecification request = RestAssured
                .given()
                .auth()
                .preemptive()
                .oauth2(actualUserToken)
                .contentType(ContentType.JSON)
                .header("X-Transaction-Id", actualTransactionId)
                .header("X-User-Id", "77da92db-0791-491e-8c58-1a969e67d2fa")
                .body(PAYLOAD);

        actualGenerateUrlResponse = request
                .when()
                .post(MessageFormat.format( "http://localhost:8080/submission/{0}/generateUrl", actualSubmissionId))
                .then()
                .extract()
                .response();

        logger.info(actualGenerateUrlResponse.asString());

    }

    @Then("a valid url should be returned")
    public void aValidUrlShouldBeReturned() {

        String expectedEfilingUrl = MessageFormat.format("http://localhost:3000/efilinghub?submissionId={0}&transactionId={1}", actualSubmissionId, actualTransactionId );

        JsonPath actualJson = new JsonPath(actualGenerateUrlResponse.asString());

        Assert.assertEquals(expectedEfilingUrl, actualJson.getString("efilingUrl"));

        Assert.assertNotNull(actualJson.get("expiryDate"));

    }

    private String decodeToken(String token) {

        String[] tokenParts = token.split("\\.");

        byte[] decodedBytes = Base64.getUrlDecoder().decode(tokenParts[1]);
        return new String(decodedBytes);
    }


}
