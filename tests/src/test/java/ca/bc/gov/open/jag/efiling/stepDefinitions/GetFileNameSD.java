package ca.bc.gov.open.jag.efiling.stepDefinitions;

import ca.bc.gov.open.jag.efiling.helpers.SubmissionHelper;
import ca.bc.gov.open.jag.efiling.models.UserIdentity;
import ca.bc.gov.open.jag.efiling.services.OauthService;
import ca.bc.gov.open.jag.efiling.services.SubmissionService;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import io.restassured.specification.MultiPartSpecification;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.UUID;

public class GetFileNameSD {

    private final OauthService oauthService;
    private final SubmissionService submissionService;

    private static final String TEST_DOCUMENT_PDF = "test-document.pdf";
    private static String FILE_NAME_PATH = "document/test-document.pdf";

    private UUID actualTransactionId;
    private Response actualDocumentResponse;
    private String actualSubmissionId;
    private UserIdentity actualUserIdentity;
    private Response actualFileNameResponse;

    public Logger logger = LogManager.getLogger(GetFileNameSD.class);

    public GetFileNameSD(OauthService oauthService, SubmissionService submissionService) {
        this.oauthService = oauthService;
        this.submissionService = submissionService;
        actualTransactionId = UUID.randomUUID();
    }

    @Given("valid admin user account is authenticated")
    public void validAdminAccountThatAuthenticated() {

        actualUserIdentity = oauthService.getUserIdentity();
    }

    @When("user submits request to get document using filename")
    public void fileNameRequest() throws IOException {
        logger.info("Submitting get filing package request");


        File resource = new ClassPathResource(
                MessageFormat.format("data/{0}", TEST_DOCUMENT_PDF)).getFile();

        MultiPartSpecification fileSpec = SubmissionHelper.fileSpecBuilder(resource,TEST_DOCUMENT_PDF, "text/application.pdf");

        actualDocumentResponse = submissionService.documentUploadResponse(actualUserIdentity.getAccessToken(), actualTransactionId,
                actualUserIdentity.getUniversalId(), fileSpec);

        actualSubmissionId = submissionService.getSubmissionId(actualDocumentResponse);

        // Generate Url Response
        submissionService.generateUrlResponse(actualTransactionId, actualUserIdentity.getUniversalId(),
                actualUserIdentity.getAccessToken(), actualSubmissionId);


        actualFileNameResponse = submissionService.getSubmissionDetailsResponse(actualUserIdentity.getAccessToken(),actualTransactionId,
                actualSubmissionId, FILE_NAME_PATH);

        logger.info("Api response status code: {}", actualFileNameResponse.getStatusCode());
        logger.info("Api response: {}", actualFileNameResponse.asString());
    }

    @Then("a valid document is returned")
    public void getByFileNameResults() {

        logger.info("Asserting get filing name response");

        Assert.assertEquals(200, actualFileNameResponse.getStatusCode());
        Assert.assertEquals("application/octet-stream", actualFileNameResponse.getContentType());

        logger.info("Response matches the requirements");

    }

}
