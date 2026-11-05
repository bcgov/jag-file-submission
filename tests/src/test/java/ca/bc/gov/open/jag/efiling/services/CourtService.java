package ca.bc.gov.open.jag.efiling.services;

import ca.bc.gov.open.jag.efiling.Keys;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.springframework.beans.factory.annotation.Value;

import java.text.MessageFormat;

public class CourtService {

    @Value("${EFILING_HOST:http://localhost:8080}")
    private String eFilingHost;

    public Response getCourtsResponse(String accessToken, String courtLevel) {

        RequestSpecification request = RestAssured
                .given()
                .auth()
                .preemptive()
                .oauth2(accessToken);

        return request.queryParam(courtLevel).when()
                .get(MessageFormat.format("{0}/{1}", eFilingHost, Keys.COURTS_PATH))
                .then()
                .extract()
                .response();
    }

}
