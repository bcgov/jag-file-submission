package ca.bc.gov.open.jagefilingapi.qa.restgenerateurl;

import ca.bc.gov.open.jagefilingapi.qa.generateurlrestfile.*;
import ca.bc.gov.open.jagefilingapi.qa.generateurlrestfile.Error;
import ca.bc.gov.open.jagefilingapi.qa.util.ReadConfig;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

public class GenerateUrl {

    ReadConfig readConfig;

   public void generateEfilingUrl() throws IOException {
       readConfig = new ReadConfig();

       baseURI= readConfig.getBaseUri();
       String resource = readConfig.getResource();

        given().log().all().header("Content-Type", "application/json")
               .body(GenerateUrlPayload.generateUrlFinalPayload()).when().post(resource)
               .then().log().all().assertThat().statusCode(200);
    }
}
