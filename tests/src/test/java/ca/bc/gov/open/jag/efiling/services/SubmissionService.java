package ca.bc.gov.open.jag.efiling.services;

import ca.bc.gov.open.jag.efiling.Keys;
import ca.bc.gov.open.jag.efiling.error.EfilingTestException;
import ca.bc.gov.open.jag.efiling.helpers.PayloadHelper;
import ca.bc.gov.open.jag.efiling.helpers.SubmissionHelper;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.MultiPartSpecification;
import io.restassured.specification.RequestSpecification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import java.io.File;
import java.text.MessageFormat;
import java.util.UUID;

public class SubmissionService {

    @Value("${EFILING_HOST:http://localhost:8080}")
    private String eFilingHost;

    @Value("classpath:data/test-document-additional.pdf")
    File additionalPdfDocument;

    private final Logger logger = LoggerFactory.getLogger(SubmissionService.class);

    public Response documentUploadResponse(String accessToken, UUID transactionId, String universalId, MultiPartSpecification fileSpec) {

        logger.info("Submitting document upload request to the host {}", eFilingHost);

        RequestSpecification request = RestAssured.given().auth().preemptive()
                .oauth2(accessToken)
                .header(Keys.X_TRANSACTION_ID, transactionId)
                .header(Keys.X_USER_ID, universalId)
                .multiPart(fileSpec);

        return request.when().post(MessageFormat.format("{0}/{1}", eFilingHost, Keys.SUBMISSION_DOCUMENTS_PATH))
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
                .header(Keys.X_TRANSACTION_ID, transactionId)
                .header(Keys.X_USER_ID, universalId)
                .body(PayloadHelper.generateUrlPayload(Keys.TEST_DOCUMENT_PDF));

        return request
                .when()
                .post(MessageFormat.format("{0}/{1}/{2}/{3}", eFilingHost, Keys.SUBMISSION_PATH, submissionId, Keys.GENERATE_URL_PATH))
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
                .header(Keys.X_TRANSACTION_ID, transactionId);

        return request
                .when()
                .get(MessageFormat.format("{0}/{1}/{2}/{3}", eFilingHost, Keys.SUBMISSION_PATH, submissionId, path))
                .then()
                .extract()
                .response();

    }

    public Response createPaymentServiceResponse(String accessToken, UUID transactionId, String submissionId, String path) {

        logger.info("Submitting request with submit parameters to the host {}", eFilingHost);

        RequestSpecification request = RestAssured
                .given()
                .auth()
                .preemptive()
                .oauth2(accessToken)
                .contentType(ContentType.JSON)
                .header(Keys.X_TRANSACTION_ID, transactionId)
                .body(new ObjectMapper().createObjectNode());

        return request
                .when()
                .post(MessageFormat.format("{0}/{1}/{2}/{3}", eFilingHost, Keys.SUBMISSION_PATH, submissionId, path))
                .then()
                .extract()
                .response();

    }

    public Response additionalDocumentUploadResponse(String accessToken, UUID transactionId, String submissionId, String path) {

        logger.info("Submitting request to upload additional document to the host {}", eFilingHost);

        MultiPartSpecification fileSpec = SubmissionHelper.fileSpecBuilder(additionalPdfDocument, "test-document-additional.pdf", "text/application.pdf");

        RequestSpecification request = RestAssured
                .given()
                .auth()
                .preemptive()
                .oauth2(accessToken)
                .header(Keys.X_TRANSACTION_ID, transactionId)
                .multiPart(fileSpec);

        return request
                .when()
                .post(MessageFormat.format("{0}/{1}/{2}/{3}", eFilingHost, Keys.SUBMISSION_PATH, submissionId, path))
                .then()
                .extract()
                .response();

    }

    public Response updateDocumentPropertiesResponse(String accessToken, UUID transactionId, String submissionId, String path) {

        logger.info("Submitting request to update document properties to the host {}", eFilingHost);


        RequestSpecification request = RestAssured
                .given()
                .auth()
                .preemptive()
                .oauth2(accessToken)
                .contentType(ContentType.JSON)
                .header(Keys.X_TRANSACTION_ID, transactionId)
                .body(PayloadHelper.updateDocumentProperties(Keys.TEST_DOCUMENT_PDF));

        return request
                .when()
                .post(MessageFormat.format("{0}/{1}/{2}/{3}", eFilingHost, Keys.SUBMISSION_PATH, submissionId, path))
                .then()
                .extract()
                .response();

    }

    public String getSubmissionId(Response documentResponse) {

        JsonPath submissionIdJsonPath = new JsonPath(documentResponse.asString());

        if(submissionIdJsonPath.get("submissionId") == null) throw new EfilingTestException("submissionId not present in response");

        return  submissionIdJsonPath.get("submissionId");

    }

}
