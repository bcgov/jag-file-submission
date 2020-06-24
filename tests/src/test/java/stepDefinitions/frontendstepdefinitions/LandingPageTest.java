package stepDefinitions.frontendstepdefinitions;

import ca.bc.gov.open.jagefilingapi.qa.config.ReadConfig;
import ca.bc.gov.open.jagefilingapi.qa.frontend.pages.EfilingPage;
import ca.bc.gov.open.jagefilingapi.qa.frontend.pages.LandingPage;
import ca.bc.gov.open.jagefilingapi.qa.frontendutils.DriverClass;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;

import java.io.IOException;

public class LandingPageTest extends DriverClass {

    ReadConfig readConfig;
    LandingPage landingPage;
    EfilingPage eFilingPage;

    @Before ("@frontend")
    public void setUp() throws IOException {
        initializeDriver();
        log.info("Browser is initialized");
    }

    @After ("@frontend")
    public void tearDown() {
        driver.quit();
        log.info("Browser closed");
    }

    @Given("user is on the landing page")
    public void user_is_on_the_landing_page() throws IOException {
        readConfig = new ReadConfig();
        landingPage = new LandingPage(driver);

        String url = readConfig.getBaseUrl();
        driver.get(url);
        log.info("Page is launched");

        String actualTitle = landingPage.verifyLandingPageTitle();
        String expTitle = "eFiling Frontend Demo App";

        Assert.assertEquals(expTitle, actualTitle);
        log.info("Landing page title is verified");
    }

    @When("user enters invalid account guid {string}")
    public void user_enters_invalid_account_guid(String accountGuid) {
        landingPage = new LandingPage(driver);

        landingPage.enterAccountGuid(accountGuid);
    }

    @Then("efiling page is displayed and cancel button exists")
    public void efiling_page_is_displayed_and_cancel_button_exists() {
        landingPage = new LandingPage(driver);
        eFilingPage = new EfilingPage(driver);

        landingPage.clickGenerateUrlButton();
        log.info("Generate Url button is clicked");

        String actualTitle = eFilingPage.verifyEfilingPageTitle();
        String expTitle = "eFiling Frontend";

        Assert.assertEquals(expTitle, actualTitle);
        log.info("Efiling page title is verified");

        eFilingPage.verifyCancelButtonIsPresent();
    }

    @Then("error message is displayed")
    public void error_message_is_displayed() {
        landingPage = new LandingPage(driver);

        landingPage.clickGenerateUrlButton();
        log.info("Generate Url button is clicked");

        String expMsg = "An error occurred while generating the URL. Please try again.";
        String actMsg =  landingPage.getErrorMessageText();
        Assert.assertEquals(actMsg, expMsg);

        log.info("Expected message is verified");
    }
}

