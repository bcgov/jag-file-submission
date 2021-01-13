package ca.bc.gov.open.jag.efiling.services;

import ca.bc.gov.open.jag.efiling.models.UserIdentity;
import io.restassured.RestAssured;
import io.restassured.builder.MultiPartSpecBuilder;
import io.restassured.response.Response;
import io.restassured.specification.MultiPartSpecification;
import io.restassured.specification.RequestSpecification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class SubmissionService {

    private static final String TEST_DOCUMENT_PDF = "test-document.pdf";

    private static final String X_USER_ID = "X-User-Id";
    private static final String X_TRANSACTION_ID = "X-Transaction-Id";

    private UUID actualTransactionId;
    private io.restassured.response.Response actualDocumentResponse;
    private io.restassured.response.Response actualGenerateUrlResponse;
    private UUID actualSubmissionId;
    private UserIdentity actualUserIdentity;
    private Logger logger = LoggerFactory.getLogger(SubmissionService.class);

    public Response getSubmissionId(String accessToken, String actualTransactionId) throws IOException {

        logger.info("Submitting document upload request");

        File resource = new ClassPathResource(
                "data/test-document.pdf").getFile();

        MultiPartSpecification spec = new MultiPartSpecBuilder(resource).
                fileName(TEST_DOCUMENT_PDF).
                controlName("files").
                mimeType("text/application-pdf").
                build();

        RequestSpecification request = RestAssured.given().auth().preemptive().oauth2(actualUserIdentity.getAccessToken())
                .header(X_TRANSACTION_ID, actualTransactionId)
                .header(X_USER_ID, actualUserIdentity.getUniversalId())
                .multiPart(spec);

        return request.when().post("http://localhost:8080/submission/documents").then()
                .extract().response();

    }



    // TEst data outside
    // return a Response
    // Add service for the post up date
}
