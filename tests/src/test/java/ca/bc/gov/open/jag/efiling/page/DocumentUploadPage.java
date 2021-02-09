package ca.bc.gov.open.jag.efiling.page;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class DocumentUploadPage extends Base {

    Logger log = LogManager.getLogger(DocumentUploadPage.class);

    @FindBy(xpath = "//*[@data-testid='dropdownzone']/div/input")
    WebElement selectFile;

    @FindBy(xpath = "//label[@for='yes-isAmendment-test-document-additional.pdf']")
    WebElement isAmendmentRadioBtn;

    @FindBy(xpath = "//label[@for='no-isSupremeCourtScheduling-test-document-additional.pdf']")
    WebElement isSupremeCourtRadioBtn;

    @FindBy(xpath = "//button[@data-test-id='continue-upload-btn']")
    WebElement continueBtn;

    @FindBy(xpath = "//button[@data-test-id='cancel-upload-btn']")
    WebElement cancelUpload;

    @FindBy(xpath = "//button[@data-testid='remove-icon']")
    WebElement removeFileIcon;

    //Actions:
    public void selectFileToUpload(String file) {
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@data-testid='dropdownzone']/div/input")));
        selectFile.sendKeys(file);
    }

    public void clickIsAmendmentRadioBtn() {
        isAmendmentRadioBtn.click();
    }

    public void clickIsSupremeCourtBtn() {
        isSupremeCourtRadioBtn.click();
    }

    public void clickContinueBtn() {
        wait.until(ExpectedConditions.elementToBeClickable(continueBtn));
        continueBtn.click();
    }

    public void clickCancelUpload() {
        cancelUpload.click();
    }

    public void clickRemoveFileIcon() {
        Actions action = new Actions(driver);
        action.moveToElement(removeFileIcon).click();
    }
}
