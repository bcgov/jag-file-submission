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


public class CompleteDivorceQuestionnaire {

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

        RegisterEdivorce questionnaire = new RegisterEdivorce();
        questionnaire.test();

        new WebDriverWait(driver, Duration.ofSeconds(60)).until(
                ExpectedConditions.presenceOfElementLocated(By.xpath("//*[contains(text(), 'What Are You Asking For?')]"))).click();
        new WebDriverWait(driver, Duration.ofSeconds(60)).until(
                ExpectedConditions.presenceOfElementLocated(By.xpath("//*[contains(text(), 'Continue')]"))).click();
        JavascriptExecutor jse = (JavascriptExecutor) driver;
        element = driverWait
                .until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@value='A legal end to the marriage' and @name='want_which_orders']")));
        jse.executeScript("arguments[0].click();", element);
        new WebDriverWait(driver, Duration.ofSeconds(10)).until(
                ExpectedConditions.presenceOfElementLocated(By.xpath("//*[contains(text(), 'Next')]"))).click();
        JavascriptExecutor jse1 = (JavascriptExecutor) driver;
        element = driverWait
                .until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@value='A legal end to the marriage' and @type='checkbox']")));
        jse1.executeScript("arguments[0].click();", element);
        new WebDriverWait(driver, Duration.ofSeconds(10)).until(
                ExpectedConditions.presenceOfElementLocated(By.xpath("//*[contains(text(), 'Next')]"))).click();
        new WebDriverWait(driver, Duration.ofSeconds(60)).until(
                ExpectedConditions.presenceOfElementLocated(By.xpath("//*[contains(text(), 'Continue')]"))).click();
        element = driverWait.until(ExpectedConditions.presenceOfElementLocated(By.name("given_name_1_you")));
        element.clear();
        element = driverWait.until(ExpectedConditions.presenceOfElementLocated(By.name("given_name_1_you")));
        element.sendKeys("FistNameTest");
        element = driverWait.until(ExpectedConditions.presenceOfElementLocated(By.name("last_name_you")));
        element.clear();
        element = driverWait.until(ExpectedConditions.presenceOfElementLocated(By.name("last_name_you")));
        element.sendKeys("LastNameTest");
        JavascriptExecutor jse2 = (JavascriptExecutor) driver;
        element = driverWait
                .until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@value='NO' and @name='any_other_name_you']")));
        jse2.executeScript("arguments[0].click();", element);
        element = driverWait.until(ExpectedConditions.presenceOfElementLocated(By.name("last_name_born_you")));
        element.clear();
        element = driverWait.until(ExpectedConditions.presenceOfElementLocated(By.name("last_name_born_you")));
        element.sendKeys("FistNameTest");
        element = driverWait.until(ExpectedConditions.presenceOfElementLocated(By.name("last_name_before_married_you")));
        element.clear();
        element = driverWait.until(ExpectedConditions.presenceOfElementLocated(By.name("last_name_before_married_you")));
        element.sendKeys("LastNameTest");
        element = driverWait.until(ExpectedConditions.presenceOfElementLocated(By.id("birth_date")));
        element.clear();
        element = driverWait.until(ExpectedConditions.presenceOfElementLocated(By.id("birth_date")));
        element.sendKeys("11/20/2000");
        element = driverWait.until(ExpectedConditions.presenceOfElementLocated(By.name("occupation_you")));
        element.clear();
        element = driverWait.until(ExpectedConditions.presenceOfElementLocated(By.name("occupation_you")));
        element.sendKeys("EmployeeTest");
        JavascriptExecutor jse3 = (JavascriptExecutor) driver;
        element = driverWait
                .until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@value='Since birth' and @type='radio']")));
        jse3.executeScript("arguments[0].click();", element);
        Thread.sleep(1000);
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].scrollIntoView();", element);

        element = driver.findElement(By.xpath("/html/body/div[3]/div[2]/div/div[10]/a[2]"));
        element.click();
        new WebDriverWait(driver, Duration.ofSeconds(60)).until(
                ExpectedConditions.presenceOfElementLocated(By.xpath("//*[contains(text(), 'Continue')]"))).click();

        CompleteDivorceQuestionnaire spouseDetails = new CompleteDivorceQuestionnaire();
        spouseDetails.spouseDetails(element, driverWait, driver);

        CompleteDivorceQuestionnaire marriageDetails = new CompleteDivorceQuestionnaire();
        marriageDetails.marriage(element, driverWait, driver);

        //Separation
//        driverWait
//                .until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@name='no_collusion' and @value='I agree']"))).click();
//        Thread.sleep(1000);
//        driverWait
//                .until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@name='no_reconciliation_possible' and @type='checkbox']"))).click();
//        Thread.sleep(1000);
        new WebDriverWait(driver, Duration.ofSeconds(10)).until(
                ExpectedConditions.presenceOfElementLocated(By.linkText("Next"))).click();
        Thread.sleep(1000);
        CompleteDivorceQuestionnaire otherInfo = new CompleteDivorceQuestionnaire();
        otherInfo.otherQuestions(element, driverWait, driver);

        //Filing Location
        element = driverWait.until(ExpectedConditions.presenceOfElementLocated(By.name("court_registry_for_filing")));
        element.sendKeys("Kelowna");
        new WebDriverWait(driver, Duration.ofSeconds(10)).until(
                ExpectedConditions.presenceOfElementLocated(By.linkText("Next"))).click();
        new WebDriverWait(driver, Duration.ofSeconds(60)).until(
                ExpectedConditions.presenceOfElementLocated(By.xpath("//*[contains(text(), 'Continue')]"))).click();
        //Review Your Answers
        new WebDriverWait(driver, Duration.ofSeconds(10)).until(
                ExpectedConditions.presenceOfElementLocated(By.linkText("Next"))).click();


    }
    public void spouseDetails(WebElement element, WebDriverWait driverWait, WebDriver driver) throws Exception {

        element = driverWait.until(ExpectedConditions.presenceOfElementLocated(By.name("given_name_1_spouse")));
        element.clear();
        element = driverWait.until(ExpectedConditions.presenceOfElementLocated(By.name("given_name_1_spouse")));
        element.sendKeys("FistNameSpouseTest");
        element = driverWait.until(ExpectedConditions.presenceOfElementLocated(By.name("last_name_spouse")));
        element.clear();
        element = driverWait.until(ExpectedConditions.presenceOfElementLocated(By.name("last_name_spouse")));
        element.sendKeys("LastNameSpouseTest");
        JavascriptExecutor jse2 = (JavascriptExecutor) driver;
        element = driverWait
                .until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@value='NO' and @name='any_other_name_spouse']")));
        jse2.executeScript("arguments[0].click();", element);
        element = driverWait.until(ExpectedConditions.presenceOfElementLocated(By.name("last_name_born_spouse")));
        element.clear();
        element = driverWait.until(ExpectedConditions.presenceOfElementLocated(By.name("last_name_born_spouse")));
        element.sendKeys("FistNameSTest");
        Thread.sleep(1000);
        element = driverWait.until(ExpectedConditions.presenceOfElementLocated(By.name("last_name_before_married_spouse")));
        element.clear();
        element = driverWait.until(ExpectedConditions.presenceOfElementLocated(By.name("last_name_before_married_spouse")));
        element.sendKeys("LastNameSTest");
        Thread.sleep(1000);
        element = driverWait.until(ExpectedConditions.presenceOfElementLocated(By.id("birth_date")));
        element.clear();
        element = driverWait.until(ExpectedConditions.presenceOfElementLocated(By.id("birth_date")));
        element.sendKeys("11/20/2000");
        element = driverWait.until(ExpectedConditions.presenceOfElementLocated(By.name("occupation_spouse")));
        element.clear();
        element = driverWait.until(ExpectedConditions.presenceOfElementLocated(By.name("occupation_spouse")));
        element.sendKeys("EmployeeTest");
        JavascriptExecutor jse3 = (JavascriptExecutor) driver;
        element = driverWait
                .until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@value='Since birth' and @type='radio']")));
        jse3.executeScript("arguments[0].click();", element);
        Thread.sleep(1000);
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].scrollIntoView();", element);
        new WebDriverWait(driver, Duration.ofSeconds(10)).until(
                ExpectedConditions.presenceOfElementLocated(By.linkText("Next"))).click();
        new WebDriverWait(driver, Duration.ofSeconds(60)).until(
                ExpectedConditions.presenceOfElementLocated(By.xpath("//*[contains(text(), 'Continue')]"))).click();
    }

    public void marriage(WebElement element, WebDriverWait driverWait, WebDriver driver) throws Exception {

        element = driverWait.until(ExpectedConditions.presenceOfElementLocated(By.name("when_were_you_married")));
        element.clear();
        element = driverWait.until(ExpectedConditions.presenceOfElementLocated(By.name("when_were_you_married")));
        element.sendKeys("11/20/2010");
        element = driverWait.until(ExpectedConditions.presenceOfElementLocated(By.name("when_were_you_live_married_like")));
        element.clear();
        element = driverWait.until(ExpectedConditions.presenceOfElementLocated(By.name("when_were_you_live_married_like")));
        element.sendKeys("11/20/2009");
        element = driverWait.until(ExpectedConditions.presenceOfElementLocated(By.name("where_were_you_married_city")));
        element.clear();
        element = driverWait.until(ExpectedConditions.presenceOfElementLocated(By.name("where_were_you_married_city")));
        element.sendKeys("Kelowna");
        element = driverWait.until(ExpectedConditions.presenceOfElementLocated(By.name("where_were_you_married_prov")));
        element.clear();
        element = driverWait.until(ExpectedConditions.presenceOfElementLocated(By.name("where_were_you_married_prov")));
        element.sendKeys("BC");

        JavascriptExecutor jse3 = (JavascriptExecutor) driver;
        element = driverWait
                .until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@name='marital_status_before_you' and @type='radio']")));
        jse3.executeScript("arguments[0].click();", element);
        JavascriptExecutor jse4 = (JavascriptExecutor) driver;
        element = driverWait
                .until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@name='marital_status_before_spouse' and @type='radio']")));
        jse4.executeScript("arguments[0].click();", element);
        Thread.sleep(1000);
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("arguments[0].scrollIntoView();", element);
        new WebDriverWait(driver, Duration.ofSeconds(10)).until(
                ExpectedConditions.presenceOfElementLocated(By.linkText("Next"))).click();
        Thread.sleep(1000);
        new WebDriverWait(driver, Duration.ofSeconds(60)).until(
                ExpectedConditions.presenceOfElementLocated(By.xpath("//*[contains(text(), 'Continue')]"))).click();
    }

    public void otherQuestions(WebElement element, WebDriverWait driverWait, WebDriver driver) throws Exception {

        element = driverWait.until(ExpectedConditions.presenceOfElementLocated(By.name("address_to_send_official_document_street_you")));
        element.clear();
        element = driverWait.until(ExpectedConditions.presenceOfElementLocated(By.name("address_to_send_official_document_street_you")));
        element.sendKeys("3220");
        element = driverWait.until(ExpectedConditions.presenceOfElementLocated(By.name("address_to_send_official_document_city_you")));
        element.clear();
        element = driverWait.until(ExpectedConditions.presenceOfElementLocated(By.name("address_to_send_official_document_city_you")));
        element.sendKeys("Kelowna");
        element = driverWait.until(ExpectedConditions.presenceOfElementLocated(By.name("address_to_send_official_document_prov_you")));
        element.clear();
        element = driverWait.until(ExpectedConditions.presenceOfElementLocated(By.name("address_to_send_official_document_prov_you")));
        element.sendKeys("BC");

        JavascriptExecutor jse4 = (JavascriptExecutor) driver;
        element = driverWait
                .until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@name='address_to_send_official_document_country_you' and @type='radio']")));
        jse4.executeScript("arguments[0].click();", element);
        Thread.sleep(1000);

        //Spouse info
        element = driverWait.until(ExpectedConditions.presenceOfElementLocated(By.name("address_to_send_official_document_street_spouse")));
        element.clear();
        element = driverWait.until(ExpectedConditions.presenceOfElementLocated(By.name("address_to_send_official_document_street_spouse")));
        element.sendKeys("3220");
        element = driverWait.until(ExpectedConditions.presenceOfElementLocated(By.name("address_to_send_official_document_city_spouse")));
        element.clear();
        element = driverWait.until(ExpectedConditions.presenceOfElementLocated(By.name("address_to_send_official_document_city_spouse")));
        element.sendKeys("Kelowna");
        element = driverWait.until(ExpectedConditions.presenceOfElementLocated(By.name("address_to_send_official_document_prov_spouse")));
        element.clear();
        element = driverWait.until(ExpectedConditions.presenceOfElementLocated(By.name("address_to_send_official_document_prov_spouse")));
        element.sendKeys("BC");

        JavascriptExecutor jse31 = (JavascriptExecutor) driver;
        element = driverWait
                .until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@name='address_to_send_official_document_country_spouse' and @type='radio']")));
        jse31.executeScript("arguments[0].click();", element);
        JavascriptExecutor jse41 = (JavascriptExecutor) driver;
        element = driverWait
                .until(ExpectedConditions.elementToBeClickable(By.xpath("//input[@name='divorce_take_effect_on' and @type='radio']")));
        jse41.executeScript("arguments[0].click();", element);
        Thread.sleep(1000);
        JavascriptExecutor js1 = (JavascriptExecutor) driver;
        js1.executeScript("arguments[0].scrollIntoView();", element);
        new WebDriverWait(driver, Duration.ofSeconds(10)).until(
                ExpectedConditions.presenceOfElementLocated(By.linkText("Next"))).click();
        new WebDriverWait(driver, Duration.ofSeconds(60)).until(
                ExpectedConditions.presenceOfElementLocated(By.xpath("//*[contains(text(), 'Continue')]"))).click();
    }

}
