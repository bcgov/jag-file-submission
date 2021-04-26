package ca.bc.gov.open.jag.efiling.stepDefinitions;

import ca.bc.gov.open.jag.efiling.Keys;
import ca.bc.gov.open.jag.efiling.services.DocumentTypeConfigService;
import io.cucumber.java.en.And;
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

public class ConfigureRestrictedDocumentedTypesSD {

    private final DocumentTypeConfigService documentTypeConfigService;

    private Response actualCreateRestrictedDocumentConfigResponse;
    private Response actualUpdatedRestrictedDocumentConfigResponse;
    private Response actualDeleteRestrictedDocumentTypeByIdResponse;
    private Response actualGetAllRestrictedDocumentTypeResponse;

    private JsonPath actualGetAllDocTypeJsonPath;

    private final Logger logger = LoggerFactory.getLogger(ConfigureRestrictedDocumentedTypesSD.class);

    public ConfigureRestrictedDocumentedTypesSD(DocumentTypeConfigService documentTypeConfigService) {

        this.documentTypeConfigService = documentTypeConfigService;
    }

    @Given("user adds a new restricted document type")
    public void useAddsANewRestrictedDocumentType() throws IOException {
        logger.info("Creating a new restricted document type configuration");

        actualCreateRestrictedDocumentConfigResponse = documentTypeConfigService.createDocumentTypeConfigResponse(Keys.RESTRICTED_DOCUMENT_TYPE_PAYLOAD, Keys.RESTRICTED_DOCUMENT_TYPE_CONFIGURATION_PATH);

        logger.info("Api response status code: {}", actualCreateRestrictedDocumentConfigResponse.getStatusCode());

    }

    @When("restricted document type config is created")
    public void restrictedDocumentTypeConfigIsCreated() {

        Assert.assertEquals(HttpStatus.SC_OK, actualCreateRestrictedDocumentConfigResponse.getStatusCode());
        Assert.assertEquals("application/json", actualCreateRestrictedDocumentConfigResponse.getContentType());

    }

    @Then("document details can be retrieved using id")
    public void documentDetailsCanBeRetrievedUsingId() {

        JsonPath actualCreatedConfigResponseJsonPath = new JsonPath(actualCreateRestrictedDocumentConfigResponse.asString());

        Assert.assertNotNull(UUID.fromString(actualCreatedConfigResponseJsonPath.get("id")));
        Assert.assertEquals("UNACCEPTED", actualCreatedConfigResponseJsonPath.get("documentType.type"));
        Assert.assertEquals("Unaccepted document type", actualCreatedConfigResponseJsonPath.get("documentType.description"));

    }

    @Given("user updates an existing restricted document type")
    public void userUpdatesAnExistingRestrictedDocumentType() throws IOException, JSONException {
        logger.info("Updating a restricted document type configuration");

        actualUpdatedRestrictedDocumentConfigResponse = documentTypeConfigService
                .updateDocumentTypeConfigResponse(getDocumentId().toString(),
                        Keys.RESTRICTED_DOCUMENT_TYPE_UPDATE_PAYLOAD,
                        Keys.RESTRICTED_DOCUMENT_TYPE_CONFIGURATION_PATH);

        logger.info("Api response status code: {}", actualUpdatedRestrictedDocumentConfigResponse.getStatusCode());

    }

    @When("document details are updated")
    public void documentDetailsAreUpdated() {

        Assert.assertEquals(HttpStatus.SC_NO_CONTENT, actualUpdatedRestrictedDocumentConfigResponse.getStatusCode());
    }


    @Then("updated document details can be retrieved by id")
    public void updatedDocumentDetailsCanBeRetrievedById() {
        logger.info("Requesting to get restricted doc type by id");

        Response actualGetRestrictedDocumentTypeByIdResponse = documentTypeConfigService.getRestrictedDocumentTypeByIdResponse(getDocumentId());

        JsonPath actualUpdatedConfigResponseJsonPath = new JsonPath(actualGetRestrictedDocumentTypeByIdResponse.asString());

        Assert.assertEquals(HttpStatus.SC_OK, actualGetRestrictedDocumentTypeByIdResponse.getStatusCode());
        Assert.assertEquals("application/json", actualGetRestrictedDocumentTypeByIdResponse.getContentType());
        Assert.assertEquals(getDocumentId(), UUID.fromString(actualUpdatedConfigResponseJsonPath.get("id")));
        Assert.assertEquals("UNACCEPTED UPDATED", actualUpdatedConfigResponseJsonPath.get("documentType.type"));
        Assert.assertEquals("Updated unaccepted document type", actualUpdatedConfigResponseJsonPath.get("documentType.description"));

    }

    @Given("user deletes an existing restricted document type using id")
    public void userDeletesAnExistingRestrictedDocumentTypeUsingId() {
        logger.info("Requesting to delete the document type by id");

        actualDeleteRestrictedDocumentTypeByIdResponse = documentTypeConfigService.deleteDocumentTypeByIdResponse(getDocumentId(),
                                                            Keys.RESTRICTED_DOCUMENT_TYPE_CONFIGURATION_PATH);
    }

    @Then("requested document is deleted")
    public void requestedDocumentIsDeleted() {

        Assert.assertEquals(HttpStatus.SC_NO_CONTENT, actualDeleteRestrictedDocumentTypeByIdResponse.getStatusCode());
    }

    @Given("user requests to get all existing restricted document types")
    public void userRequestsToGetAllExistingRestrictedDocumentTypes() throws IOException {
        logger.info("Creating a first restricted document type configuration");

        actualCreateRestrictedDocumentConfigResponse = documentTypeConfigService.createDocumentTypeConfigResponse(Keys.RESTRICTED_DOCUMENT_TYPE_PAYLOAD, Keys.RESTRICTED_DOCUMENT_TYPE_CONFIGURATION_PATH);

        Assert.assertEquals(HttpStatus.SC_OK, actualCreateRestrictedDocumentConfigResponse.getStatusCode());

        logger.info("Creating a second restricted document type configuration");
        Response actualCreateSecondRestrictedDocumentConfigResponse = documentTypeConfigService.createDocumentTypeConfigResponse(Keys.ADDITIONAL_RESTRICTED_DOCUMENT_TYPE_PAYLOAD, Keys.RESTRICTED_DOCUMENT_TYPE_CONFIGURATION_PATH);

        Assert.assertEquals(HttpStatus.SC_OK, actualCreateSecondRestrictedDocumentConfigResponse.getStatusCode());
    }

    @Then("all restricted document types details can be retrieved")
    public void allRestrictedDocumentTypesDetailsCanBeRetrieved() {
        logger.info("Requesting to get all document types");

        actualGetAllRestrictedDocumentTypeResponse = documentTypeConfigService.getDocumentTypeConfiguration(Keys.RESTRICTED_DOCUMENT_TYPE_CONFIGURATION_PATH);
        Assert.assertEquals(HttpStatus.SC_OK, actualGetAllRestrictedDocumentTypeResponse.getStatusCode());

        actualGetAllDocTypeJsonPath = new JsonPath(actualGetAllRestrictedDocumentTypeResponse.asString());
        Assert.assertNotNull(actualGetAllDocTypeJsonPath.get(Keys.ID_INDEX_FROM_RESPONSE));
        Assert.assertEquals("UNACCEPTED", actualGetAllDocTypeJsonPath.get("documentType.type[0]"));
        Assert.assertEquals("Unaccepted document type", actualGetAllDocTypeJsonPath.get("documentType.description[0]"));

        Assert.assertNotNull(actualGetAllDocTypeJsonPath.get("id[1]"));
        Assert.assertEquals("UNACCEPTED2", actualGetAllDocTypeJsonPath.get("documentType.type[1]"));
        Assert.assertEquals("Unaccepted document type2", actualGetAllDocTypeJsonPath.get("documentType.description[1]"));

    }

    @And("documents can be deleted")
    public void deleteMultipleDocuments() {
        logger.info("Requesting to delete the first document type by id");

        actualDeleteRestrictedDocumentTypeByIdResponse = documentTypeConfigService
                .deleteDocumentTypeByIdResponse(UUID.fromString(actualGetAllDocTypeJsonPath.get("id[0]")), Keys.RESTRICTED_DOCUMENT_TYPE_CONFIGURATION_PATH);

        actualGetAllRestrictedDocumentTypeResponse = documentTypeConfigService.getDocumentTypeConfiguration(Keys.RESTRICTED_DOCUMENT_TYPE_CONFIGURATION_PATH);
        Assert.assertEquals(HttpStatus.SC_OK, actualGetAllRestrictedDocumentTypeResponse.getStatusCode());

        actualGetAllDocTypeJsonPath = new JsonPath(actualGetAllRestrictedDocumentTypeResponse.asString());
        Assert.assertNotNull(UUID.fromString(actualGetAllDocTypeJsonPath.get(Keys.ID_INDEX_FROM_RESPONSE)));
        Assert.assertEquals("UNACCEPTED2", actualGetAllDocTypeJsonPath.get("documentType.type[0]"));
        Assert.assertEquals("Unaccepted document type2", actualGetAllDocTypeJsonPath.get("documentType.description[0]"));

        logger.info("Requesting to delete the second document type by id");

        actualDeleteRestrictedDocumentTypeByIdResponse = documentTypeConfigService
                .deleteDocumentTypeByIdResponse(UUID.fromString(actualGetAllDocTypeJsonPath.get(Keys.ID_INDEX_FROM_RESPONSE)), Keys.RESTRICTED_DOCUMENT_TYPE_CONFIGURATION_PATH);
        Assert.assertEquals(HttpStatus.SC_NO_CONTENT, actualDeleteRestrictedDocumentTypeByIdResponse.getStatusCode());

        actualGetAllRestrictedDocumentTypeResponse = documentTypeConfigService.getDocumentTypeConfiguration(Keys.RESTRICTED_DOCUMENT_TYPE_CONFIGURATION_PATH);
        Assert.assertEquals(HttpStatus.SC_OK, actualGetAllRestrictedDocumentTypeResponse.getStatusCode());
    }

    private UUID getDocumentId() {
        logger.info("Requesting to get all document types");

        actualGetAllDocTypeJsonPath = new JsonPath(documentTypeConfigService.getDocumentTypeConfiguration(Keys.RESTRICTED_DOCUMENT_TYPE_CONFIGURATION_PATH).asString());
        return UUID.fromString(actualGetAllDocTypeJsonPath.get(Keys.ID_INDEX_FROM_RESPONSE));
    }
}
