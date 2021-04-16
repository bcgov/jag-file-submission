package ca.bc.gov.open.jag.efiling.services;

import ca.bc.gov.open.jag.efiling.Keys;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.response.Response;
import io.restassured.specification.MultiPartSpecification;
import io.restassured.specification.RequestSpecification;
import org.springframework.beans.factory.annotation.Value;

import java.text.MessageFormat;
import java.util.UUID;

public class ExtractDocumentService {

    @Value("${EFILING_REVIEWER_HOST:http://localhost:8090}")
    private String eFilingReviewerHost;

    public Response extractDocumentsResponse(UUID transactionId, String documentType, MultiPartSpecification fileSpec) {

        RequestSpecification request = RestAssured
                .given()
                .relaxedHTTPSValidation("TLS")
                .contentType("multipart/form-data")
                .header(Keys.X_TRANSACTION_ID, transactionId)
                .header(Keys.X_DOCUMENT_TYPE, documentType)
                .multiPart(fileSpec);

        return request
                .when()
                .post(MessageFormat.format("{0}/{1}", eFilingReviewerHost, Keys.EXTRACT_DOCUMENTS_PATH))
                .then()
                .extract()
                .response();

    }

    public Response getProcessedDocumentDataById(UUID transactionId, Integer documentId) {

        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());

        RequestSpecification request = RestAssured
                .given()
                .header(Keys.X_TRANSACTION_ID, transactionId);

        return request
                .when()
                .get(MessageFormat.format("{0}/{1}/{2}", eFilingReviewerHost, Keys.DOCUMENTS_PROCESSED_PATH, String.valueOf(documentId)))
                .then()
                .extract()
                .response();

    }
}
