package ca.bc.gov.open.jagFileSubmission;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerOptions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomWebDriverManager {

    public static CustomWebDriverManager instance = null;

    // web driver objects
    private static WebDriver driver = null;
    private static WebDriverWait driverWait = null;
    private static WebElement element = null;
    private static List<WebElement> elements = null;
    private static Select select = null;

    /**
     * private ctor - initialize everything
     */
    private CustomWebDriverManager() {
        driver = initDriver();
        Duration duration4 = Duration.ofSeconds(30);
        driverWait = (new WebDriverWait(driver,duration4));
        element = null;
        elements = null;
        select = null;
    }

    public static CustomWebDriverManager getInstance() {
        if (instance == null) {
            instance = new CustomWebDriverManager();
        }
        return instance;
    }

    @SuppressWarnings("deprecation")
    private static WebDriver initDriver() {

        if (Config.SELECTED_DRIVER.equals(Constants.CHROME_DRIVER)) {

            ChromeOptions options = getChromeOptions();
            WebDriverManager.chromedriver().setup();
            WebDriver driver = new ChromeDriver(options);
            driver.manage().window().maximize();
            return driver;

        } else if (Config.SELECTED_DRIVER.equals(Constants.FIREFOX_DRIVER)) {

            FirefoxOptions options = new FirefoxOptions();
//			options.addArguments("--headless");
            WebDriverManager.firefoxdriver().setup();
            WebDriver driver = new FirefoxDriver(options);
            driver.manage().window().maximize();
            return driver;

        } else if (Config.SELECTED_DRIVER.equals(Constants.IE_DRIVER)) {

            InternetExplorerOptions options = new InternetExplorerOptions();
            WebDriverManager.iedriver().setup();
            WebDriver driver = new InternetExplorerDriver(options);
            driver.manage().window().maximize();
            return driver;

        } else if (Config.SELECTED_DRIVER.equals(Constants.EDGE_DRIVER)) {

            EdgeOptions options = new EdgeOptions();
            // Use the addArguments method for configuring headless
            // options.addArguments("headless");
            WebDriverManager.iedriver().setup();
            WebDriver driver = new EdgeDriver(options);
            driver.manage().window().maximize();
            return driver;

        }

        return driver;

    }
    private static ChromeOptions getChromeOptions() {

        Map<String,String> prefs = new HashMap<>();
        prefs.put("safebrowsing.enabled", "false"); // Bypass warning message, keep file anyway (for .exe, .jar, etc.)

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-notifications"); //disables 3rd party notifications
        options.addArguments("disable-extensions"); //disables 3rd party extensions
        options.addArguments("--no-sandbox");
        options.addArguments("test-type");
        options.setExperimentalOption("prefs", prefs);
        options.addArguments("start-maximized");
        options.addArguments("--remote-allow-origins=*");
        //options.addArguments("--headless", "--disable-gpu", "--window-size=1920,1080","--ignore-certificate-errors","--no-sandbox", "--disable-dev-shm-usage");

        return options;
    }

    public static WebDriver getDriver() {
        return getInstance().driver;
    }

    public static WebDriverWait getDriverWait() {
        return getInstance().driverWait;
    }

    public static WebElement getElement() {
        return getInstance().element;
    }

    public static List<WebElement> getElements() {
        return getInstance().elements;
    }

    public static Select getSelect() {
        return getInstance().select;
    }

}