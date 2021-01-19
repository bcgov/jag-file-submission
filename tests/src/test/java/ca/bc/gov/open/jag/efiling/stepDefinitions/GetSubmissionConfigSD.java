package ca.bc.gov.open.jag.efiling.stepDefinitions;

import ca.bc.gov.open.jag.efiling.helpers.SubmissionHelper;
import ca.bc.gov.open.jag.efiling.models.UserIdentity;
import ca.bc.gov.open.jag.efiling.services.OauthService;
import ca.bc.gov.open.jag.efiling.services.SubmissionService;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.MultiPartSpecification;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class GetSubmissionConfigSD {

    private final OauthService oauthService;
    private final SubmissionService submissionService;

    private static final String TEST_DOCUMENT_PDF = "test-document.pdf";
    private static final String RESPONSE_NAVIGATION_URL = "http//somewhere.com";
    private static String CONFIG_PATH = "config";

    private UUID actualTransactionId;
    private Response actualDocumentResponse;
    private String actualSubmissionId;
    private UserIdentity actualUserIdentity;
    private Response actualSubmissionDetailsResponse;

    public Logger logger = LogManager.getLogger(GetSubmissionConfigSD.class);

    public GetSubmissionConfigSD(OauthService oauthService, SubmissionService submissionService) {
        this.oauthService = oauthService;
        this.submissionService = submissionService;
        actualTransactionId = UUID.randomUUID();
    }

    @Given("valid admin account that authenticated")
    public void validAdminAccountThatAuthenticated() {

        actualUserIdentity = oauthService.getUserIdentity();

    }

    @When("user submits request to get submission configuration")
    public void aUserSubmitsRequestToGetSubmissionConfiguration() throws IOException {

        File resource = new ClassPathResource(
                "data/test-document.pdf").getFile();

        MultiPartSpecification fileSpec = SubmissionHelper.fileSpecBuilder(resource,TEST_DOCUMENT_PDF, "text/application.pdf");

        actualDocumentResponse = submissionService.documentUploadResponse(actualUserIdentity.getAccessToken(), actualTransactionId,
                actualUserIdentity.getUniversalId(), fileSpec);

        actualSubmissionId = submissionService.getSubmissionId(actualDocumentResponse);

        // Generate Url Response
       submissionService.generateUrlResponse(actualTransactionId, actualUserIdentity.getUniversalId(),
                actualUserIdentity.getAccessToken(), actualSubmissionId);


        actualSubmissionDetailsResponse = submissionService.getSubmissionDetailsResponse(actualUserIdentity.getAccessToken(),actualTransactionId,
                                                                                                actualSubmissionId, CONFIG_PATH);

        logger.info("Api response: {}", actualSubmissionDetailsResponse.asString());

    }

    @Then("a valid config information is returned")
    public void aValidConfigInformationIsReturned() {

        logger.info("Asserting get submission config response");

        JsonPath actualSubmissionDetailsJsonPath = new JsonPath(actualSubmissionDetailsResponse.asString());

        Assert.assertEquals("my app", actualSubmissionDetailsJsonPath.get("clientAppName"));
        Assert.assertEquals("http://localhost/cso", actualSubmissionDetailsJsonPath.get("csoBaseUrl"));


        Assert.assertEquals(RESPONSE_NAVIGATION_URL, actualSubmissionDetailsJsonPath.get("navigationUrls.success"));
        Assert.assertEquals(RESPONSE_NAVIGATION_URL, actualSubmissionDetailsJsonPath.get("navigationUrls.error"));
        Assert.assertEquals(RESPONSE_NAVIGATION_URL, actualSubmissionDetailsJsonPath.get("navigationUrls.cancel"));

        logger.info("Response matched requirements");

    }
}
