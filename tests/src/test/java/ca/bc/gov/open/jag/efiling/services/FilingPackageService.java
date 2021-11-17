package ca.bc.gov.open.jag.efiling.services;

import ca.bc.gov.open.jag.efiling.Keys;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.springframework.beans.factory.annotation.Value;

import java.text.MessageFormat;

public class FilingPackageService {

    @Value("${EFILING_HOST:http://localhost:8080}")
    private String eFilingHost;

    public Response getFilingPackages(String accessToken, String parentApp) {

        RequestSpecification request = RestAssured
                .given()
                .auth()
                .preemptive()
                .oauth2(accessToken);

        return request.queryParam("parentApplication", parentApp)
                .when()
                .get(MessageFormat.format("{0}/{1}", eFilingHost, Keys.FILING_PACKAGES_PATH))
                .then()
                .extract()
                .response();
    }

    public Response getByPackageId(String accessToken, Integer packageId) {

        RequestSpecification request = RestAssured
                .given()
                .auth()
                .preemptive()
                .oauth2(accessToken);

        return request.when()
                .get(MessageFormat.format("{0}/filingpackages/{1}", eFilingHost, packageId))
                .then()
                .extract()
                .response();
    }

    public Response getSubmissionSheet(String accessToken, Integer packageId) {

        RequestSpecification request = RestAssured
                .given()
                .auth()
                .preemptive()
                .oauth2(accessToken);

        return request.when()
                .get(MessageFormat.format("{0}/filingpackages/{1}/submissionSheet", eFilingHost, packageId))
                .then()
                .extract()
                .response();
    }

    public Response getDocumentById(String accessToken, Integer packageId, Integer documentId) {

        RequestSpecification request = RestAssured
                .given()
                .auth()
                .preemptive()
                .oauth2(accessToken);

        return request.when()
                .get(MessageFormat.format("{0}/filingpackages/{1}/document/{2}", eFilingHost, packageId, documentId))
                .then()
                .extract()
                .response();
    }

    public Response getRushDocument(String accessToken, Integer packageId, String documentId) {

        RequestSpecification request = RestAssured
                .given()
                .auth()
                .preemptive()
                .oauth2(accessToken);

        return request.when()
                .get(MessageFormat.format("{0}/filingpackages/{1}/rushDocument/{2}", eFilingHost, packageId, documentId))
                .then()
                .extract()
                .response();
    }
}
