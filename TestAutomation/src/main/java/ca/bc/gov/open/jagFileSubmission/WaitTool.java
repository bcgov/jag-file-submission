package ca.bc.gov.open.jagFileSubmission;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

public class WaitTool {

    /** Default wait time for an element. 7  seconds. */
    public static final int DEFAULT_WAIT_4_ELEMENT = 7;
    /** Default wait time for a page to be displayed.  12 seconds.
     * The average webpage load time is 6 seconds in 2012.
     * Based on your tests, please set this value.
     * "0" will nullify implicitlyWait and speed up a test. */
    public static final int DEFAULT_WAIT_4_PAGE = 12;




    /**
     * Wait for the element to be present in the DOM, and displayed on the page.
     * And returns the first WebElement using the given method.
     *
     * @param driver	The driver object to be used
     * @param by	selector to find the element
     * @param timeOutInSeconds	The time in seconds to wait until returning a failure
     *
     * @return WebElement	the first WebElement using the given method, or null (if the timeout is reached)
     */
    public static WebElement waitForElement(WebDriver driver, final By by, Duration timeOutInSeconds) {
        WebElement element;
        try {
            //To use WebDriverWait(), we would have to nullify implicitlyWait().
            //Because implicitlyWait time also set "driver.findElement()" wait time.
            //info from: https://groups.google.com/forum/?fromgroups=#!topic/selenium-users/6VO_7IXylgY
            driver.manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS); //nullify implicitlyWait()

            WebDriverWait wait = new WebDriverWait(driver, timeOutInSeconds);
            element = wait.until(ExpectedConditions.visibilityOfElementLocated(by));

            driver.manage().timeouts().implicitlyWait(DEFAULT_WAIT_4_PAGE, TimeUnit.SECONDS); //reset implicitlyWait
            return element; //return the element
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;

    }
    }
