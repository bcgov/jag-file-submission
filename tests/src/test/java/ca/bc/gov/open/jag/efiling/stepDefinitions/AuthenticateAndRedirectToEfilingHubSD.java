
package ca.bc.gov.open.jag.efiling.stepDefinitions;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import ca.bc.gov.open.jag.efiling.Keys;
import ca.bc.gov.open.jag.efiling.error.EfilingTestException;
import ca.bc.gov.open.jag.efiling.models.FileSpec;
import ca.bc.gov.open.jag.efiling.page.AuthenticationPage;
import ca.bc.gov.open.jag.efiling.page.EfilingAdminHomePage;
import ca.bc.gov.open.jag.efiling.page.PackageConfirmationPage;
import ca.bc.gov.open.jag.efiling.services.GenerateUrlService;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class AuthenticateAndRedirectToEfilingHubSD {

    @Value("${EFILING_ADMIN_URL}")
    private String efilingAdminUrl;

    private final AuthenticationPage authenticationPage;
    private final PackageConfirmationPage packageConfirmationPage;
    private final EfilingAdminHomePage efilingAdminHomePage;
    private final GenerateUrlService generateUrlService;

    private final Logger logger = LoggerFactory.getLogger(AuthenticateAndRedirectToEfilingHubSD.class);

    public AuthenticateAndRedirectToEfilingHubSD(AuthenticationPage authenticationPages, PackageConfirmationPage packageConfirmationPage,
                                                 EfilingAdminHomePage efilingAdminHomePage, GenerateUrlService generateUrlService) {
        this.packageConfirmationPage = packageConfirmationPage;
        this.authenticationPage = authenticationPages;
        this.efilingAdminHomePage = efilingAdminHomePage;
        this.generateUrlService = generateUrlService;
    }

    @Given("User uploads a successful document from parent app")
    public void userUploadsSuccessfulDocument() throws IOException {
    	userUploadsDocumentFromParentApp(new FileSpec(Keys.TEST_DOCUMENT_PDF, Keys.ACTION_STATUS_SUB));
    }

    @Given("User uploads a rejected document from parent app")
    public void userUploadsRejectedDocument() throws IOException {
    	userUploadsDocumentFromParentApp(new FileSpec(Keys.TEST_DOCUMENT_PDF, Keys.ACTION_STATUS_REJ));
    }

    @Given("User uploads duplicate documents from parent app")
    public void userUploadsDuplicateDocuments() throws IOException {
    	userUploadsDocumentFromParentApp(
    			new FileSpec(Keys.TEST_DOCUMENT_PDF, Keys.ACTION_STATUS_SUB),
    			new FileSpec(Keys.TEST_DOCUMENT_PDF, Keys.ACTION_STATUS_SUB));
    }

    @Given("User uploads different documents from parent app")
    public void userUploadsDifferentDocuments() throws IOException {
    	userUploadsDocumentFromParentApp(
    			new FileSpec(Keys.TEST_DOCUMENT_PDF, Keys.ACTION_STATUS_SUB),
    			new FileSpec(Keys.SECOND_DOCUMENT_PDF, Keys.ACTION_STATUS_SUB));
    }
    
	private void userUploadsDocumentFromParentApp(FileSpec... fileSpecs) throws IOException {
		if (this.authenticationPage.getName().equalsIgnoreCase("keycloak")) {
			String generatedUrl = this.generateUrlService.getGeneratedUrl(fileSpecs);
			if (generatedUrl == null)
				throw new EfilingTestException("Redirect url is not generated.");
			this.efilingAdminHomePage.goTo(generatedUrl);
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

	@When("Rush radio option Yes is selected")
	public void rushRadioYesIsSelected() {
		packageConfirmationPage.selectRushYesOption();
	}
    
	@When("Rush model is closed")
    public void rushModelIsClosed() {
    	packageConfirmationPage.clickCloseOnRushModal();
    }
	
    @Then("Package information is displayed")
    public void verifyPackageInformation() {
        assertEquals(Keys.EFILE_SUBMISSION_PAGE_TITLE, this.packageConfirmationPage.verifyPageTitle());
        logger.info("Efiling submission page title is verified");

        assertEquals(Keys.TEST_DOCUMENT_PDF, this.packageConfirmationPage.getInitialDocumentName());
        logger.info("Actual document name matches the uploaded document name");
    }
    
    @And("Rush radio options are available")
    public void rushRadioOptionsAvailable() {
    	assertTrue(packageConfirmationPage.verifyRushRadioOptionsExist());
    }
    
    @And("Rush sidecard is visible")
    public void rushSidecardIsVisible() {
    	assertTrue(packageConfirmationPage.verifyRushSideCardExist());
    }
    
    @And("Rush sidecard is not visible")
    public void rushSidecardIsNotVisible() {
    	assertFalse(packageConfirmationPage.verifyRushSideCardExist());
    }
    
    @And("Rejected Document banner exists")
    public void verifyRejectedBannerExists() {
    	assertTrue(packageConfirmationPage.verifyRejectedBannerExists());
    }
    
    @And("Rejected Document banner doesn't exist")
    public void verifyRejectedBannerNotExists() {
    	assertFalse(packageConfirmationPage.verifyRejectedBannerExists());
    }
    
    @And("Duplicate Document banner exists")
    public void verifyDuplicateDocumentBannerExists() {
    	assertTrue(packageConfirmationPage.verifyDuplicateBannerExists());
    }
    
    @And("Duplicate Document banner doesn't exist")
    public void verifyDuplicateDocumentNotBannerExists() {
    	assertFalse(packageConfirmationPage.verifyDuplicateBannerExists());
    }
    
    @And("Rejected Document sidecard exists")
    public void verifyRejectedSideCardExists() {
    	assertTrue(packageConfirmationPage.verifyRejectedSidecardExists());
    }
    
    @And("Rejected Document sidecard doesn't exist")
    public void verifyRejectedSideCardNotExists() {
    	assertFalse(packageConfirmationPage.verifyRejectedSidecardExists());
    }

    @And("Continue to payment button is enabled")
    public void verifyContinueToPaymentButtonIsEnabled() {
        Assert.assertTrue(this.packageConfirmationPage.verifyContinuePaymentBtnIsEnabled());
        logger.info("Continue payment button is enabled");
    }

    @And("User clicks Continue")
    public void userClicksContinue() {
        Assert.assertTrue(packageConfirmationPage.verifyContinuePaymentBtnIsEnabled());
        packageConfirmationPage.clickContinueBtn();
    }

    @Then("Rush modal is displayed")
    public void rushModalIsDisplayed() {
        Assert.assertTrue(packageConfirmationPage.verifyRushModalIsDisplayed());
    }
    
    @And("Rush Details screen is displayed")
    public void rushDetailsScreenIsDisplayed() {
        Assert.assertTrue(packageConfirmationPage.verifyRushDetailsScreenIsDisplayed());
    }
}
