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
import static org.junit.Assert.assertNotNull;

public class ExtractValidatedDocumentsSD {

    private final ExtractDocumentService extractDocumentService;
    private Response actualExtractDocumentServiceResponse;
    private UUID actualTransactionId;

    private final Logger logger = LoggerFactory.getLogger(ExtractValidatedDocumentsSD.class);

    public ExtractValidatedDocumentsSD(ExtractDocumentService extractDocumentService) {
        this.extractDocumentService = extractDocumentService;
        actualTransactionId = UUID.randomUUID();

    }

    @Given("user uploaded a {string} document type")
    public void validDocumentTypeIsUploaded(String docType) throws IOException {

        logger.info("Submitting request to upload document");

        File resource = new ClassPathResource(
                MessageFormat.format("data/{0}", Keys.TEST_VALID_DOCUMENT_PDF)).getFile();

        MultiPartSpecification fileSpec = SubmissionHelper.fileSpecBuilder(resource, Keys.TEST_VALID_DOCUMENT_PDF);

        actualExtractDocumentServiceResponse = extractDocumentService.extractDocumentsResponse(actualTransactionId, docType, fileSpec);

        logger.info("Api response status code: {}", actualExtractDocumentServiceResponse.getStatusCode());
        logger.info("Api response: {}", actualExtractDocumentServiceResponse.asString());
    }

    @When("document is processed")
    public void documentIsProcessed() {

        assertEquals(HttpStatus.SC_OK, actualExtractDocumentServiceResponse.getStatusCode());

    }

    @Then("document form data is extracted")
    public void documentFormDataIsExtracted() {
        logger.info("Asserting extract document response");

        JsonPath actualExtractDocumentsJsonPath = new JsonPath(actualExtractDocumentServiceResponse.asString());

        assertNotNull(actualExtractDocumentsJsonPath.get("extract.id"));
        Assert.assertEquals(actualTransactionId, UUID.fromString(actualExtractDocumentsJsonPath.get("extract.transactionId")));

        Assert.assertNotNull(actualExtractDocumentsJsonPath.get("document.documentId"));
        Assert.assertTrue(actualExtractDocumentsJsonPath.get("document.documentId") instanceof Integer);
        Assert.assertEquals("RCC", actualExtractDocumentsJsonPath.get("document.type"));
        Assert.assertEquals(Keys.TEST_VALID_DOCUMENT_PDF, actualExtractDocumentsJsonPath.get("document.fileName"));
        Assert.assertEquals(Integer.valueOf(689336), actualExtractDocumentsJsonPath.get("document.size"));
        Assert.assertEquals("application/octet-stream", actualExtractDocumentsJsonPath.get("document.contentType"));

        logger.info("Response matched requirements");

    }

    @Then("document is not processed")
    public void documentIsNotProcessed() {
        logger.info("Asserting invalid document response");

        assertEquals(HttpStatus.SC_BAD_REQUEST, actualExtractDocumentServiceResponse.getStatusCode());
        Assert.assertEquals("Document type not found", actualExtractDocumentServiceResponse.getBody().asString());

        logger.info("Response matched requirements");
    }
}
