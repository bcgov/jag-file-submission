package ca.bc.gov.open.jag.efiling.services;

import ca.bc.gov.open.jag.efiling.error.EfilingTestException;
import ca.bc.gov.open.jag.efiling.helpers.SubmissionHelper;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.MultiPartSpecification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import java.util.UUID;

public class SubmissionService {

    @Value("${EFILING_HOST:http://localhost:8080}")
    private String eFilingHost;

    private Logger logger = LoggerFactory.getLogger(SubmissionService.class);

    public Response documentUploadResponse(UUID transactionId, String universalId, String accessToken, MultiPartSpecification fileSpec) {

        logger.info("Submitting document upload request to the host {}", eFilingHost);

        Response documentResponse = SubmissionHelper.uploadADocumentRequest(transactionId, accessToken, universalId,
                fileSpec, eFilingHost);

        logger.info("Api response status code: {}", documentResponse.getStatusCode());

        return documentResponse;

    }

    public Response generateUrlResponse(UUID transactionId, String universalId, String accessToken,
                                        String documentName, String submissionId) {

        logger.info("Submitting generate url request");

        Response urlResponse = SubmissionHelper.generateUrlRequest(transactionId, universalId, accessToken,
                                                     documentName, eFilingHost, submissionId);

        logger.info("Api response status code: {}", urlResponse.getStatusCode());

        return urlResponse;

    }


    public String getSubmissionId(Response documentResponse) {

        JsonPath submissionIdJsonPath = new JsonPath(documentResponse.asString());

        if(submissionIdJsonPath.get("submissionId") == null) throw new EfilingTestException("submissionId not present in response");

        return  submissionIdJsonPath.get("submissionId");

    }

}
