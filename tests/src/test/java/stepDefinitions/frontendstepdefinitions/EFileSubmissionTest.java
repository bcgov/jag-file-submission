package stepDefinitions.frontendstepdefinitions;

import ca.bc.gov.open.jagefilingapi.qa.backendutils.GenerateUrlHelper;
import ca.bc.gov.open.jagefilingapi.qa.backendutils.TestUtil;
import ca.bc.gov.open.jagefilingapi.qa.frontend.pages.AuthenticationPage;
import ca.bc.gov.open.jagefilingapi.qa.frontend.pages.DocumentUploadPage;
import ca.bc.gov.open.jagefilingapi.qa.frontend.pages.EFileSubmissionPage;
import ca.bc.gov.open.jagefilingapi.qa.frontend.pages.PackageConfirmationPage;
import com.google.common.collect.ImmutableList;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.TimeoutException;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class EFileSubmissionTest extends ca.bc.gov.open.jagefilingapi.qa.frontendutils.DriverClass {

    EFileSubmissionPage eFileSubmissionPage;
    PackageConfirmationPage packageConfirmationPage;
    private static final String EFILE_SUBMISSION_PAGE_TITLE = "E-File submission";
    private static final String BASE_PATH = "user.dir";
    private static final String SECOND_PDF_PATH = "/src/test/java/testdatasource/test-document-2.pdf";
    private final List<String> expectedUploadedFilesList = ImmutableList.of("data/test-document.pdf", "test-document-2.pdf");

    @Before
    public void setUp() {
        TestUtil testUtil = new TestUtil();
        testUtil.restAssuredConfig();
        driverSetUp();
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

    @Given("user is on the eFiling submission page")
    public void userIsOnTheEfilingSubmissionPage() throws IOException {
        eFileSubmissionPage = new EFileSubmissionPage(driver);
        try {
                GenerateUrlHelper generateUrlHelper = new GenerateUrlHelper();
                String respUrl = generateUrlHelper.getGeneratedUrl();

                driver.get(respUrl);
                log.info("EFiling submission page url is accessed successfully");

                AuthenticationPage authenticationPage = new AuthenticationPage(driver);
                if(System.getProperty("ENV").equals("demo")) {
                    authenticationPage.clickBceid();
                }
                Thread.sleep(4000L);
                authenticationPage.signInWithBceid(System.getProperty("BCEID_USERNAME"), System.getProperty("BCEID_PASSWORD"));
                log.info("user is authenticated before reaching eFiling hub page");

        } catch (TimeoutException | InterruptedException tx) {
            log.info("Efiling hub page is not displayed");
        }
        Assert.assertEquals(EFILE_SUBMISSION_PAGE_TITLE, eFileSubmissionPage.verifyEfilingPageTitle());
        log.info("Efiling submission page title is verified");
    }

    @When("user can upload an additional document")
    public void userCanUploadAnAdditionalDocument() {
        eFileSubmissionPage = new EFileSubmissionPage(driver);
        packageConfirmationPage = new PackageConfirmationPage(driver);

        packageConfirmationPage.verifyContinuePaymentBtnIsDisplayed();
        packageConfirmationPage.clickUploadLink();

        DocumentUploadPage documentUploadPage = new DocumentUploadPage(driver);

        String filePath = System.getProperty(BASE_PATH) + SECOND_PDF_PATH;
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
}
