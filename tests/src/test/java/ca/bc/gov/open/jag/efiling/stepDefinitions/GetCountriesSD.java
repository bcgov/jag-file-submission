package ca.bc.gov.open.jag.efiling.stepDefinitions;

import ca.bc.gov.open.jag.efiling.models.UserIdentity;
import ca.bc.gov.open.jag.efiling.services.CountryService;
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

import java.util.UUID;

public class GetCountriesSD {

    private final OauthService oauthService;
    private final CountryService countryService;
    private UserIdentity actualUserIdentity;
    private Response actualCountriesResponse;
    private final UUID actualTransactionId;


    private final Logger logger = LoggerFactory.getLogger(GetCountriesSD.class);

    public GetCountriesSD(OauthService oauthService, CountryService countryService) {
        this.oauthService = oauthService;
        this.countryService = countryService;
        actualTransactionId = UUID.randomUUID();
    }

    @Given("validate user is authenticated")
    public void userInfoIsAuthenticated() {

        actualUserIdentity = oauthService.getUserIdentity();
    }

    @When("user submits request to get countries")
    public void getCountriesRequest() {
        logger.info("Submitting get countries request");

        actualCountriesResponse = countryService.getCountriesResponse(actualUserIdentity.getAccessToken(), actualTransactionId);

        logger.info("Api response status code: {}", Integer.valueOf(actualCountriesResponse.getStatusCode()));
        logger.info("Api response: {}", actualCountriesResponse.asString());
    }

    @Then("a valid country code and description is returned")
    public void getCountryCodeAndDescription() {

        logger.info("Asserting get countries response");

        JsonPath jsonPath = new JsonPath(actualCountriesResponse.asString());

        Assert.assertEquals(HttpStatus.SC_OK, actualCountriesResponse.getStatusCode());
        Assert.assertEquals("application/json", actualCountriesResponse.getContentType());

        Assert.assertEquals("1", jsonPath.get("code[0]"));
        Assert.assertEquals("Canada", jsonPath.get("description[0]"));

        Assert.assertEquals("1", jsonPath.get("code[1]"));
        Assert.assertEquals("United States", jsonPath.get("description[1]"));

        Assert.assertEquals("34", jsonPath.get("code[2]"));
        Assert.assertEquals("Spain", jsonPath.get("description[2]"));

        logger.info("Response matches the requirements");
    }

}
