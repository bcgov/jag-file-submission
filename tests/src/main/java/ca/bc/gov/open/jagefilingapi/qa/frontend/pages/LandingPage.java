package ca.bc.gov.open.jagefilingapi.qa.frontend.pages;

import ca.bc.gov.open.jagefilingapi.qa.config.ReadConfig;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.io.IOException;

public class LandingPage {

    private final WebDriver driver;

    Logger log = LogManager.getLogger(LandingPage.class);
    ReadConfig readConfig;

    //Page Objects:
    @FindBy(id = "textInputId")
    WebElement guidInputForm;

    @FindBy(xpath = "//*[@data-test-id='generate-url-btn']")
    WebElement generateUrlButton;

    @FindBy(xpath = "//p[@data-test-id='error-text']")
    WebElement getErrorText;

    //Initializing the driver:
    public LandingPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    //Actions:
    public String verifyLandingPageTitle() {
        WebDriverWait wait = new WebDriverWait(driver, 30);
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
         guidInputForm.sendKeys(guid);
    }

    public void clickGenerateUrlButton() {
        generateUrlButton.click();
    }

    public String getErrorMessageText() {
        return getErrorText.getText();
    }
}
