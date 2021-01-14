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

    private static String CONFIG_PATH = "config";

    private Logger logger = LoggerFactory.getLogger(SubmissionService.class);

    public Response documentUploadResponse(UUID transactionId, String universalId, String accessToken, MultiPartSpecification fileSpec) {

        logger.info("Submitting document upload request to the host {}", eFilingHost);

        Response documentResponse = SubmissionHelper.uploadADocumentRequest(transactionId, accessToken, universalId,
                fileSpec);

        logger.info("documentResponse returned with http status: {}", documentResponse.getStatusCode());

        return documentResponse;

    }

    public Response generateUrlResponse(UUID transactionId, String universalId, String accessToken,
                                        String documentName, String submissionId) {

        logger.info("Requesting to generate Url");

        Response urlResponse = SubmissionHelper.generateUrlRequest(transactionId, universalId, accessToken,
                                                    submissionId);

        logger.info("urlResponse returned with http status: {}", urlResponse.getStatusCode());

        return urlResponse;

    }

    public Response getSubmissionDetailsResponse(String accessToken, UUID transactionId, String submissionId) {

        logger.info("Requesting submission information");

        Response submissionResponse = SubmissionHelper.getSubmissionDetailsRequest(accessToken, transactionId,
                                                                                            submissionId, CONFIG_PATH);

        logger.info("submissionResponse returned with http status: {}", submissionResponse.getStatusCode());

        return submissionResponse;

    }


    public String getSubmissionId(Response documentResponse) {

        JsonPath submissionIdJsonPath = new JsonPath(documentResponse.asString());

        if(submissionIdJsonPath.get("submissionId") == null) throw new EfilingTestException("submissionId not present in response");

        return  submissionIdJsonPath.get("submissionId");

    }

}
