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

public class CreatePaymentServiceSD {

    private final OauthService oauthService;
    private final SubmissionService submissionService;

    private static final String TEST_DOCUMENT_PDF = "test-document.pdf";
    private static String SUBMIT_PATH = "submit";

    private UUID actualTransactionId;
    private Response actualDocumentResponse;
    private String actualSubmissionId;
    private UserIdentity actualUserIdentity;
    private Response actualSubmitResponse;

    public Logger logger = LogManager.getLogger(CreatePaymentServiceSD.class);

    public CreatePaymentServiceSD(OauthService oauthService, SubmissionService submissionService) {
        this.oauthService = oauthService;
        this.submissionService = submissionService;
        actualTransactionId = UUID.randomUUID();
    }

    @Given("valid admin user is authenticated")
    public void userAccountIsAuthenticated() {

        actualUserIdentity = oauthService.getUserIdentity();
    }

    @When("request is posted to submit to create service")
    public void submitToCreateService() throws IOException {
        logger.info("Submitting request with submit parameters");

        File resource = new ClassPathResource(
                MessageFormat.format("data/{0}", TEST_DOCUMENT_PDF)).getFile();

        MultiPartSpecification fileSpec = SubmissionHelper.fileSpecBuilder(resource,TEST_DOCUMENT_PDF, "text/application.pdf");

        actualDocumentResponse = submissionService.documentUploadResponse(actualUserIdentity.getAccessToken(), actualTransactionId,
                actualUserIdentity.getUniversalId(), fileSpec);

        actualSubmissionId = submissionService.getSubmissionId(actualDocumentResponse);

        // Generate Url Response
        submissionService.generateUrlResponse(actualTransactionId, actualUserIdentity.getUniversalId(),
                actualUserIdentity.getAccessToken(), actualSubmissionId);


        actualSubmitResponse = submissionService.createPaymentServiceResponse(actualUserIdentity.getAccessToken(),actualTransactionId,
                actualSubmissionId, SUBMIT_PATH);

        logger.info("Api response status code: {}", actualSubmitResponse.getStatusCode());
        logger.info("Api response: {}", actualSubmitResponse.asString());
    }

    @Then("payment processing is created")
    public void getPackageReference() {

        logger.info("Asserting create payment service submission response");

        JsonPath submitResponseJsonPath = new JsonPath(actualSubmitResponse.asString());

        Assert.assertEquals(201, actualSubmitResponse.getStatusCode());
        Assert.assertEquals("application/json", actualSubmitResponse.getContentType());

        Assert.assertEquals("aHR0cDovL2xvY2FsaG9zdDozMDAwL2VmaWxpbmdodWIvcGFja2FnZXJldmlldy8x", submitResponseJsonPath.get("packageRef"));

        logger.info("Response matches the requirements");

    }

}
