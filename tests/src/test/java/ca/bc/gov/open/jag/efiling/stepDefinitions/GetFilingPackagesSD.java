package ca.bc.gov.open.jag.efiling.stepDefinitions;

import ca.bc.gov.open.jag.efiling.Keys;
import ca.bc.gov.open.jag.efiling.models.UserIdentity;
import ca.bc.gov.open.jag.efiling.services.FilingPackageService;
import ca.bc.gov.open.jag.efiling.services.OauthService;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class GetFilingPackagesSD {

    private final OauthService oauthService;
    private UserIdentity actualUserIdentity;
    private final FilingPackageService filingPackageService;
    private Response actualFilingPackagesResponse;
    private JsonPath actualFilingPackagesResponseJsonPath;

    private final Logger logger = LoggerFactory.getLogger(GetFilingPackagesSD.class);

    public GetFilingPackagesSD(OauthService oauthService, FilingPackageService filingPackageService) {
        this.oauthService = oauthService;
        this.filingPackageService = filingPackageService;
    }

    @Given("user id is authenticated")
    public void userIdIsAuthenticated() {

        actualUserIdentity = oauthService.getUserIdentity();

    }

    @When("user submits request to get filing packages history")
    public void getFilingPackages() {

        actualFilingPackagesResponse = filingPackageService.getFilingPackages(actualUserIdentity.getAccessToken(), Keys.PARENT_APPLICATION);

        logger.info("Api response status code: {}", Integer.valueOf(actualFilingPackagesResponse.getStatusCode()));
        logger.info("Api response: {}", actualFilingPackagesResponse.asString());

    }

    @Then("valid packages history is returned")
    public void verifyFilingPackagesHistory() {

        logger.info("Asserting get filing packages history response");

        actualFilingPackagesResponseJsonPath = new JsonPath(actualFilingPackagesResponse.asString());

        Assert.assertEquals(HttpStatus.SC_OK, actualFilingPackagesResponse.getStatusCode());
        Assert.assertEquals("application/json", actualFilingPackagesResponse.getContentType());

        //Package details
        Assert.assertEquals(Integer.valueOf(1), actualFilingPackagesResponseJsonPath.get("packageNumber[0]"));
        Assert.assertEquals("Han", actualFilingPackagesResponseJsonPath.get("submittedBy.firstName[0]"));
        Assert.assertEquals("Solo", actualFilingPackagesResponseJsonPath.get("submittedBy.lastName[0]"));
        Assert.assertNotNull(actualFilingPackagesResponseJsonPath.get("submittedDate[0]"));

        logger.info("Package details response matches the requirements");
    }

    @Then("valid document and court details are returned")
    public void validateCourtAndDocumentsDetails() {

        logger.info("Asserting court and documents details from get filing package by id response");

        //Court
        Assert.assertEquals("Kelowna Law Courts", actualFilingPackagesResponseJsonPath.get("court.location[0]"));
        Assert.assertEquals("P", actualFilingPackagesResponseJsonPath.get("court.level[0]"));
        Assert.assertEquals("F", actualFilingPackagesResponseJsonPath.get("court.courtClass[0]"));
        Assert.assertEquals("DIVISION", actualFilingPackagesResponseJsonPath.get("court.division[0]"));
        Assert.assertEquals("123", actualFilingPackagesResponseJsonPath.get("court.fileNumber[0]"));
        Assert.assertEquals(Keys.DESCRIPTION, actualFilingPackagesResponseJsonPath.get("court.participatingClass[0]"));
        Assert.assertEquals(Integer.valueOf(1), actualFilingPackagesResponseJsonPath.get("court.agencyId[0]"));
        Assert.assertEquals(Keys.DESCRIPTION, actualFilingPackagesResponseJsonPath.get("court.locationDescription[0]"));
        Assert.assertEquals(Keys.DESCRIPTION, actualFilingPackagesResponseJsonPath.get("court.levelDescription[0]"));
        Assert.assertEquals(Keys.DESCRIPTION, actualFilingPackagesResponseJsonPath.get("court.classDescription[0]"));

        //Documents
        Assert.assertEquals("1", actualFilingPackagesResponseJsonPath.get("documents[0].identifier[0]"));
        Assert.assertNotNull(actualFilingPackagesResponseJsonPath.get("documents[0].filingDate[0]"));
        Assert.assertEquals("Affidavit", actualFilingPackagesResponseJsonPath.get("documents[0].description[0]"));
        Assert.assertEquals(Boolean.FALSE, actualFilingPackagesResponseJsonPath.get("documents[0].paymentProcessed[0]"));
        Assert.assertEquals("test-document.pdf", actualFilingPackagesResponseJsonPath.get("documents[0].documentProperties.name[0]"));
        Assert.assertEquals("AFF", actualFilingPackagesResponseJsonPath.get("documents[0].documentProperties.type[0]"));
        Assert.assertEquals("Submitted", actualFilingPackagesResponseJsonPath.get("documents[0].status.description[0]"));
        Assert.assertEquals("SUB", actualFilingPackagesResponseJsonPath.get("documents[0].status.code[0]"));
        Assert.assertNotNull(actualFilingPackagesResponseJsonPath.get("documents[0].status.changeDate[0]"));

        logger.info("Court and documents response matches the requirements");
    }

    @And("valid parties, org and payment details are returned")
    public void validatePartiesAndPaymentDetails() {

        logger.info("Asserting parties and payment details in get filing package by id response");

        //Parties
        Assert.assertEquals("APP", actualFilingPackagesResponseJsonPath.get("parties[0].roleType[0]"));
        Assert.assertEquals("Bob", actualFilingPackagesResponseJsonPath.get("parties[0].firstName[0]"));
        Assert.assertEquals("Q", actualFilingPackagesResponseJsonPath.get("parties[0].middleName[0]"));
        Assert.assertEquals("Ross", actualFilingPackagesResponseJsonPath.get("parties[0].lastName[0]"));
        Assert.assertEquals("Applicant", actualFilingPackagesResponseJsonPath.get("parties[0].roleDescription[0]"));
        Assert.assertEquals("Individual", actualFilingPackagesResponseJsonPath.get("parties[0].partyDescription[0]"));

        //Organization Parties
        Assert.assertEquals("APP", actualFilingPackagesResponseJsonPath.get("organizationParties[0].roleType[0]"));
        Assert.assertEquals("Applicant", actualFilingPackagesResponseJsonPath.get("organizationParties[0].roleDescription[0]"));
        Assert.assertEquals("Organization", actualFilingPackagesResponseJsonPath.get("organizationParties[0].partyDescription[0]"));
        Assert.assertEquals("The Organization Org.", actualFilingPackagesResponseJsonPath.get("organizationParties[0].name[0]"));

        //Payments
        Assert.assertEquals(Boolean.FALSE, actualFilingPackagesResponseJsonPath.get("payments[0].feeExempt[0]"));
        Assert.assertEquals(Integer.valueOf(1), actualFilingPackagesResponseJsonPath.get("payments[0].paymentCategory[0]"));
        Assert.assertEquals(Integer.valueOf(7), actualFilingPackagesResponseJsonPath.get("payments[0].processedAmount[0]"));
        Assert.assertEquals(Integer.valueOf(7), actualFilingPackagesResponseJsonPath.get("payments[0].submittedAmount[0]"));
        Assert.assertEquals(Integer.valueOf(1), actualFilingPackagesResponseJsonPath.get("payments[0].serviceIdentifier[0]"));
        Assert.assertNotNull(actualFilingPackagesResponseJsonPath.get("payments[0].transactionDate[0]"));

        Assert.assertEquals("http://localhost:8080/wherearemypackage", actualFilingPackagesResponseJsonPath.get("links.packageHistoryUrl[0]"));
        Assert.assertEquals(Boolean.TRUE, actualFilingPackagesResponseJsonPath.get("hasRegistryNotice[0]"));

        logger.info("Response matches the requirements");
    }

    @Then("rush processing and supporting documents are returned")
    public void validateRushProcessAndSupportingDoc() {
        logger.info("Asserting rush object and supporting documents");

        Assert.assertEquals( "other", actualFilingPackagesResponseJsonPath.get("rush.rushType[0]"));
        Assert.assertEquals( "Bob", actualFilingPackagesResponseJsonPath.get("rush.firstName[0]"));
        Assert.assertEquals("Ross",  actualFilingPackagesResponseJsonPath.get("rush.lastName[0]"));
        Assert.assertEquals("Paint It", actualFilingPackagesResponseJsonPath.get("rush.organization[0]"));
        Assert.assertEquals( "1231231234", actualFilingPackagesResponseJsonPath.get("rush.phoneNumber[0]"));
        Assert.assertEquals("Canada", actualFilingPackagesResponseJsonPath.get("rush.country[0]"));
        Assert.assertEquals( "1", actualFilingPackagesResponseJsonPath.get("rush.countryCode[0]"));
        Assert.assertEquals("This is a reason. This is a reason. This is a reason. This is a reason.", actualFilingPackagesResponseJsonPath.get("rush.reason[0]"));
        Assert.assertEquals("Processing", actualFilingPackagesResponseJsonPath.get("rush.status[0]"));

        Assert.assertEquals("Test.pdf", actualFilingPackagesResponseJsonPath.get("rush.supportingDocuments[0].fileName[0]"));
        Assert.assertEquals("9b35f5d6-50e9-4cd5-9d46-8ce1f9e484c8", actualFilingPackagesResponseJsonPath.get("rush.supportingDocuments[0].identifier[0]"));

    }

}
