package ca.bc.gov.open.ui;

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


public class RegisterEdivorce {


    private WebDriver driver;
    private static String  bceidUSERNAME = System.getenv("USERNAME_BCEID");
    private static String bceidPASSWORD = System.getenv("PASSWORD_BCEID");
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

        EDivorcePreScreening register = new EDivorcePreScreening();
        register.test();
        Thread.sleep(1000);
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("window.scrollTo(0, document.body.scrollHeight)");
        element = driverWait
                .until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"login\"]/a")));
        element.click();

        element = driverWait.until(ExpectedConditions.presenceOfElementLocated(By.id("user")));
        element.sendKeys(bceidUSERNAME);
        element = driverWait.until(ExpectedConditions.presenceOfElementLocated(By.id("password")));
        element.sendKeys(bceidPASSWORD);
        element = driverWait.until(ExpectedConditions.presenceOfElementLocated(By.name("btnSubmit")));
        element.click();
        new WebDriverWait(driver, Duration.ofSeconds(60)).until(
                ExpectedConditions.presenceOfElementLocated(By.xpath("//*[contains(text(), 'Continue')]"))).click();

        }

    }
