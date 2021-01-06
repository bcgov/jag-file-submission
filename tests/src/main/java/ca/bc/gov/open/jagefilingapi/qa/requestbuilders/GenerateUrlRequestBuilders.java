package ca.bc.gov.open.jagefilingapi.qa.requestbuilders;

import ca.bc.gov.open.jagefilingapi.qa.backend.generateurlpayload.GenerateUrlPayload;
import ca.bc.gov.open.jagefilingapi.qa.backendutils.APIResources;
import ca.bc.gov.open.jagefilingapi.qa.backendutils.TestUtil;
import ca.bc.gov.open.jagefilingapi.qa.frontendutils.JsonDataReader;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import java.io.File;
import java.io.IOException;

import static io.restassured.RestAssured.given;

public class GenerateUrlRequestBuilders {

    private RequestSpecification request;
    private static final String X_TRANSACTION_ID = "X-Transaction-Id";
    private static final String X_USER_ID = "X-User-Id";
    private static final String GENERATE_URL_PATH = "/generateUrl";
    private static final String UPLOAD_FILE_PATH = "src/test/java/testdatasource";
    private static final String FILES = "files";
    private static final String FILE_NAME_PATH = "/test-document.pdf";
    private static final String CLIENT_ID = "client_id";
    private static final String GRANT_TYPE = "grant_type";
    private static final String CLIENT_SECRET = "client_secret";
    private static final String ACCESS_TOKEN = "access_token";
    private static final String GET_CONFIG_PATH = "/config";
    private static final String FILING_PACKAGE_PATH_PARAM = "/filing-package";
    private static final String DOCUMENT_PATH_PARAM = "/document";
    private static final String SECOND_FILE_NAME_PATH = "/test-document-2.pdf";
    private static final String UPDATE_DOCUMENTS_PATH_PARAM = "/update-documents";
    private GenerateUrlPayload payloadData;

    public Response getBearerToken() {
        String resourceAPI = System.getProperty("KEYCLOAK_URL");
        String clientSecret = System.getProperty("EFILING_DEMO_KEYCLOAK_CREDENTIALS_SECRET");
        request = RestAssured.given()
                .formParam(CLIENT_ID, "efiling-demo")
                .formParam(GRANT_TYPE, "client_credentials")
                .formParam(CLIENT_SECRET, clientSecret);

        return request.when().post(resourceAPI).then()
                .spec(TestUtil.validResponseSpecification())
                .extract().response();
    }

    public Response requestWithSinglePdfDocument(String resourceValue, String accountGuid, String fileNamePath) throws IOException {

        payloadData = new GenerateUrlPayload();
        APIResources resourceAPI = APIResources.valueOf(resourceValue);
        String validUserid = JsonDataReader.getCsoAccountGuid().getValidUserId();

        Response response = getBearerToken();
        JsonPath jsonPath = new JsonPath(response.asString());

        String accessToken = jsonPath.get(ACCESS_TOKEN);
        File file = new File(UPLOAD_FILE_PATH + fileNamePath);

        request = RestAssured.given().auth().preemptive().oauth2(accessToken)
                .spec(TestUtil.submitDocumentsRequestSpecification())
                .header(X_TRANSACTION_ID, accountGuid)
                .header(X_USER_ID, validUserid)
                .multiPart(FILES, file);

        return request.when().post(resourceAPI.getResource()).then()
                .extract().response();
    }

    public Response validRequestWithMultipleDocuments(String resourceValue) throws IOException {
        payloadData = new GenerateUrlPayload();

        APIResources resourceAPI = APIResources.valueOf(resourceValue);
        String validExistingCSOGuid = JsonDataReader.getCsoAccountGuid().getValidExistingCSOGuid();
        String validUserid = JsonDataReader.getCsoAccountGuid().getValidUserId();

        Response response = getBearerToken();
        JsonPath jsonPath = new JsonPath(response.asString());

        String accessToken = jsonPath.get(ACCESS_TOKEN);

        File firstPdfFile = new File(UPLOAD_FILE_PATH + FILE_NAME_PATH);
        File secondPdfFile = new File(UPLOAD_FILE_PATH + "/test-document-2.pdf");

        request = RestAssured.given().auth().preemptive().oauth2(accessToken)
                .spec(TestUtil.submitDocumentsRequestSpecification())
                .header(X_TRANSACTION_ID, validExistingCSOGuid)
                .header(X_USER_ID, validUserid)
                .multiPart(FILES, firstPdfFile)
                .multiPart(FILES, secondPdfFile);

        return request.when().post(resourceAPI.getResource()).then()
                .spec(TestUtil.validResponseSpecification())
                .extract().response();
    }

    public Response requestToGetCourts(String resource) throws IOException {
        APIResources resourceGet = APIResources.valueOf(resource);

        Response response = getBearerToken();
        JsonPath jsonPath = new JsonPath(response.asString());

        String accessToken = jsonPath.get(ACCESS_TOKEN);

        request = RestAssured.given().auth().preemptive().oauth2(accessToken)
                .spec(TestUtil.requestSpecification());
        return request.when().get(resourceGet.getResource())
                .then()
                .spec(TestUtil.validResponseSpecification())
                .extract().response();
    }

    public Response requestToGetSubmissionConfig(String resource, String accountGuid, String submissionId, String userJwt) throws IOException {
        APIResources resourceGet = APIResources.valueOf(resource);

        request = given().auth().preemptive().oauth2(userJwt)
                .spec(TestUtil.requestSpecification())
                .header(X_TRANSACTION_ID, accountGuid);

        return request.when().get(resourceGet.getResource() + submissionId + GET_CONFIG_PATH)
                .then()
                .spec(TestUtil.validResponseSpecification())
                .extract().response();
    }

    public Response requestToGetFilingPackage(String resource, String accountGuid,
                                              String submissionId, String userJwt) throws IOException {
        APIResources resourceGet = APIResources.valueOf(resource);

        request = given().auth().preemptive().oauth2(userJwt)
                .spec(TestUtil.requestSpecification())
                .header(X_TRANSACTION_ID, accountGuid);

        return request.when().get(resourceGet.getResource() + submissionId + FILING_PACKAGE_PATH_PARAM)
                .then()
                .spec(TestUtil.validResponseSpecification())
                .extract().response();
    }

    public Response requestToGetDocumentUsingFileName(String resource, String accountGuid,
                                              String submissionId, String userJwt) throws IOException {
        APIResources resourceGet = APIResources.valueOf(resource);

        request = given().auth().preemptive().oauth2(userJwt)
                .spec(TestUtil.requestSpecification())
                .header(X_TRANSACTION_ID, accountGuid);

        return request.when().get(resourceGet.getResource() + submissionId + DOCUMENT_PATH_PARAM + FILE_NAME_PATH)
                .then()
                .spec(TestUtil.validResponseCodeSpecification())
                .extract().response();
    }

    public Response requestToGetDocumentUsingSecondFileName(String resource, String accountGuid,
                                                      String submissionId, String userJwt) throws IOException {
        APIResources resourceGet = APIResources.valueOf(resource);

        request = given().auth().preemptive().oauth2(userJwt)
                .spec(TestUtil.requestSpecification())
                .header(X_TRANSACTION_ID, accountGuid);

        return request.when().get(resourceGet.getResource() + submissionId + DOCUMENT_PATH_PARAM + SECOND_FILE_NAME_PATH)
                .then()
                .spec(TestUtil.validResponseCodeSpecification())
                .extract().response();
    }


    public Response postRequestWithPayload(String resourceValue, String accountGuid,
                                           String submissionId, String pathParam) throws IOException {
        payloadData = new GenerateUrlPayload();
        APIResources resourceGet = APIResources.valueOf(resourceValue);
        String validUserid = JsonDataReader.getCsoAccountGuid().getValidUserId();

        Response response = getBearerToken();
        JsonPath jsonPath = new JsonPath(response.asString());

        String accessToken = jsonPath.get(ACCESS_TOKEN);

         request = given().auth().preemptive().oauth2(accessToken)
                .spec(TestUtil.requestSpecification())
                .header(X_TRANSACTION_ID, accountGuid)
                .header(X_USER_ID,validUserid )
                .body(payloadData.validGenerateUrlPayload());

       return request.when().post(resourceGet.getResource() + submissionId + pathParam)
                .then()
                .spec(TestUtil.validResponseSpecification())
                .extract().response();
    }

    public Response requestWithInvalidCSOAccountGuid(String resourceValue) throws IOException {
        payloadData = new GenerateUrlPayload();
        APIResources resourceAPI = APIResources.valueOf(resourceValue);
        String invalidNoFilingRoleGuid = JsonDataReader.getCsoAccountGuid().getInvalidNoFilingRoleGuid();
        String validUserId = JsonDataReader.getCsoAccountGuid().getValidUserId();

        Response response = getBearerToken();
        JsonPath jsonPath = new JsonPath(response.asString());

        String accessToken = jsonPath.get(ACCESS_TOKEN);
        File pdfFile = new File(UPLOAD_FILE_PATH + FILE_NAME_PATH);

        request = given().auth().preemptive().oauth2(accessToken)
                .spec(TestUtil.submitDocumentsRequestSpecification())
                .header(X_TRANSACTION_ID, invalidNoFilingRoleGuid)
                .header(X_USER_ID, validUserId)
                .multiPart(FILES, pdfFile);

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
                .header(X_TRANSACTION_ID, validExistingCSOGuid)
                .body(payloadData.validGenerateUrlPayload());

        return request.when().post(resourceInvalid.getResource() + validExistingCSOGuid + "/generateUrs")
                .then()
                .extract().response();
    }

    public Response requestWithoutIdInThePath(String resourceValue) throws IOException {
        payloadData = new GenerateUrlPayload();

        APIResources resourceValid = APIResources.valueOf(resourceValue);
        String validExistingCSOGuid = JsonDataReader.getCsoAccountGuid().getValidExistingCSOGuid();

        request = given().spec(TestUtil.requestSpecification())
                .header(X_TRANSACTION_ID, validExistingCSOGuid)
                .body(payloadData.validGenerateUrlPayload());

        return request.when().post(resourceValid.getResource() + "generateUrl")
                .then()
                .extract().response();
    }

    public Response requestToUpdateDocumentProperties(String resource, String accountGuid, String submissionId,
                                                      String userJwt) throws IOException {
        payloadData = new GenerateUrlPayload();
        APIResources resourceGet = APIResources.valueOf(resource);

        request = given().auth().preemptive().oauth2(userJwt)
                .spec(TestUtil.requestSpecification())
                .header(X_TRANSACTION_ID, accountGuid)
                .body(payloadData.updatePropertiesPayload());

        return request.when().post(resourceGet.getResource() + submissionId + UPDATE_DOCUMENTS_PATH_PARAM)
                .then()
                .spec(TestUtil.validResponseSpecification())
                .extract().response();
    }

    public Response requestToDeleteDocuments(String resource, String accountGuid, String submissionId, String userJwt) throws IOException {
        APIResources resourceGet = APIResources.valueOf(resource);

        request = given().auth().preemptive().oauth2(userJwt)
                .spec(TestUtil.requestSpecification())
                .header(X_TRANSACTION_ID, accountGuid);

        return request.when().delete(resourceGet.getResource() + submissionId)
                .then()
                .spec(TestUtil.validResponseCodeSpecification())
                .extract().response();
    }
}
