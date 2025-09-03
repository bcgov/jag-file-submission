package ca.bc.gov.open.jag.efiling.services;

import java.text.MessageFormat;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;

import ca.bc.gov.open.jag.efiling.Keys;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class CountryService {

    @Value("${EFILING_HOST:http://localhost:8080}")
    private String eFilingHost;

    public Response getCountriesResponse(String accessToken, UUID transactionId) {

        RequestSpecification request = RestAssured
                .given()
                .auth()
                .preemptive()
                .oauth2(accessToken)
                .contentType(ContentType.JSON)
                .header(Keys.X_TRANSACTION_ID, transactionId);


        return request.when()
                .get(MessageFormat.format("{0}/{1}", eFilingHost, Keys.COUNTRIES_PATH))
                .then()
                .extract()
                .response();
    }
}
