package ca.bc.gov.open.jagefilingapi.qa.frontend.pages;

import ca.bc.gov.open.jagefilingapi.qa.frontendutils.DriverClass;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class EfilingPage extends DriverClass {
        private final WebDriver driver;
        private final By cancelButton = By.xpath("//*[@id='root']/div/main/div/div/div/div/button");

        //Initializing the driver
        public EfilingPage(WebDriver driver) {
            this.driver = driver;
        }

        //Actions:
        public String verifyEfilingPageTitle() {
            WebDriverWait wait = new WebDriverWait(driver, 30);
            wait.until(ExpectedConditions.titleIs("eFiling Frontend"));
            log.info("Waiting for the page to load...");
            return driver.getTitle();
        }

        public boolean verifyCancelButtonIsPresent() {
            return driver.findElement(cancelButton).isDisplayed();
        }
    }

