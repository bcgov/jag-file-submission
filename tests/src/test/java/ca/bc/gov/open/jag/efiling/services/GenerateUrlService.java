package ca.bc.gov.open.jag.efiling.services;

import ca.bc.gov.open.jag.efiling.Keys;
import ca.bc.gov.open.jag.efiling.error.EfilingTestException;
import ca.bc.gov.open.jag.efiling.helpers.SubmissionHelper;
import ca.bc.gov.open.jag.efiling.models.UserIdentity;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.MultiPartSpecification;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.UUID;

public class GenerateUrlService {

    private final OauthService oauthService;
    private final SubmissionService submissionService;

    private UUID actualTransactionId;

    public Logger logger = LogManager.getLogger(GenerateUrlService.class);

    public GenerateUrlService(OauthService oauthService, SubmissionService submissionService) {
        this.oauthService = oauthService;
        this.submissionService = submissionService;
        actualTransactionId = UUID.randomUUID();
    }

    public String getGeneratedUrl(String actionDocumentStatus) {

        UserIdentity actualUserIdentity = oauthService.getUserIdentity();

        logger.info("Submitting document upload request");

        File resource = null;

        try {
            resource = new ClassPathResource(
                    MessageFormat.format("data/{0}", Keys.TEST_DOCUMENT_PDF)).getFile();
        } catch (IOException e) {
            logger.error("Exception while getting test file");
            throw new EfilingTestException("Exception while getting test file", e);
        }

        MultiPartSpecification fileSpec = SubmissionHelper.fileSpecBuilder(resource, Keys.TEST_DOCUMENT_PDF, "text/application.pdf");

        Response actualDocumentResponse = submissionService.documentUploadResponse(actualUserIdentity.getAccessToken(), actualTransactionId,
                actualUserIdentity.getUniversalId(), fileSpec);

        logger.info("Api response status code: {}", Integer.valueOf(actualDocumentResponse.getStatusCode()));
        logger.info("Api response: {}", actualDocumentResponse.asString());


        String actualSubmissionId = submissionService.getSubmissionId(actualDocumentResponse);

        Response actualGenerateUrlResponse = submissionService.generateUrlResponse(actualTransactionId, actualUserIdentity.getUniversalId(),
                actualUserIdentity.getAccessToken(), actualSubmissionId, actionDocumentStatus);

        logger.info("Api response status code: {}", Integer.valueOf(actualDocumentResponse.getStatusCode()));
        logger.info("Api response: {}", actualDocumentResponse.asString());

        logger.info("Returning E-Filing url from generateUrl response");

        JsonPath actualJson = new JsonPath(actualGenerateUrlResponse.asString());


        return actualJson.getString("efilingUrl");

    }
}
