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
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.emptyString;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertNotNull;

public class GetSubmissionConfigSD {

    private final OauthService oauthService;
    private final SubmissionService submissionService;


    private static final String TEST_DOCUMENT_PDF = "test-document.pdf";
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

    @Given("valid admin account {string}:{string} that authenticated")
    public void validAdminAccountThatAuthenticated(String username, String password) {

        actualUserIdentity = oauthService.getUserIdentity(username,password);

    }

    @When("user submits request to get submission configuration")
    public void aUserSubmitsRequestToGetSubmissionConfiguration() throws IOException {

        File resource = new ClassPathResource(
                "data/test-document.pdf").getFile();

        MultiPartSpecification fileSpec = SubmissionHelper.fileSpecBuilder(resource,TEST_DOCUMENT_PDF, "text/application.pdf");

        actualDocumentResponse = submissionService.documentUploadResponse(actualTransactionId, actualUserIdentity.getUniversalId(),
                actualUserIdentity.getAccessToken(), fileSpec );

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

        assertThat(actualSubmissionDetailsJsonPath.get("clientAppName"), is(not(emptyString())));
        assertThat(actualSubmissionDetailsJsonPath.get("csoBaseUrl"), is(not(emptyString())));

        assertNotNull(actualSubmissionDetailsJsonPath.get("navigationUrls.success"));
        assertNotNull(actualSubmissionDetailsJsonPath.get("navigationUrls.error"));
        assertNotNull(actualSubmissionDetailsJsonPath.get("navigationUrls.cancel"));

        logger.info("Response matched requirements");

    }
}
