package ca.bc.gov.open.jag.submission;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

@QuarkusTest
public class ReviewResourceTest {

    @Test
    public void testEndpoint() {
        given()
                .when().get("/graphql/schema.graphql")
                .then()
                .statusCode(200);
    }

}