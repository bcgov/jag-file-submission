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
import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.UUID;

public class GetFilingPackageSD {

    private final OauthService oauthService;
    private final SubmissionService submissionService;
    private final UUID actualTransactionId;
    private UserIdentity actualUserIdentity;
    private Response actualFilingPackageResponse;

    private final Logger logger = LoggerFactory.getLogger(GetFilingPackageSD.class);

    public GetFilingPackageSD(OauthService oauthService, SubmissionService submissionService) {
        this.oauthService = oauthService;
        this.submissionService = submissionService;
        actualTransactionId = UUID.randomUUID();
    }

    @Given("valid user account is authenticated")
    public void validAdminAccountThatAuthenticated() {

        actualUserIdentity = oauthService.getUserIdentity();
    }

    @When("user submits request to get filing package information")
    public void filingPackageRequest() throws IOException {
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

        actualFilingPackageResponse = submissionService.getSubmissionDetailsResponse(actualUserIdentity.getAccessToken(),actualTransactionId,
                actualSubmissionId, Keys.FILING_PACKAGE_PATH);

        logger.info("Api response status code: {}", Integer.valueOf(actualFilingPackageResponse.getStatusCode()));
        logger.info("Api response: {}", actualFilingPackageResponse.asString());
    }

    @Then("a valid filing package information is returned")
    public void getFilingPackageResults() {

        logger.info("Asserting get filing package response");

        JsonPath filingPackageJsonPath = new JsonPath(actualFilingPackageResponse.asString());

        Assert.assertEquals(Integer.valueOf(7), filingPackageJsonPath.get("submissionFeeAmount"));

        Assert.assertEquals("1211", filingPackageJsonPath.get("court.location"));
        Assert.assertEquals("P", filingPackageJsonPath.get("court.level"));
        Assert.assertEquals("F", filingPackageJsonPath.get("court.courtClass"));
        Assert.assertEquals(Integer.valueOf(10), filingPackageJsonPath.get("court.agencyId"));

        Assert.assertEquals("Imma Court", filingPackageJsonPath.get("court.locationDescription"));
        Assert.assertEquals("Imma Level", filingPackageJsonPath.get("court.levelDescription"));
        Assert.assertEquals("Imma Class", filingPackageJsonPath.get("court.classDescription"));

        Assert.assertEquals(Keys.TEST_DOCUMENT_PDF, filingPackageJsonPath.get("documents.documentProperties.name[0]"));

        Assert.assertEquals("AFF", filingPackageJsonPath.get("documents.documentProperties.type[0]"));
        Assert.assertEquals("This is a doc", filingPackageJsonPath.get("documents.description[0]"));
        Assert.assertEquals("application/pdf", filingPackageJsonPath.get("documents.mimeType[0]"));
        Assert.assertEquals(Integer.valueOf(7), filingPackageJsonPath.get("documents.statutoryFeeAmount[0]"));

        Assert.assertEquals("first", filingPackageJsonPath.get("parties.firstName[0]"));
        Assert.assertEquals("middle", filingPackageJsonPath.get("parties.middleName[0]"));
        Assert.assertEquals("last", filingPackageJsonPath.get("parties.lastName[0]"));

        logger.info("Response matches the requirements");

    }

}
