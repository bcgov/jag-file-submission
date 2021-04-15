package ca.bc.gov.open.jag.efiling.stepDefinitions;

import ca.bc.gov.open.jag.efiling.Keys;
import ca.bc.gov.open.jag.efiling.services.DocumentTypeConfigService;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.UUID;

public class CreateDocumentTypeConfigSD {

    private final DocumentTypeConfigService documentTypeConfigService;
    private final Logger logger = LoggerFactory.getLogger(CreateDocumentTypeConfigSD.class);

    public CreateDocumentTypeConfigSD(DocumentTypeConfigService documentTypeConfigService) {

        this.documentTypeConfigService = documentTypeConfigService;
    }

    @Given("user configures a document type")
    public void configureADocumentType() throws IOException {
        logger.info("Creating a new document type configuration");
        Response actualCreateDocumentTypeConfigResponse = documentTypeConfigService.createDocumentTypeConfigResponse(Keys.DOCUMENT_TYPE_PAYLOAD, Keys.DOCUMENT_TYPE_CONFIGURATION_PATH);

        logger.info("Api response status code: {}", actualCreateDocumentTypeConfigResponse.getStatusCode());
    }

    @When("document type config is created")
    public void verifyDocumentTypeConfig() {
        logger.info("Get created document type configuration");
        Response actualCreatedConfigResponse = documentTypeConfigService.getCreatedDocumentTypeConfiguration();

        JsonPath actualCreatedConfigResponseJsonPath = new JsonPath(actualCreatedConfigResponse.asString());

        Assert.assertEquals(HttpStatus.SC_OK, actualCreatedConfigResponse.getStatusCode());
        Assert.assertEquals("application/json", actualCreatedConfigResponse.getContentType());

        Assert.assertNotNull(UUID.fromString(actualCreatedConfigResponseJsonPath.get("id[0]")));
    }
}
