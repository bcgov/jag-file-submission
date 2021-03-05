
package ca.bc.gov.open.jag.efiling.stepDefinitions;

import ca.bc.gov.open.jag.efiling.page.AuthenticationPage;
import ca.bc.gov.open.jag.efiling.page.EfilingAdminHomePage;
import ca.bc.gov.open.jag.efiling.page.PackageConfirmationPage;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

public class AuthenticateAndRedirectToEfilingHubSD {

    private final AuthenticationPage authenticationPage;
    private final PackageConfirmationPage packageConfirmationPage;
    private final EfilingAdminHomePage efilingAdminHomePage;

    private static final String EFILE_SUBMISSION_PAGE_TITLE = "E-File submission";
    private static final String INITIAL_DOCUMENT_NAME = "test-document.pdf";


    Logger logger = LoggerFactory.getLogger(AuthenticateAndRedirectToEfilingHubSD.class);

    public AuthenticateAndRedirectToEfilingHubSD(AuthenticationPage authenticationPages, PackageConfirmationPage packageConfirmationPage, EfilingAdminHomePage efilingAdminHomePage) {
        this.packageConfirmationPage = packageConfirmationPage;
        this.authenticationPage = authenticationPages;
        this.efilingAdminHomePage = efilingAdminHomePage;
    }

    @Given("user is on the eFiling submission page")
    public void userIsOnTheEfilingSubmissionPage() throws IOException {
            this.authenticationPage.signIn();
            logger.info("user is authenticated with: {}", this.authenticationPage.getName());

        if(!this.authenticationPage.getName().equalsIgnoreCase("keycloak")) {
            logger.info("Uloading and submitting document to Efiling hub");
            this.efilingAdminHomePage.redirectToEfilingHub();
        }
    }

    @Then("Package information is displayed")
    public void verifyPackageInformation() {
        Assert.assertEquals(EFILE_SUBMISSION_PAGE_TITLE, this.packageConfirmationPage.verifyPageTitle());
        logger.info("Efiling submission page title is verified");

        Assert.assertEquals(INITIAL_DOCUMENT_NAME, this.packageConfirmationPage.getInitialDocumentName());
        logger.info("Actual document name matches the uploaded document name");

    }

    @And("continue to payment button is enabled")
    public void verifyContinueToPaymentButtonIsEnabled() {
        Assert.assertTrue(this.packageConfirmationPage.verifyContinuePaymentBtnIsEnabled());
        logger.info("Continue payment button is enabled");
    }
}
