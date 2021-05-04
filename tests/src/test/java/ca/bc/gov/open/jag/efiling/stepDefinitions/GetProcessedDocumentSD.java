package ca.bc.gov.open.jag.efiling.stepDefinitions;

import ca.bc.gov.open.jag.efiling.Keys;
import ca.bc.gov.open.jag.efiling.helpers.SubmissionHelper;
import ca.bc.gov.open.jag.efiling.services.DocumentTypeConfigService;
import ca.bc.gov.open.jag.efiling.services.ExtractDocumentService;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.MultiPartSpecification;
import org.apache.http.HttpStatus;
import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.UUID;

import static org.junit.Assert.assertEquals;

public class GetProcessedDocumentSD {

    private final ExtractDocumentService extractDocumentService;
    private Response actualExtractDocumentServiceResponse;
    private final DocumentTypeConfigService documentTypeConfigService;
    JsonPath actualExtractDocumentsJsonPath;

    private final Logger logger = LoggerFactory.getLogger(GetProcessedDocumentSD.class);

    public GetProcessedDocumentSD(DocumentTypeConfigService documentTypeConfigService, ExtractDocumentService extractDocumentService) {
        this.extractDocumentService = extractDocumentService;
        this.documentTypeConfigService = documentTypeConfigService;
    }


    @Given("user uploaded a valid RCC document with {string} document type")
    public void validRCCDocumentIsUploaded(String docType) throws IOException {

        logger.info("Creating a new document type configuration");

        Response actualCreatedConfigResponse = documentTypeConfigService.createDocumentTypeConfigResponse(Keys.DOCUMENT_TYPE_CONFIG_PAYLOAD, Keys.DOCUMENT_TYPE_CONFIGURATION_PATH);

        logger.info("Api response status code: {}", actualCreatedConfigResponse.getStatusCode());

        logger.info("Submitting request to upload document");

        File resource = new ClassPathResource(
                MessageFormat.format("data/{0}", Keys.TEST_VALID_DOCUMENT_PDF)).getFile();

        MultiPartSpecification fileSpec = SubmissionHelper.fileSpecBuilder(resource, Keys.TEST_VALID_DOCUMENT_PDF);

        actualExtractDocumentServiceResponse = extractDocumentService.extractDocumentsResponse(UUID.fromString(Keys.ACTUAL_X_TRANSACTION_ID), docType, fileSpec);

        logger.info("Api response status code for valid docType: {}", actualExtractDocumentServiceResponse.getStatusCode());
        logger.info("Api response for valid docType: {}", actualExtractDocumentServiceResponse.asString());

        assertEquals(HttpStatus.SC_OK, actualExtractDocumentServiceResponse.getStatusCode());
        assertEquals("application/json", actualExtractDocumentServiceResponse.getContentType());

    }

    @When("document event is retrieved by document id")
    public void retrievedDocumentById() {

        actualExtractDocumentsJsonPath = new JsonPath(actualExtractDocumentServiceResponse.asString());

        Integer documentIdForValidDocument = actualExtractDocumentsJsonPath.get("document.documentId");

        actualExtractDocumentServiceResponse = extractDocumentService.getProcessedDocumentDataById(UUID.fromString(Keys.ACTUAL_X_TRANSACTION_ID), documentIdForValidDocument);

        logger.info("Api response status code for valid docType event: {}", actualExtractDocumentServiceResponse.getStatusCode());
        logger.info("Api response for valid docType event: {}", actualExtractDocumentServiceResponse.asString());

        Assert.assertEquals(HttpStatus.SC_OK, actualExtractDocumentServiceResponse.getStatusCode());

    }

    @Then("document type validation flag is set for processing")
    public void verifyDocumentTypeValidationFlagIsSet() {

        actualExtractDocumentsJsonPath = new JsonPath(actualExtractDocumentServiceResponse.asString());

        Assert.assertEquals(Keys.TEST_VALID_DOCUMENT_PDF, actualExtractDocumentsJsonPath.get("document.fileName"));
        Assert.assertNotNull(actualExtractDocumentsJsonPath.get("validation"));
        Assert.assertEquals("1234", actualExtractDocumentsJsonPath.get("result.court.fileNumber"));

    }

    @And("document is deleted")
    public void verifyDocumentIsDeleted() {

        logger.info("Requesting to get all document types");

        JsonPath actualConfigResponseJsonPath = new JsonPath(documentTypeConfigService.getDocumentTypeConfiguration(Keys.DOCUMENT_TYPE_CONFIGURATION_PATH).asString());
        UUID getDocTypeId = UUID.fromString(actualConfigResponseJsonPath.get(Keys.ID_INDEX_FROM_RESPONSE));

        logger.info("Requesting to delete the document type by id");
        Response actualDeleteDocumentTypeByIdResponse = documentTypeConfigService.deleteDocumentTypeByIdResponse(getDocTypeId,
                Keys.DOCUMENT_TYPE_CONFIGURATION_PATH);

        assertEquals(HttpStatus.SC_NO_CONTENT, actualDeleteDocumentTypeByIdResponse.getStatusCode());

    }

    @Given("user uploaded a invalid RCC document with {string} document type")
    public void invalidRCCDocumentIsUploaded(String docType) throws IOException {

        logger.info("Creating a new document type configuration");

        Response actualCreatedConfigResponse = documentTypeConfigService.createDocumentTypeConfigResponse(Keys.DOCUMENT_TYPE_CONFIG_PAYLOAD, Keys.DOCUMENT_TYPE_CONFIGURATION_PATH);

        logger.info("Api response status code: {}", actualCreatedConfigResponse.getStatusCode());

        logger.info("Submitting request to upload document");

        File resource = new ClassPathResource(
                MessageFormat.format("data/{0}", Keys.TEST_INVALID_DOCUMENT_PDF)).getFile();

        MultiPartSpecification fileSpec = SubmissionHelper.fileSpecBuilder(resource, Keys.TEST_INVALID_DOCUMENT_PDF);

        actualExtractDocumentServiceResponse = extractDocumentService.extractDocumentsResponse(UUID.fromString(Keys.ACTUAL_X_TRANSACTION_ID), docType, fileSpec);

        logger.info("Api response status code for invalid docType event: {}", actualExtractDocumentServiceResponse.getStatusCode());
        logger.info("Api response for invalid docType event: {}", actualExtractDocumentServiceResponse.asString());

        assertEquals(HttpStatus.SC_OK, actualExtractDocumentServiceResponse.getStatusCode());
        assertEquals("application/json", actualExtractDocumentServiceResponse.getContentType());

    }

    @When("document event is retrieved")
    public void retrieveDocumentById() {

        actualExtractDocumentsJsonPath = new JsonPath(actualExtractDocumentServiceResponse.asString());

        Integer documentIdForInvalidDocument = actualExtractDocumentsJsonPath.get("document.documentId");

        actualExtractDocumentServiceResponse = extractDocumentService.getProcessedDocumentDataById(UUID.fromString(Keys.ACTUAL_X_TRANSACTION_ID), documentIdForInvalidDocument);

        logger.info("Api response status code for invalid docType event: {}", actualExtractDocumentServiceResponse.getStatusCode());
        logger.info("Api response for invalid docType event: {}", actualExtractDocumentServiceResponse.asString());

        Assert.assertEquals(HttpStatus.SC_OK, actualExtractDocumentServiceResponse.getStatusCode());

    }

    @Then("document type validation flag is not set and provides error details")
    public void verifyDocumentTypeValidationFlagIsNotSet() {

        actualExtractDocumentsJsonPath = new JsonPath(actualExtractDocumentServiceResponse.asString());

        Assert.assertEquals(Keys.TEST_INVALID_DOCUMENT_PDF, actualExtractDocumentsJsonPath.get("document.fileName"));
        Assert.assertEquals("DOCUMENT_TYPE", actualExtractDocumentsJsonPath.get("validation[0].type"));
        Assert.assertEquals("Response to Civil Claim", actualExtractDocumentsJsonPath.get("validation[0].expected"));
        Assert.assertEquals("This is invalid", actualExtractDocumentsJsonPath.get("validation[0].actual"));
        Assert.assertEquals("1234", actualExtractDocumentsJsonPath.get("result.court.fileNumber"));

    }
}
