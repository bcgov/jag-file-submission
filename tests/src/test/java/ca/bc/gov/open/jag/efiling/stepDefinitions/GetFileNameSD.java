package ca.bc.gov.open.jag.efiling.stepDefinitions;

import ca.bc.gov.open.jag.efiling.Keys;
import ca.bc.gov.open.jag.efiling.helpers.SubmissionHelper;
import ca.bc.gov.open.jag.efiling.models.UserIdentity;
import ca.bc.gov.open.jag.efiling.services.OauthService;
import ca.bc.gov.open.jag.efiling.services.SubmissionService;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
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

public class GetFileNameSD {

    private final OauthService oauthService;
    private final SubmissionService submissionService;
    private final UUID actualTransactionId;
    private UserIdentity actualUserIdentity;
    private Response actualFileNameResponse;

    private final Logger logger = LoggerFactory.getLogger(GetFileNameSD.class);

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
                MessageFormat.format("data/{0}", Keys.TEST_DOCUMENT_PDF)).getFile();

        MultiPartSpecification fileSpec = SubmissionHelper.fileSpecBuilder(resource, Keys.TEST_DOCUMENT_PDF, "text/application.pdf");

        Response actualDocumentResponse = submissionService.documentUploadResponse(actualUserIdentity.getAccessToken(), actualTransactionId,
                actualUserIdentity.getUniversalId(), fileSpec);

        String actualSubmissionId = submissionService.getSubmissionId(actualDocumentResponse);

        // Generate Url Response
        submissionService.generateUrlResponse(actualTransactionId, actualUserIdentity.getUniversalId(),
                actualUserIdentity.getAccessToken(), actualSubmissionId, Keys.ACTION_STATUS_SUB);

        actualFileNameResponse = submissionService.getSubmissionDetailsResponse(actualUserIdentity.getAccessToken(),actualTransactionId,
                actualSubmissionId, Keys.FILE_NAME_PATH);

        logger.info("Api response status code: {}", Integer.valueOf(actualFileNameResponse.getStatusCode()));

    }

    @Then("a valid document is returned")
    public void getByFileNameResults() {

        logger.info("Asserting get filing name response");

        Assert.assertEquals(HttpStatus.SC_OK, actualFileNameResponse.getStatusCode());
        Assert.assertEquals("application/octet-stream", actualFileNameResponse.getContentType());

        logger.info("Response matches the requirements");

    }

}
