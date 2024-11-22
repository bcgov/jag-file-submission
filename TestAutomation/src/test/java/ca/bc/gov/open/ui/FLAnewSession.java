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
import java.util.Iterator;
import java.util.Set;

import static ca.bc.gov.open.jagFileSubmission.CustomWebDriverManager.getDriver;

public class FLAnewSession {

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

        FLAlogin login = new FLAlogin();
        login.test();

        new WebDriverWait(driver, Duration.ofSeconds(50)).until(
                ExpectedConditions.presenceOfElementLocated(By.xpath("//*[contains(text(), 'Begin NEW Session')]"))).click();
        new WebDriverWait(driver, Duration.ofSeconds(50)).until(
                ExpectedConditions.presenceOfElementLocated(By.xpath("//*[contains(text(), ' Apply for an order ')]"))).click();
        new WebDriverWait(driver, Duration.ofSeconds(50)).until(
                ExpectedConditions.presenceOfElementLocated(By.xpath("//*[contains(text(), ' Next ')]"))).click();
        new WebDriverWait(driver, Duration.ofSeconds(50)).until(
                ExpectedConditions.presenceOfElementLocated(By.xpath("//*[contains(text(), 'Family law matter')]"))).click();
        new WebDriverWait(driver, Duration.ofSeconds(50)).until(
                ExpectedConditions.presenceOfElementLocated(By.xpath("//*[contains(text(), ' Next ')]"))).click();
        new WebDriverWait(driver, Duration.ofSeconds(50)).until(
                ExpectedConditions.presenceOfElementLocated(By.xpath("//*[contains(text(), ' Continue to court process ')]"))).click();
        new WebDriverWait(driver, Duration.ofSeconds(50)).until(
                ExpectedConditions.presenceOfElementLocated(By.xpath("//*[contains(text(), ' Next ')]"))).click();
        Thread.sleep(1000);

        // Switch to pop-up window
        String parentWindowHandler = driver.getWindowHandle(); // Store your parent window
        String subWindowHandler = null;
        Set<String> handles = driver.getWindowHandles(); // get all window handles
        Iterator<String> iterator = handles.iterator();
        while (iterator.hasNext()) {
            subWindowHandler = iterator.next();
        }
        driver.switchTo().window(subWindowHandler); // switch to popup window
        driverWait
                .until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/div[2]/div[1]/div/div/footer/button"))).click();
        System.out.println("Submit in pop-up clicked");
        driver.switchTo().window(parentWindowHandler); // switch back to parent window

        Thread.sleep(1000);
        new WebDriverWait(driver, Duration.ofSeconds(50)).until(
                ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/div/main/div/main/div[2]/div/div/div[1]/form/div[2]/div/div/div[2]/div/div/div/div/div[3]/div/div/div/div[2]/div[2]/div/label[2]/span"))).click();
        new WebDriverWait(driver, Duration.ofSeconds(50)).until(
                ExpectedConditions.presenceOfElementLocated(By.xpath("//*[contains(text(), ' Next ')]"))).click();
        element = driverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/div/main/div/main/div[2]/div/div/div[1]/form/div[2]/div[2]/div/div[2]/div/div/div/div/div[3]/div[1]/div/div/div[2]/div[2]/div/div/div[1]/input")));
        element.sendKeys("FirstNameTest");
        element = driverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/div/main/div/main/div[2]/div/div/div[1]/form/div[2]/div[2]/div/div[2]/div/div/div/div/div[3]/div[1]/div/div/div[2]/div[2]/div/div/div[3]/input")));
        element.sendKeys("LastNameTest");

        //DOB
        Thread.sleep(1000);
        element = driverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/div/main/div/main/div[2]/div/div/div[1]/form/div[2]/div[2]/div/div[2]/div/div/div/div/div[3]/div[2]/div/div/div[2]/div[2]/div/div/div/select[1]")));
        element.sendKeys("2005");
        Thread.sleep(1000);
        element = driverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/div/main/div/main/div[2]/div/div/div[1]/form/div[2]/div[2]/div/div[2]/div/div/div/div/div[3]/div[2]/div/div/div[2]/div[2]/div/div/div/select[2]")));
        element.sendKeys("May");
        Thread.sleep(1000);
        element = driverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/div/main/div/main/div[2]/div/div/div[1]/form/div[2]/div[2]/div/div[2]/div/div/div/div/div[3]/div[2]/div/div/div[2]/div[2]/div/div/div/select[3]")));
        element.sendKeys("3");
        Thread.sleep(1000);

        new WebDriverWait(driver, Duration.ofSeconds(50)).until(
                ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/div/main/div/main/div[2]/div/div/div[1]/form/div[2]/div[2]/div/div[2]/div/div/div/div/div[3]/div[4]/div/div/div[2]/div[2]/div/label[2]/span"))).click();
        new WebDriverWait(driver, Duration.ofSeconds(50)).until(
                ExpectedConditions.presenceOfElementLocated(By.xpath("//*[contains(text(), 'Continue')]"))).click();

        //Address
        element = driverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/div/main/div/main/div[2]/div/div/div[1]/form/div[2]/div[2]/div/div[2]/div/div/div/div/div[3]/div[8]/div/div/div/div/div[3]/div[1]/div/div/div[2]/div[2]/div/div[1]/div/input")));
        element.sendKeys("3220 Qua");
        element = driverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/div/main/div/main/div[2]/div/div/div[1]/form/div[2]/div[2]/div/div[2]/div/div/div/div/div[3]/div[8]/div/div/div/div/div[3]/div[1]/div/div/div[2]/div[2]/div/div[2]/div[1]/input")));
        element.sendKeys("Victoria");
        element = driverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/div/main/div/main/div[2]/div/div/div[1]/form/div[2]/div[2]/div/div[2]/div/div/div/div/div[3]/div[8]/div/div/div/div/div[3]/div[1]/div/div/div[2]/div[2]/div/div[3]/div[2]/input")));
        element.sendKeys("V8X 1G3");
        element = driverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/div/main/div/main/div[2]/div/div/div[1]/form/div[2]/div[2]/div/div[2]/div/div/div/div/div[3]/div[8]/div/div/div/div/div[3]/div[2]/div/div/div[2]/div[2]/div/div/div[1]/input")));
        element.sendKeys("9999999999");
        element = driverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/div/main/div/main/div[2]/div/div/div[1]/form/div[2]/div[2]/div/div[2]/div/div/div/div/div[3]/div[8]/div/div/div/div/div[3]/div[2]/div/div/div[2]/div[2]/div/div/div[2]/input")));
        element.sendKeys("test@test.com");

        new WebDriverWait(driver, Duration.ofSeconds(50)).until(
                ExpectedConditions.presenceOfElementLocated(By.xpath("//*[contains(text(), ' Next ')]"))).click();
        new WebDriverWait(driver, Duration.ofSeconds(50)).until(
                ExpectedConditions.presenceOfElementLocated(By.xpath("//*[contains(text(), '+Add Other Party')]"))).click();

        //Add child

        element = driverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/div/main/div/main/div[2]/div/div/div/div/div[2]/div/div[1]/form/div[2]/div[2]/div/div[2]/div/div/div[2]/div[2]/div/div/div[1]/input")));
        element.sendKeys("FirstNameTest");
        element = driverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/div/main/div/main/div[2]/div/div/div/div/div[2]/div/div[1]/form/div[2]/div[2]/div/div[2]/div/div/div[2]/div[2]/div/div/div[3]/input")));
        element.sendKeys("LastNameTest");

        new WebDriverWait(driver, Duration.ofSeconds(50)).until(
                ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/div/main/div/main/div[2]/div/div/div/div/div[2]/div/div[1]/form/div[2]/div[2]/div/div[3]/div/div/div[2]/div[2]/div/label[2]/span"))).click();

        element = driverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/div/main/div/main/div[2]/div/div/div/div/div[2]/div/div[1]/form/div[2]/div[2]/div/div[6]/div/div/div[2]/div[2]/div/div[1]/div/input")));
        element.sendKeys("3220 Qua");
        element = driverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/div/main/div/main/div[2]/div/div/div/div/div[2]/div/div[1]/form/div[2]/div[2]/div/div[6]/div/div/div[2]/div[2]/div/div[2]/div[1]/input")));
        element.sendKeys("Victoria");
        element = driverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/div/main/div/main/div[2]/div/div/div/div/div[2]/div/div[1]/form/div[2]/div[2]/div/div[6]/div/div/div[2]/div[2]/div/div[3]/div[2]/input")));
        element.sendKeys("V8X 1G3");
        element = driverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/div/main/div/main/div[2]/div/div/div/div/div[2]/div/div[1]/form/div[2]/div[2]/div/div[8]/div/div/div[2]/div[2]/div/div/div[1]/input")));
        element.sendKeys("9999999999");
        element = driverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/div/main/div/main/div[2]/div/div/div/div/div[2]/div/div[1]/form/div[2]/div[2]/div/div[8]/div/div/div[2]/div[2]/div/div/div[2]/input")));
        element.sendKeys("test@test.com");

        new WebDriverWait(driver, Duration.ofSeconds(50)).until(
                ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/div/main/div/main/div[2]/div/div/div/div/div[2]/div/div[2]/div[2]/button"))).click();
        new WebDriverWait(driver, Duration.ofSeconds(50)).until(
                ExpectedConditions.presenceOfElementLocated(By.xpath("//*[contains(text(), ' Next ')]"))).click();

        FLAnewSession understand = new FLAnewSession();
        understand.IUnderstand(element, driverWait, driver);

        new WebDriverWait(driver, Duration.ofSeconds(50)).until(
                ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/div/main/div/main/div[2]/div/div/div[1]/form/div[2]/div/div/div[2]/div/div/div/div/div[3]/div[1]/div/div/div[2]/div[2]/div/label[2]/span"))).click();
        new WebDriverWait(driver, Duration.ofSeconds(50)).until(
                ExpectedConditions.presenceOfElementLocated(By.xpath("//*[contains(text(), 'Continue')]"))).click();
        //Court loc
        element = driverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/div/main/div/main/div[2]/div/div/div[1]/form/div[2]/div/div/div[2]/div/div/div/div/div[3]/div[6]/div/div/div[2]/div[2]/div[1]/select")));
        element.sendKeys("Victoria Law Courts");
        JavascriptExecutor jse2 = (JavascriptExecutor) driver;
        element = driverWait
                .until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@value='It is the court location closest to where I live because my case does not involve a child-related issue' and @type='radio']")));
        jse2.executeScript("arguments[0].click();", element);

        FLAnewSession understandAgain = new FLAnewSession();
        understandAgain.IUnderstandAgain(element, driverWait, driver);
        Thread.sleep(1000);
        new WebDriverWait(driver, Duration.ofSeconds(50)).until(
                ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/div/main/div/main/div[2]/div/div/div[1]/form/div[2]/div/div/div[2]/div/div/div/div/div[3]/div[10]/div/div/div/div/div[3]/div[2]/div/div/div[2]/div[2]/div/label[2]/span"))).click();
        new WebDriverWait(driver, Duration.ofSeconds(50)).until(
                ExpectedConditions.presenceOfElementLocated(By.xpath("//*[contains(text(), ' Next ')]"))).click();
        new WebDriverWait(driver, Duration.ofSeconds(50)).until(
                ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/div/main/div/main/div[2]/div/div/div[1]/div/div[3]/fieldset/div/div/div[5]/div/label/div"))).click();
        new WebDriverWait(driver, Duration.ofSeconds(50)).until(
                ExpectedConditions.presenceOfElementLocated(By.xpath("//*[contains(text(), ' Next ')]"))).click();

        //Declaration
        element = driverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/div/main/div/main/div[2]/div/div/div[1]/form/div[2]/div[2]/div/div[2]/div/div/div/div/div[3]/div[1]/div/div/div[2]/div[2]/div/label[2]/span")));
        element.click();
        element = driverWait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/div/main/div/main/div[2]/div/div/div[1]/form/div[2]/div[2]/div/div[2]/div/div/div/div/div[3]/div[2]/div/div/div[2]/div[2]/div/label[2]/span")));
        element.click();

        new WebDriverWait(driver, Duration.ofSeconds(50)).until(
                ExpectedConditions.presenceOfElementLocated(By.xpath("//*[contains(text(), ' Next ')]"))).click();
        new WebDriverWait(driver, Duration.ofSeconds(50)).until(
                ExpectedConditions.presenceOfElementLocated(By.xpath("//*[contains(text(), ' Next ')]"))).click();
        Thread.sleep(2000);
        new WebDriverWait(driver, Duration.ofSeconds(50)).until(
                ExpectedConditions.presenceOfElementLocated(By.xpath("//*[contains(text(), ' Next ')]"))).click();
        JavascriptExecutor jse3 = (JavascriptExecutor) driver;
        element = driverWait
                .until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@value='byefiling' and @type='radio']")));
        jse3.executeScript("arguments[0].click();", element);
        new WebDriverWait(driver, Duration.ofSeconds(50)).until(
                ExpectedConditions.presenceOfElementLocated(By.xpath("//*[contains(text(), ' Next ')]"))).click();
        new WebDriverWait(driver, Duration.ofSeconds(50)).until(
                ExpectedConditions.presenceOfElementLocated(By.xpath("//*[contains(text(), ' Review and Print ')]"))).click();

        new WebDriverWait(driver, Duration.ofSeconds(50)).until(
                ExpectedConditions.presenceOfElementLocated(By.xpath("//*[contains(text(), ' Proceed to Submit ')]"))).click();
        new WebDriverWait(driver, Duration.ofSeconds(50)).until(
                ExpectedConditions.presenceOfElementLocated(By.xpath("//*[contains(text(), 'Continue')]"))).click();
        element = driverWait.until(ExpectedConditions.presenceOfElementLocated(By.id("agreeCallout")));
        element.click();
        new WebDriverWait(driver, Duration.ofSeconds(50)).until(
                ExpectedConditions.presenceOfElementLocated(By.xpath("//*[contains(text(), 'Submit')]"))).click();
        //Success
        new WebDriverWait(driver, Duration.ofSeconds(50)).until(
                ExpectedConditions.presenceOfElementLocated(By.xpath("//*[contains(text(), 'Success')]")));
        //Check File No
        WebElement e = driver.findElement(
                By.xpath("/html/body/div/main/div/div/div/div[2]/div[8]/span/strong"));
        String actualpackageID = e.getText();

        System.out.println("packageID: " + actualpackageID);

        new WebDriverWait(driver, Duration.ofSeconds(10)).until(
                ExpectedConditions.presenceOfElementLocated(By.xpath("//*[contains(text(), 'Back to the Application Page')]"))).click();
        new WebDriverWait(driver, Duration.ofSeconds(50)).until(
                ExpectedConditions.presenceOfElementLocated(By.xpath("//*[contains(text(), 'Previous Activity')]")));
        new WebDriverWait(driver, Duration.ofSeconds(50)).until(
                ExpectedConditions.presenceOfElementLocated(By.xpath("//*[contains(text(), '" + actualpackageID + "')]")));

    }

    public void IUnderstand(WebElement element, WebDriverWait driverWait, WebDriver driver) throws Exception {

        String parentWindowHandler = driver.getWindowHandle(); // Store your parent window
        String subWindowHandler = null;
        Set<String> handles = driver.getWindowHandles(); // get all window handles
        Iterator<String> iterator = handles.iterator();
        while (iterator.hasNext()) {
            subWindowHandler = iterator.next();
        }
        driver.switchTo().window(subWindowHandler); // switch to popup window
        driverWait
                .until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/div[2]/div[1]/div/div/footer/button[2]"))).click();
        System.out.println("I understand clicked");
        driver.switchTo().window(parentWindowHandler); // switch back to parent window

        Thread.sleep(1000);
    }

    public void IUnderstandAgain(WebElement element, WebDriverWait driverWait, WebDriver driver) throws Exception {

        String parentWindowHandler = driver.getWindowHandle(); // Store your parent window
        String subWindowHandler = null;
        Set<String> handles = driver.getWindowHandles(); // get all window handles
        Iterator<String> iterator = handles.iterator();
        while (iterator.hasNext()) {
            subWindowHandler = iterator.next();
        }
        driver.switchTo().window(subWindowHandler); // switch to popup window
        driverWait
                .until(ExpectedConditions.presenceOfElementLocated(By.xpath("/html/body/div[2]/div[1]/div/div/footer/button"))).click();
        System.out.println("I understand new clicked");
        driver.switchTo().window(parentWindowHandler); // switch back to parent window

        Thread.sleep(1000);
    }
}
