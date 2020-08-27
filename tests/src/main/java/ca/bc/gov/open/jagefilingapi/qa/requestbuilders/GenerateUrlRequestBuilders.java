package ca.bc.gov.open.jagefilingapi.qa.requestbuilders;

import ca.bc.gov.open.jagefilingapi.qa.backend.generateurlpayload.GenerateUrlPayload;
import ca.bc.gov.open.jagefilingapi.qa.backendutils.APIResources;
import ca.bc.gov.open.jagefilingapi.qa.backendutils.TestUtil;
import ca.bc.gov.open.jagefilingapi.qa.config.ReadConfig;
import ca.bc.gov.open.jagefilingapi.qa.frontend.pages.AuthenticationPage;
import ca.bc.gov.open.jagefilingapi.qa.frontend.pages.LandingPage;
import ca.bc.gov.open.jagefilingapi.qa.frontend.pages.PackageConfirmationPage;
import ca.bc.gov.open.jagefilingapi.qa.frontendutils.DriverClass;
import ca.bc.gov.open.jagefilingapi.qa.frontendutils.JsonDataReader;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.junit.Assert;
import org.openqa.selenium.JavascriptExecutor;
import java.io.File;
import java.io.IOException;

import static io.restassured.RestAssured.given;


public class GenerateUrlRequestBuilders extends DriverClass {

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
    private static final String BASE_PATH = "user.dir";
    private static final String PDF_PATH = "/src/test/java/testdatasource/test-document.pdf";
    private static final String ACCESS_TOKEN = "access_token";

    private GenerateUrlPayload payloadData;

    public Response getBearerToken() throws IOException {
        ReadConfig readConfig = new ReadConfig();
        String resourceAPI = readConfig.getKeycloakUrl();
        String validUserid = JsonDataReader.getCsoAccountGuid().getClientSecret();

        request = RestAssured.given().spec(TestUtil.submitDocumentsRequestSpecification())
                .formParam(CLIENT_ID, "efiling-demo")
                .formParam(GRANT_TYPE, "client_credentials")
                .formParam(CLIENT_SECRET, validUserid);

        return request.when().post(resourceAPI).then()
                .spec(TestUtil.validDocumentResponseSpecification())
                .extract().response();
    }

    public Response validRequestWithSingleDocument(String resourceValue) throws IOException {
        payloadData = new GenerateUrlPayload();

        APIResources resourceAPI = APIResources.valueOf(resourceValue);
        String validExistingCSOGuid = JsonDataReader.getCsoAccountGuid().getValidExistingCSOGuid();
        String validUserid = JsonDataReader.getCsoAccountGuid().getValidUserId();

        Response response = getBearerToken();
        JsonPath jsonPath = new JsonPath(response.asString());

        String accessToken = jsonPath.get(ACCESS_TOKEN);

        File pdfFile = new File(UPLOAD_FILE_PATH + FILE_NAME_PATH);

        request = RestAssured.given().auth().preemptive().oauth2(accessToken).spec(TestUtil.submitDocumentsRequestSpecification())
                .header(X_TRANSACTION_ID, validExistingCSOGuid)
                .header(X_USER_ID, validUserid)
                .multiPart(FILES, pdfFile);

        return request.when().post(resourceAPI.getResource()).then()
                .spec(TestUtil.validDocumentResponseSpecification())
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
                .spec(TestUtil.validDocumentResponseSpecification())
                .extract().response();
    }

    public Response requestWithIncorrectFileType(String resourceValue) throws IOException {
        payloadData = new GenerateUrlPayload();

        APIResources resourceAPI = APIResources.valueOf(resourceValue);
        String validExistingCSOGuid = JsonDataReader.getCsoAccountGuid().getValidExistingCSOGuid();
        String validUserid = JsonDataReader.getCsoAccountGuid().getValidUserId();

        Response response = getBearerToken();
        JsonPath jsonPath = new JsonPath(response.asString());

        String accessToken = jsonPath.get(ACCESS_TOKEN);
        File pngFile = new File(UPLOAD_FILE_PATH + "/test-image-document.png");

        request = RestAssured.given().auth().preemptive().oauth2(accessToken)
                .spec(TestUtil.submitDocumentsRequestSpecification())
                .header(X_TRANSACTION_ID, validExistingCSOGuid)
                .header(X_USER_ID, validUserid)
                .multiPart("file", pngFile);

        return request.when().post(resourceAPI.getResource()).then()
                .spec(TestUtil.createCsoAccountIncorrectTypeErrorResponseSpecification())
                .extract().response();
    }

    public Response requestWithNonExistingCSOAccountGuid(String resourceValue) throws IOException {
        payloadData = new GenerateUrlPayload();

        APIResources resourceAPI = APIResources.valueOf(resourceValue);
        String nonExistingCSOGuid = JsonDataReader.getCsoAccountGuid().getNonExistingCSOGuid();
        String validUserid = JsonDataReader.getCsoAccountGuid().getValidUserId();

        Response response = getBearerToken();
        JsonPath jsonPath = new JsonPath(response.asString());

        String accessToken = jsonPath.get(ACCESS_TOKEN);

        File pdfFile = new File(UPLOAD_FILE_PATH + FILE_NAME_PATH);

        request = RestAssured.given().auth().preemptive().oauth2(accessToken)
                .spec(TestUtil.submitDocumentsRequestSpecification())
                .header(X_TRANSACTION_ID, nonExistingCSOGuid)
                .header(X_USER_ID, validUserid)
                .multiPart(FILES, pdfFile);

        return request.when().post(resourceAPI.getResource()).then()
                .spec(TestUtil.validDocumentResponseSpecification())
                .extract().response();
    }

    public Response requestWithInvalidCSOAccountGuid(String resourceValue) throws IOException {
        payloadData = new GenerateUrlPayload();
        APIResources resourceAPI = APIResources.valueOf(resourceValue);
        String invalidNoFilingRoleGuid = JsonDataReader.getCsoAccountGuid().getInvalidNoFilingRoleGuid();
        String inValidUserId = JsonDataReader.getCsoAccountGuid().getInValidUserId();

        Response response = getBearerToken();
        JsonPath jsonPath = new JsonPath(response.asString());

        String accessToken = jsonPath.get(ACCESS_TOKEN);
        File pdfFile = new File(UPLOAD_FILE_PATH + FILE_NAME_PATH);

        request = given().auth().preemptive().oauth2(accessToken)
                .spec(TestUtil.submitDocumentsRequestSpecification())
                .header(X_TRANSACTION_ID, invalidNoFilingRoleGuid)
                .header(X_USER_ID, inValidUserId)
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

    public String getUserJwtToken() throws IOException {
        ReadConfig readConfig = new ReadConfig();

        driverSetUp();
        String url = readConfig.getBaseUrl();

        String username = System.getProperty("BCEID_USERNAME");
        String password = System.getProperty("BCEID_PASSWORD");

        driver.get(url);
        log.info("Landing page url is accessed successfully");

        // ** Leaving this step for demo mode **
        // authenticationPage.clickBceid();
        AuthenticationPage authenticationPage = new AuthenticationPage(driver);
        authenticationPage.signInWithIdir(username, password);
        log.info("user is authenticated before reaching eFiling demo page");

        // ** Leaving this step for demo mode **
        //validExistingCSOGuid = JsonDataReader.getCsoAccountGuid().getValidExistingCSOGuid();
        //landingPage.enterAccountGuid(validExistingCSOGuid);
        LandingPage landingPage = new LandingPage(driver);
        String filePath = System.getProperty(BASE_PATH) + PDF_PATH;
        landingPage.chooseFileToUpload(filePath);
        landingPage.enterJsonData();
        landingPage.clickEfilePackageButton();
        log.info("Pdf file is uploaded successfully.");

        // ** Leaving this step for demo mode **
        // authenticationPage.clickBceid();
        authenticationPage.signInWithIdir(username, password);
        log.info("user is authenticated in eFiling demo page.");

        PackageConfirmationPage packageConfirmationPage = new PackageConfirmationPage(driver);
        boolean continuePaymentBtnIsDisplayed = packageConfirmationPage.verifyContinuePaymentBtnIsDisplayed();
        Assert.assertTrue(continuePaymentBtnIsDisplayed);

        JavascriptExecutor js = (JavascriptExecutor) driver;
        String userToken = js.executeScript("return window.localStorage.getItem('jwt');").toString();
        driver.quit();
        return userToken;
    }
}
