package ca.bc.gov.open.jag.efiling.stepDefinitions;

import ca.bc.gov.open.jag.efiling.Keys;
import ca.bc.gov.open.jag.efiling.helpers.SubmissionHelper;
import ca.bc.gov.open.jag.efiling.services.ExtractDocumentService;
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

public class GetExtractedDocumentEventByIdSD {

    private final ExtractDocumentService extractDocumentService;
    private Response actualExtractDocumentServiceResponse;
    private UUID actualTransactionId;
    JsonPath actualExtractDocumentsJsonPath;

    private final Logger logger = LoggerFactory.getLogger(GetExtractedDocumentEventByIdSD.class);

    public GetExtractedDocumentEventByIdSD(ExtractDocumentService extractDocumentService) {
        this.extractDocumentService = extractDocumentService;
        actualTransactionId = UUID.randomUUID();

    }


    @Given("user uploaded a valid RCC document with {string} document type")
    public void validRCCDocumentIsUploaded(String docType) throws IOException {

        logger.info("Submitting request to upload document");

        File resource = new ClassPathResource(
                MessageFormat.format("data/{0}", Keys.TEST_RCC_DOCUMENT_PDF)).getFile();

        MultiPartSpecification fileSpec = SubmissionHelper.fileSpecBuilder(resource, Keys.TEST_RCC_DOCUMENT_PDF);

        actualExtractDocumentServiceResponse = extractDocumentService.extractDocumentsResponse(actualTransactionId, docType, fileSpec);

        logger.info("Api response status code for valid docType: {}", actualExtractDocumentServiceResponse.getStatusCode());
        logger.info("Api response for valid docType: {}", actualExtractDocumentServiceResponse.asString());

        assertEquals(HttpStatus.SC_OK, actualExtractDocumentServiceResponse.getStatusCode());
        assertEquals("application/json", actualExtractDocumentServiceResponse.getContentType());

    }

    @When("document event is retrieved by document id")
    public void retrievedDocumentById() {

        actualExtractDocumentsJsonPath = new JsonPath(actualExtractDocumentServiceResponse.asString());

        Integer documentIdForValidDocument = actualExtractDocumentsJsonPath.get("document.documentId");

        actualExtractDocumentServiceResponse = extractDocumentService.getProcessedDocumentDataById(actualTransactionId, documentIdForValidDocument);

        logger.info("Api response status code for valid docType event: {}", actualExtractDocumentServiceResponse.getStatusCode());
        logger.info("Api response for valid docType event: {}", actualExtractDocumentServiceResponse.asString());

        Assert.assertEquals(HttpStatus.SC_OK, actualExtractDocumentServiceResponse.getStatusCode());

    }

    @Then("document type validation flag is set for processing")
    public void verifyDocumentTypeValidationIsSet() {

        actualExtractDocumentsJsonPath = new JsonPath(actualExtractDocumentServiceResponse.asString());

        Assert.assertEquals(Keys.TEST_RCC_DOCUMENT_PDF, actualExtractDocumentsJsonPath.get("document.fileName"));
        Assert.assertNotNull(actualExtractDocumentsJsonPath.get("validation"));
        Assert.assertEquals("209753", actualExtractDocumentsJsonPath.get("result.court.fileNumber"));

    }

    @Given("user uploaded a invalid RCC document with {string} document type")
    public void invalidRCCDocumentIsUploaded(String docType) throws IOException {

        logger.info("Submitting request to upload document");

        File resource = new ClassPathResource(
                MessageFormat.format("data/{0}", Keys.TEST_DOCUMENT_PDF)).getFile();

        MultiPartSpecification fileSpec = SubmissionHelper.fileSpecBuilder(resource, Keys.TEST_DOCUMENT_PDF);

        actualExtractDocumentServiceResponse = extractDocumentService.extractDocumentsResponse(actualTransactionId, docType, fileSpec);

        logger.info("Api response status code for invalid docType event: {}", actualExtractDocumentServiceResponse.getStatusCode());
        logger.info("Api response for invalid docType event: {}", actualExtractDocumentServiceResponse.asString());

        assertEquals(HttpStatus.SC_OK, actualExtractDocumentServiceResponse.getStatusCode());
        assertEquals("application/json", actualExtractDocumentServiceResponse.getContentType());

    }

    @Then("document type validation flag is not set and provides error details")
    public void verifyDocumentTypeValidationIsNotSet() {

        actualExtractDocumentsJsonPath = new JsonPath(actualExtractDocumentServiceResponse.asString());

        Assert.assertEquals(Keys.TEST_DOCUMENT_PDF, actualExtractDocumentsJsonPath.get("document.fileName"));
        Assert.assertEquals("DOCUMENT_TYPE", actualExtractDocumentsJsonPath.get("validation[0].type"));
        Assert.assertEquals("Response to Civil Claim", actualExtractDocumentsJsonPath.get("validation[0].expected"));
        Assert.assertEquals("No Document Found", actualExtractDocumentsJsonPath.get("validation[0].actual"));

    }
}
