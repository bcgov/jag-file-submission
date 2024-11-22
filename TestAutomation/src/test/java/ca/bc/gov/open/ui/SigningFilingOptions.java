package ca.bc.gov.open.ui;

import ca.bc.gov.open.jagFileSubmission.CustomWebDriverManager;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static ca.bc.gov.open.jagFileSubmission.CustomWebDriverManager.getDriver;

public class SigningFilingOptions {


    private WebDriver driver;

    @After
    public void tearDown() {
        driver.close();
        driver.quit();
    }

    @AfterClass
    public static void afterClass() {
        CustomWebDriverManager.instance = null;
    }

    @Test
    public void test() throws Exception {
        driver = getDriver();
        WebDriverWait driverWait = CustomWebDriverManager.getDriverWait();
        WebElement element;
        CustomWebDriverManager.getElements();

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
//        element = driverWait.until(ExpectedConditions.presenceOfElementLocated(By.name("court_file_number")));
//        element.clear();
//        element = driverWait.until(ExpectedConditions.presenceOfElementLocated(By.name("court_file_number")));
//        element.sendKeys("22122022");
//        element = driverWait.until(ExpectedConditions.presenceOfElementLocated(By.id("submitDocuments")));
//        element.click();
        //Pay
//        new WebDriverWait(driver, Duration.ofSeconds(60)).until(
//                ExpectedConditions.presenceOfElementLocated(By.xpath("//*[contains(text(), 'Continue')]"))).click();
//        element = driverWait.until(ExpectedConditions.presenceOfElementLocated(By.id("agreeCallout")));
//        element.click();
        new WebDriverWait(driver, Duration.ofSeconds(10)).until(
                ExpectedConditions.presenceOfElementLocated(By.linkText("Next"))).click();
        new WebDriverWait(driver, Duration.ofSeconds(10)).until(
                ExpectedConditions.presenceOfElementLocated(By.linkText("Next"))).click();
//        new WebDriverWait(driver, Duration.ofSeconds(60)).until(
//                ExpectedConditions.presenceOfElementLocated(By.xpath("//*[contains(text(), 'Submit')]"))).click();
//        new WebDriverWait(driver, Duration.ofSeconds(100)).until(
//                ExpectedConditions.presenceOfElementLocated(By.linkText("Next"))).click();
        new WebDriverWait(driver, Duration.ofSeconds(60)).until(
                ExpectedConditions.presenceOfElementLocated(By.xpath("//*[contains(text(), 'Order a Certificate of Divorce')]")));



    }
}
