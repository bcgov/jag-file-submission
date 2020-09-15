package stepDefinitions.backendstepdefinitions;

import ca.bc.gov.open.jagefilingapi.qa.backendutils.GenerateUrlHelper;
import ca.bc.gov.open.jagefilingapi.qa.frontendutils.FrontendTestUtil;
import ca.bc.gov.open.jagefilingapi.qa.requestbuilders.LookUpRequestBuilders;
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

public class GetDocumentTypeTest {

    private LookUpRequestBuilders lookUpRequestBuilders;
    private static final String CONTENT_TYPE = "application/json";
    public Logger log = LogManager.getLogger(GetDocumentTypeTest.class);
    private Response response;

    @Given("Get http request is made to {string} with court level and class details")
    public void GetHttpRequestIsMadeWithCourtLevelAndClassDetails(String resource) throws IOException, InterruptedException {
        lookUpRequestBuilders = new LookUpRequestBuilders();
        GenerateUrlHelper generateUrlHelper = new GenerateUrlHelper();
        String respUrl = generateUrlHelper.getGeneratedUrl();

        FrontendTestUtil frontendTestUtil = new FrontendTestUtil();
        String userToken = frontendTestUtil.getUserJwtToken(respUrl);

        response = lookUpRequestBuilders.requestToGetDocumentTypes(resource, userToken);
    }

    @When("response code {int} and content type are verified")
    public void statusIsAndContentTypeIsVerified(Integer status) {
        lookUpRequestBuilders = new LookUpRequestBuilders();

        assertEquals(200, response.getStatusCode());
        assertEquals(CONTENT_TYPE, response.getContentType());

        log.info("Expected status code did not match with the provided codes.");
    }

    @Then("verify response returns documentType and description")
    public void verifyResponseReturnsDocumentTypeAndDescription() {
        JsonPath jsonPath = new JsonPath(response.asString());

        assertNotNull(jsonPath.get("documentTypes.type"));
        assertNotNull(jsonPath.get("documentTypes.description"));
    }
}
