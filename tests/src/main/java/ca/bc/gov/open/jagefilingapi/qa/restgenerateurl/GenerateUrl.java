package ca.bc.gov.open.jagefilingapi.qa.restgenerateurl;

import ca.bc.gov.open.jagefilingapi.qa.generateurlrestfile.*;
import ca.bc.gov.open.jagefilingapi.qa.generateurlrestfile.Error;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class GenerateUrl {

   public void generateEfilingUrl() throws JsonProcessingException {
        baseURI = "http://localhost:8080";

        given().log().all().header("Content-Type", "application/json")
               .body(GenerateUrlPayload.generateUrlFinalPayload()).when().post("submission/generateUrl")
               .then().log().all().assertThat().statusCode(200).body("efilingUrl", containsString("https://httpbin.org"));
    }
}
