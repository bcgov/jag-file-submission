package ca.bc.gov.open.jag.efiling.stepDefinitions;

import ca.bc.gov.open.jag.efiling.models.UserIdentity;
import ca.bc.gov.open.jag.efiling.services.FilingPackageService;
import ca.bc.gov.open.jag.efiling.services.OauthService;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;

public class GetFilingPackageByIdSD {

    private final OauthService oauthService;
    private UserIdentity actualUserIdentity;
    private FilingPackageService filingPackageService;

    private Response actualFilingPackageResponse;
    private JsonPath actualFilingPackageResponseJsonPath;

    public Logger logger = LogManager.getLogger(GetFilingPackageByIdSD.class);

    public GetFilingPackageByIdSD(OauthService oauthService, FilingPackageService filingPackageService) {
        this.oauthService = oauthService;
        this.filingPackageService = filingPackageService;
    }

    @Given("user account information is authenticated")
    public void userInformationIsAuthenticated() {

        actualUserIdentity = oauthService.getUserIdentity();
    }

    @When("user submits request to get filing packages details")
    public void getFilingPackagesRequest() {

        actualFilingPackageResponse = filingPackageService.getByPackageId(actualUserIdentity.getAccessToken(), 1);

        logger.info("Api response status code: {}", actualFilingPackageResponse.getStatusCode());
        logger.info("Api response: {}", actualFilingPackageResponse.asString());

    }

    @Then("valid package details are returned")
    public void verifyFilingPackageDetails() {

        logger.info("Asserting get filing package by id response");

        actualFilingPackageResponseJsonPath = new JsonPath(actualFilingPackageResponse.asString());

        Assert.assertEquals(200, actualFilingPackageResponse.getStatusCode());
        Assert.assertEquals("application/json", actualFilingPackageResponse.getContentType());

        //Package details
        Assert.assertEquals(Integer.valueOf(1), actualFilingPackageResponseJsonPath.get("packageNumber"));
        Assert.assertEquals("Han", actualFilingPackageResponseJsonPath.get("submittedBy.firstName"));
        Assert.assertEquals("Solo", actualFilingPackageResponseJsonPath.get("submittedBy.lastName"));
        Assert.assertNotNull(actualFilingPackageResponseJsonPath.get("submittedDate"));

        logger.info("Package details response matches the requirements");
    }

    @Then("valid court and documents details are returned")
    public void verifyCourtAndDocumentsDetails() {

        logger.info("Asserting court and documents details from get filing package by id response");

        //Court
        Assert.assertEquals("Kelowna Law Courts", actualFilingPackageResponseJsonPath.get("court.location"));
        Assert.assertEquals("P", actualFilingPackageResponseJsonPath.get("court.level"));
        Assert.assertEquals("F", actualFilingPackageResponseJsonPath.get("court.courtClass"));
        Assert.assertEquals("DIVISION", actualFilingPackageResponseJsonPath.get("court.division"));
        Assert.assertEquals("123", actualFilingPackageResponseJsonPath.get("court.fileNumber"));
        Assert.assertEquals("DESCRIPTION", actualFilingPackageResponseJsonPath.get("court.participatingClass"));
        Assert.assertEquals(Integer.valueOf(1), actualFilingPackageResponseJsonPath.get("court.agencyId"));
        Assert.assertEquals("DESCRIPTION", actualFilingPackageResponseJsonPath.get("court.locationDescription"));
        Assert.assertEquals("DESCRIPTION", actualFilingPackageResponseJsonPath.get("court.levelDescription"));
        Assert.assertEquals("DESCRIPTION", actualFilingPackageResponseJsonPath.get("court.classDescription"));

        //Documents
        Assert.assertEquals("1", actualFilingPackageResponseJsonPath.get("documents.identifier[0]"));
        Assert.assertNotNull(actualFilingPackageResponseJsonPath.get("documents.filingDate[0]"));
        Assert.assertEquals("Affidavit", actualFilingPackageResponseJsonPath.get("documents.description[0]"));
        Assert.assertFalse(actualFilingPackageResponseJsonPath.get("documents.paymentProcessed[0]"));
        Assert.assertEquals("test-document.pdf", actualFilingPackageResponseJsonPath.get("documents.documentProperties[0].name"));
        Assert.assertEquals("AFF", actualFilingPackageResponseJsonPath.get("documents.documentProperties[0].type"));
        Assert.assertEquals("Submitted", actualFilingPackageResponseJsonPath.get("documents.status[0].description"));
        Assert.assertEquals("SUB", actualFilingPackageResponseJsonPath.get("documents.status[0].code"));
        Assert.assertNotNull(actualFilingPackageResponseJsonPath.get("documents.status[0].changeDate"));

        logger.info("Court and documents response matches the requirements");
    }

    @And("valid parties and payment detail are returned")
    public void verifyPartiesAndPaymentDetails() {

        logger.info("Asserting parties and payment details in get filing package by id response");

        //Parties
        Assert.assertEquals("IND", actualFilingPackageResponseJsonPath.get("parties.partyType[0]"));
        Assert.assertEquals("APP", actualFilingPackageResponseJsonPath.get("parties.roleType[0]"));
        Assert.assertEquals("Bob", actualFilingPackageResponseJsonPath.get("parties.firstName[0]"));
        Assert.assertEquals("Q", actualFilingPackageResponseJsonPath.get("parties.middleName[0]"));
        Assert.assertEquals("Ross", actualFilingPackageResponseJsonPath.get("parties.lastName[0]"));
        Assert.assertEquals("Applicant", actualFilingPackageResponseJsonPath.get("parties.roleDescription[0]"));
        Assert.assertEquals("Individual", actualFilingPackageResponseJsonPath.get("parties.partyDescription[0]"));

        //Payments
        Assert.assertTrue(actualFilingPackageResponseJsonPath.get("payments.feeExempt[1]"));
        Assert.assertEquals(Integer.valueOf(10), actualFilingPackageResponseJsonPath.get("payments.paymentCategory[1]"));
        Assert.assertNull(actualFilingPackageResponseJsonPath.get("payments.processedAmount[1]"));
        Assert.assertEquals(Integer.valueOf(1), actualFilingPackageResponseJsonPath.get("payments.submittedAmount[1]"));
        Assert.assertNull(actualFilingPackageResponseJsonPath.get("payments.serviceIdentifier[1]"));
        Assert.assertNull(actualFilingPackageResponseJsonPath.get("payments.transactionDate[1]"));

        logger.info("Response matches the requirements");
    }

}
