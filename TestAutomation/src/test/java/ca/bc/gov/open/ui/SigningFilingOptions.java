package ca.bc.gov.open.ui;

import ca.bc.gov.open.jagFileSubmission.WebDriverManager;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class SigningFilingOptions {


    private WebDriver driver;

//    @After
//    public void tearDown() {
//        driver.close();
//        driver.quit();
//    }
//    @AfterClass
//    public static void afterClass() {
//        WebDriverManager.instance = null;
//    }


    @Test
    public void test() throws Exception {
        driver = WebDriverManager.getDriver();
        WebDriverWait driverWait = WebDriverManager.getDriverWait();
        WebElement element = WebDriverManager.getElement();
        WebDriverManager.getElements();

        CompleteDivorceQuestionnaire signOption = new CompleteDivorceQuestionnaire();
        signOption.test();
        //Signing Options
        new WebDriverWait(driver, Duration.ofSeconds(10)).until(
                ExpectedConditions.presenceOfElementLocated(By.linkText("Next"))).click();
        Thread.sleep(1000);
        //Review and Print your Divorce Forms
        new WebDriverWait(driver, Duration.ofSeconds(10)).until(
                ExpectedConditions.presenceOfElementLocated(By.linkText("Next"))).click();
        Thread.sleep(1000);
        //Initial Filing
        new WebDriverWait(driver, Duration.ofSeconds(10)).until(
                ExpectedConditions.presenceOfElementLocated(By.linkText("Next"))).click();
        //Wait for Court File Number
        new WebDriverWait(driver, Duration.ofSeconds(10)).until(
                ExpectedConditions.presenceOfElementLocated(By.linkText("Next"))).click();
        Thread.sleep(1000);
        //Swear / Affirm Forms
        new WebDriverWait(driver, Duration.ofSeconds(10)).until(
                ExpectedConditions.presenceOfElementLocated(By.linkText("Next"))).click();
        //Final Filing


    }
}
