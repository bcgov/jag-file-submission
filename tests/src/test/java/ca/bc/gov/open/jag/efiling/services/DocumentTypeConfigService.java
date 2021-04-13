package ca.bc.gov.open.jag.efiling.services;

import ca.bc.gov.open.jag.efiling.Keys;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.IOException;
import java.text.MessageFormat;

public class DocumentTypeConfigService {

    @Value("${EFILING_REVIEWER_HOST:http://localhost:8090}")
    private String eFilingReviewerHost;

    public Response createDocumentTypeConfigResponse() throws IOException {

        File resource = new ClassPathResource(
                MessageFormat.format("payload/{0}", "document-type-config-payload.json")).getFile();

        RequestSpecification request = RestAssured
                .given()
                .contentType(ContentType.JSON)
                .body(resource);
        return request
                .when()
                .post(MessageFormat.format("{0}/{1}", eFilingReviewerHost, Keys.DOCUMENT_TYPE_CONFIGURATION_PATH))
                .then()
                .extract()
                .response();

    }

    public Response getCreatedDocumentTypeConfiguration() {

        RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());

        return RestAssured.when()
                .get(MessageFormat.format("{0}/{1}", eFilingReviewerHost, Keys.DOCUMENT_TYPE_CONFIGURATION_PATH))
                .then()
                .extract()
                .response();
    }
}
