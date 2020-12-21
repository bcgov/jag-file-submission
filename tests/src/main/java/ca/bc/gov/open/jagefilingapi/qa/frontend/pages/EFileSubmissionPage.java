package ca.bc.gov.open.jagefilingapi.qa.frontend.pages;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class EFileSubmissionPage {

    private final WebDriver driver;
    private WebDriverWait wait;

    Logger log = LogManager.getLogger(EFileSubmissionPage.class);

    //Page Objects:
    @FindBy(xpath = "//button[@data-test-id='main-cancel-btn']")
    WebElement cancelButton;

    @FindBy(xpath = "//button[@data-test-id='modal-confirm-btn']")
    WebElement confirmCancelSubmission;

    @FindBy(xpath = "//button[@data-test-id='modal-cancel-btn']")
    WebElement resumeSubmission;

    @FindBy(xpath = "//button[@data-test-id='return-home-btn']")
    WebElement returnHomeButton;

    @FindBy(id = "acceptTerms")
    WebElement acceptTermsCheckbox;

    @FindBy(xpath = "//button[@data-test-id='create-cso-btn']")
    WebElement createCsoAccountButton;

    //Initializing the driver:
    public EFileSubmissionPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    //Actions:
    public String verifyEfilingPageTitle() {
        wait = new WebDriverWait(driver, 90);
        wait.until(ExpectedConditions.titleIs("E-File submission"));
        log.info("Waiting for the page to load...");
        return driver.getTitle();
    }

    public void clickCancelButton() {
        wait = new WebDriverWait(driver, 60);
        wait.until(ExpectedConditions.visibilityOf(cancelButton));

        Actions actions = new Actions(driver);
        actions.moveToElement(cancelButton).click().build().perform();

        wait = new WebDriverWait(driver, 30);
        wait.until(ExpectedConditions.visibilityOf(confirmCancelSubmission));
    }

    public void clickResumeSubmission() {
        driver.switchTo().activeElement();
        resumeSubmission.click();
        driver.switchTo().defaultContent();
    }

    public boolean verifyCancelPageIsDisplayed() {
        wait = new WebDriverWait(driver, 60);
        wait.until(ExpectedConditions.visibilityOf(returnHomeButton));
        return returnHomeButton.isDisplayed();
    }

    public boolean verifyCreateCsoAccountBtnIsDisplayed() {
        wait = new WebDriverWait(driver, 60);
        wait.until(ExpectedConditions.visibilityOf(createCsoAccountButton));
        return createCsoAccountButton.isDisplayed();
    }
}
