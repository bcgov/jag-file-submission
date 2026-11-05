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

public class UpdateDocumentPropertiesSD {

    private final OauthService oauthService;
    private final SubmissionService submissionService;
    private final UUID actualTransactionId;
    private UserIdentity actualUserIdentity;
    private Response actualUpdatedDocumentPropertiesResponse;

    private final Logger logger = LoggerFactory.getLogger(UpdateDocumentPropertiesSD.class);

    public UpdateDocumentPropertiesSD(OauthService oauthService, SubmissionService submissionService) {
        this.oauthService = oauthService;
        this.submissionService = submissionService;
        actualTransactionId = UUID.randomUUID();
    }


    @Given("valid user account are authenticated")
    public void authenticateUserAccount() {

        actualUserIdentity = oauthService.getUserIdentity();
    }

    @When("request is made to update document properties")
    public void updateDocumentProperties() throws IOException {

        logger.info("Submitting request to update document properties");

        File resource = new ClassPathResource(
                MessageFormat.format("data/{0}", Keys.TEST_DOCUMENT_PDF)).getFile();

        MultiPartSpecification fileSpec = SubmissionHelper.fileSpecBuilder(resource, Keys.TEST_DOCUMENT_PDF, "text/application.pdf");

        Response actualDocumentResponse = submissionService.documentUploadResponse(actualUserIdentity.getAccessToken(), actualTransactionId,
                actualUserIdentity.getUniversalId(), fileSpec);

        String actualSubmissionId = submissionService.getSubmissionId(actualDocumentResponse);

        // Generate Url Response
        submissionService.generateUrlResponse(actualTransactionId, actualUserIdentity.getUniversalId(),
                actualUserIdentity.getAccessToken(), actualSubmissionId, Keys.ACTION_STATUS_SUB);

        actualUpdatedDocumentPropertiesResponse = submissionService.updateDocumentPropertiesResponse(actualUserIdentity.getAccessToken(), actualTransactionId,
                actualSubmissionId, Keys.UPDATE_DOCUMENTS_PATH);

        logger.info("Api response status code: {}", Integer.valueOf(actualUpdatedDocumentPropertiesResponse.getStatusCode()));
        logger.info("Api response: {}", actualUpdatedDocumentPropertiesResponse.asString());

    }


    @Then("valid updated document properties are returned")
    public void additionalDocumentPropertiesAreReturned() {

        logger.info("Asserting updated document response");

        JsonPath additionalDocumentUploadJsonPath = new JsonPath(actualUpdatedDocumentPropertiesResponse.asString());

        Assert.assertEquals(HttpStatus.SC_OK, actualUpdatedDocumentPropertiesResponse.getStatusCode());
        Assert.assertEquals("application/json", actualUpdatedDocumentPropertiesResponse.getContentType());

        Assert.assertEquals("AAB", additionalDocumentUploadJsonPath.get("documents.type[1]"));
        Assert.assertEquals(Boolean.TRUE, additionalDocumentUploadJsonPath.get("documents.isAmendment[1]"));
        Assert.assertEquals(Boolean.TRUE, additionalDocumentUploadJsonPath.get("documents.isSupremeCourtScheduling[1]"));


        logger.info("Response matches the requirements");

    }
}
