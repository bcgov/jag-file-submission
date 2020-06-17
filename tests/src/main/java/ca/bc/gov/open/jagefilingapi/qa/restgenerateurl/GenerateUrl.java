package ca.bc.gov.open.jagefilingapi.qa.restgenerateurl;

import ca.bc.gov.open.jagefilingapi.qa.restdata.Payload;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;


public class GenerateUrl {

   public void generateEfilingUrl() {
        baseURI = "http://localhost:8080";

        given().log().all().header("Content-Type", "application/json")
               .body(Payload.submitDocumentProperties()).when().post("submission/generateUrl")
               .then().log().all().assertThat().statusCode(200).body("efilingUrl", containsString("https://httpbin.org"));
    }
}
