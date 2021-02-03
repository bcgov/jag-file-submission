package ca.bc.gov.open.jag.efiling.services;

import ca.bc.gov.open.jag.efiling.helpers.SubmissionHelper;
import ca.bc.gov.open.jag.efiling.models.UserIdentity;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.MultiPartSpecification;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.UUID;

public class GenerateUrlService {

    private final OauthService oauthService;
    private final SubmissionService submissionService;

    private static final String TEST_DOCUMENT_PDF = "test-document.pdf";

    private UUID actualTransactionId;
    private Response actualDocumentResponse;
    private Response actualGenerateUrlResponse;
    private String actualSubmissionId;
    private UserIdentity actualUserIdentity;

    public Logger logger = LogManager.getLogger(GenerateUrlService.class);

    public GenerateUrlService(OauthService oauthService, SubmissionService submissionService) {
        this.oauthService = oauthService;
        this.submissionService = submissionService;
        actualTransactionId = UUID.randomUUID();
    }

    public String getGeneratedUrl() throws IOException {

        actualUserIdentity = oauthService.getUserIdentity();

        logger.info("Submitting document upload request");

        File resource = new ClassPathResource(
                MessageFormat.format("data/{0}", TEST_DOCUMENT_PDF)).getFile();

        MultiPartSpecification fileSpec = SubmissionHelper.fileSpecBuilder(resource, TEST_DOCUMENT_PDF, "text/application.pdf");

        actualDocumentResponse = submissionService.documentUploadResponse(actualUserIdentity.getAccessToken(), actualTransactionId,
                actualUserIdentity.getUniversalId(), fileSpec);

        logger.info("Api response status code: {}", actualDocumentResponse.getStatusCode());
        logger.info("Api response: {}", actualDocumentResponse.asString());


        actualSubmissionId = submissionService.getSubmissionId(actualDocumentResponse);

        actualGenerateUrlResponse = submissionService.generateUrlResponse(actualTransactionId, actualUserIdentity.getUniversalId(),
                actualUserIdentity.getAccessToken(), actualSubmissionId);

        logger.info("Api response status code: {}", actualDocumentResponse.getStatusCode());
        logger.info("Api response: {}", actualDocumentResponse.asString());

        logger.info("Returning E-Filing url from generateUrl response");

        JsonPath actualJson = new JsonPath(actualGenerateUrlResponse.asString());

        Assert.assertEquals(200, actualGenerateUrlResponse.getStatusCode());
        return actualJson.getString("efilingUrl");

    }
}
