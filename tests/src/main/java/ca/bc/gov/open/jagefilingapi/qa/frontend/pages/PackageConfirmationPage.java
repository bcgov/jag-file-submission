package ca.bc.gov.open.jagefilingapi.qa.frontend.pages;

import ca.bc.gov.open.jagefilingapi.qa.frontendutils.DriverClass;
import ca.bc.gov.open.jagefilingapi.qa.frontendutils.FrontendTestUtil;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.IOException;

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

    public boolean verifyContinuePaymentBtnIsDisplayed() throws IOException {
        WebDriverWait wait = new WebDriverWait(driver, 60);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//button[@data-test-id='continue-btn']")));
        try {
            return continuePaymentBtn.isDisplayed();
        } catch (org.openqa.selenium.TimeoutException ex) {
            FrontendTestUtil frontendTestUtil = new FrontendTestUtil();
            frontendTestUtil.getUserJwtToken();
        }
        return continuePaymentBtn.isDisplayed();
    }

    public void clickContinuePaymentBtn() {
         continuePaymentBtn.click();
    }
}
