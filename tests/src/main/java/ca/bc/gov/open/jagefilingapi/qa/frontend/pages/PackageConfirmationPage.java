package ca.bc.gov.open.jagefilingapi.qa.frontend.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class PackageConfirmationPage {

    private final WebDriver driver;

    //Page Objects:
    @FindBy(xpath = "//button[@data-test-id='upload-btn']")
    WebElement uploadDocumentsBtn;

    @FindBy(xpath = "//button[@data-test-id='continue-btn']")
    WebElement continuePaymentBtn;

    //Initializing the driver:
    public PackageConfirmationPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    //Actions:
    public boolean verifyUploadDocumentIsDisplayed() {
        return uploadDocumentsBtn.isDisplayed();
    }

    public boolean verifyContinuePaymentBtnIsDisplayed() {
        return continuePaymentBtn.isDisplayed();
    }
}
