package ca.bc.gov.open.jagefilingapi.qa.frontendutils;

import ca.bc.gov.open.jagefilingapi.qa.config.ReadConfig;
import ca.bc.gov.open.jagefilingapi.qa.frontend.pages.AuthenticationPage;
import ca.bc.gov.open.jagefilingapi.qa.frontend.pages.LandingPage;
import ca.bc.gov.open.jagefilingapi.qa.frontend.pages.PackageConfirmationPage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.JavascriptExecutor;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class FrontendTestUtil extends DriverClass {

    private static final Logger log = LogManager.getLogger(FrontendTestUtil.class);
    private static final String BASE_PATH = "user.dir";
    private static final String PDF_PATH = "/src/test/java/testdatasource/test-document.pdf";

    public static void verifyLinkActive(String linkUrl) throws IOException {

        URL url = new URL(linkUrl);
        HttpURLConnection httpURLConnection=(HttpURLConnection)url.openConnection();
        httpURLConnection.setConnectTimeout(3000);
        httpURLConnection.connect();

        if(httpURLConnection.getResponseCode() == 200) {
            log.info(linkUrl+" - "+httpURLConnection.getResponseMessage());
        }

        if(httpURLConnection.getResponseCode()==HttpURLConnection.HTTP_NOT_FOUND) {
            log.info(linkUrl+" - "+httpURLConnection.getResponseMessage() + " - "+ HttpURLConnection.HTTP_NOT_FOUND);
        }
    }

    public String getUserJwtToken() throws IOException {
        ReadConfig readConfig = new ReadConfig();

        driverSetUp();
        String url = readConfig.getBaseUrl();

        String username = System.getProperty("BCEID_USERNAME");
        String password = System.getProperty("BCEID_PASSWORD");
        System.out.println(username);
        System.out.println(password);

        driver.get(url);
        log.info("Landing page url is accessed successfully");

        AuthenticationPage authenticationPage = new AuthenticationPage(driver);
        authenticationPage.clickBceid();
        authenticationPage.signInWithBceid(username, password);
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
         authenticationPage.clickBceid();
        authenticationPage.signInWithBceid(username, password);
        log.info("user is authenticated in eFiling demo page.");

        PackageConfirmationPage packageConfirmationPage = new PackageConfirmationPage(driver);
        boolean continuePaymentBtnIsDisplayed = packageConfirmationPage.verifyContinuePaymentBtnIsDisplayed();
     //   Assert.assertTrue(continuePaymentBtnIsDisplayed);

        JavascriptExecutor js = (JavascriptExecutor) driver;
        String userToken = js.executeScript("return window.localStorage.getItem('jwt');").toString();
        driver.quit();
        return userToken;
    }
}
