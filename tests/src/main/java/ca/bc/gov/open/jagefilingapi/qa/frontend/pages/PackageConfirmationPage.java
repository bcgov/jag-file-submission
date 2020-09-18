package ca.bc.gov.open.jagefilingapi.qa.frontend.pages;

import ca.bc.gov.open.jagefilingapi.qa.frontendutils.DriverClass;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.ArrayList;
import java.util.List;

public class PackageConfirmationPage extends DriverClass {

    private final WebDriver driver;

    @FindBy(xpath = "//button[@data-test-id='continue-btn']")
    WebElement continuePaymentBtn;

    @FindBy(xpath = "//*[@data-test-id='upload-link']")
    WebElement uploadLink;

    @FindBy(xpath = "//span[@data-test-id='uploaded-file']")
    List<WebElement> uploadedFiles;

    //Initializing the driver:
    public PackageConfirmationPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    //Actions:
    public boolean verifyContinuePaymentBtnIsDisplayed() {
        WebDriverWait wait = new WebDriverWait(driver, 120);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//button[@data-test-id='continue-btn']")));
            return continuePaymentBtn.isDisplayed();
    }

    public void clickContinuePaymentBtn() {
         continuePaymentBtn.click();
    }

    public void clickUploadLink() {
        try {
            for (int i = 0; i < 3; i++) {
                WebDriverWait wait = new WebDriverWait(driver, 120);
                wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@data-test-id='upload-link']")));
                uploadLink.click();
                if (!uploadLink.isDisplayed()) {
                    break;
                }
            }
        } catch (org.openqa.selenium.TimeoutException tx) {
            log.info("Upload link is not displayed in the page");
        }
    }

    public List<String> getUploadedFilesList() {
        List<String> UploadedFileList = new ArrayList<>();
        for (WebElement webElement : uploadedFiles) {
            UploadedFileList.add(webElement.getText());
        }
        return UploadedFileList;
    }
}
