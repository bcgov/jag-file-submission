package ca.bc.gov.open.jag.efiling.services;

import ca.bc.gov.open.jag.efiling.error.EfilingTestException;
import ca.bc.gov.open.jag.efiling.helpers.PayloadHelper;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.MultiPartSpecification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import java.text.MessageFormat;
import java.util.UUID;

public class SubmissionService {

    @Value("${EFILING_HOST:http://localhost:8080}")
    private String eFilingHost;

    private static final String X_TRANSACTION_ID = "X-Transaction-Id";
    private static final String X_USER_ID = "X-User-Id";

    private Logger logger = LoggerFactory.getLogger(SubmissionService.class);

    public Response documentUploadResponse(String accessToken, UUID transactionId, String universalId, MultiPartSpecification fileSpec) {

        logger.info("Submitting document upload request to the host {}", eFilingHost);

        RequestSpecification request = RestAssured.given().auth().preemptive()
                .oauth2(accessToken)
                .header(X_TRANSACTION_ID, transactionId)
                .header(X_USER_ID, universalId)
                .multiPart(fileSpec);

        return request.when().post(MessageFormat.format("{0}/submission/documents", eFilingHost))
                .then()
                .extract().response();

    }

    public Response generateUrlResponse(UUID transactionId, String universalId, String accessToken,
                                         String submissionId) {

        logger.info("Requesting to generate Url");

        RequestSpecification request = RestAssured
                .given()
                .auth()
                .preemptive()
                .oauth2(accessToken)
                .contentType(ContentType.JSON)
                .header(X_TRANSACTION_ID, transactionId)
                .header(X_USER_ID, universalId)
                .body(PayloadHelper.generateUrlPayload("test-document.pdf"));

        return request
                .when()
                .post(MessageFormat.format("{0}/submission/{1}/generateUrl", eFilingHost,  submissionId))
                .then()
                .extract()
                .response();

    }

    public Response getSubmissionDetailsResponse(String accessToken, UUID transactionId, String submissionId, String path) {

        logger.info("Requesting submission information");

        RequestSpecification request = RestAssured
                .given()
                .auth()
                .preemptive()
                .oauth2(accessToken)
                .header(X_TRANSACTION_ID, transactionId);

        return request
                .when()
                .get(MessageFormat.format("{0}/submission/{1}/{2}", eFilingHost,submissionId, path))
                .then()
                .extract()
                .response();

    }

    public Response getSubmissionDetailsResponse(String accessToken, UUID transactionId, String submissionId) {

        logger.info("Submitting get submission information");

        Response submissionResponse = SubmissionHelper.getSubmissionDetailsRequest(accessToken, transactionId,
                 eFilingHost, submissionId, CONFIG_PATH);

        logger.info("Api response status code: {}", submissionResponse.getStatusCode());

        return submissionResponse;

    }


    public String getSubmissionId(Response documentResponse) {

        JsonPath submissionIdJsonPath = new JsonPath(documentResponse.asString());

        if(submissionIdJsonPath.get("submissionId") == null) throw new EfilingTestException("submissionId not present in response");

        return  submissionIdJsonPath.get("submissionId");

    }

}
