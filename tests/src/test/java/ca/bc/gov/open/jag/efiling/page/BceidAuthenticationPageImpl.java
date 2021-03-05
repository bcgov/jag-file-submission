package ca.bc.gov.open.jag.efiling.page;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.springframework.beans.factory.annotation.Value;

import java.io.File;

public class BceidAuthenticationPageImpl extends BasePage implements AuthenticationPage {

    @Value("${EFILING_ADMIN}")
    private String efilingAdminUrl;

    @Value("${USERNAME_BCEID}")
    private String bceidUsername;

    @Value("${PASSWORD_BCEID}")
    private String bceidPassword;

    @Value("classpath:data/test-document.pdf")
    private File pdfDocumentToUpload;


    //Page Objects:
    @FindBy(id = "zocial-bceid")
    private WebElement bceid;

    @FindBy(id = "user")
    private WebElement bceidUsernameField;

    @FindBy(id = "password")
    private WebElement bceidPasswordField;

    @FindBy(xpath = "//input[@name='btnSubmit']")
    private WebElement submitButton;

    @FindBy(xpath = "//*[@data-testid='dropdownzone']/div/input")
    private WebElement fileUploadInput;

    @FindBy(xpath = "//button[@data-test-id='generate-url-btn']")
    private WebElement filingPackageButton;

    public void redirectToEfilingHub() {
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

    public void loginWithBceid() {
        wait.until(ExpectedConditions.titleContains("Log in to Family Law Act Application"));
        bceid.click();
        wait.until(ExpectedConditions.visibilityOf(bceidUsernameField));
        bceidUsernameField.sendKeys(bceidUsername);
        bceidPasswordField.sendKeys(bceidPassword);
        submitButton.click();
        wait.until(ExpectedConditions.titleIs("eFiling Demo Client"));
    }

    @Override
    public void goTo(String url) {
        this.driver.get(url);
    }

    @Override
    public String getName() {
        return "bceid";
    }

    @Override
    public void signIn() {
        goTo(efilingAdminUrl);
        loginWithBceid();
        redirectToEfilingHub();
    }
}
