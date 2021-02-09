package ca.bc.gov.open.jag.efiling.page;


import ca.bc.gov.open.jag.efiling.error.EfilingTestException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.ArrayList;
import java.util.List;

public class PackageConfirmationPage extends Base {

    Logger log = LogManager.getLogger(PackageConfirmationPage.class);

    //Page Objects:
    @FindBy(xpath = "//button[@data-test-id='continue-btn']")
    WebElement continuePaymentBtn;

    @FindBy(xpath = "//*[@data-test-id='upload-link']")
    WebElement uploadLink;

    @FindBy(xpath = "//span[@data-test-id='uploaded-file']")
    WebElement uploadedInitialFile;

    @FindBy(xpath = "//span[@data-test-id='uploaded-file']")
    List<WebElement> uploadedFiles;

    //Actions:
    public boolean verifyContinuePaymentBtnIsEnabled() {
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//button[@data-test-id='continue-btn']")));

        if(!continuePaymentBtn.isDisplayed()) throw new EfilingTestException("User may not have a CSO account created");
        return continuePaymentBtn.isEnabled();
    }

    public void clickContinuePaymentBtn() {
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//button[@data-test-id='continue-btn']")));
        continuePaymentBtn.click();
    }

    public String getInitialDocumentName() {
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//span[@data-test-id='uploaded-file']")));
        return uploadedInitialFile.getText();
    }

    public void clickUploadLink() {
        try {
            Actions action = new Actions(driver);
            wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@data-test-id='upload-link']")));
            action.moveToElement(uploadLink).click().build().perform();

        } catch (org.openqa.selenium.TimeoutException | org.openqa.selenium.NoSuchElementException tx) {
            JavascriptExecutor js = (JavascriptExecutor) this.driver;
            js.executeScript("arguments[0].click();", uploadLink);
        }
    }

    public String verifyPageTitle() {
        wait.until(ExpectedConditions.titleIs("E-File submission"));
        log.info("Waiting for the page to load...");
        return this.driver.getTitle();
    }

    public List<String> getUploadedFilesList() {
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//span[@data-test-id='uploaded-file']")));
        List<String> UploadedFileList = new ArrayList<>();
        for (WebElement webElement : uploadedFiles) {
            UploadedFileList.add(webElement.getText());
        }
        return UploadedFileList;
    }
}
