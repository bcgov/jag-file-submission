package ca.bc.gov.open.ui;

import ca.bc.gov.open.jagFileSubmission.CommonUtils;
import ca.bc.gov.open.jagFileSubmission.CustomWebDriverManager;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

import static ca.bc.gov.open.jagFileSubmission.CustomWebDriverManager.getDriver;

public class EDivorcePreScreening {

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

        driver.get("https://test.justice.gov.bc.ca/divorce/current");

        //reset user
        new WebDriverWait(driver, Duration.ofSeconds(10)).until(
                ExpectedConditions.presenceOfElementLocated(By.xpath("//*[contains(text(), 'Reset User')]"))).click();
        Thread.sleep(1000);
        driver.switchTo().alert().accept();

        //Go E-Divorce page

        CommonUtils.loginEDivorce();

        ((WebElement) driverWait
                .until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@type='radio' and @name='married_marriage_like']"))))
                .click();
        new WebDriverWait(driver, Duration.ofSeconds(10)).until(
                ExpectedConditions.presenceOfElementLocated(By.xpath("//*[contains(text(), 'Next')]"))).click();
        //Before begin

        JavascriptExecutor jse1 = (JavascriptExecutor) driver;
        element = driverWait
                .until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@value='YES' and @name='lived_in_bc']")));
        jse1.executeScript("arguments[0].click();", element);
        JavascriptExecutor jse2 = (JavascriptExecutor) driver;
        element = driverWait
                .until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@value='YES' and @name='lived_in_bc_at_least_year']")));
        jse2.executeScript("arguments[0].click();", element);
        new WebDriverWait(driver, Duration.ofSeconds(10)).until(
                ExpectedConditions.presenceOfElementLocated(By.xpath("//*[contains(text(), 'Next')]"))).click();
        element = driverWait.until(ExpectedConditions.presenceOfElementLocated(By.id("separated_date")));
        element.sendKeys("11/20/2021");

        JavascriptExecutor jse3 = (JavascriptExecutor) driver;
        element = driverWait
                .until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@value='NO' and @name='try_reconcile_after_separated']")));
        jse3.executeScript("arguments[0].click();", element);

        new WebDriverWait(driver, Duration.ofSeconds(10)).until(
                ExpectedConditions.presenceOfElementLocated(By.xpath("//*[contains(text(), 'Next')]"))).click();
        //No children
        JavascriptExecutor jse4 = (JavascriptExecutor) driver;
        element = driverWait
                .until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@value='NO' and @name='children_of_marriage']")));
        jse4.executeScript("arguments[0].click();", element);
        new WebDriverWait(driver, Duration.ofSeconds(10)).until(
                ExpectedConditions.presenceOfElementLocated(By.xpath("//*[contains(text(), 'Next')]"))).click();
        //Marriage cert
        JavascriptExecutor jse5 = (JavascriptExecutor) driver;
        element = driverWait
                .until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@value='YES' and @name='original_marriage_certificate']")));
        jse5.executeScript("arguments[0].click();", element);
        JavascriptExecutor jse6 = (JavascriptExecutor) driver;
        element = driverWait
                .until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@value='YES' and @name='marriage_certificate_in_english']")));
        jse6.executeScript("arguments[0].click();", element);
        new WebDriverWait(driver, Duration.ofSeconds(10)).until(
                ExpectedConditions.presenceOfElementLocated(By.xpath("//*[contains(text(), 'Next')]"))).click();
        //Reason
        JavascriptExecutor jse7 = (JavascriptExecutor) driver;
        element = driverWait
                .until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@value='live separate' and @name='divorce_reason']")));
        jse7.executeScript("arguments[0].click();", element);
        new WebDriverWait(driver, Duration.ofSeconds(10)).until(
                ExpectedConditions.presenceOfElementLocated(By.xpath("//*[contains(text(), 'Next')]"))).click();

    }
}
