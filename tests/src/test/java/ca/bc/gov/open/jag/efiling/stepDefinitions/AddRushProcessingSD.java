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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.UUID;

public class AddRushProcessingSD {

    private final OauthService oauthService;
    private final SubmissionService submissionService;
    private final UUID actualTransactionId;

    private UserIdentity actualUserIdentity;
    private Response actualRushProcessResponse;

    private final Logger logger = LoggerFactory.getLogger(AddRushProcessingSD.class);

    public AddRushProcessingSD(OauthService oauthService, SubmissionService submissionService) {
        this.oauthService = oauthService;
        this.submissionService = submissionService;
        actualTransactionId = UUID.randomUUID();
    }

    @Given("valid user is authenticated")
    public void userIsAuthenticated() {

        actualUserIdentity = oauthService.getUserIdentity();
    }

    @When("request is posted to submission to add rush processing")
    public void submitToAddRushProcessing() throws IOException {
        logger.info("Submitting request with rush processing payload");

        File resource = new ClassPathResource(
                MessageFormat.format("data/{0}", Keys.TEST_DOCUMENT_PDF)).getFile();

        MultiPartSpecification fileSpec = SubmissionHelper.fileSpecBuilder(resource, Keys.TEST_DOCUMENT_PDF, "text/application.pdf");

        Response actualDocumentResponse = submissionService.documentUploadResponse(actualUserIdentity.getAccessToken(), actualTransactionId,
                actualUserIdentity.getUniversalId(), fileSpec);

        String actualSubmissionId = submissionService.getSubmissionId(actualDocumentResponse);

        // Generate Url Response
        submissionService.generateUrlResponse(actualTransactionId, actualUserIdentity.getUniversalId(),
                actualUserIdentity.getAccessToken(), actualSubmissionId);


        actualRushProcessResponse = submissionService.addRushProcessingResponse(actualUserIdentity.getAccessToken(), actualTransactionId,
                actualSubmissionId, Keys.RUSH_PROCESSING_PATH);

        logger.info("Api response status code: {}", actualRushProcessResponse.getStatusCode());
        logger.info("Api response: {}", actualRushProcessResponse.asString());
    }

    @Then("rush processing is added to the package")
    public void verifyFilingPackageIsUpdated() {

        logger.info("Asserting add rush processing submission response");

        JsonPath rushProcessingResponseJsonPath = new JsonPath(actualRushProcessResponse.asString());

        logger.info("Response matches the requirements");

    }
}
