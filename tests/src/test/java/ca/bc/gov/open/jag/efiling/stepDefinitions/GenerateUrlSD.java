package ca.bc.gov.open.jag.efiling.stepDefinitions;

import ca.bc.gov.open.PayloadHelper;
import ca.bc.gov.open.TokenHelper;
import ca.bc.gov.open.jagefilingapi.qa.backendutils.TestUtil;
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
import stepDefinitions.backendstepdefinitions.GenerateUrlAndSubmissionTest;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.text.MessageFormat;
import java.util.Base64;
import java.util.UUID;

public class GenerateUrlSD {


    private static final String TEST_DOCUMENT_PDF = "test-document.pdf";


    private UUID actualTransactionId;
    private Response actualDocumentResponse;
    private Response actualGenerateUrlResponse;
    private String actualUserToken;
    private UUID actualSubmissionId;
    private String actualUniversalId;

    public Logger logger = LogManager.getLogger(GenerateUrlAndSubmissionTest.class);

    public GenerateUrlSD() {
        actualTransactionId = UUID.randomUUID();
    }

    @Given("admin account {string}:{string} that authenticated with keycloak at {string} on realm {string}")
    public void adminAccountThatAuthenticatedWithKeycloakAtOnRealm(String username, String password, String keycloakHost, String keycloakRealm) {

        setBearerToken(keycloakHost, keycloakRealm, username, password);

    }

    @When("user Submit a valid pdf document")
    public void userSubmitAValidPdfDocument() throws IOException {

        logger.info("Submitting document upload request");

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

        logger.info("Api response status code: {}", actualDocumentResponse.getStatusCode());
        logger.info("Api response: {}", actualDocumentResponse.asString());

    }

    @Then("a valid transaction should be generated")
    public void aValidTransactionIsShouldBeGenerated() {

        logger.info("Asserting document upload response");

        JsonPath jsonPath = new JsonPath(actualDocumentResponse.asString());
        Assert.assertEquals("File Received not don't match", new BigDecimal(1), new BigDecimal(jsonPath.get("received").toString()));
        actualSubmissionId = UUID.fromString(jsonPath.getString("submissionId"));

        logger.info("Response match requirements");

    }

    @And("user request a submission url")
    public void userRequestASubmissionUrl() {

        logger.info("Submitting generate url request");

        RequestSpecification request = RestAssured
                .given()
                .auth()
                .preemptive()
                .oauth2(actualUserToken)
                .contentType(ContentType.JSON)
                .header("X-Transaction-Id", actualTransactionId)
                .header("X-User-Id", "77da92db-0791-491e-8c58-1a969e67d2fa")
                .body(PayloadHelper.generateUrlPayload(TEST_DOCUMENT_PDF));

        actualGenerateUrlResponse = request
                .when()
                .post(MessageFormat.format( "http://localhost:8080/submission/{0}/generateUrl", actualSubmissionId))
                .then()
                .extract()
                .response();

        logger.info("Api response status code: {}", actualGenerateUrlResponse.getStatusCode());
        logger.info("Api response: {}", actualGenerateUrlResponse.asString());

    }

    @Then("a valid url should be returned")
    public void aValidUrlShouldBeReturned() {


        logger.info("Asserting generateUrl response");

        String expectedEfilingHubUrl = MessageFormat.format("http://localhost:3000/efilinghub?submissionId={0}&transactionId={1}", actualSubmissionId, actualTransactionId );

        JsonPath actualJson = new JsonPath(actualGenerateUrlResponse.asString());

        Assert.assertEquals(200, actualGenerateUrlResponse.getStatusCode());
        Assert.assertEquals(expectedEfilingHubUrl, actualJson.getString("efilingUrl"));
        Assert.assertNotNull(actualJson.get("expiryDate"));

        logger.info("Response match requirements");

    }

    private void setBearerToken(String keycloakHost, String keycloakRealm, String username, String password) {

        logger.info("Requesting bearer token from {} issuer", keycloakHost);

        Response response = TokenHelper.getUserAccessToken(keycloakHost, keycloakRealm, username, password, "efiling-frontend");

        logger.info("Issuer respond with http status: {}", response.getStatusCode());

        JsonPath oidcTokenJsonPath = new JsonPath(response.asString());

        if(oidcTokenJsonPath.get("access_token") == null) throw new RuntimeException("access_token not present in response");

        actualUserToken = oidcTokenJsonPath.get("access_token");
        JsonPath tokenJsonPath = new JsonPath(TokenHelper.decodeTokenToJsonString(actualUserToken));

        if(tokenJsonPath.get("universal-id") == null) throw new RuntimeException("universal-id not present in response");

        actualUniversalId = tokenJsonPath.get("universal-id");

    }


}
