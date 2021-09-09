package ca.bc.gov.open.jag.efiling.stepDefinitions;

import ca.bc.gov.open.jag.efiling.Keys;
import ca.bc.gov.open.jag.efiling.TestConfig;
import ca.bc.gov.open.jag.efiling.helpers.SubmissionHelper;
import ca.bc.gov.open.jag.efiling.models.UserIdentity;
import ca.bc.gov.open.jag.efiling.services.OauthService;
import ca.bc.gov.open.jag.efiling.services.SubmissionService;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.MultiPartSpecification;
import org.apache.http.HttpStatus;
import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.UUID;

@CucumberContextConfiguration
@SpringBootTest(classes = TestConfig.class)
public class GenerateUrlSD {

    private final OauthService oauthService;
    private final SubmissionService submissionService;
    private final UUID actualTransactionId;

    private Response actualDocumentResponse;
    private Response actualGenerateUrlResponse;
    private String actualSubmissionId;
    private UserIdentity actualUserIdentity;

    private final Logger logger = LoggerFactory.getLogger(GenerateUrlSD.class);

    public GenerateUrlSD(OauthService oauthService, SubmissionService submissionService) {
        this.oauthService = oauthService;
        this.submissionService = submissionService;
        actualTransactionId = UUID.randomUUID();
    }

    @Given("admin account is authenticated")
    public void adminAccountThatAuthenticatedWithKeycloakOnRealm() {

        actualUserIdentity = oauthService.getUserIdentity();
    }

    @When("user Submit a valid pdf document")
    public void userSubmitAValidPdfDocument() throws IOException {

        logger.info("Submitting document upload request");

        File resource = new ClassPathResource(
                MessageFormat.format("data/{0}", Keys.TEST_DOCUMENT_PDF)).getFile();

        MultiPartSpecification fileSpec = SubmissionHelper.fileSpecBuilder(resource, Keys.TEST_DOCUMENT_PDF, "text/application.pdf");

        actualDocumentResponse = submissionService.documentUploadResponse(actualUserIdentity.getAccessToken(), actualTransactionId,
                actualUserIdentity.getUniversalId(), fileSpec);


        logger.info("Api response status code: {}", Integer.valueOf(actualDocumentResponse.getStatusCode()));

    }

    @Then("a valid transaction should be generated")
    public void aValidTransactionIsShouldBeGenerated() {

        logger.info("Asserting document upload response");

        JsonPath jsonPath = new JsonPath(actualDocumentResponse.asString());
        Assert.assertTrue(((Integer) jsonPath.get("received")).intValue() >= 1);

        actualSubmissionId = submissionService.getSubmissionId(actualDocumentResponse);

        logger.info("Response match requirements");

    }

    @And("user request a submission url")
    public void userRequestASubmissionUrl() {

        actualGenerateUrlResponse = submissionService.generateUrlResponse(actualTransactionId, actualUserIdentity.getUniversalId(),
                actualUserIdentity.getAccessToken(), actualSubmissionId, Keys.ACTION_STATUS_SUB);

        logger.info("Api response status code: {}", Integer.valueOf(actualGenerateUrlResponse.getStatusCode()));

        logger.info("Api response: {}", actualGenerateUrlResponse.asString());

    }

    @Then("a valid url should be returned")
    public void aValidUrlShouldBeReturned() {


        logger.info("Asserting generateUrl response");

        String expectedEfilingHubUrl = MessageFormat.format("http://localhost:3000/efilinghub?submissionId={0}&transactionId={1}", actualSubmissionId, actualTransactionId);

        JsonPath actualJson = new JsonPath(actualGenerateUrlResponse.asString());

        Assert.assertEquals(HttpStatus.SC_OK, actualGenerateUrlResponse.getStatusCode());
        Assert.assertEquals(expectedEfilingHubUrl, actualJson.getString("efilingUrl"));
        Assert.assertNotNull(actualJson.get("expiryDate"));

        logger.info("Response match requirements");

    }

    @And("user request a submission url with invalid FileNumber")
    public void userRequestASubmissionUrlWithInvalidFileNo() {

        actualGenerateUrlResponse = submissionService.generateUrlResponseForInvalidFileNo(actualTransactionId, actualUserIdentity.getUniversalId(),
                actualUserIdentity.getAccessToken(), actualSubmissionId);

        logger.info("Api response status code: {}", Integer.valueOf(actualGenerateUrlResponse.getStatusCode()));

        logger.info("Api response: {}", actualGenerateUrlResponse.asString());

    }

    @Then("a valid error message should be returned")
    public void validErrorMessageShouldBeReturned() {

        logger.info("Asserting generateUrl response");

        JsonPath actualJson = new JsonPath(actualGenerateUrlResponse.asString());

        Assert.assertEquals(HttpStatus.SC_BAD_REQUEST, actualGenerateUrlResponse.getStatusCode());
        Assert.assertEquals("INVALID_INITIAL_SUBMISSION_PAYLOAD", actualJson.getString("error"));
        Assert.assertEquals("Initial Submission payload invalid, find more in the details array.", actualJson.getString("message"));
        Assert.assertEquals("FileNumber [3] does not exists.", actualJson.getString("details[0]"));

        logger.info("Response match requirements");

    }
}
