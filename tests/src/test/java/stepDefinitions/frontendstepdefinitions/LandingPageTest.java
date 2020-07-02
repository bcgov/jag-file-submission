package stepDefinitions.frontendstepdefinitions;

import ca.bc.gov.open.jagefilingapi.qa.frontend.pages.EfilingPage;
import ca.bc.gov.open.jagefilingapi.qa.frontend.pages.LandingPage;
import ca.bc.gov.open.jagefilingapi.qa.frontendutils.DriverClass;
import ca.bc.gov.open.jagefilingapi.qa.frontendutils.JsonReader;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;

import java.io.IOException;
import java.text.ParseException;

public class LandingPageTest extends DriverClass {

     LandingPage landingPage;
     EfilingPage eFilingPage;

    //DataProvider
    public static Object[][] accountData() throws IOException {
        return JsonReader.getData("src/test/java/testdatasource/account-data.json", "accountGuid", 1, 3);
    }

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
    public void userIsOnTheLandingPage() throws IOException {
        landingPage = new LandingPage(driver);

        landingPage.getUrl();
        log.info("Landing page url is accessed successfully");

        String actualTitle = landingPage.verifyLandingPageTitle();
        String expectedTitle = "eFiling Demo Client";

        Assert.assertEquals(expectedTitle, actualTitle);
        log.info("Landing page title is verified");
    }

    @When("user enters a valid account guid {string}")
    public void userEntersAValidAccountGuid(String validGuid) throws IOException {
        landingPage = new LandingPage(driver);
        validGuid = (String) accountData()[0][0];
        landingPage.enterAccountGuid(validGuid);
    }

    @Then("eFiling frontend page is displayed and cancel button exists")
    public void eFilingFrontendPageIsDisplayedAndCancelButtonExists() {
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
    public void errorMessageIsDisplayed() {
        landingPage = new LandingPage(driver);

        landingPage.clickGenerateUrlButton();
        log.info("Generate Url button in Efiling frontend page is clicked");

        String expMsg = "An error occurred while generating the URL. Please try again.";
        String actMsg =  landingPage.getErrorMessageText();
        Assert.assertEquals(actMsg, expMsg);

        log.info("Expected message is verified");
    }

    @When("user enters non existing account guid {string}")
    public void userEntersNonExistingAccountGuid(String incorrectGuid) throws IOException {
        landingPage = new LandingPage(driver);
        incorrectGuid = (String) accountData()[0][1];

        landingPage.enterAccountGuid(incorrectGuid);
    }

    @When("user enters invalid account guid {string}")
    public void userEntersInvalidAccountGuid(String invalidGuid) throws IOException {
        landingPage = new LandingPage(driver);
        invalidGuid = (String) accountData()[0][2];

        landingPage.enterAccountGuid(invalidGuid);
    }
}
