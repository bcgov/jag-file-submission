package ca.bc.gov.open.jagFileSubmission;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.logging.Logger;

public class CommonUtils {

    private static Logger log = Logger.getLogger("CommonUtils.class");

    public static void login() throws Exception {

        WebDriver driver = CustomWebDriverManager.getDriver();
        WebElement element = CustomWebDriverManager.getElement();

        if (Config.ENVIROMENT.equals(Constants.DEV)) {
            driver.get("https://court-of-appeal-dev.apps.silver.devops.gov.bc.ca/court-of-appeal/");
            driver.navigate().to("https://court-of-appeal-dev.apps.silver.devops.gov.bc.ca/court-of-appeal/");
            driver.navigate().refresh();

        } else if (Config.ENVIROMENT.equals(Constants.TST)) {
            driver.get("");
            driver.navigate().to("");
            driver.navigate().refresh();

        }
    }

    public static void loginEDivorce() throws Exception {

        WebDriver driver = CustomWebDriverManager.getDriver();
        WebElement element = CustomWebDriverManager.getElement();

        if (Config.ENVIROMENT.equals(Constants.DEV)) {
            driver.get("https://test.justice.gov.bc.ca/divorce/prequalification/step_01");
            driver.navigate().to("https://test.justice.gov.bc.ca/divorce/prequalification/step_01");
            driver.navigate().refresh();

        } else if (Config.ENVIROMENT.equals(Constants.TST)) {
            driver.get("");
            driver.navigate().to("");
            driver.navigate().refresh();
        }
    }

    public static void FLA() throws Exception {

        WebDriver driver = CustomWebDriverManager.getDriver();
        WebElement element = CustomWebDriverManager.getElement();

        if (Config.ENVIROMENT.equals(Constants.DEV)) {
            driver.get("https://family-law-act-dev.apps.silver.devops.gov.bc.ca/apply-for-family-order/");
            driver.navigate().to("https://family-law-act-dev.apps.silver.devops.gov.bc.ca/apply-for-family-order/");
            driver.navigate().refresh();

        } else if (Config.ENVIROMENT.equals(Constants.TST)) {
            driver.get("https://family-law-act-test.apps.silver.devops.gov.bc.ca/apply-for-family-order/");
            driver.navigate().to("https://family-law-act-test.apps.silver.devops.gov.bc.ca/apply-for-family-order/");
            driver.navigate().refresh();
        }

    }
}

