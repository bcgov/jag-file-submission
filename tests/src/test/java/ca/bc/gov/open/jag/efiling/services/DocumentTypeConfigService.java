package ca.bc.gov.open.jag.efiling.services;

import ca.bc.gov.open.jag.efiling.Keys;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.MessageFormat;
import java.util.UUID;

public class DocumentTypeConfigService {

    @Value("${EFILING_REVIEWER_HOST:http://localhost:8090}")
    private String eFilingReviewerHost;

    private final String COMMON_MESSAGE_FORMAT = "{0}/{1}";

    public Response createDocumentTypeConfigResponse(String payloadPath, String pathParam) throws IOException {

        File resource = new ClassPathResource(
                MessageFormat.format("payload/{0}", payloadPath)).getFile();

        RequestSpecification request = RestAssured
                .given()
                .contentType(ContentType.JSON)
                .body(resource);
        return request
                .when()
                .post(MessageFormat.format(COMMON_MESSAGE_FORMAT, eFilingReviewerHost, pathParam))
                .then()
                .extract()
                .response();

    }

    public Response updateDocumentTypeConfigResponse(String id, String payloadPath, String pathParam) throws IOException, JSONException {

        File resource = new ClassPathResource(
                MessageFormat.format("payload/{0}", payloadPath)).getFile();

        String json = new String(Files.readAllBytes(Paths.get(String.valueOf(resource))));

        JSONObject jsonObject = new JSONObject(json);
        jsonObject.put("id", id);

        String updatedJsonWithId = jsonObject.toString();

        RequestSpecification request = RestAssured
                .given()
                .contentType(ContentType.JSON)
                .body(updatedJsonWithId);
        return request
                .when()
                .put(MessageFormat.format(COMMON_MESSAGE_FORMAT, eFilingReviewerHost, pathParam))
                .then()
                .extract()
                .response();

    }

    public Response getCreatedDocumentTypeConfiguration() {

        return RestAssured.when()
                .get(MessageFormat.format(COMMON_MESSAGE_FORMAT, eFilingReviewerHost, Keys.DOCUMENT_TYPE_CONFIGURATION_PATH))
                .then()
                .extract()
                .response();
    }

    public Response getRestrictedDocumentTypeByIdResponse(UUID id) {

        return RestAssured.when()
                .get(MessageFormat.format("{0}/{1}/{2}", eFilingReviewerHost,
                        Keys.RESTRICTED_DOCUMENT_TYPE_CONFIGURATION_PATH, id))
                .then()
                .extract()
                .response();
    }

    public Response getAllRestrictedDocumentTypeResponse() {

        return RestAssured.when()
                .get(MessageFormat.format(COMMON_MESSAGE_FORMAT, eFilingReviewerHost,
                        Keys.RESTRICTED_DOCUMENT_TYPE_CONFIGURATION_PATH))
                .then()
                .extract()
                .response();
    }

    public Response deleteRestrictedDocumentTypeByIdResponse(UUID id) {

        return RestAssured.when()
                .delete(MessageFormat.format("{0}/{1}/{2}", eFilingReviewerHost,
                        Keys.RESTRICTED_DOCUMENT_TYPE_CONFIGURATION_PATH, id))
                .then()
                .extract()
                .response();
    }

}
