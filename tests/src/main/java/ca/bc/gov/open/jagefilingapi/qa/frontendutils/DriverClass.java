package ca.bc.gov.open.jagefilingapi.qa.frontendutils;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.PageLoadStrategy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class DriverClass {

        public WebDriver driver;
        public Logger log = LogManager.getLogger(DriverClass.class);

        public void initializeDriver() {
            String browser = System.getProperty("BROWSER");

            if(StringUtils.equals("firefox", browser)) {
                WebDriverManager.firefoxdriver().setup();
                driver = new FirefoxDriver();
            } else {
                WebDriverManager.chromedriver().setup();
                ChromeOptions options = new ChromeOptions();
                options.addArguments("enable-automation");
                options.addArguments("--no-sandbox");
                options.addArguments("--disable-dev-shm-usage");
                options.addArguments("--headless");
                options.addArguments("--window-size=1920,1080");
                options.addArguments("--disable-extensions");
                options.addArguments("--dns-prefetch-disable");
                options.addArguments("--disable-gpu");
                options.setPageLoadStrategy(PageLoadStrategy.NONE);
                driver = new ChromeDriver(options);
            }

        }

        public void  driverSetUp() {
            initializeDriver();
            driver.manage().timeouts().implicitlyWait(120, TimeUnit.SECONDS);
            driver.manage().timeouts().setScriptTimeout(120, TimeUnit.SECONDS);
            driver.manage().deleteAllCookies();
        }

}
