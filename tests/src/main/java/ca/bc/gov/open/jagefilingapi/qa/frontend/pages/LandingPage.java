package ca.bc.gov.open.jagefilingapi.qa.frontend.pages;

import ca.bc.gov.open.jagefilingapi.qa.frontendutils.DriverClass;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class LandingPage extends DriverClass {
    private final WebDriver driver;

    //Initializing the driver
    public LandingPage(WebDriver driver) {
        this.driver = driver;
    }

    //Actions:
    public String verifyLandingPageTitle() {
        WebDriverWait wait = new WebDriverWait(driver, 40);
        wait.until(ExpectedConditions.titleIs("eFiling Frontend"));
        log.info("Waiting for the page to load...");
        return driver.getTitle();
    }
}
