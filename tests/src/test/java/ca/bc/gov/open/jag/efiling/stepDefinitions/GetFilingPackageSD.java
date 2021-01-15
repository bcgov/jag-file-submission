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

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.emptyString;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

public class GetFilingPackageSD {

    private final OauthService oauthService;
    private final SubmissionService submissionService;


    private static final String TEST_DOCUMENT_PDF = "test-document.pdf";
    private static String FILING_PACKAGE_PATH = "filing-package";

    private UUID actualTransactionId;
    private Response actualDocumentResponse;
    private String actualSubmissionId;
    private UserIdentity actualUserIdentity;
    private Response actualFilingPackageResponse;

    public Logger logger = LogManager.getLogger(GetFilingPackageSD.class);

    public GetFilingPackageSD(OauthService oauthService, SubmissionService submissionService) {
        this.oauthService = oauthService;
        this.submissionService = submissionService;
        actualTransactionId = UUID.randomUUID();
    }

    @Given("valid user account {string}:{string} is authenticated")
    public void validAdminAccountThatAuthenticated(String username, String password) throws IOException {

        actualUserIdentity = oauthService.getUserIdentity(username,password);
    }

    @When("user submits request to get filing package information")
    public void filingPackageRequest() throws IOException {
        logger.info("Submitting get filing package request");


        File resource = new ClassPathResource(
                "data/test-document.pdf").getFile();

        MultiPartSpecification fileSpec = SubmissionHelper.fileSpecBuilder(resource,TEST_DOCUMENT_PDF, "text/application.pdf");

        actualDocumentResponse = submissionService.documentUploadResponse(actualTransactionId, actualUserIdentity.getUniversalId(),
                actualUserIdentity.getAccessToken(), fileSpec );

        actualSubmissionId = submissionService.getSubmissionId(actualDocumentResponse);

        // Generate Url Response
        submissionService.generateUrlResponse(actualTransactionId, actualUserIdentity.getUniversalId(),
                actualUserIdentity.getAccessToken(), actualSubmissionId);


        actualFilingPackageResponse = submissionService.getSubmissionDetailsResponse(actualUserIdentity.getAccessToken(),actualTransactionId,
                actualSubmissionId, FILING_PACKAGE_PATH);


        logger.info("Api response status code: {}", actualFilingPackageResponse.getStatusCode());
        logger.info("Api response: {}", actualFilingPackageResponse.asString());
    }

    @Then("a valid filing package information is returned")
    public void getFilingPackageResults() {

        logger.info("Asserting get filing package response");

        JsonPath filingPackageJsonPath = new JsonPath(actualFilingPackageResponse.asString());

        assertThat(filingPackageJsonPath.get("court.location"), is(not(emptyString())));
        assertThat(filingPackageJsonPath.get("court.level"), is(not(emptyString())));
        assertThat(filingPackageJsonPath.get("court.class"), is(not(emptyString())));
        assertThat(filingPackageJsonPath.get("court.division"), is(not(emptyString())));
        assertThat(filingPackageJsonPath.get("court.fileNumber"), is(not(emptyString())));
        assertThat(filingPackageJsonPath.get("court.participatingClass"), is(not(emptyString())));
        assertThat(filingPackageJsonPath.get("court.locationDescription"), is(not(emptyString())));
        assertThat(filingPackageJsonPath.get("court.levelDescription"), is(not(emptyString())));
        assertThat(filingPackageJsonPath.get("parties"), is(not(emptyString())));
        Assert.assertEquals(Integer.valueOf(7), filingPackageJsonPath.get("submissionFeeAmount"));
        logger.info("Court fee and document details response have valid values");

        assertFalse(filingPackageJsonPath.get("documents.name").toString().isEmpty());
        assertFalse(filingPackageJsonPath.get("documents.type").toString().isEmpty());
        assertFalse(filingPackageJsonPath.get("documents.subType").toString().isEmpty());
        assertFalse(filingPackageJsonPath.get("documents.isAmendment").toString().isEmpty());
        assertFalse(filingPackageJsonPath.get("documents.isSupremeCourtScheduling").toString().isEmpty());
        assertFalse(filingPackageJsonPath.get("documents.description").toString().isEmpty());
        assertFalse(filingPackageJsonPath.get("documents.statutoryFeeAmount").toString().isEmpty());
        assertNotNull(filingPackageJsonPath.get("documents.statutoryFeeAmount"));
        logger.info("Response matches the requirements");

    }

}
