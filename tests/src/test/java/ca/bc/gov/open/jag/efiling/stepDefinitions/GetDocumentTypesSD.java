package ca.bc.gov.open.jag.efiling.stepDefinitions;

import ca.bc.gov.open.jag.efiling.models.UserIdentity;
import ca.bc.gov.open.jag.efiling.services.DocumentService;
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

public class GetDocumentTypesSD {

    private final OauthService oauthService;
    private final DocumentService documentService;
    private UserIdentity actualUserIdentity;
    private Response actualDocumentTypesResponse;

    private final Logger logger = LoggerFactory.getLogger(ca.bc.gov.open.jag.efiling.stepDefinitions.GetDocumentTypesSD.class);

    public GetDocumentTypesSD(OauthService oauthService, DocumentService documentService) {
        this.oauthService = oauthService;
        this.documentService = documentService;
    }

    @Given("user credentials are authenticated")
    public void userCredentialsAreAuthenticated() {

        actualUserIdentity = oauthService.getUserIdentity();
    }

    @When("user submits request to get document types")
    public void getDocumentsTypesRequest() {
        logger.info("Submitting get document types request");

        actualDocumentTypesResponse = documentService.getDocumentTypesResponse(actualUserIdentity.getAccessToken(), "P", "F");

        logger.info("Api response status code: {}", Integer.valueOf(actualDocumentTypesResponse.getStatusCode()));
        logger.info("Api response: {}", actualDocumentTypesResponse.asString());
    }

    @Then("valid document type and description is returned")
    public void getDocumentTypesAndDescription() {

        logger.info("Asserting get document types response");

        JsonPath actualDocumentTypeResponseJsonPath = new JsonPath(actualDocumentTypesResponse.asString());

        Assert.assertEquals(HttpStatus.SC_OK, actualDocumentTypesResponse.getStatusCode());
        Assert.assertEquals("application/json", actualDocumentTypesResponse.getContentType());

        Assert.assertEquals("AFF", actualDocumentTypeResponseJsonPath.get("type[0]"));
        Assert.assertEquals("Description1", actualDocumentTypeResponseJsonPath.get("description[0]"));
        Assert.assertEquals("Type2", actualDocumentTypeResponseJsonPath.get("type[1]"));
        Assert.assertEquals("Description2", actualDocumentTypeResponseJsonPath.get("description[1]"));

        logger.info("Response matches the requirements");
    }

}
