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
    @FindBy(xpath = "//*[@id='root']/div/main/div/div/div/button")
    WebElement cancelButton;

    @FindBy(css = "body > div.fade.modal.show > div > div > div.mx-auto.mb-5 > div:nth-child(1) > button")
    WebElement confirmCancelSubmission;

    @FindBy(css = "body > div.fade.modal.show > div > div > div.mx-auto.mb-5 > div:nth-child(3) > button")
    WebElement resumeSubmission;

    @FindBy(xpath = "//*[@id='root']/div/main/div/div/div/button")
    WebElement returnHomeButton;

    @FindBy(id = "acceptTerms")
    WebElement acceptTermsCheckbox;

    @FindBy(xpath = "//*[@id='root']/div/main/div/div/section/div[2]/button")
    WebElement createCsoAccountButton;

    @FindBy(xpath = "//*[@id='root']/div/main/div/div/section/div[1]/button")
    WebElement acceptTermsCancelButton;

    //Initializing the driver:
    public EFileSubmissionPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    //Actions:
    public String verifyEfilingPageTitle() {
        wait = new WebDriverWait(driver, 30);
        wait.until(ExpectedConditions.titleIs("eFiling Frontend"));
        log.info("Waiting for the page to load...");
        return driver.getTitle();
    }

    public void clickCancelButton() {
        wait = new WebDriverWait(driver, 30);
        wait.until(ExpectedConditions.visibilityOf(cancelButton));

        Actions actions = new Actions(driver);
        actions.moveToElement(cancelButton).click().build().perform();

        wait = new WebDriverWait(driver, 30);
        wait.until(ExpectedConditions.visibilityOf(confirmCancelSubmission));
    }

    public void clickConfirmCancellation() {
        driver.switchTo().activeElement();
        confirmCancelSubmission.click();
        driver.switchTo().defaultContent();
    }

    public void clickResumeSubmission() {
        driver.switchTo().activeElement();
        resumeSubmission.click();
        driver.switchTo().defaultContent();
    }

    public boolean verifyCancelPageIsDisplayed() {
        return returnHomeButton.isDisplayed();
    }

    public void clickReturnHomeButton() {
        wait = new WebDriverWait(driver, 30);
        wait.until(ExpectedConditions.visibilityOf(returnHomeButton));
        returnHomeButton.click();
    }

    public void selectCheckbox() {
        acceptTermsCheckbox.click();
        if(acceptTermsCheckbox.isSelected()) {
            log.info("Accept user agreement checkbox is toggled on.");
        } else {
            log.info("Accept user agreement checkbox is toggled off.");
        }
    }

    public boolean verifyCreateCsoAccountBtnIsDisplayed() {
        return createCsoAccountButton.isDisplayed();
    }

    public void clickAcceptTermsCancelButton() {
        acceptTermsCancelButton.click();
    }
}
