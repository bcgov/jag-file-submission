package ca.bc.gov.open.jagefilingapi.qa.frontend.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class CreateCsoAccountPage {

    private final WebDriver driver;

    //Page Objects
    @FindBy(xpath = "//button[@data-test-id='create-cso-btn']")
    WebElement createCsoBtn;

    //Initializing the driver:
    public CreateCsoAccountPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    //Actions:

    public boolean verifyCsoBtnIsDisplayed() {
        WebDriverWait wait = new WebDriverWait(driver, 90);
        wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//button[@data-test-id='create-cso-btn']")));
        try {
            return createCsoBtn.isDisplayed();
        } catch (org.openqa.selenium.TimeoutException ex) {
            return createCsoBtn.isDisplayed();
        }
    }

    public void clickCreateCsoAccountBtn() {
         createCsoBtn.click();
    }
}
