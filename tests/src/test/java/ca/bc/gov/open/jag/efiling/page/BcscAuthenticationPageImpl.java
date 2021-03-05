package ca.bc.gov.open.jag.efiling.page;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.springframework.beans.factory.annotation.Value;

import java.io.File;

public class BcscAuthenticationPageImpl extends BasePage implements AuthenticationPage {

    @Value("${EFILING_ADMIN}")
    private String efilingAdminUrl;

    @Value("${BCSC_CARD_NUMBER}")
    private String bcscCardNumber;

    @Value("${BCSC_PASSCODE}")
    private String bcscPasscode;

    @Value("classpath:data/test-document.pdf")
    private File pdfDocumentToUpload;

    //Page Objects:
    @FindBy(id = "zocial-bcsc")
    private WebElement bcscLink;

    @FindBy(id = "tile_virtual_device_div_id")
    private WebElement bcscVirtualCardButton;

    @FindBy(id = "csn-entry-wrapper")
    private WebElement bcscNumberEntrySection;

    @FindBy(id = "csn")
    private WebElement bcscNumberInputField;

    @FindBy(id = "continue")
    private WebElement continueButton;

    @FindBy(id = "btnSubmit")
    private WebElement continueSubmitButton;

    @FindBy(id = "passcode")
    private WebElement passcodeInputField;

    @FindBy(id = "form_setConfirmation")
    private WebElement personalIdentitySection;

    @FindBy(xpath = "//*[@data-testid='dropdownzone']/div/input")
    private WebElement fileUploadInput;

    @FindBy(xpath = "//button[@data-test-id='generate-url-btn']")
    private WebElement filingPackageButton;

    public void redirectToEfilingHubUsingBcsc() {
        uploadAPdfOnDemoClient();
        selectDocumentTypeAndSubmit();
    }

    public void uploadAPdfOnDemoClient() {
        try {
            wait.until(ExpectedConditions.visibilityOf(fileUploadInput)).sendKeys(pdfDocumentToUpload.getPath());
        } catch (org.openqa.selenium.TimeoutException ex) {
            fileUploadInput.sendKeys(pdfDocumentToUpload.getPath());
            System.out.println(pdfDocumentToUpload.getPath());
        }
    }

    public void selectDocumentTypeAndSubmit() {
        Select selectDocumentType = new Select(this.driver.findElement(By.id("dropdown")));
        selectDocumentType.selectByValue("AFF");
        filingPackageButton.click();
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//button[@data-test-id='continue-btn']")));
    }

    public void accessBcscLogIn() {
        wait.until(ExpectedConditions.titleContains("Log in to Family Law Act Application"));
        bcscLink.click();
        wait.until(ExpectedConditions.visibilityOf(bcscVirtualCardButton));

        try {
            bcscVirtualCardButton.click();
        } catch (org.openqa.selenium.StaleElementReferenceException ex) {
            bcscVirtualCardButton.click();
        }

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("csn-entry-wrapper")));

    }

    public void enterBcscCardNumber() {
        try {
            wait.until(ExpectedConditions.elementToBeClickable(bcscNumberInputField));
            bcscNumberInputField.sendKeys(bcscCardNumber);
        } catch (org.openqa.selenium.StaleElementReferenceException ex) {
            bcscNumberInputField.sendKeys(bcscCardNumber);
        }

        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("continue")));
        continueButton.click();

        wait.until(ExpectedConditions.visibilityOf(passcodeInputField));

    }

    public void enterBcscPasscode() {
        try {
            passcodeInputField.sendKeys(bcscPasscode);
        } catch (org.openqa.selenium.StaleElementReferenceException ex) {
            passcodeInputField.sendKeys(bcscPasscode);
        }

        continueSubmitButton.click();

    }

    public void submitBcscCredentials() {
        wait.until(ExpectedConditions.visibilityOf(personalIdentitySection));

        continueSubmitButton.click();
        wait.until(ExpectedConditions.titleIs("eFiling Demo Client"));

    }

    @Override
    public void goTo(String url) {
        this.driver.get(url);
    }

    @Override
    public String getName() {
        return "bcsc";
    }

    @Override
    public void signIn() {
        goTo(efilingAdminUrl);
        accessBcscLogIn();
        enterBcscCardNumber();
        enterBcscPasscode();
        submitBcscCredentials();
        redirectToEfilingHubUsingBcsc();
    }
}
