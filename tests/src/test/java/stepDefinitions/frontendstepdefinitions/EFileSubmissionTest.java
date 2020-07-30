package stepDefinitions.frontendstepdefinitions;

import ca.bc.gov.open.jagefilingapi.qa.config.ReadConfig;
import ca.bc.gov.open.jagefilingapi.qa.frontend.pages.AuthenticationPage;
import ca.bc.gov.open.jagefilingapi.qa.frontend.pages.EFileSubmissionPage;
import ca.bc.gov.open.jagefilingapi.qa.frontend.pages.LandingPage;
import ca.bc.gov.open.jagefilingapi.qa.frontend.pages.PackageConfirmationPage;
import ca.bc.gov.open.jagefilingapi.qa.frontendutils.DriverClass;
import ca.bc.gov.open.jagefilingapi.qa.frontendutils.FrontendTestUtil;
import ca.bc.gov.open.jagefilingapi.qa.frontendutils.JsonDataReader;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.github.cdimascio.dotenv.Dotenv;
import org.junit.Assert;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.IOException;
import java.util.List;

public class EFileSubmissionTest extends DriverClass {

     ReadConfig readConfig;
     LandingPage landingPage;
     EFileSubmissionPage eFileSubmissionPage;
     PackageConfirmationPage packageConfirmationPage;
     private static final String EFILE_SUBMISSION_PAGE_TITLE = "E-File submission";
     private static final String BASE_PATH = "user.dir";
     private static final String PDF_PATH = "/src/test/java/testdatasource/test-pdf-document.pdf";
     private String filePath;
     private String username;
     private String password;

    @Before("@frontend")
    public void setUp() throws IOException {
        driverSetUp();
        log.info("Browser is initialized from the driver class");
    }

    @After("@frontend")
    public void tearDown(Scenario scenario){
        if(scenario.isFailed()) {
            String testName = scenario.getName();
                log.info(testName + "is Failed");
                final byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
                scenario.attach(screenshot, "image/png", "Failed test");
        }
        driver.close();
        driver.quit();
        log.info("Browser closed");
    }

    @Given("user is on the landing page")
    public void userIsOnTheLandingPage() throws IOException {
        readConfig = new ReadConfig();
        String url = readConfig.getBaseUrl();
        Dotenv dotenv = Dotenv.load();

        username = dotenv.get("BCEID_USERNAME");
        password = dotenv.get("BCEID_PASSWORD");

        driver.get(url);
        log.info("Landing page url is accessed successfully");

        AuthenticationPage authenticationPage = new AuthenticationPage(driver);
        authenticationPage.clickBceid();
        authenticationPage.signInWithIdir(username, password);
        log.info("user is authenticated before reaching eFiling demo client");

        WebDriverWait wait = new WebDriverWait(driver, 60);
        wait.until(ExpectedConditions.titleIs("eFiling Demo Client"));

        landingPage = new LandingPage(driver);

        String actualTitle = landingPage.verifyLandingPageTitle();
        String expectedTitle = "eFiling Demo Client";

        Assert.assertEquals(expectedTitle, actualTitle);
        log.info("Landing page title is verified");
    }

    @When("user enters a valid existing CSO account guid {string} and uploads a document")
    public void userEntersAValidExistingCsoAccountGuidAndUploadsADocument(String validExistingCSOGuid) throws IOException {
        readConfig = new ReadConfig();
        landingPage = new LandingPage(driver);

        validExistingCSOGuid = JsonDataReader.getCsoAccountGuid().getValidExistingCSOGuid();
        landingPage.enterAccountGuid(validExistingCSOGuid);

        filePath = System.getProperty(BASE_PATH) + PDF_PATH;
        landingPage.chooseFileToUpload(filePath);

        landingPage.enterJsonData();

        landingPage.clickGenerateUrlButton();
        log.info("Pdf file is uploaded successfully.");

        AuthenticationPage authenticationPage = new AuthenticationPage(driver);
        authenticationPage.clickBceid();
        authenticationPage.signInWithIdir(username, password);
        log.info("user is authenticated in eFiling demo client.");
    }

    @Then("eFile submission page is displayed and user clicks the cancel button")
    public void eFileSubmissionPageIsDisplayedAncUserClicksTheCancelButton() {
        eFileSubmissionPage = new EFileSubmissionPage(driver);
        packageConfirmationPage = new PackageConfirmationPage(driver);

        String actualTitle = eFileSubmissionPage.verifyEfilingPageTitle();
        Assert.assertEquals(EFILE_SUBMISSION_PAGE_TITLE, actualTitle);
        log.info("eFiling Frontend page title is verified");

        boolean continuePaymentBtnIsDisplayed = packageConfirmationPage.verifyContinuePaymentBtnIsDisplayed();
        Assert.assertTrue(continuePaymentBtnIsDisplayed);

        packageConfirmationPage.clickContinuePaymentBtn();

        eFileSubmissionPage.clickCancelButton();
    }

    @Then("user confirms the cancellation in the confirmation window")
    public void userConfirmsTheCancellationInTheConfirmationWindow() {
        eFileSubmissionPage = new EFileSubmissionPage(driver);

        eFileSubmissionPage.clickConfirmCancellation();
        log.info("Clicked confirm cancellation in the confirmation modal.");
    }

    @And("user is navigated to the cancel page")
    public void userIsNavigatedToTheCancelPage() {
        eFileSubmissionPage = new EFileSubmissionPage(driver);

        boolean cancelPageIsDisplayed = eFileSubmissionPage.verifyCancelPageIsDisplayed();
        Assert.assertTrue(cancelPageIsDisplayed);
        log.info("Navigated to the cancel page from the E-File submission page.");
    }

    @Then("user clicks Return Home button to navigate to the landing page")
    public void userClicksReturnHomeButtonToNavigateToTheLandingPage() {
        eFileSubmissionPage = new EFileSubmissionPage(driver);
        landingPage = new LandingPage(driver);

        eFileSubmissionPage.clickReturnHomeButton();
        log.info("Navigated to the landing page from cancel page");

        String actualTitle = landingPage.verifyLandingPageTitle();
        String expectedTitle = "eFiling Demo Client";

        Assert.assertEquals(expectedTitle, actualTitle);
        log.info("Landing page title is verified");
    }

    @Then("user clicks resume E-File submission in the confirmation window")
    public void userClicksResumeEFileSubmissionInTheConfirmationWindow() {
        eFileSubmissionPage = new EFileSubmissionPage(driver);

        eFileSubmissionPage.clickResumeSubmission();
        log.info("Confirmation modal is closed and E-File submission page is retained");
    }

    @And("the user stays on the E-File submission page")
    public void theUserStaysOnTheEFileSubmissionPage() {
        eFileSubmissionPage = new EFileSubmissionPage(driver);

        String actualTitle = eFileSubmissionPage.verifyEfilingPageTitle();
        Assert.assertEquals(EFILE_SUBMISSION_PAGE_TITLE, actualTitle);
        log.info("eFiling Frontend page title is verified");
    }

    @When("user enters non existing CSO account guid {string} and uploads a document")
    public void userEntersNonExistingCsoAccountGuidAndUploadsADocument(String nonExistingCSOGuid) throws IOException {
        landingPage = new LandingPage(driver);

        nonExistingCSOGuid = JsonDataReader.getCsoAccountGuid().getNonExistingCSOGuid();
        landingPage.enterAccountGuid(nonExistingCSOGuid);

        filePath = System.getProperty(BASE_PATH) + PDF_PATH;
        landingPage.chooseFileToUpload(filePath);

        landingPage.enterJsonData();

        landingPage.clickGenerateUrlButton();
        log.info("Pdf file is uploaded successfully.");

        AuthenticationPage authenticationPage = new AuthenticationPage(driver);
        authenticationPage.clickBceid();
        authenticationPage.signInWithIdir(username, password);
        log.info("user is authenticated in eFiling demo client.");
    }

    @Then("eFile submission page with user agreement is displayed")
    public void eFileSubmissionPageWithUserAgreementIsDisplayed() {
        eFileSubmissionPage = new EFileSubmissionPage(driver);
        landingPage = new LandingPage(driver);

        String actualTitle = eFileSubmissionPage.verifyEfilingPageTitle();
        Assert.assertEquals(EFILE_SUBMISSION_PAGE_TITLE, actualTitle);
        log.info("eFile submission page title is verified");

        boolean createCsoAccountBtnIsDisplayed = eFileSubmissionPage.verifyCreateCsoAccountBtnIsDisplayed();
        Assert.assertTrue(createCsoAccountBtnIsDisplayed);
    }

    @Then("user accepts agreement and clicks cancel button")
    public void userAcceptsAgreementAndClicksCancelButton() {
        eFileSubmissionPage = new EFileSubmissionPage(driver);

        eFileSubmissionPage.selectCheckbox();
        eFileSubmissionPage.clickAcceptTermsCancelButton();
    }

    @When("user enters invalid CSO account guid without eFiling role {string}")
    public void userEntersInvalidCsoAccountGuidWithoutEfilingRole(String invalidNoFilingRoleGuid) throws IOException {
        landingPage = new LandingPage(driver);

        invalidNoFilingRoleGuid = JsonDataReader.getCsoAccountGuid().getInvalidNoFilingRoleGuid();
        landingPage.enterAccountGuid(invalidNoFilingRoleGuid);

        filePath = System.getProperty(BASE_PATH) + PDF_PATH;
        landingPage.chooseFileToUpload(filePath);

        landingPage.enterJsonData();
    }

    @Then("error message is displayed")
    public void errorMessageIsDisplayed() {
        landingPage = new LandingPage(driver);

        landingPage.clickGenerateUrlButton();
        log.info("Generate Url button in eFiling frontend page is clicked");

        String expMsg = "An error occurred while eFiling your package. Please make sure you upload at least one file and try again.";
        String actMsg =  landingPage.getErrorMessageText();
        Assert.assertEquals(actMsg, expMsg);

        log.info("Expected message is verified");
    }

    @Then("verify there are no broken links in the page")
    public void verifyThereAreNoBrokenLinksInThePage() throws IOException {
        List<WebElement> links = driver.findElements(By.tagName("a"));

        log.info("Total links are " + links.size());

        for (WebElement element : links) {
            String url = element.getAttribute("href");
            FrontendTestUtil.verifyLinkActive(url);
        }
    }
}
