package ca.bc.gov.open.jagefilingapi.qa.backendutils;

import ca.bc.gov.open.jagefilingapi.qa.config.ReadConfig;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

import java.io.IOException;

import static io.restassured.RestAssured.baseURI;

public class TestUtil {

    ReadConfig readConfig;
    RequestSpecification requestSpec;
    ResponseSpecification responseSpec;

    public RequestSpecification requestSpecification() throws IOException {
        readConfig = new ReadConfig();

        baseURI= readConfig.getBaseUri();
        requestSpec = new RequestSpecBuilder().setBaseUri(baseURI).setContentType(ContentType.JSON).build();
        return requestSpec;
    }

    public ResponseSpecification responseSpecification() throws IOException {
        readConfig = new ReadConfig();

        baseURI= readConfig.getBaseUri();
        responseSpec =  new ResponseSpecBuilder().expectStatusCode(200).expectContentType(ContentType.JSON).build();
        return responseSpec;
    }
}
