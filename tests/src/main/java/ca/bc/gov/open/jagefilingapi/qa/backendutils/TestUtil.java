package ca.bc.gov.open.jagefilingapi.qa.backendutils;

import ca.bc.gov.open.jagefilingapi.qa.config.ReadConfig;
import ca.bc.gov.open.jagefilingapi.qa.requestbuilders.GenerateUrlRequestBuilders;
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

    private TestUtil() {
        throw new IllegalStateException("Utility class");
    }

    public static RequestSpecification requestSpecification() throws IOException {
        ReadConfig readConfig = new ReadConfig();

        PrintStream log = new PrintStream(new FileOutputStream("logs/backendLogging.txt"));
        baseURI= readConfig.getBaseUri();
        return new RequestSpecBuilder().setBaseUri(baseURI).addFilter(RequestLoggingFilter
                .logRequestTo(log)).addFilter(ResponseLoggingFilter.logResponseTo(log))
                .setContentType(ContentType.JSON).build();
    }

    public static RequestSpecification submitDocumentsRequestSpecification() throws IOException {
        ReadConfig readConfig = new ReadConfig();

        PrintStream log = new PrintStream(new FileOutputStream("logs/backendLogging.txt"));
        baseURI= readConfig.getBaseUri();
        return new RequestSpecBuilder().setBaseUri(baseURI).addFilter(RequestLoggingFilter
                .logRequestTo(log)).addFilter(ResponseLoggingFilter.logResponseTo(log))
                .build();
    }

    public static ResponseSpecification validDocumentResponseSpecification() {
        return new ResponseSpecBuilder().expectStatusCode(200).expectContentType(ContentType.JSON).build();
    }

    public static ResponseSpecification responseSpecification() {
        return new ResponseSpecBuilder().expectStatusCode(200).expectContentType(ContentType.JSON).build();
    }

    public static ResponseSpecification errorResponseSpecification() {
        return new ResponseSpecBuilder().expectStatusCode(403).expectContentType(ContentType.JSON).build();
    }

    public static ResponseSpecification withoutIdResponseSpecification() {
        return new ResponseSpecBuilder().expectStatusCode(405).expectContentType(ContentType.JSON).build();
    }

    public static ResponseSpecification createCsoAccountResponseSpecification() {
        return new ResponseSpecBuilder().expectStatusCode(201).expectContentType(ContentType.JSON).build();
    }

    public static ResponseSpecification createCsoAccountIncorrectTypeErrorResponseSpecification() {
        return new ResponseSpecBuilder().expectStatusCode(400).expectContentType(ContentType.JSON).build();
    }

    public static ResponseSpecification createCsoAccountIncorrectPathErrorResponseSpecification() {
        return new ResponseSpecBuilder().expectStatusCode(404).expectContentType(ContentType.JSON).build();
    }

    public static ResponseSpecification documentValidResponseSpecification() {
        return new ResponseSpecBuilder().expectStatusCode(200).expectContentType(ContentType.fromContentType("application/octet-stream")).build();
    }

    public static String getJsonPath(Response response, String key) {
        String resp = response.asString();
        JsonPath jsonPath = new JsonPath(resp);
        return jsonPath.get(key);
    }
}
