package ca.bc.gov.open.jagefilingapi.qa.backendutils;

import ca.bc.gov.open.jagefilingapi.qa.config.ReadConfig;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;


import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import static io.restassured.RestAssured.baseURI;

public class TestUtil {

    ReadConfig readConfig;
    RequestSpecification requestSpec;
    ResponseSpecification responseSpec;

    public RequestSpecification requestSpecification() throws IOException {
        readConfig = new ReadConfig();

        PrintStream log = new PrintStream(new FileOutputStream("logs/backendLogging.txt"));
        baseURI= readConfig.getBaseUri();
        requestSpec = new RequestSpecBuilder().setBaseUri(baseURI).addFilter(RequestLoggingFilter.logRequestTo(log)).addFilter(ResponseLoggingFilter.logResponseTo(log)).setContentType(ContentType.JSON).build();
        return requestSpec;
    }

    public ResponseSpecification responseSpecification() {
        responseSpec =  new ResponseSpecBuilder().expectStatusCode(200).expectContentType(ContentType.JSON).build();
        return responseSpec;
    }

    public String getJsonPath(Response response, String key) {
        String resp = response.asString();
        JsonPath jsonPath = new JsonPath(resp);
        return jsonPath.get(key);
    }
}


