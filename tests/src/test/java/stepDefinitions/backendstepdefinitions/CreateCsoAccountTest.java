package stepDefinitions.backendstepdefinitions;

import ca.bc.gov.open.jagefilingapi.qa.backend.createcsoaccountpayload.CreateCsoAccountPayload;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class CreateCsoAccountTest {

    @Given("POST http request is made to {string} with a valid request body")
    public void postHttpRequestIsMadeToWithAValidRequestBody(String string) throws JsonProcessingException {
        CreateCsoAccountPayload createCsoAccountPayload = new CreateCsoAccountPayload();

        System.out.println(createCsoAccountPayload.createCsoAccountPayload());
    }

    @When("status is {int} and content type is verified")
    public void statusIsAndContentTypeIsVerified(Integer int1) {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }

    @Then("verify response returns names, email and accounts with type and identifiers")
    public void verifyResponseReturnsNamesEmailAndAccountsWithTypeAndIdentifiers() {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }

    @Given("POST http request is made to {string} with incorrect account type")
    public void postHttpRequestIsMadeToWithIncorrectAccountType(String string) {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }

    @Then("verify response returns bad request error, status and an empty message")
    public void verifyResponseReturnsBadRequestErrorStatusAndAnEmptyMessage() {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }

    @Given("POST http request is made to {string} with incorrect path value")
    public void postHttpRequestIsMadeToWithIncorrectPathBalue(String string) {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }

    @Then("verify response returns error not found error, status and an empty message")
    public void verifyResponseReturnsErrorNotFoundErrorStatusAndAnEmptyMessage() {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }

    @Given("POST http request is made to {string} without request body")
    public void postHttpRequestIsMadeToWithoutRequestBody(String string) {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }

    @Then("verify response returns unsupported media type error, status, and an empty message")
    public void verifyResponseReturnsUnsupportedMediaTypeErrorStatusAndAnEmptyMessage() {
        // Write code here that turns the phrase above into concrete actions
        throw new io.cucumber.java.PendingException();
    }
}
