package ca.bc.gov.open.jag.efiling.stepDefinitions;

import ca.bc.gov.open.jag.efiling.models.UserIdentity;
import ca.bc.gov.open.jag.efiling.services.OauthService;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.text.MessageFormat;
import java.util.UUID;

public class GetSubmissionConfigSD {

    private static final String X_TRANSACTION_ID = "X-Transaction-Id";


    private final OauthService oauthService;
    private UUID actualSubmissionId;
    private UUID actualTransactionId;
    private String actualUserToken;
    private Response actualSubmissionConfigResponse;
    private UserIdentity userIdentity;
    private UserIdentity actualUserIdentity;

    public Logger logger = LogManager.getLogger(GetSubmissionConfigSD.class);

    public GetSubmissionConfigSD(OauthService oauthService) {
        this.oauthService = oauthService;
        actualTransactionId = UUID.randomUUID();
    }

    @Given("valid admin account {string}:{string} that authenticated")
    public void validAdminAccountThatAuthenticated(String username, String password) {

        actualUserIdentity = oauthService.getUserIdentity(username,password);
    }

    @When("user submits request to get submission configuration")
    public void aUserSubmitsRequestToGetSubmissionConfiguration() {

        logger.info("Submitting get submission config request");

        RequestSpecification request = RestAssured
                .given()
                .auth()
                .preemptive()
                .oauth2(actualUserIdentity.getAccessToken())
                .header(X_TRANSACTION_ID, actualTransactionId);

        actualSubmissionConfigResponse = request
                .when()
                .get(MessageFormat.format( "http://localhost:8080/submission/{0}/config", actualSubmissionId))
                .then()
                .extract()
                .response();

        logger.info("Api response status code: {}", actualSubmissionConfigResponse.getStatusCode());
        logger.info("Api response: {}", actualSubmissionConfigResponse.asString());

    }

    // TO DO
    @Then("a valid config information is returned")
    public void aValidConfigInformationIsReturned() {

        logger.info("Asserting get submission config response");

    }
}
