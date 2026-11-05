package ca.bc.gov.open.jag.efiling.stepDefinitions;

import ca.bc.gov.open.jag.efiling.models.UserIdentity;
import ca.bc.gov.open.jag.efiling.services.CourtService;
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

public class GetCourtLocationSD {

    private final OauthService oauthService;
    private final CourtService courtService;
    private UserIdentity actualUserIdentity;
    private Response actualCourtLocationResponse;

    private final Logger logger = LoggerFactory.getLogger(GetCourtLocationSD.class);

    public GetCourtLocationSD(OauthService oauthService, CourtService courtService) {
        this.oauthService = oauthService;
        this.courtService = courtService;
    }

    @Given("valid user account information is authenticated")
    public void userInformationIsAuthenticated() {

        actualUserIdentity = oauthService.getUserIdentity();
    }

    @When("user submits request to get court location information")
    public void getCourtsRequest() {
        logger.info("Submitting get court location request");

        actualCourtLocationResponse = courtService.getCourtsResponse(actualUserIdentity.getAccessToken(), "s");

        logger.info("Api response status code: {}", Integer.valueOf(actualCourtLocationResponse.getStatusCode()));
        logger.info("Api response: {}", actualCourtLocationResponse.asString());
    }

    @Then("a valid court location information is returned")
    public void getCourtLocationDetails() {

        logger.info("Asserting get court locations response");

        JsonPath jsonPath = new JsonPath(actualCourtLocationResponse.asString());

        Assert.assertEquals(HttpStatus.SC_OK, actualCourtLocationResponse.getStatusCode());
        Assert.assertEquals("application/json", actualCourtLocationResponse.getContentType());

        Assert.assertEquals(Integer.valueOf(10264), jsonPath.get("courts.id[0]"));
        Assert.assertEquals("5871", jsonPath.get("courts.identifierCode[0]"));
        Assert.assertEquals("Campbell River", jsonPath.get("courts.name[0]"));
        Assert.assertEquals("OMH", jsonPath.get("courts.code[0]"));
        Assert.assertEquals(Boolean.TRUE, jsonPath.get("courts.isSupremeCourt[0]"));
        Assert.assertEquals("500 - 13th Avenue", jsonPath.get("courts.address.addressLine1[0]"));
        Assert.assertEquals("V9W 6P1", jsonPath.get("courts.address.postalCode[0]"));
        Assert.assertEquals("Campbell River", jsonPath.get("courts.address.cityName[0]"));
        Assert.assertEquals("British Columbia", jsonPath.get("courts.address.provinceName[0]"));
        Assert.assertEquals("Canada", jsonPath.get("courts.address.countryName[0]"));

        Assert.assertEquals(Integer.valueOf(9393), jsonPath.get("courts.id[1]"));
        Assert.assertEquals("3561", jsonPath.get("courts.identifierCode[1]"));
        Assert.assertEquals("ABB", jsonPath.get("courts.code[1]"));

        logger.info("Response matches the requirements");
    }

}
