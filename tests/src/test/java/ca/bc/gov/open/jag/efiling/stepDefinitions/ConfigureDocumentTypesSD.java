package ca.bc.gov.open.jag.efiling.stepDefinitions;

import ca.bc.gov.open.jag.efiling.Keys;
import ca.bc.gov.open.jag.efiling.services.DocumentTypeConfigService;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.json.JSONException;
import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.UUID;

public class ConfigureDocumentTypesSD {

    private final DocumentTypeConfigService documentTypeConfigService;

    private Response actualCreatedConfigResponse;
    private Response actualUpdatedConfigResponse;
    private Response actualDeleteDocumentTypeByIdResponse;

    private JsonPath actualConfigResponseJsonPath;

    private final Logger logger = LoggerFactory.getLogger(ConfigureDocumentTypesSD.class);

    public ConfigureDocumentTypesSD(DocumentTypeConfigService documentTypeConfigService) {

        this.documentTypeConfigService = documentTypeConfigService;
    }

    @Given("user configures a new document type")
    public void configureADocumentType() throws IOException {
        logger.info("Creating a new document type configuration");

        actualCreatedConfigResponse = documentTypeConfigService.createDocumentTypeConfigResponse(Keys.DOCUMENT_TYPE_CONFIG_PAYLOAD, Keys.DOCUMENT_TYPE_CONFIGURATION_PATH);

        logger.info("Api response status code: {}", actualCreatedConfigResponse.getStatusCode());
    }

    @When("document type config is created")
    public void verifyDocumentTypeConfig() {

        Assert.assertEquals(HttpStatus.SC_OK, actualCreatedConfigResponse.getStatusCode());
        Assert.assertEquals("application/json", actualCreatedConfigResponse.getContentType());
    }

    @Then("document type configuration can be retrieved")
    public void documentTypeConfigCanBeRetrieved() {
        logger.info("Get created document type configuration");

        actualCreatedConfigResponse = documentTypeConfigService.getDocumentTypeConfiguration(Keys.DOCUMENT_TYPE_CONFIGURATION_PATH);

        logger.info("Api response: {}", actualCreatedConfigResponse.asString());

        actualConfigResponseJsonPath = new JsonPath(actualCreatedConfigResponse.asString());

        Assert.assertNotNull(UUID.fromString(actualConfigResponseJsonPath.get(Keys.ID_INDEX_FROM_RESPONSE)));
    }

    @Given("user updates an existing configured document type")
    public void userUpdatesAnExistingConfiguredDocumentType() throws IOException, JSONException {
        logger.info("Updating a document type configuration");

        actualUpdatedConfigResponse = documentTypeConfigService
                .updateDocumentTypeConfigResponse(getActualDocumentId().toString(),
                        Keys.DOCUMENT_TYPE_CONFIG_UPDATE_PAYLOAD,
                        Keys.DOCUMENT_TYPE_CONFIGURATION_PATH);

        logger.info("Api response status code: {}", actualUpdatedConfigResponse.getStatusCode());
    }

    @When("document configuration details are updated")
    public void documentConfigurationDetailsAreUpdated() {

        Assert.assertEquals(HttpStatus.SC_OK, actualUpdatedConfigResponse.getStatusCode());

    }

    @Then("updated document type configuration can be retrieved")
    public void updatedDocumentTypeConfigurationCanBeRetrieved() {
        logger.info("Requesting to get updated doc type");

        Response actualGetAllDocumentTypesResponse = documentTypeConfigService.getDocumentTypeConfiguration(Keys.DOCUMENT_TYPE_CONFIGURATION_PATH);

        JsonPath actualUpdatedConfigResponseJsonPath = new JsonPath(actualGetAllDocumentTypesResponse.asString());

        Assert.assertEquals(HttpStatus.SC_OK, actualGetAllDocumentTypesResponse.getStatusCode());
        Assert.assertEquals("application/json", actualGetAllDocumentTypesResponse.getContentType());
        Assert.assertEquals(getActualDocumentId(), UUID.fromString(actualUpdatedConfigResponseJsonPath.get(Keys.ID_INDEX_FROM_RESPONSE)));
        Assert.assertEquals("RCC", actualUpdatedConfigResponseJsonPath.get("documentType[0].type"));
        Assert.assertEquals("Update Document Configuration TEST", actualUpdatedConfigResponseJsonPath.get("documentType[0].description"));
        Assert.assertEquals("Updated Court", actualUpdatedConfigResponseJsonPath.get("documentConfig[0].properties.court.type"));

    }

    @Given("user deletes an existing configured document type using id")
    public void userDeletesAnExistingConfiguredDocumentTypeUsingId() {
        logger.info("Requesting to delete the document type by id");

        actualDeleteDocumentTypeByIdResponse = documentTypeConfigService.deleteDocumentTypeByIdResponse(getActualDocumentId(),
                Keys.DOCUMENT_TYPE_CONFIGURATION_PATH);
    }

    @Then("requested document type configuration is deleted")
    public void requestedDocumentTypeConfigurationIsDeleted() {

        Assert.assertEquals(HttpStatus.SC_NO_CONTENT, actualDeleteDocumentTypeByIdResponse.getStatusCode());
    }

    private UUID getActualDocumentId() {
        logger.info("Requesting to get all document types");

        actualConfigResponseJsonPath = new JsonPath(documentTypeConfigService.getDocumentTypeConfiguration(Keys.DOCUMENT_TYPE_CONFIGURATION_PATH).asString());
        return UUID.fromString(actualConfigResponseJsonPath.get(Keys.ID_INDEX_FROM_RESPONSE));
    }
}
