package ca.bc.gov.open.jagefilingapi.qa.frontend.pages;

import ca.bc.gov.open.jagefilingapi.qa.config.ReadConfig;
import ca.bc.gov.open.jagefilingapi.qa.frontendutils.DriverClass;
import ca.bc.gov.open.jagefilingapi.qa.frontendutils.JsonDataReader;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.IOException;

public class LandingPage extends DriverClass {

    private final WebDriver driver;
    ReadConfig readConfig;
    private final By guidInputForm = By.id("textInputId");
    private final By generateUrlButton = By.xpath("//*[@id='root']/div/main/div/div/div[2]/button");
    private final By getErrorText = By.xpath("//*[@id='root']/div/main/div/div/p");

    //Initializing the driver
    public LandingPage(WebDriver driver) {
        this.driver = driver;
    }

    //Actions:
    public String verifyLandingPageTitle() {
        WebDriverWait wait = new WebDriverWait(driver, 40);
        wait.until(ExpectedConditions.titleIs("eFiling Demo Client"));
        log.info("Waiting for the page to load...");
        return driver.getTitle();
    }

    public LandingPage getUrl() throws IOException {
        readConfig = new ReadConfig();
        String url = readConfig.getBaseUrl();
        driver.get(url);
        return new LandingPage(driver);
    }

    public void enterAccountGuid(String guid) {
         driver.findElement(guidInputForm).sendKeys(guid);
    }

    public void clickGenerateUrlButton() {
        driver.findElement(generateUrlButton).click();
    }

    public String getErrorMessageText() {
        return driver.findElement(getErrorText).getText();
    }
}
