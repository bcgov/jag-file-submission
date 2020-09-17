package ca.bc.gov.open.jagefilingapi.qa.frontendutils;

import ca.bc.gov.open.jagefilingapi.qa.frontend.pages.AuthenticationPage;
import ca.bc.gov.open.jagefilingapi.qa.frontend.pages.PackageConfirmationPage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

public class FrontendTestUtil extends DriverClass {

    private static final Logger log = LogManager.getLogger(FrontendTestUtil.class);

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

    public String getUserJwtToken(String respUrl) throws InterruptedException, IOException {
        driverSetUp();
        String username = System.getProperty("BCEID_USERNAME");
        String password = System.getProperty("BCEID_PASSWORD");

        try {
            for(int i = 0; i<3; i++) {

                driver.get(respUrl);
                log.info("Package confirmation page url is accessed successfully");

                AuthenticationPage authenticationPage = new AuthenticationPage(driver);
                authenticationPage.clickBceid();
                Thread.sleep(4000L);
                authenticationPage.signInWithBceid(username, password);
                log.info("user is authenticated before reaching eFiling hub page");

                PackageConfirmationPage packageConfirmationPage = new PackageConfirmationPage(driver);
                packageConfirmationPage.verifyContinuePaymentBtnIsDisplayed();

                if(packageConfirmationPage.verifyContinuePaymentBtnIsDisplayed()) {
                    break;
                }
            }
        } catch (org.openqa.selenium.TimeoutException tx) {
            log.info("Package confirmation page is not displayed");
        }

        Thread.sleep(4000L);
        JavascriptExecutor js = (JavascriptExecutor) driver;
        return js.executeScript("return window.localStorage.getItem('jwt');").toString();
    }
}
