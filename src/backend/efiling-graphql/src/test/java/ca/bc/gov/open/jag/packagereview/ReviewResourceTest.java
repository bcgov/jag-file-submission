package ca.bc.gov.open.jag.packagereview;

import static io.restassured.RestAssured.given;

//@QuarkusTest
public class ReviewResourceTest {

    //@Test
    public void testEndpoint() {
        given()
                .when().get("/graphql/schema.graphql")
                .then()
                .statusCode(200);
    }

}
