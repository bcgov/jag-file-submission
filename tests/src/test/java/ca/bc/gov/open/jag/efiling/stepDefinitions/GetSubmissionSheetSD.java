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

public class GetSubmissionSheetSD {

    private OauthService oauthService;
    private UserIdentity actualUserIdentity;
    private FilingPackageService filingPackageService;

    private Response actualSubmissionSheetResponse;

    private final Logger logger = LoggerFactory.getLogger(GetSubmissionSheetSD.class);

    public GetSubmissionSheetSD(OauthService oauthService, FilingPackageService filingPackageService) {
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

        actualSubmissionSheetResponse = filingPackageService.getSubmissionSheet(actualUserIdentity.getAccessToken(), Integer.valueOf(1));

        logger.info("Api response status code: {}", Integer.valueOf(actualSubmissionSheetResponse.getStatusCode()));
        logger.info("Api response: {}", Integer.valueOf(actualSubmissionSheetResponse.asString().getBytes(StandardCharsets.UTF_8).length));

    }

    @Then("valid submission sheet is returned")
    public void verifySubmissionSheetInformation() {

        logger.info("Asserting get submission sheet response");

        JsonPath actualSubmissionSheetResponseJsonPath = new JsonPath(actualSubmissionSheetResponse.asString());

        Assert.assertEquals(HttpStatus.SC_OK, actualSubmissionSheetResponse.getStatusCode());
        Assert.assertEquals("application/pdf", actualSubmissionSheetResponse.getContentType());
        Assert.assertNotNull(actualSubmissionSheetResponseJsonPath);

    }
}
