package ca.bc.gov.open.jag.efiling.stepDefinitions;

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
    private final SubmissionService submissionService;

    private static final String TEST_DOCUMENT_PDF = "test-document.pdf";

    private UUID actualTransactionId;
    private Response actualDocumentResponse;
    private Response actualGenerateUrlResponse;
    private String actualSubmissionId;
    private UserIdentity actualUserIdentity;

    public Logger logger = LogManager.getLogger(GenerateUrlAndSubmissionTest.class);

    public GenerateUrlSD(OauthService oauthService, SubmissionService submissionService) {
        this.oauthService = oauthService;
        this.submissionService = submissionService;
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

        MultiPartSpecification fileSpec = SubmissionHelper.fileSpecBuilder(resource,TEST_DOCUMENT_PDF, "text/application.pdf");

        actualDocumentResponse = submissionService.documentUploadResponse(actualTransactionId, actualUserIdentity.getUniversalId(),
                                                                    actualUserIdentity.getAccessToken(), fileSpec );


       logger.info("Api response status code: {}", actualDocumentResponse.getStatusCode());
       logger.info("Api response: {}", actualDocumentResponse.asString());

    }

    @Then("a valid transaction should be generated")
    public void aValidTransactionIsShouldBeGenerated() {

        logger.info("Asserting document upload response");

        JsonPath jsonPath = new JsonPath(actualDocumentResponse.asString());
        Assert.assertEquals("File Received not don't match", new BigDecimal(1), new BigDecimal(jsonPath.get("received").toString()));

        actualSubmissionId = submissionService.getSubmissionId(actualDocumentResponse);

        logger.info("Response match requirements");

    }

    @And("user request a submission url")
    public void userRequestASubmissionUrl() {

        actualGenerateUrlResponse = submissionService.generateUrlResponse(actualTransactionId, actualUserIdentity.getUniversalId(),
                                                                                 actualUserIdentity.getAccessToken(), actualSubmissionId);

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
