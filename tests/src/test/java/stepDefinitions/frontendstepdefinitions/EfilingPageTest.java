package stepDefinitions.frontendstepdefinitions;

import ca.bc.gov.open.jagefilingapi.qa.config.ReadConfig;
import ca.bc.gov.open.jagefilingapi.qa.frontend.pages.EfilingPage;
import ca.bc.gov.open.jagefilingapi.qa.frontend.pages.LandingPage;
import ca.bc.gov.open.jagefilingapi.qa.frontendutils.DriverClass;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;

import java.io.IOException;

public class EfilingPageTest extends DriverClass {

        ReadConfig readConfig;
        LandingPage landingPage;
        EfilingPage eFilingPage;

        @Before("@frontend")
        public void setUp() throws IOException {
            initializeDriver();
            log.info("Browser is initialized");
        }

        //@After ("@frontend")
        public void tearDown() {
            driver.quit();
            log.info("Browser closed");
        }
    }
