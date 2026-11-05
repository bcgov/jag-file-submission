package ca.bc.gov.open.jag.efiling.services;

import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.core.io.ClassPathResource;

import ca.bc.gov.open.jag.efiling.helpers.SubmissionHelper;
import ca.bc.gov.open.jag.efiling.models.FileSpec;
import ca.bc.gov.open.jag.efiling.models.UserIdentity;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.MultiPartSpecification;

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

    public String getGeneratedUrl(FileSpec... filespecs) throws IOException {

        UserIdentity actualUserIdentity = oauthService.getUserIdentity();

        logger.info("Submitting document upload request");

        List<MultiPartSpecification> multiPartSpecifications = new ArrayList<MultiPartSpecification>();
        for (FileSpec fileSpec : filespecs) {
            File resource = new ClassPathResource(MessageFormat.format("data/{0}", fileSpec.getFilename())).getFile();
            multiPartSpecifications.add(SubmissionHelper.fileSpecBuilder(resource, fileSpec.getFilename(), "text/application.pdf"));
		}

        Response actualDocumentResponse = submissionService.documentUploadResponse(actualUserIdentity.getAccessToken(), actualTransactionId,
                actualUserIdentity.getUniversalId(), multiPartSpecifications.toArray(new MultiPartSpecification[0]));

        logger.info("Api response status code: {}", Integer.valueOf(actualDocumentResponse.getStatusCode()));
        logger.info("Api response: {}", actualDocumentResponse.asString());


        String actualSubmissionId = submissionService.getSubmissionId(actualDocumentResponse);

        Response actualGenerateUrlResponse = submissionService.generateUrlResponse(actualTransactionId, actualUserIdentity.getUniversalId(),
                actualUserIdentity.getAccessToken(), actualSubmissionId, filespecs);

        logger.info("Api response status code: {}", Integer.valueOf(actualDocumentResponse.getStatusCode()));
        logger.info("Api response: {}", actualDocumentResponse.asString());

        logger.info("Returning E-Filing url from generateUrl response");

        JsonPath actualJson = new JsonPath(actualGenerateUrlResponse.asString());


        return actualJson.getString("efilingUrl");

    }
}
