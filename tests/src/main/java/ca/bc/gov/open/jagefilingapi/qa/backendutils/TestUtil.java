package ca.bc.gov.open.jagefilingapi.qa.backendutils;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.config.HttpClientConfig;
import io.restassured.config.RestAssuredConfig;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

public class TestUtil {

    public static RequestSpecification requestSpecification() throws IOException {

        PrintStream log = new PrintStream(new FileOutputStream("logs/backendLogging.txt"));
        String baseURI = System.getProperty("BASE_URI");
        return new RequestSpecBuilder().setBaseUri(baseURI).addFilter(RequestLoggingFilter
                .logRequestTo(log)).addFilter(ResponseLoggingFilter.logResponseTo(log))
                .setContentType(ContentType.JSON).build();
    }

    public static RequestSpecification submitDocumentsRequestSpecification() throws IOException {

        PrintStream log = new PrintStream(new FileOutputStream("logs/backendLogging.txt"));
        String baseURI = System.getProperty("BASE_URI");
        return new RequestSpecBuilder().setBaseUri(baseURI).addFilter(RequestLoggingFilter
                .logRequestTo(log)).addFilter(ResponseLoggingFilter.logResponseTo(log))
                .build();
    }

    public static ResponseSpecification validResponseCodeSpecification() {
        return new ResponseSpecBuilder().expectStatusCode(200).build();
    }

    public static ResponseSpecification validResponseSpecification() {
        return new ResponseSpecBuilder().expectStatusCode(200).expectContentType(ContentType.JSON).build();
    }

    public static ResponseSpecification errorResponseSpecification() {
        return new ResponseSpecBuilder().expectStatusCode(415).expectContentType(ContentType.JSON).build();
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

    public static String getJsonPath(Response response, String key) {
        String resp = response.asString();
        JsonPath jsonPath = new JsonPath(resp);
        return jsonPath.get(key);
    }

    public static List<String> getSubmissionAndTransId(String respUrl, String submissionId, String transactionId) throws URISyntaxException {
        List<NameValuePair> params = URLEncodedUtils.parse(new URI(respUrl), StandardCharsets.UTF_8);

        String respSubId = null;
        String respTransId = null;
        
        for (NameValuePair param : params) {
            if (param.getName().equals(submissionId)) {
                respSubId = param.getValue();
            } else if (param.getName().equals(transactionId)) {
                respTransId = param.getValue();
            }
        }
        return Arrays.asList(respSubId, respTransId);
    }

    public void restAssuredConfig() {
        RestAssured.config= RestAssuredConfig.config().httpClient(HttpClientConfig.httpClientConfig().
                setParam("http.connection.timeout",500000).
                setParam("http.socket.timeout",500000).
                setParam("http.connection-manager.timeout",500000));
    }
}
