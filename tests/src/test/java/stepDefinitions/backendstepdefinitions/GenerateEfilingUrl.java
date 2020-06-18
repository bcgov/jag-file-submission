package stepDefinitions.backendstepdefinitions;

import ca.bc.gov.open.jagefilingapi.qa.backend.generateurl.GenerateUrl;
import ca.bc.gov.open.jagefilingapi.qa.backendutils.TestUtil;
import static org.junit.Assert.*;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import java.io.IOException;

public class GenerateEfilingUrl extends TestUtil {

    GenerateUrl generateUrl;

    @Given("user calls efilingAPI with POST http request")
    public void user_calls_efilingAPI_with_POST_http_request() throws IOException {
        generateUrl = new GenerateUrl();

        generateUrl.postGenerateUrlPayload();
    }

    @When("status code and content type are verified")
    public void status_code_and_content_type_are_verified() throws IOException {
        responseSpecification();
    }

    @Then("the response returns efiling url and expiry date")
    public void the_response_returns_efiling_url_and_expiry_date() throws IOException {
        generateUrl = new GenerateUrl();

        assertNotNull(generateUrl.verifyFirstEfilingUrlIsGenerated());
        assertNotNull(generateUrl.verifySecondEfilingUrlIsGenerated());
        assertNotEquals(0, generateUrl.verifyExpDateIsGenerated());
    }
}
