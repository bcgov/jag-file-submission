package ca.bc.gov.open.jagefilingapi.qa.frontendutils;

import ca.bc.gov.open.jagefilingapi.qa.config.ReadConfig;
import ca.bc.gov.open.jagefilingapi.qa.frontend.pages.AuthenticationPage;
import ca.bc.gov.open.jagefilingapi.qa.frontend.pages.CreateCsoAccountPage;
import ca.bc.gov.open.jagefilingapi.qa.frontend.pages.LandingPage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

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

    public String getUserJwtToken() throws IOException, InterruptedException {
        ReadConfig readConfig = new ReadConfig();

        driverSetUp();
        String url = readConfig.getBaseUrl();

        String username = System.getProperty("BCEID_USERNAME");
        String password = System.getProperty("BCEID_PASSWORD");

        driver.get(url);
        log.info("Landing page url is accessed successfully");

        AuthenticationPage authenticationPage = new AuthenticationPage(driver);
        authenticationPage.clickBceid();
        Thread.sleep(2000L);
        authenticationPage.signInWithBceid(username, password);
        log.info("user is authenticated before reaching eFiling demo page");

        LandingPage landingPage = new LandingPage(driver);
        String filePath = System.getProperty(BASE_PATH) + PDF_PATH;
        landingPage.chooseFileToUpload(filePath);
        landingPage.enterJsonData();
        landingPage.clickEfilePackageButton();
        log.info("Pdf file is uploaded successfully.");

        Thread.sleep(4000L);

        JavascriptExecutor js = (JavascriptExecutor) driver;
        String userToken = js.executeScript("return window.localStorage.getItem('jwt');").toString();
        driver.quit();
        return userToken;
    }
}
