package ca.bc.gov.open.jagefilingapi.qa.frontend.pages;

import ca.bc.gov.open.jagefilingapi.qa.frontendutils.DriverClass;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
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
            Actions action = new Actions(driver);
            WebDriverWait wait = new WebDriverWait(driver, 120);
            wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@data-test-id='upload-link']")));
            action.moveToElement(uploadLink).click().build().perform();

        } catch (org.openqa.selenium.TimeoutException | org.openqa.selenium.NoSuchElementException tx) {
            JavascriptExecutor js = (JavascriptExecutor)driver;
            js.executeScript("arguments[0].click();", uploadLink);
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
