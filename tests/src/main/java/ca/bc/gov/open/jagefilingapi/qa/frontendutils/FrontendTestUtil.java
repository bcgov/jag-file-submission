package ca.bc.gov.open.jagefilingapi.qa.frontendutils;

import ca.bc.gov.open.jagefilingapi.qa.frontend.pages.AuthenticationPage;
import ca.bc.gov.open.jagefilingapi.qa.frontend.pages.EFileSubmissionPage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.JavascriptExecutor;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import org.junit.Assert;

public class FrontendTestUtil extends DriverClass {

    private static final Logger log = LogManager.getLogger(FrontendTestUtil.class);
    private static final String EFILE_SUBMISSION_PAGE_TITLE = "E-File submission";

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

    public void accessFrontEndPage(String respUrl) throws InterruptedException, IOException {
            try {
                driverSetUp();
                driver.get(respUrl);
                log.info("Efiling hub page url is accessed successfully");

                AuthenticationPage authenticationPage = new AuthenticationPage(driver);
                if(System.getProperty("ENV").equals("demo")) {
                    authenticationPage.clickBceid();
                }
                Thread.sleep(4000L);
                authenticationPage.signInWithBceid(System.getProperty("BCEID_USERNAME"), System.getProperty("BCEID_PASSWORD"));
                log.info("user is authenticated before reaching eFiling hub page");
                EFileSubmissionPage eFileSubmissionPage = new EFileSubmissionPage(driver);
                Assert.assertEquals(EFILE_SUBMISSION_PAGE_TITLE, eFileSubmissionPage.verifyEfilingPageTitle());
                log.info("Efiling submission page title is verified");

        } catch (org.openqa.selenium.TimeoutException tx) {
            log.info("Create CSO account or Package confirmation page is not displayed");
        }
        Thread.sleep(4000L);
    }

    public String getUserJwtToken(String respUrl) throws IOException, InterruptedException {
        accessFrontEndPage(respUrl);
        JavascriptExecutor js = (JavascriptExecutor) driver;
        String userToken = js.executeScript("return window.localStorage.getItem('jwt');").toString();
        driver.quit();
        return userToken;

    }
}
