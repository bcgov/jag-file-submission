
package ca.bc.gov.open.jag.efiling.stepDefinitions;

import ca.bc.gov.open.jag.efiling.Keys;
import ca.bc.gov.open.jag.efiling.error.EfilingTestException;
import ca.bc.gov.open.jag.efiling.page.*;
import ca.bc.gov.open.jag.efiling.services.GenerateUrlService;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import java.io.IOException;

public class AuthenticateAndRedirectToEfilingHubSD {

    @Value("${EFILING_ADMIN_URL}")
    private String efilingAdminUrl;

    private final AuthenticationPage authenticationPage;
    private final PackageConfirmationPage packageConfirmationPage;
    private final EfilingAdminHomePage efilingAdminHomePage;
    private final GenerateUrlService generateUrlService;

    private static final String EFILE_SUBMISSION_PAGE_TITLE = "E-File submission";

    Logger logger = LoggerFactory.getLogger(AuthenticateAndRedirectToEfilingHubSD.class);

    public AuthenticateAndRedirectToEfilingHubSD(AuthenticationPage authenticationPages, PackageConfirmationPage packageConfirmationPage,
                                                 EfilingAdminHomePage efilingAdminHomePage, GenerateUrlService generateUrlService) {
        this.packageConfirmationPage = packageConfirmationPage;
        this.authenticationPage = authenticationPages;
        this.efilingAdminHomePage = efilingAdminHomePage;
        this.generateUrlService = generateUrlService;
    }

    @Given("user is on the eFiling submission page")
    public void userIsOnTheEfilingSubmissionPage() throws IOException {

        if (this.authenticationPage.getName().equalsIgnoreCase("keycloak")) {
            if (this.generateUrlService.getGeneratedUrl() == null)
                throw new EfilingTestException("Redirect url is not generated.");
            this.efilingAdminHomePage.goTo(this.generateUrlService.getGeneratedUrl());
        } else {
            this.efilingAdminHomePage.goTo(efilingAdminUrl);
        }
        logger.info("Waiting for the page to load...");

        this.authenticationPage.signIn();
        logger.info("user is authenticated with: {}", this.authenticationPage.getName());

        if (!this.authenticationPage.getName().equalsIgnoreCase("keycloak")) {
            logger.info("Uloading and submitting document to Efiling hub");
            this.efilingAdminHomePage.redirectToEfilingHub();
        }
    }

    @Then("Package information is displayed")
    public void verifyPackageInformation() {
        Assert.assertEquals(EFILE_SUBMISSION_PAGE_TITLE, this.packageConfirmationPage.verifyPageTitle());
        logger.info("Efiling submission page title is verified");

        Assert.assertEquals(Keys.TEST_DOCUMENT_PDF, this.packageConfirmationPage.getInitialDocumentName());
        logger.info("Actual document name matches the uploaded document name");

    }

    @And("continue to payment button is enabled")
    public void verifyContinueToPaymentButtonIsEnabled() {
        Assert.assertTrue(this.packageConfirmationPage.verifyContinuePaymentBtnIsEnabled());
        logger.info("Continue payment button is enabled");
    }
}
