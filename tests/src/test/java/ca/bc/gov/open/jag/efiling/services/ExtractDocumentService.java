package ca.bc.gov.open.jag.efiling.services;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.MultiPartSpecification;
import io.restassured.specification.RequestSpecification;
import org.springframework.beans.factory.annotation.Value;

import java.text.MessageFormat;
import java.util.UUID;

public class ExtractDocumentService {

    @Value("${EFILING_REVIEWER_HOST}")
    private String eFilingReviewerHost;

    private static final String X_TRANSACTION_ID = "X-Transaction-Id";
    private static final String X_DOCUMENT_TYPE = "X-Document-Type";

    public Response extractDocumentsResponse(UUID transactionId, String documentType, MultiPartSpecification fileSpec) {

        RequestSpecification request = RestAssured
                .given()
                .relaxedHTTPSValidation("TLS")
                .contentType("multipart/form-data")
                .header(X_TRANSACTION_ID, transactionId)
                .header(X_DOCUMENT_TYPE, documentType)
                .multiPart(fileSpec);

        return request
                .when()
                .post(MessageFormat.format("{0}/documents/extract", eFilingReviewerHost))
                .then()
                .extract()
                .response();

    }
}
