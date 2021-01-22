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
import java.text.MessageFormat;
import java.util.UUID;

public class UploadAdditionalDocumentSD {

    private final OauthService oauthService;
    private final SubmissionService submissionService;

    private static final String TEST_DOCUMENT_PDF = "test-document.pdf";
    private static String DOCUMENTS_PATH = "documents";

    private UUID actualTransactionId;
    private Response actualDocumentResponse;
    private String actualSubmissionId;
    private UserIdentity actualUserIdentity;
    private Response actualAdditionalDocumentResponse;

    public Logger logger = LogManager.getLogger(UploadAdditionalDocumentSD.class);

    public UploadAdditionalDocumentSD(OauthService oauthService, SubmissionService submissionService) {
        this.oauthService = oauthService;
        this.submissionService = submissionService;
        actualTransactionId = UUID.randomUUID();
    }

    @Given("valid admin user credentials are authenticated")
    public void userAccountIsAuthenticated() {

        actualUserIdentity = oauthService.getUserIdentity();
    }

    @When("request is made to upload additional documents")
    public void userSubmitAdditionalPdfDocument() throws IOException {
        logger.info("Submitting request with additional document");

        File resource = new ClassPathResource(
                MessageFormat.format("data/{0}", TEST_DOCUMENT_PDF)).getFile();

        MultiPartSpecification fileSpec = SubmissionHelper.fileSpecBuilder(resource, TEST_DOCUMENT_PDF, "text/application.pdf");

        actualDocumentResponse = submissionService.documentUploadResponse(actualUserIdentity.getAccessToken(), actualTransactionId,
                actualUserIdentity.getUniversalId(), fileSpec);

        actualSubmissionId = submissionService.getSubmissionId(actualDocumentResponse);

        // Generate Url Response
        submissionService.generateUrlResponse(actualTransactionId, actualUserIdentity.getUniversalId(),
                actualUserIdentity.getAccessToken(), actualSubmissionId);

        actualAdditionalDocumentResponse = submissionService.additionalDocumentUploadResponse(actualUserIdentity.getAccessToken(), actualTransactionId,
                actualSubmissionId, DOCUMENTS_PATH);

        logger.info("Api response status code: {}", actualAdditionalDocumentResponse.getStatusCode());
        logger.info("Api response: {}", actualAdditionalDocumentResponse.asString());
    }

    @Then("valid document count is returned")
    public void validateAdditionalDocumentUpload() {

        logger.info("Asserting additional document upload response");

        JsonPath additionalDocumentUploadJsonPath = new JsonPath(actualAdditionalDocumentResponse.asString());

        Assert.assertEquals(200, actualAdditionalDocumentResponse.getStatusCode());
        Assert.assertEquals("application/json", actualAdditionalDocumentResponse.getContentType());

        Assert.assertEquals(Integer.valueOf(1), additionalDocumentUploadJsonPath.get("received"));
        Assert.assertEquals(actualSubmissionId, additionalDocumentUploadJsonPath.get("submissionId"));

        logger.info("Response matches the requirements");

    }
}
