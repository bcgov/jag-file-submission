package stepDefinitions.frontendstepdefinitions;

import ca.bc.gov.open.jagefilingapi.qa.backendutils.TestUtil;
import ca.bc.gov.open.jagefilingapi.qa.config.ReadConfig;
import ca.bc.gov.open.jagefilingapi.qa.frontend.pages.*;
import ca.bc.gov.open.jagefilingapi.qa.frontendutils.FrontendTestUtil;
import ca.bc.gov.open.jagefilingapi.qa.frontendutils.JsonDataReader;
import com.google.common.collect.ImmutableList;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.*;

public class EFileSubmissionTest extends ca.bc.gov.open.jagefilingapi.qa.frontendutils.DriverClass {

    ReadConfig readConfig;
    LandingPage landingPage;
    EFileSubmissionPage eFileSubmissionPage;
    PackageConfirmationPage packageConfirmationPage;
    private static final String EFILE_SUBMISSION_PAGE_TITLE = "E-File submission";
    private static final String EFILING_DEMO_CLIENT_PAGE_TITLE = "eFiling Demo Client";
    private static final String BASE_PATH = "user.dir";
    private static final String PDF_PATH = "/src/test/java/testdatasource/test-document.pdf";
    private static final String SECOND_PDF_PATH = "/src/test/java/testdatasource/test-document-2.pdf";
    private final List<String> expectedUploadedFilesList = ImmutableList.of("test-document.pdf", "test-document-2.pdf");
    private String filePath;
    private String username;
    private String password;

    @Before
    public void setUp() throws IOException {
        TestUtil testUtil = new TestUtil();
        testUtil.restAssuredConfig();
        driverSetUp();
        log.info("Browser is initialized from the driver class");
    }

    @After
    public void tearDown(Scenario scenario) {
        if (scenario.isFailed()) {
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
    public void userIsOnTheLandingPage() throws IOException, InterruptedException {
        readConfig = new ReadConfig();
        String url = readConfig.getBaseUrl();

        username = System.getProperty("BCEID_USERNAME");
        password = System.getProperty("BCEID_PASSWORD");

        driver.get(url);
        log.info("Landing page url is accessed successfully");

        AuthenticationPage authenticationPage = new AuthenticationPage(driver);
        authenticationPage.clickBceid();
        authenticationPage.signInWithBceid(username, password);
        log.info("user is authenticated before reaching eFiling demo page");

        WebDriverWait wait = new WebDriverWait(driver, 60);
        wait.until(ExpectedConditions.titleIs(EFILING_DEMO_CLIENT_PAGE_TITLE));

        landingPage = new LandingPage(driver);

        String actualTitle = landingPage.verifyLandingPageTitle();

        Assert.assertEquals(EFILING_DEMO_CLIENT_PAGE_TITLE, actualTitle);
        log.info("Landing page title is verified");
    }

    @When("user enters a valid existing CSO account guid and uploads a document")
    public void userEntersAValidExistingCsoAccountGuidAndUploadsADocument() throws IOException {
        readConfig = new ReadConfig();
        landingPage = new LandingPage(driver);
        filePath = System.getProperty(BASE_PATH) + PDF_PATH;
        landingPage.chooseFileToUpload(filePath);

        landingPage.enterJsonData();

        landingPage.clickEfilePackageButton();
        log.info("Pdf file is uploaded successfully.");
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
        assertTrue(cancelPageIsDisplayed);
        log.info("Navigated to the cancel page from the E-File submission page.");
    }

    @Then("user clicks Return Home button to navigate to the landing page")
    public void userClicksReturnHomeButtonToNavigateToTheLandingPage() {
        eFileSubmissionPage = new EFileSubmissionPage(driver);
        landingPage = new LandingPage(driver);

        eFileSubmissionPage.clickReturnHomeButton();
        log.info("Navigated to the landing page from cancel page");

        String actualTitle = landingPage.verifyLandingPageTitle();

        Assert.assertEquals(EFILING_DEMO_CLIENT_PAGE_TITLE, actualTitle);
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

    @Then("user clicks on create CSO account")
    public void userClicksOnCreateCSOAccount() {
        CreateCsoAccountPage createCsoAccountPage = new CreateCsoAccountPage(driver);
        createCsoAccountPage.clickCreateCsoAccountBtn();
    }

    @Then("the CSO account is created successfully")
    public void theCSOAccountIsCreatedSuccessfully() {
        packageConfirmationPage = new PackageConfirmationPage(driver);
        assertTrue(packageConfirmationPage.verifyContinuePaymentBtnIsDisplayed());
    }

    @Then("user can upload an additional document")
    public void userCanUploadAnAdditionalDocument() {
        eFileSubmissionPage = new EFileSubmissionPage(driver);
        packageConfirmationPage = new PackageConfirmationPage(driver);
        landingPage = new LandingPage(driver);

        packageConfirmationPage.clickUploadLink();

        DocumentUploadPage documentUploadPage = new DocumentUploadPage(driver);

        filePath = System.getProperty(BASE_PATH) + SECOND_PDF_PATH;
        documentUploadPage.selectFileToUpload(filePath);

        documentUploadPage.clickIsAmendmentRadioBtn();
        documentUploadPage.clickIsSupremeCourtBtn();
        log.info("Additional document is added successfully.");
    }

    @And("submit and verify the document is uploaded")
    public void submitAndVerifyTheDocumentIsUploaded() {
        DocumentUploadPage documentUploadPage = new DocumentUploadPage(driver);
        packageConfirmationPage = new PackageConfirmationPage(driver);

        documentUploadPage.clickContinueBtn();

        List<String>uploadedFiles = packageConfirmationPage.getUploadedFilesList();
        assertEquals(uploadedFiles, expectedUploadedFilesList);
        log.info("Additional file is uploaded successfully.");
    }

    @Then("user clicks continue payment button")
    public void userClicksContinuePaymentButton() {
        eFileSubmissionPage = new EFileSubmissionPage(driver);
        packageConfirmationPage = new PackageConfirmationPage(driver);

        packageConfirmationPage.clickContinuePaymentBtn();
    }

    @And("delete the selected additional document")
    public void userCanDeleteTheSelectedAdditionalDocument() {
        DocumentUploadPage documentUploadPage = new DocumentUploadPage(driver);

        documentUploadPage.clickRemoveFileIcon();
        log.info("Added file is removed.");
    }

    @Then("user clicks cancel upload button")
    public void userClicksCancelUploadButton() {
        DocumentUploadPage documentUploadPage = new DocumentUploadPage(driver);
        packageConfirmationPage = new PackageConfirmationPage(driver);

        documentUploadPage.clickCancelUpload();
        packageConfirmationPage.verifyContinuePaymentBtnIsDisplayed();
        log.info("Document upload is cancelled in upload page.");
    }

    @When("user enters non existing CSO account guid and uploads a document")
    public void userEntersNonExistingCsoAccountGuidAndUploadsADocument() throws IOException, InterruptedException {
        landingPage = new LandingPage(driver);

        String nonExistingCSOGuid = JsonDataReader.getCsoAccountGuid().getNonExistingCSOGuid();
        landingPage.enterAccountGuid(nonExistingCSOGuid);

        filePath = System.getProperty(BASE_PATH) + PDF_PATH;
        landingPage.chooseFileToUpload(filePath);

        landingPage.enterJsonData();

        landingPage.clickEfilePackageButton();
        log.info("Pdf file is uploaded successfully.");

        AuthenticationPage authenticationPage = new AuthenticationPage(driver);
        authenticationPage.clickBceid();
        authenticationPage.signInWithBceid(username, password);
        log.info("user is authenticated in eFiling demo page.");
    }

    @Then("eFile submission page with user agreement is displayed")
    public void eFileSubmissionPageWithUserAgreementIsDisplayed() throws IOException {
        eFileSubmissionPage = new EFileSubmissionPage(driver);
        landingPage = new LandingPage(driver);

        String actualTitle = eFileSubmissionPage.verifyEfilingPageTitle();
        Assert.assertEquals(EFILE_SUBMISSION_PAGE_TITLE, actualTitle);
        log.info("eFile submission page title is verified");

        CreateCsoAccountPage createCsoAccountPage = new CreateCsoAccountPage(driver);
        createCsoAccountPage.verifyCsoBtnIsDisplayed();
    }

    @Then("user accepts agreement and clicks cancel button")
    public void userAcceptsAgreementAndClicksCancelButton() {
        eFileSubmissionPage = new EFileSubmissionPage(driver);

        eFileSubmissionPage.selectCheckbox();
        eFileSubmissionPage.clickAcceptTermsCancelButton();
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
