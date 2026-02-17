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

public class GetDocumentByDocumentIdSD {

    private OauthService oauthService;
    private UserIdentity actualUserIdentity;
    private FilingPackageService filingPackageService;

    private Response actualDocumentResponse;

    private final Logger logger = LoggerFactory.getLogger(ca.bc.gov.open.jag.efiling.stepDefinitions.GetDocumentByDocumentIdSD.class);

    public GetDocumentByDocumentIdSD(OauthService oauthService, FilingPackageService filingPackageService) {
        this.oauthService = oauthService;
        this.filingPackageService = filingPackageService;
    }

    @Given("user is authenticated")
    public void userAccountIsAuthenticated() {
        actualUserIdentity = oauthService.getUserIdentity();
    }

    @When("user submits request to get document")
    public void getSubmissionSheetRequest() {
        logger.info("Requesting get document from filing package");

        actualDocumentResponse = filingPackageService.getDocumentById(actualUserIdentity.getAccessToken(), Integer.valueOf(1), Integer.valueOf(1));

        logger.info("Api response status code: {}", Integer.valueOf(actualDocumentResponse.getStatusCode()));
        logger.info("Api response: {}", Integer.valueOf(actualDocumentResponse.asString().getBytes(StandardCharsets.UTF_8).length));
    }

    @Then("valid document is returned")
    public void verifyDocumentIsReturned() {
        logger.info("Asserting get document response");

        JsonPath actualDocumentResponseJsonPath = new JsonPath(actualDocumentResponse.asString());

        Assert.assertEquals(HttpStatus.SC_OK, actualDocumentResponse.getStatusCode());
        Assert.assertEquals("application/pdf", actualDocumentResponse.getContentType());
        Assert.assertNotNull(actualDocumentResponseJsonPath);
    }
}
