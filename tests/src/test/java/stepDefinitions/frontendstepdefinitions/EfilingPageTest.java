package stepDefinitions.frontendstepdefinitions;

import ca.bc.gov.open.jagefilingapi.qa.frontendutils.DriverClass;
import io.cucumber.java.Before;

import java.io.IOException;

public class EfilingPageTest extends DriverClass {

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

