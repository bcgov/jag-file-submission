package ca.bc.gov.open.jagefilingapi.qa.tests;

import ca.bc.gov.open.jagefilingapi.qa.pages.LandingPage;
import ca.bc.gov.open.jagefilingapi.qa.util.DriverClass;
import ca.bc.gov.open.jagefilingapi.qa.util.ReadConfig;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

public class LandingPageTest extends DriverClass {

    ReadConfig readConfig;
    LandingPage landingPage;

    @Before
    public void initialization() throws IOException {
        readConfig = new ReadConfig();

        initializeDriver();
        log.info("Browser is initialized");

        String url = readConfig.getBaseUrl();
        driver.get(url);
        log.info("Page is launched");
    }

    @After
    public void tearDown() {
        driver.quit();
        log.info("Browser closed");
    }

    @Test
    public void verifyLandingPageTitleTest() {
        landingPage = new LandingPage(driver);

        String actualTitle = landingPage.verifyLandingPageTitle();
        String expTitle = "eFiling Frontend";

        Assert.assertEquals(expTitle, actualTitle);
        log.info("Correct page title is verified.");
    }
}
