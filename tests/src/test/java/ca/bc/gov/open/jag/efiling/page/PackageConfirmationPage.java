package ca.bc.gov.open.jag.efiling.page;


import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ca.bc.gov.open.jag.efiling.error.EfilingTestException;

public class PackageConfirmationPage extends BasePage {

    private final Logger logger = LoggerFactory.getLogger(PackageConfirmationPage.class);

    //Page Objects:
    @FindBy(xpath = "//button[@data-testid='continue-btn']")
    private WebElement continueBtn;

    @FindBy(xpath = "//*[@data-test-id='upload-link']")
    private WebElement uploadLink;

    @FindBy(xpath = "//span[@data-test-id='uploaded-file']")
    private WebElement uploadedInitialFile;

    @FindBy(xpath = "//span[@data-test-id='uploaded-file']")
    private List<WebElement> uploadedFiles;

    @FindBy(xpath = "//label[@for='Yes']")
    private WebElement rushYesRadioBtn;
    
    @FindBy(xpath = "//div[contains(@class, 'modal-header')]/button[@class='close']")
    private WebElement rushModalCloseBtn;

    //Actions:
    public boolean verifyContinuePaymentBtnIsEnabled() {
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//button[@data-testid='continue-btn']")));

        if (!continueBtn.isDisplayed())
            throw new EfilingTestException("User may not have a CSO account created");
        return continueBtn.isEnabled();
    }

    public void clickContinueBtn() {
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//button[@data-testid='continue-btn']")));
        continueBtn.click();
    }

    /** Clicks the Yes radio button for the label "Do you want to request that this submission be processed on a rush basis?" */
	public void selectRushYesOption() {
		rushYesRadioBtn.click();
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

        } catch (@SuppressWarnings("unused") org.openqa.selenium.TimeoutException | org.openqa.selenium.NoSuchElementException tx) {
            JavascriptExecutor js = (JavascriptExecutor) this.driver;
            js.executeScript("arguments[0].click();", uploadLink);
        }
    }

    public String verifyPageTitle() {
        wait.until(ExpectedConditions.titleIs("E-File submission"));
        logger.info("Waiting for the page to load...");
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
    
    /** Returns true if the rejected banner exists. */ 
    public boolean verifyRejectedBannerExists() { 
    	List<WebElement> elements = driver.findElements(By.className("rejectedMsg")); 
    	return elements != null && !elements.isEmpty();
	}
    
    /** Returns true if the "rush basis" radio options exist. */ 
    public boolean verifyRushRadioOptionsExist() { 
    	List<WebElement> elements = driver.findElements(By.xpath("//div[@data-testid='rushRadioOpts']")); 
    	return elements != null && !elements.isEmpty();
	}
    
    /** Returns true if the Rush sidecard is visible. */ 
    public boolean verifyRushSideCardExist() { 
    	List<WebElement> elements = driver.findElements(By.id("rushSubmissionCard")); 
    	return elements != null && !elements.isEmpty();
	}
    
    /** Returns true if the duplicate banner exists. */ 
    public boolean verifyDuplicateBannerExists() { 
    	List<WebElement> elements = driver.findElements(By.xpath("//div[@data-testid='duplicateDocMsg']")); 
    	return elements != null && !elements.isEmpty();
	}
    
    /** Returns true if the rejected sidecard exists. */ 
    public boolean verifyRejectedSidecardExists() { 
    	List<WebElement> elements = driver.findElements(By.id("rejectedDocumentsCard")); 
    	return elements != null && !elements.isEmpty();
	}

    /** Returns true if the Rush modal is visible. */ 
    public boolean verifyRushModalIsDisplayed() {  	
    	List<WebElement> elements = driver.findElements(By.xpath("//div[contains(@class, 'modal-header')]/div/h2[text()='Rush Documents']"));
    	return elements != null && !elements.isEmpty();
    }

    /** Returns true if the Rush modal is visible. */
    public void clickCloseOnRushModal() {
        rushModalCloseBtn.click();
    }

    public boolean verifyRushDetailsScreenIsDisplayed() {
        List<WebElement> elements = driver.findElements(By.xpath("//div[contains(@class, 'ct-rush')]")); // the main Rush div
        return elements != null && !elements.isEmpty();
    }

}
