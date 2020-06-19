package ca.bc.gov.open.jagefilingapi.qa.backend.generateurl;

import ca.bc.gov.open.jagefilingapi.qa.backend.generateurlpayload.GenerateUrlPayload;
import ca.bc.gov.open.jagefilingapi.qa.backendutils.TestUtil;
import ca.bc.gov.open.jagefilingapi.qa.config.ReadConfig;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import java.io.IOException;
import static io.restassured.RestAssured.*;

public class GenerateUrl extends TestUtil {

    ReadConfig readConfig;
    GenerateUrlPayload payloadData;
    RequestSpecification postRequest;
    Response response;
    private String validateResp;

    public Response postGenerateUrlPayload() throws IOException {
        readConfig = new ReadConfig();
        payloadData = new GenerateUrlPayload();

        String resource = readConfig.getResource();
        postRequest = given().spec(requestSpecification()).body(payloadData.generateUrlFinalPayload());
        response = postRequest.when().post(resource).then().spec(responseSpecification()).extract().response();
        return response;
    }

   public String verifyFirstEfilingUrlIsGenerated() throws IOException {

        validateResp = postGenerateUrlPayload().asString();
        JsonPath jsPath = new JsonPath(validateResp);
        return jsPath.get("efilingUrl");
    }

    public String verifySecondEfilingUrlIsGenerated() throws IOException {

        validateResp = postGenerateUrlPayload().asString();
        JsonPath jsPath = new JsonPath(validateResp);
        return jsPath.get("eFilingUrl");
    }

    public long verifyExpDateIsGenerated() throws IOException {
        readConfig = new ReadConfig();
        payloadData = new GenerateUrlPayload();

        validateResp = postGenerateUrlPayload().asString();
        JsonPath jsPath = new JsonPath(validateResp);
        return jsPath.getLong("expiryDate");
    }
}
