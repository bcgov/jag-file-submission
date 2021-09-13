package ca.bc.gov.open.jag.efiling.stepDefinitions;

import ca.bc.gov.open.jag.efiling.Keys;
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
import org.apache.http.HttpStatus;
import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.UUID;

public class UploadAdditionalDocumentSD {

    private final OauthService oauthService;
    private final SubmissionService submissionService;
    private final UUID actualTransactionId;
    private String actualSubmissionId;
    private UserIdentity actualUserIdentity;
    private Response actualAdditionalDocumentResponse;

    private final Logger logger = LoggerFactory.getLogger(UploadAdditionalDocumentSD.class);

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
                MessageFormat.format("data/{0}", Keys.TEST_DOCUMENT_PDF)).getFile();

        MultiPartSpecification fileSpec = SubmissionHelper.fileSpecBuilder(resource, Keys.TEST_DOCUMENT_PDF, "text/application.pdf");

        Response actualDocumentResponse = submissionService.documentUploadResponse(actualUserIdentity.getAccessToken(), actualTransactionId,
                actualUserIdentity.getUniversalId(), fileSpec);

        actualSubmissionId = submissionService.getSubmissionId(actualDocumentResponse);

        // Generate Url Response
        submissionService.generateUrlResponse(actualTransactionId, actualUserIdentity.getUniversalId(),
                actualUserIdentity.getAccessToken(), actualSubmissionId, Keys.ACTION_STATUS_SUB);

        actualAdditionalDocumentResponse = submissionService.additionalDocumentUploadResponse(actualUserIdentity.getAccessToken(), actualTransactionId,
                actualSubmissionId, Keys.DOCUMENTS_PATH);

        logger.info("Api response status code: {}", Integer.valueOf(actualAdditionalDocumentResponse.getStatusCode()));
        logger.info("Api response: {}", actualAdditionalDocumentResponse.asString());
    }

    @Then("valid document count is returned")
    public void validateAdditionalDocumentUpload() {

        logger.info("Asserting additional document upload response");

        JsonPath additionalDocumentUploadJsonPath = new JsonPath(actualAdditionalDocumentResponse.asString());

        Assert.assertEquals(HttpStatus.SC_OK, actualAdditionalDocumentResponse.getStatusCode());
        Assert.assertEquals("application/json", actualAdditionalDocumentResponse.getContentType());

        Assert.assertEquals(Integer.valueOf(1), additionalDocumentUploadJsonPath.get("received"));
        Assert.assertEquals(actualSubmissionId, additionalDocumentUploadJsonPath.get("submissionId"));

        logger.info("Response matches the requirements");

    }
}
