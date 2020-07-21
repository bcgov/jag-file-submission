package ca.bc.gov.open.jagefilingapi.qa.requestbuilders;

import ca.bc.gov.open.jagefilingapi.qa.backend.generateurlpayload.GenerateUrlPayload;
import ca.bc.gov.open.jagefilingapi.qa.backendutils.APIResources;
import ca.bc.gov.open.jagefilingapi.qa.backendutils.TestUtil;
import ca.bc.gov.open.jagefilingapi.qa.frontendutils.JsonDataReader;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.io.File;
import java.io.IOException;

import static io.restassured.RestAssured.given;

public class GenerateUrlRequestBuilders {

    private  RequestSpecification request;
    private static final String X_AUTH_USER_ID = "X-Auth-UserId";
    private static final String GENERATE_URL_PATH = "/generateUrl";
    private static final String UPLOAD_FILE_PATH = "src/test/java/testdatasource";

    private  GenerateUrlPayload payloadData;

    public Response validRequestWithSingleDocument(String resourceValue) throws IOException {
        payloadData = new GenerateUrlPayload();

        APIResources resourceAPI = APIResources.valueOf(resourceValue);
        String validExistingCSOGuid = JsonDataReader.getCsoAccountGuid().getValidExistingCSOGuid();

        File pngFile = new File(UPLOAD_FILE_PATH + "/backend.png");

        request = RestAssured.given().spec(TestUtil.submitDocumentsRequestSpecification())
                .header(X_AUTH_USER_ID,validExistingCSOGuid)
                .multiPart("files",  pngFile);

        return request.when().post(resourceAPI.getResource()).then()
                .spec(TestUtil.validDocumentResponseSpecification())
                .extract().response();
    }

    public Response validRequestWithMultipleDocuments(String resourceValue) throws IOException {
        payloadData = new GenerateUrlPayload();

        APIResources resourceAPI = APIResources.valueOf(resourceValue);
        String validExistingCSOGuid = JsonDataReader.getCsoAccountGuid().getValidExistingCSOGuid();

        File pngFile = new File(UPLOAD_FILE_PATH + "/backend.png");
        File textFile = new File(UPLOAD_FILE_PATH + "/test-document.txt");
        File pdfFile = new File(UPLOAD_FILE_PATH + "/test-pdf-document.pdf");
        File jpgFile = new File(UPLOAD_FILE_PATH + "/workflow-automation.jpeg");

        request = RestAssured.given().spec(TestUtil.submitDocumentsRequestSpecification())
                .header(X_AUTH_USER_ID,validExistingCSOGuid)
                .multiPart("files",  pngFile)
                .multiPart("files",  textFile)
                .multiPart("files",  pdfFile)
                .multiPart("files",  jpgFile);

        return request.when().post(resourceAPI.getResource()).then()
                .spec(TestUtil.validDocumentResponseSpecification())
                .extract().response();
    }

    public Response requestWithInvalidKeyValue(String resourceValue) throws IOException {
        payloadData = new GenerateUrlPayload();

        APIResources resourceAPI = APIResources.valueOf(resourceValue);
        String validExistingCSOGuid = JsonDataReader.getCsoAccountGuid().getValidExistingCSOGuid();

        File pngFile = new File(UPLOAD_FILE_PATH + "/backend.png");

        request = RestAssured.given().spec(TestUtil.submitDocumentsRequestSpecification())
                .header(X_AUTH_USER_ID,validExistingCSOGuid)
                .multiPart("file",  pngFile);

        return request.when().post(resourceAPI.getResource()).then()
                .spec(TestUtil.createCsoAccountIncorrectTypeErrorResponseSpecification())
                .extract().response();
    }

    public Response requestWithNonExistingCSOAccountGuid(String resourceValue) throws IOException {
        payloadData = new GenerateUrlPayload();

        APIResources resourceAPI = APIResources.valueOf(resourceValue);
        String nonExistingCSOGuid = JsonDataReader.getCsoAccountGuid().getNonExistingCSOGuid();

        File pngFile = new File(UPLOAD_FILE_PATH + "/backend.png");

        request = RestAssured.given().spec(TestUtil.submitDocumentsRequestSpecification())
                .header(X_AUTH_USER_ID,nonExistingCSOGuid)
                .multiPart("files",  pngFile);

        return request.when().post(resourceAPI.getResource()).then()
                .spec(TestUtil.validDocumentResponseSpecification())
                .extract().response();
    }

    public Response requestWithInvalidCSOAccountGuid(String resourceValue) throws IOException {
        payloadData = new GenerateUrlPayload();

        APIResources resourceAPI = APIResources.valueOf(resourceValue);
        String invalidNoFilingRoleGuid = JsonDataReader.getCsoAccountGuid().getInvalidNoFilingRoleGuid();

        request = given().spec(TestUtil.requestSpecification()).header(X_AUTH_USER_ID,invalidNoFilingRoleGuid)
                .body(payloadData.validGenerateUrlPayload());

        return request.when().post(resourceAPI.getResource() + invalidNoFilingRoleGuid + GENERATE_URL_PATH)
                .then()
                .spec(TestUtil.errorResponseSpecification())
                .extract().response();
    }

    public Response requestWithIncorrectPath(String resourceValue) throws IOException {
        payloadData = new GenerateUrlPayload();

        APIResources resourceIncorrect = APIResources.valueOf(resourceValue);
        String validExistingCSOGuid = JsonDataReader.getCsoAccountGuid().getValidExistingCSOGuid();

        request = given().spec(TestUtil.requestSpecification())
                .header(X_AUTH_USER_ID, validExistingCSOGuid)
                .body(payloadData.validGenerateUrlPayload());

        return request.when().post(resourceIncorrect.getResource() + validExistingCSOGuid + GENERATE_URL_PATH)
                .then()
                .extract().response();
    }

    public Response requestWithInvalidPath(String resourceValue) throws IOException {
        payloadData = new GenerateUrlPayload();

        APIResources resourceInvalid = APIResources.valueOf(resourceValue);
        String validExistingCSOGuid = JsonDataReader.getCsoAccountGuid().getValidExistingCSOGuid();

        request = given().spec(TestUtil.requestSpecification())
                .header(X_AUTH_USER_ID,validExistingCSOGuid)
                .body(payloadData.validGenerateUrlPayload());

        return request.when().post(resourceInvalid.getResource()  + validExistingCSOGuid + "/generateUrs")
                .then()
                .extract().response();
    }

    public Response requestWithoutIdInThePath(String resourceValue) throws IOException {
        payloadData = new GenerateUrlPayload();

        APIResources resourceValid = APIResources.valueOf(resourceValue);
        String validExistingCSOGuid = JsonDataReader.getCsoAccountGuid().getValidExistingCSOGuid();

        request = given().spec(TestUtil.requestSpecification())
                .header(X_AUTH_USER_ID,validExistingCSOGuid)
                .body(payloadData.validGenerateUrlPayload());

        return request.when().post(resourceValid.getResource() + "generateUrl")
                .then()
                .extract().response();
    }
}
