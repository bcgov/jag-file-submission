package ca.bc.gov.open.jagFileSubmission;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WebDriverManager {

    public static WebDriverManager instance = null;

    // web driver objects
    private static WebDriver driver = null;
    private static WebDriverWait driverWait = null;
    private static WebElement element = null;
    private static List<WebElement> elements = null;
    private static Select select = null;

    /**
     * private ctor - initialize everything
     */
    private WebDriverManager() {
        driver = initDriver();
        Duration duration4 = Duration.ofSeconds(10);
        driverWait = (new WebDriverWait(driver,duration4));
        element = null;
        elements = null;
        select = null;
    }

    public static WebDriverManager getInstance() {
        if (instance == null) {
            instance = new WebDriverManager();
        }
        return instance;
    }

    @SuppressWarnings("deprecation")
    private static WebDriver initDriver() {

        if (Config.SELECTED_DRIVER.equals(Constants.CHROME_DRIVER)) {

            File file = new File("bin/chromedriver.exe");

            System.setProperty("webdriver.chrome.driver", file.getAbsolutePath());

            DesiredCapabilities capabilities = new DesiredCapabilities();

            Map<String,String> prefs = new HashMap<>();
            prefs.put("safebrowsing.enabled", "false"); // Bypass warning message, keep file anyway (for .exe, .jar, etc.)

            ChromeOptions options = new ChromeOptions();

            options.addArguments("test-type");
            options.setExperimentalOption("prefs", prefs);
            options.addArguments("start-maximized");
            options.addArguments("--remote-allow-origins=*");
            options.addArguments("--headless", "--disable-gpu", "--window-size=1920,1080","--ignore-certificate-errors","--no-sandbox", "--disable-dev-shm-usage");

            capabilities.setCapability("chrome.binary", file.getAbsolutePath());

            capabilities.setCapability(ChromeOptions.CAPABILITY, options);

            driver = new ChromeDriver(options);

        } else if (Config.SELECTED_DRIVER.equals(Constants.FIREFOX_DRIVER)) {

            FirefoxBinary firefoxBinary = new FirefoxBinary();
            firefoxBinary.addCommandLineOptions("--headless");
            File file = new File("bin/geckodriver.exe");
            System.setProperty("webdriver.gecko.driver", file.getAbsolutePath());
            FirefoxOptions firefoxOptions = new FirefoxOptions();
            firefoxOptions.setBinary(firefoxBinary);
            driver = new FirefoxDriver(firefoxOptions);
            driver.manage().window().maximize();

        } else if (Config.SELECTED_DRIVER.equals(Constants.IE_DRIVER)) {

            // driver = new InternetExplorerDriver();

            String service = "C:\\repo\\epbc-ui-test\\TestProject\\lib\\IEDriverServer.exe";
            System.setProperty("webdriver.ie.driver", service);

            // Create the DesiredCapability object of InternetExplorer
            //DesiredCapabilities capabilities = DesiredCapabilities.internetExplorer();

            // Settings to Accept the SSL Certificate in the Capability object
            //capabilities.setCapability(CapabilityType.ACCEPT_INSECURE_CERTS, true);

            InternetExplorerDriver driver = new InternetExplorerDriver();
            // driver.get("URL for which certificate error is coming");
            driver.get("https://tst-apply.educationplannerbc.ca/account/create" + "/account/login");

        } else if (Config.SELECTED_DRIVER.equals(Constants.EDGE_DRIVER)) {

            // Defining System Property for Edge
            File file = new File("bin/msedgedriver.exe");
            System.setProperty("webdriver.edge.driver", file.getAbsolutePath());

            driver = new EdgeDriver();
            driver.manage().window().maximize();

            // Initialize the EdgeOptions class
            EdgeOptions edgeOptions = new EdgeOptions();

            // Use the addArguments method for configuring headless
            // edgeOptions.addArguments("headless");

        }

        return driver;

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
