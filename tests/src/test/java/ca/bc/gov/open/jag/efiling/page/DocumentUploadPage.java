package ca.bc.gov.open.jag.efiling.page;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class DocumentUploadPage extends BasePage {

	public static String DUPLICATE_FILE_ERROR = "You cannot upload multiple files with the same name.";
	
    @FindBy(xpath = "//*[@data-testid='dropdownzone']/div/input")
    private WebElement selectFile;

    @FindBy(xpath = "//label[@for='yes-isAmendment-test-document-additional.pdf']")
    private WebElement isAmendmentRadioBtn;

    @FindBy(xpath = "//label[@for='no-isSupremeCourtScheduling-test-document-additional.pdf']")
    private WebElement isSupremeCourtRadioBtn;

    @FindBy(xpath = "//button[@data-testid='continue-upload-btn']")
    private WebElement continueBtn;

    @FindBy(xpath = "//button[@data-testid='cancel-upload-btn']")
    private WebElement cancelUpload;

    @FindBy(xpath = "//button[@data-testid='remove-icon']")
    private WebElement removeFileIcon;
    
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
    
    /** Returns the text of a <p /> tag with an error message if it exists. */
    public String getDuplicateFileError() {
    	List<WebElement> elements = driver.findElements(By.xpath("//p[@data-testid='err-dup-file']"));
    	if (elements != null && !elements.isEmpty()) {
    		return elements.get(0).getText();
    	}
    	return null;
	}
}
