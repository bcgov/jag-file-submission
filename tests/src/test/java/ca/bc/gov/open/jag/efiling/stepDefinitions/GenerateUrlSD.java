package ca.bc.gov.open.jag.efiling.stepDefinitions;

import ca.bc.gov.open.jag.efiling.TestConfig;
import ca.bc.gov.open.jag.efiling.helpers.PayloadHelper;
import ca.bc.gov.open.jag.efiling.models.UserIdentity;
import ca.bc.gov.open.jag.efiling.services.OauthService;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;
import io.restassured.RestAssured;
import io.restassured.builder.MultiPartSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.MultiPartSpecification;
import io.restassured.specification.RequestSpecification;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;
import stepDefinitions.backendstepdefinitions.GenerateUrlAndSubmissionTest;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.UUID;

@CucumberContextConfiguration
@SpringBootTest(classes = TestConfig.class)
public class GenerateUrlSD {


    private final OauthService oauthService;

    private static final String X_USER_ID = "X-User-Id";
    private static final String TEST_DOCUMENT_PDF = "test-document.pdf";
    private static final String X_TRANSACTION_ID = "X-Transaction-Id";

    private UUID actualTransactionId;
    private Response actualDocumentResponse;
    private Response actualGenerateUrlResponse;
    private UUID actualSubmissionId;
    private UserIdentity actualUserIdentity;

    public Logger logger = LogManager.getLogger(GenerateUrlAndSubmissionTest.class);

    public GenerateUrlSD(OauthService oauthService) {
        this.oauthService = oauthService;
        actualTransactionId = UUID.randomUUID();
    }

    @Given("admin account {string}:{string} that authenticated")
    public void adminAccountThatAuthenticatedWithKeycloakOnRealm(String username, String password) {

        actualUserIdentity = oauthService.getUserIdentity(username,password);
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

        RequestSpecification request = RestAssured.given().auth().preemptive().oauth2(actualUserIdentity.getAccessToken())
                .header(X_TRANSACTION_ID, actualTransactionId)
                .header(X_USER_ID, actualUserIdentity.getUniversalId())
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
                .oauth2(actualUserIdentity.getAccessToken())
                .contentType(ContentType.JSON)
                .header(X_TRANSACTION_ID, actualTransactionId)
                .header(X_USER_ID, actualUserIdentity.getUniversalId())
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


}
