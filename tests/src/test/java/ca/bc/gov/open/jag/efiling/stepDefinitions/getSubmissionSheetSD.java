package ca.bc.gov.open.jag.efiling.stepDefinitions;

import ca.bc.gov.open.jag.efiling.models.UserIdentity;
import ca.bc.gov.open.jag.efiling.services.FilingPackageService;
import ca.bc.gov.open.jag.efiling.services.OauthService;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class getSubmissionSheetSD {

    private OauthService oauthService;
    private UserIdentity actualUserIdentity;
    private FilingPackageService filingPackageService;

    private Response actualSubmissionSheetResponse;

    private Logger logger = LoggerFactory.getLogger(getSubmissionSheetSD.class);

    public getSubmissionSheetSD(OauthService oauthService, FilingPackageService filingPackageService) {
        this.oauthService = oauthService;
        this.filingPackageService = filingPackageService;
    }

    @Given("user account is authenticated")
    public void userAccountIsAuthenticated() {

        actualUserIdentity = oauthService.getUserIdentity();
    }

    @When("user submits request to get submission sheet")
    public void getSubmissionSheetRequest() {
        logger.info("Requesting submission sheet from filing packages");

        actualSubmissionSheetResponse = filingPackageService.getSubmissionSheet(actualUserIdentity.getAccessToken(), 1);

        logger.info("Api response status code: {}", actualSubmissionSheetResponse.getStatusCode());
        logger.info("Api response: {}", actualSubmissionSheetResponse.asString());

    }

    @Then("valid submission sheet is returned")
    public void verifySubmissionSheetInformation() {

        logger.info("Asserting get submission sheet response");

        JsonPath actualSubmissionSheetResponseJsonPath = new JsonPath(actualSubmissionSheetResponse.asString());

        Assert.assertEquals(200, actualSubmissionSheetResponse.getStatusCode());
        Assert.assertEquals("application/pdf", actualSubmissionSheetResponse.getContentType());
        Assert.assertNotNull(actualSubmissionSheetResponseJsonPath);

    }
}
