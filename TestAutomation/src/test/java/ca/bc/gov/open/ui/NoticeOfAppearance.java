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

public class NoticeOfAppearance {

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

        LoginBCID notice = new LoginBCID();
        notice.test();

        new WebDriverWait(driver, Duration.ofSeconds(10)).until(
                ExpectedConditions.presenceOfElementLocated(By.xpath("//*[contains(text(), ' Notice of Appearance (Form 2) ')]"))).click();

        element = driverWait.until(ExpectedConditions.presenceOfElementLocated(By.id("court-of-appeal-file-no")));
        element.sendKeys("CA46532");

        driverWait.until(
                ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"respondent\"]/div[1]/label/span"))).click();

        element = driverWait.until(ExpectedConditions.presenceOfElementLocated(By.id("first-name")));
        element.sendKeys("Blair");
        element = driverWait.until(ExpectedConditions.presenceOfElementLocated(By.id("last-name")));
        element.sendKeys("Greenwood");
        Thread.sleep(1000);
        // Scroll down till the bottom of the page
        JavascriptExecutor js =  (JavascriptExecutor) driver;
        js.executeScript("window.scrollBy(0,document.body.scrollHeight)");
        driver.findElement(By.xpath("//html")).click();
        new WebDriverWait(driver, Duration.ofSeconds(10)).until(
                ExpectedConditions.presenceOfElementLocated(By.className("btn-success"))).click();
        Thread.sleep(1000);
        element = driverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/div/main/div/div/div[4]/div/div/div/div[1]/div[2]/div[1]/select")));
        element.sendKeys("4 Pillars Consulting Group Inc.");
        Thread.sleep(1000);
        element = driverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/div/main/div/div/div[4]/div/div/div/div[1]/div[2]/div[2]/select")));
        element.sendKeys("Blair Greenwood");
        driver.findElement(By.cssSelector(".custom-control:nth-child(7) > .custom-control-label")).click();
        //text with address
        element = driverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/div/main/div/div/div[4]/div/div/div/div[2]/div[2]/div[2]/div/textarea")));
        element.sendKeys("Lorem ipsum dolor sit amet, consectetuer adipiscing elit. Aenean commodo ligula eget dolor. Aenean massa. Cum sociis natoque penatibus et magnis dis parturient montes, nascetur ridiculus mus. Donec quam felis, ultricies nec, pellentesque eu, pretium quis, sem. Nulla consequat massa quis enim. Donec pede justo, fringilla vel, aliquet nec, vulputate eget, arcu. In enim justo, rhoncus ut, imperdiet a, venenatis vi");
       //phone no
        element = driverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/div/main/div/div/div[4]/div/div/div/div[2]/div[3]/div[2]/div[1]/input")));
        element.sendKeys("999-999-9999");
        //email
        element = driverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/div/main/div/div/div[4]/div/div/div/div[2]/div[4]/div[2]/div/input")));
        element.sendKeys("test@test.ca");
        //name
        element = driverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/div/main/div/div/div[4]/div/div/div/div[2]/div[5]/div[2]/input")));
        element.sendKeys("Blair Greenwood");
        //Click Continue
        new WebDriverWait(driver, Duration.ofSeconds(10)).until(
                ExpectedConditions.presenceOfElementLocated(By.xpath("//*[contains(text(), 'Continue ')]"))).click();
        new WebDriverWait(driver, Duration.ofSeconds(10)).until(
                ExpectedConditions.presenceOfElementLocated(By.xpath("//*[contains(text(), 'OK')]"))).click();
        new WebDriverWait(driver, Duration.ofSeconds(10)).until(
                ExpectedConditions.presenceOfElementLocated(By.xpath("//*[contains(text(), 'Blair Greenwood')]")));
        new WebDriverWait(driver, Duration.ofSeconds(10)).until(
                ExpectedConditions.presenceOfElementLocated(By.xpath("//*[contains(text(), 'Lorem ipsum dolor sit amet, consectetuer adipiscing')]")));
        Thread.sleep(1500);
        new WebDriverWait(driver, Duration.ofSeconds(10)).until(
                ExpectedConditions.presenceOfElementLocated(By.xpath("//*[contains(text(), 'Proceed ')]"))).click();
        Thread.sleep(1500);
        new WebDriverWait(driver, Duration.ofSeconds(50)).until(
                ExpectedConditions.presenceOfElementLocated(By.className("btn-success"))).click();
        Thread.sleep(1000);
        new WebDriverWait(driver, Duration.ofSeconds(50)).until(
                ExpectedConditions.presenceOfElementLocated(By.xpath("//*[contains(text(), 'Continue')]"))).click();
        element = driverWait.until(ExpectedConditions.presenceOfElementLocated(By.id("agreeCallout")));
        element.click();
        new WebDriverWait(driver, Duration.ofSeconds(50)).until(
                ExpectedConditions.presenceOfElementLocated(By.xpath("//*[contains(text(), 'Submit')]"))).click();
        new WebDriverWait(driver, Duration.ofSeconds(50)).until(
                ExpectedConditions.presenceOfElementLocated(By.xpath("//*[contains(text(), ' Done ')]")));

    }
}
