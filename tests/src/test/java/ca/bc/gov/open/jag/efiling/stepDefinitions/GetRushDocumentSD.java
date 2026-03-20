package ca.bc.gov.open.jag.efiling.stepDefinitions;

import ca.bc.gov.open.jag.efiling.models.UserIdentity;
import ca.bc.gov.open.jag.efiling.services.FilingPackageService;
import ca.bc.gov.open.jag.efiling.services.OauthService;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;

public class GetRushDocumentSD {

    private OauthService oauthService;
    private UserIdentity actualUserIdentity;
    private FilingPackageService filingPackageService;

    private Response actualRushDocumentResponse;

    private final Logger logger = LoggerFactory.getLogger(GetRushDocumentSD.class);

    public GetRushDocumentSD(OauthService oauthService, FilingPackageService filingPackageService) {
        this.oauthService = oauthService;
        this.filingPackageService = filingPackageService;
    }

    @Given("user access is authenticated")
    public void userAccountIsAuthenticated() {
        actualUserIdentity = oauthService.getUserIdentity();
    }

    @When("user submits request to get a rush document")
    public void getSubmissionSheetRequest() {
        logger.info("Requesting get a rush document from filing package");

        actualRushDocumentResponse = filingPackageService.getRushDocument(actualUserIdentity.getAccessToken(), Integer.valueOf(1), "Test.pdf" );

        logger.info("Api response status code: {}", Integer.valueOf(actualRushDocumentResponse.getStatusCode()));
        logger.info("Api response: {}", Integer.valueOf(actualRushDocumentResponse.asString().getBytes(StandardCharsets.UTF_8).length));
    }

    @Then("valid Rush document is returned")
    public void verifyDocumentIsReturned() {
        logger.info("Asserting get rush document response");

        JsonPath actualRushDocumentResponseJsonPath = new JsonPath(actualRushDocumentResponse.asString());

        Assert.assertEquals(HttpStatus.SC_OK, actualRushDocumentResponse.getStatusCode());
        Assert.assertEquals("application/pdf", actualRushDocumentResponse.getContentType());
        Assert.assertNotNull(actualRushDocumentResponseJsonPath);
    }
}
