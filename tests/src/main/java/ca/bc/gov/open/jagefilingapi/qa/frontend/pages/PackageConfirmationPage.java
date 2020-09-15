package ca.bc.gov.open.jagefilingapi.qa.frontend.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class PackageConfirmationPage {

    private final WebDriver driver;

    @FindBy(xpath = "//button[@data-test-id='continue-btn']")
    WebElement continuePaymentBtn;

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
}
