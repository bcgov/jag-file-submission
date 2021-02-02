package ca.bc.gov.open.jag.efiling.stepDefinitions;

import ca.bc.gov.open.jag.efiling.page.AuthenticationPage;
import ca.bc.gov.open.jag.efiling.page.PackageConfirmationPage;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;

import java.io.IOException;


public class AuthenticateAndRedirectToEfilingHubSD {

    private AuthenticationPage authenticationPage;
    private PackageConfirmationPage packageConfirmationPage;
    private static final String EFILE_SUBMISSION_PAGE_TITLE = "E-File submission";
    private static final String INITIAL_DOCUMENT_NAME = "test-document.pdf";

    // EFileSubmissionPage eFileSubmissionPage;
    /*PackageConfirmationPage packageConfirmationPage;
    private static final String EFILE_SUBMISSION_PAGE_TITLE = "E-File submission";
    private static final String BASE_PATH = "user.dir";
    private static final String SECOND_PDF_PATH = "/src/test/java/testdatasource/test-document-2.pdf";
    private final List<String> expectedUploadedFilesList = ImmutableList.of("data/test-document.pdf", "test-document-2.pdf");
*/
    Logger log = LogManager.getLogger(AuthenticateAndRedirectToEfilingHubSD.class);

    public AuthenticateAndRedirectToEfilingHubSD(AuthenticationPage authenticationPage, PackageConfirmationPage packageConfirmationPage) {
        this.authenticationPage = authenticationPage;
        this.packageConfirmationPage = packageConfirmationPage;
    }

    @Given("user is on the eFiling submission page")
    public void userIsOnTheEfilingSubmissionPage() throws IOException {

        this.authenticationPage.goTo();
        this.authenticationPage.signInWithBceid("bobross", "changeme");
        log.info("user is authenticated with keycloak");
    }

    @Then("Package information is displayed")
    public void verifyPackageInformation() {
        Assert.assertEquals(EFILE_SUBMISSION_PAGE_TITLE, this.packageConfirmationPage.verifyPageTitle());
        log.info("Efiling submission page title is verified");

        Assert.assertEquals(INITIAL_DOCUMENT_NAME, this.packageConfirmationPage.getInitialDocumentName());
        log.info("Actual document name matches the uploaded document name");

    }

    @And("continue to payment button is enabled")
    public void verifyContinueToPaymentButtonIsEnabled() {
        Assert.assertTrue(this.packageConfirmationPage.verifyContinuePaymentBtnIsEnabled());
        log.info("Continue payment button is enabled");
    }
}
