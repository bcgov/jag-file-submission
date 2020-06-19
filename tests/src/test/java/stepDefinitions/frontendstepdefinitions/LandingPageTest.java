package stepDefinitions.frontendstepdefinitions;

import ca.bc.gov.open.jagefilingapi.qa.config.ReadConfig;
import ca.bc.gov.open.jagefilingapi.qa.frontend.pages.LandingPage;
import ca.bc.gov.open.jagefilingapi.qa.frontendutils.DriverClass;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import org.junit.Assert;
import java.io.IOException;

public class LandingPageTest extends DriverClass {

    ReadConfig readConfig;
    LandingPage landingPage;

    @Given("browser is initialized")
    public void browser_is_initialized() throws IOException {
        initializeDriver();
        log.info("Browser is initialized");
    }

    @Given("user is navigated to the landing page")
    public void user_is_navigated_to_the_landing_page() throws IOException {
        readConfig = new ReadConfig();

        String url = readConfig.getBaseUrl();
        driver.get(url);
        log.info("Page is launched");
    }

    @When("page title is verified")
    public void page_title_is_verified() {
        landingPage = new LandingPage(driver);

        String actualTitle = landingPage.verifyLandingPageTitle();
        String expTitle = "eFiling Frontend";

        Assert.assertEquals(expTitle, actualTitle);
        log.info("Correct page title is verified.");
    }

    @And("browser is closed")
    public void browser_is_closed() {
        driver.quit();
        log.info("Browser closed");
    }
}
