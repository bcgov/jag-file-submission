package ca.bc.gov.open.jag.efiling.page;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;

@Component
public class EfilingAdminHomePage extends BasePage {

    @Value("classpath:data/test-document.pdf")
    private File pdfDocumentToUpload;

    private final Logger logger = LoggerFactory.getLogger(EfilingAdminHomePage.class);


    //Page Objects:
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
        } catch (@SuppressWarnings("unused") org.openqa.selenium.TimeoutException ex) {
            fileUploadInput.sendKeys(pdfDocumentToUpload.getPath());
        }
    }

    public void selectDocumentTypeAndSubmit() {
        Select selectDocumentType = new Select(this.driver.findElement(By.id("dropdown")));
        selectDocumentType.selectByValue("AFF");

        logger.info("Submitting document to Efiling hub");
        filingPackageButton.click();

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//button[@data-test-id='continue-btn']")));
    }
}
