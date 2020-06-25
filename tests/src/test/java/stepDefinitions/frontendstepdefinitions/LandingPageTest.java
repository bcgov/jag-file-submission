package stepDefinitions.frontendstepdefinitions;

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

     LandingPage landingPage;
     EfilingPage eFilingPage;

    @Before ("@frontend")
    public void setUp() throws IOException {
        initializeDriver();
        log.info("Browser is initialized from the driver class");
    }

    @After ("@frontend")
    public void tearDown() {
        driver.close();
        driver.quit();
        log.info("Browser closed");
    }

    @Given("user is on the landing page")
    public void user_is_on_the_landing_page() throws IOException {
        landingPage = new LandingPage(driver);

        landingPage.getUrl();
        log.info("Landing page url is accessed successfully");

        String actualTitle = landingPage.verifyLandingPageTitle();
        String expectedTitle = "eFiling Demo Client";

        Assert.assertEquals(expectedTitle, actualTitle);
        log.info("Landing page title is verified");
    }

    @When("user enters a valid account guid {string}")
    public void user_enters_a_valid_account_guid(String accountGuid) {
        landingPage = new LandingPage(driver);
        landingPage.enterAccountGuid(accountGuid);
    }

    @Then("eFiling frontend page is displayed and cancel button exists")
    public void eFiling_frontend_page_is_displayed_and_cancel_button_exists() {
        landingPage = new LandingPage(driver);
        eFilingPage = new EfilingPage(driver);

        landingPage.clickGenerateUrlButton();
        log.info("Generate Url button in landing page is clicked");

        String newWindow = driver.getWindowHandle();
        driver.switchTo().window(newWindow);

        String actualTitle = eFilingPage.verifyEfilingPageTitle();
        String expTitle = "eFiling Frontend";

        Assert.assertEquals(expTitle, actualTitle);
        log.info("eFiling Frontend page title is verified");

        eFilingPage.verifyCancelButtonIsPresent();
    }

    @Then("error message is displayed")
    public void error_message_is_displayed() {
        landingPage = new LandingPage(driver);

        landingPage.clickGenerateUrlButton();
        log.info("Generate Url button in Efiling frontend page is clicked");

        String expMsg = "An error occurred while generating the URL. Please try again.";
        String actMsg =  landingPage.getErrorMessageText();
        Assert.assertEquals(actMsg, expMsg);

        log.info("Expected message is verified");
    }

    @When("user enters non existing account guid {string}")
    public void user_enters_non_existing_account_guid(String accountGuid) {

        landingPage.enterAccountGuid(accountGuid);
    }

    @When("user enters invalid account guid {string}")
    public void user_enters_invalid_account_guid(String accountGuid) {
        landingPage = new LandingPage(driver);

        landingPage.enterAccountGuid(accountGuid);
    }
}


