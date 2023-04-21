package ca.bc.gov.open.ui;

import ca.bc.gov.open.jagFileSubmission.CommonUtils;
import ca.bc.gov.open.jagFileSubmission.WebDriverManager;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Component;


import java.time.Duration;
@Component
public class LoginBCID {

    private WebDriver driver;

    @After
    public void tearDown() {
        driver.close();
        driver.quit();
    }

    @AfterClass
    public static void afterClass() {
        WebDriverManager.instance = null;
    }

    @Test
    public void test() throws Exception {
        driver = WebDriverManager.getDriver();
        WebDriverWait driverWait = WebDriverManager.getDriverWait();
        WebElement element = WebDriverManager.getElement();
        WebDriverManager.getElements();
        String  bceidUSERNAME = System.getenv("USERNAME_BCEID");
        String bceidPASSWORD = System.getenv("PASSWORD_BCEID");
        CommonUtils.login();

        new WebDriverWait(driver, Duration.ofSeconds(10)).until(
                ExpectedConditions.presenceOfElementLocated(By.xpath("//*[contains(text(), ' Login with my ')]"))).click();
        System.out.println("Login with BCID page loaded successfully");

        element = driverWait.until(ExpectedConditions.presenceOfElementLocated(By.id("user")));
        element.sendKeys(bceidUSERNAME);
        element = driverWait.until(ExpectedConditions.presenceOfElementLocated(By.id("password")));
        element.sendKeys(bceidPASSWORD);
        element = driverWait.until(ExpectedConditions.presenceOfElementLocated(By.name("btnSubmit")));
        element.click();
        //Login successfully
        new WebDriverWait(driver, Duration.ofSeconds(50)).until(
                ExpectedConditions.presenceOfElementLocated(By.xpath("//*[contains(text(), ' New Form ')]")));
        System.out.println("Login successfully");


    }
}
