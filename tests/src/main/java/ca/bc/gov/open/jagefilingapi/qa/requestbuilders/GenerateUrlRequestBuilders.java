package ca.bc.gov.open.jagefilingapi.qa.requestbuilders;

import ca.bc.gov.open.jagefilingapi.qa.backend.generateurlpayload.GenerateUrlPayload;
import ca.bc.gov.open.jagefilingapi.qa.backendutils.APIResources;
import ca.bc.gov.open.jagefilingapi.qa.backendutils.TestUtil;
import ca.bc.gov.open.jagefilingapi.qa.config.ReadConfig;
import ca.bc.gov.open.jagefilingapi.qa.frontendutils.JsonDataReader;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.io.File;
import java.io.IOException;

import static io.restassured.RestAssured.given;

public class GenerateUrlRequestBuilders {

    private  RequestSpecification request;
    private static final String X_TRANSACTION_ID = "X-Transaction-Id";
    private static final String X_USER_ID = "X-User-Id";
    private static final String GENERATE_URL_PATH = "/generateUrl";
    private static final String UPLOAD_FILE_PATH = "src/test/java/testdatasource";
    private static final String FILES = "files";
    private static final String FILE_NAME_PATH = "/test-document.pdf";
    private static final String CLIENT_ID = "client_id";
    private static final String GRANT_TYPE = "grant_type";
    private static final String CLIENT_SECRET = "client_secret";

    private  GenerateUrlPayload payloadData;

    public Response getBearerToken() throws IOException {
        ReadConfig readConfig = new ReadConfig();
        String resourceAPI = readConfig.getKeycloakUrl();
        String validUserid = JsonDataReader.getCsoAccountGuid().getClientSecret();

        request = RestAssured.given().spec(TestUtil.submitDocumentsRequestSpecification())
                .formParam(CLIENT_ID,"efiling-demo")
                .formParam(GRANT_TYPE,"client_credentials" )
                .formParam(CLIENT_SECRET, validUserid);

        return request.when().post(resourceAPI).then()
                .spec(TestUtil.validDocumentResponseSpecification())
                .extract().response();
    }

    public Response validRequestWithSingleDocument(String resourceValue) throws IOException {
        payloadData = new GenerateUrlPayload();

        APIResources resourceAPI = APIResources.valueOf(resourceValue);
        String validExistingCSOGuid = JsonDataReader.getCsoAccountGuid().getValidExistingCSOGuid();
        String validUserid = JsonDataReader.getCsoAccountGuid().getValidUserid();

        Response response = getBearerToken();
        JsonPath jsonPath = new JsonPath(response.asString());

        String accessToken = jsonPath.get("access_token");

        File pdfFile = new File(UPLOAD_FILE_PATH + FILE_NAME_PATH);

        request = RestAssured.given().auth().preemptive().oauth2(accessToken).spec(TestUtil.submitDocumentsRequestSpecification())
                .header(X_TRANSACTION_ID,validExistingCSOGuid)
                .header(X_USER_ID,validUserid )
                .multiPart(FILES, pdfFile);

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
                .header(X_TRANSACTION_ID,validExistingCSOGuid)
                .multiPart(FILES,  pngFile)
                .multiPart(FILES,  textFile)
                .multiPart(FILES,  pdfFile)
                .multiPart(FILES,  jpgFile);

        return request.when().post(resourceAPI.getResource()).then()
                .spec(TestUtil.validDocumentResponseSpecification())
                .extract().response();
    }

    public Response requestWithInvalidKeyValue(String resourceValue) throws IOException {
        payloadData = new GenerateUrlPayload();

        APIResources resourceAPI = APIResources.valueOf(resourceValue);
        String validExistingCSOGuid = JsonDataReader.getCsoAccountGuid().getValidExistingCSOGuid();

        File pngFile = new File(UPLOAD_FILE_PATH + FILE_NAME_PATH);

        request = RestAssured.given().spec(TestUtil.submitDocumentsRequestSpecification())
                .header(X_TRANSACTION_ID,validExistingCSOGuid)
                .multiPart("file",  pngFile);

        return request.when().post(resourceAPI.getResource()).then()
                .spec(TestUtil.createCsoAccountIncorrectTypeErrorResponseSpecification())
                .extract().response();
    }

    public Response requestWithNonExistingCSOAccountGuid(String resourceValue) throws IOException {
        payloadData = new GenerateUrlPayload();

        APIResources resourceAPI = APIResources.valueOf(resourceValue);
        String nonExistingCSOGuid = JsonDataReader.getCsoAccountGuid().getNonExistingCSOGuid();

        File pngFile = new File(UPLOAD_FILE_PATH + FILE_NAME_PATH);

        request = RestAssured.given().spec(TestUtil.submitDocumentsRequestSpecification())
                .header(X_TRANSACTION_ID,nonExistingCSOGuid)
                .multiPart(FILES,  pngFile);

        return request.when().post(resourceAPI.getResource()).then()
                .spec(TestUtil.validDocumentResponseSpecification())
                .extract().response();
    }

    public Response requestWithInvalidCSOAccountGuid(String resourceValue) throws IOException {
        payloadData = new GenerateUrlPayload();

        APIResources resourceAPI = APIResources.valueOf(resourceValue);
        String invalidNoFilingRoleGuid = JsonDataReader.getCsoAccountGuid().getInvalidNoFilingRoleGuid();

        request = given().spec(TestUtil.requestSpecification()).header(X_TRANSACTION_ID,invalidNoFilingRoleGuid)
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
                .header(X_TRANSACTION_ID, validExistingCSOGuid)
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
                .header(X_TRANSACTION_ID,validExistingCSOGuid)
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
                .header(X_TRANSACTION_ID,validExistingCSOGuid)
                .body(payloadData.validGenerateUrlPayload());

        return request.when().post(resourceValid.getResource() + "generateUrl")
                .then()
                .extract().response();
    }
}
