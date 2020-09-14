package stepDefinitions.backendstepdefinitions;

import ca.bc.gov.open.jagefilingapi.qa.requestbuilders.PaymentRequestBuilders;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class GenerateUpdateCardTest {

    private PaymentRequestBuilders paymentRequestBuilders;
    private static final String CONTENT_TYPE = "application/json";
    public Logger log = LogManager.getLogger(GenerateUpdateCardTest.class);
    private Response response;

    @Given("POST http request is made to {string} with internalClientNumber and redirect Url details")
    public void GetHttpRequestIsMadeWithInternalClientNumberAndRedirectUrlDetails(String resource) throws IOException, InterruptedException {
        paymentRequestBuilders = new PaymentRequestBuilders();

        response = paymentRequestBuilders.requestToGenerateUpdateCard(resource);
    }

    @When("status {int} is correct and content type are verified")
    public void statusIsAndContentTypeIsVerified(Integer status) {
        paymentRequestBuilders = new PaymentRequestBuilders();

        assertEquals(200, response.getStatusCode());
        assertEquals(CONTENT_TYPE, response.getContentType());

        log.info("Expected status code did not match with the provided codes.");
    }

    @Then("verify response returns bambora Url")
    public void verifyResponseReturnsBamboraUrl() {
        JsonPath jsonPath = new JsonPath(response.asString());

        assertNotNull(jsonPath.get("bamboraUrl"));
    }
}
