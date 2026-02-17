package ca.bc.gov.open.jag.efiling.services;

import ca.bc.gov.open.jag.efiling.Keys;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.springframework.beans.factory.annotation.Value;

import java.text.MessageFormat;

public class DocumentService {

    @Value("${EFILING_HOST:http://localhost:8080}")
    private String eFilingHost;

    public Response getDocumentTypesResponse(String accessToken, String courtLevel, String courtClassification) {

        RequestSpecification request = RestAssured
                .given()
                .auth()
                .preemptive()
                .oauth2(accessToken);

        return request.queryParam("courtLevel", courtLevel)
                .queryParam("courtClassification", courtClassification)
                .when()
                .get(MessageFormat.format("{0}/{1}", eFilingHost, Keys.DOCUMENTS_TYPES_PATH))
                .then()
                .extract()
                .response();

    }

}
