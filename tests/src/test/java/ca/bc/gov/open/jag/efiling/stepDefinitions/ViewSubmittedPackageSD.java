package ca.bc.gov.open.jag.efiling.stepDefinitions;

import ca.bc.gov.open.jag.efiling.Keys;
import ca.bc.gov.open.jag.efiling.helpers.FileDownloadHelper;
import ca.bc.gov.open.jag.efiling.page.PackageReviewPage;
import ca.bc.gov.open.jag.efiling.util.Files;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.assertj.core.util.Lists;
import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.List;

public class ViewSubmittedPackageSD {

    private final PackageReviewPage packageReviewPage;

    private final Logger logger = LoggerFactory.getLogger(ViewSubmittedPackageSD.class);

    public ViewSubmittedPackageSD(PackageReviewPage packageReviewPage) {
        this.packageReviewPage = packageReviewPage;
    }

    @Given("user is on package review page with package id")
    public void userIsOnPackageReviewPage() {
        this.packageReviewPage.signIn();

    }

    @When("package details are populated")
    public void verifyPackageDetails() {

        List<String> actualPackageDetails = packageReviewPage.getPackageDetails();

        logger.info("There are {} elements", Integer.valueOf(actualPackageDetails.size()));

        Assert.assertEquals("Han Solo", actualPackageDetails.get(0));
        Assert.assertNotNull(actualPackageDetails.get(1));
        Assert.assertEquals("Kelowna Law Courts", actualPackageDetails.get(2));
        Assert.assertEquals("1", actualPackageDetails.get(3));
        Assert.assertEquals("123", actualPackageDetails.get(4));

    }

    @Then("documents details are available in Documents tab")
    public void verifyDocumentDetails() {
        packageReviewPage.clickDocumentsTab();
        Assert.assertTrue(packageReviewPage.verifyDocumentsPaneIsDisplayed());
    }

    @And("document can be downloaded")
    public void verifyDocumentDownload() throws InterruptedException {
        packageReviewPage.clickToDownloadDocument();

        FileDownloadHelper fileDownloadHelper = new FileDownloadHelper();
        File downloadedFile = fileDownloadHelper.downloadFile(Keys.BASE_PATH + Keys.DOWNLOADED_FILES_PATH);

        logger.info("Downloaded file name is: {}", downloadedFile.getName());
        Assert.assertEquals(Keys.TEST_DOCUMENT_PDF, downloadedFile.getName());

        Assert.assertTrue(downloadedFile.length() > 0);
        logger.info("Files successfully downloaded");

        logger.info("Files deleted after validation: {}", Boolean.valueOf(Files.delete(downloadedFile)));
        Assert.assertEquals(0, downloadedFile.length());
    }

    @Then("Individual and Organization party type details are correct in parties tab")
    public void verifyIndividualAndOrganizationPartyType() {
        List<String> actualParties = packageReviewPage.getAllParties();

        List<String> expectedParties = Lists.newArrayList("Ross, Bob Q", Keys.APPLICANT, "Individual", "Loooooooooong-Looooooooooonglast, Looooooongname Q", Keys.APPLICANT, "Individual", "The Organization Org.", Keys.APPLICANT, "Organization", "This is a very very very very loooooong organization name", Keys.APPLICANT, "Organization");

        Assert.assertEquals(expectedParties.size(), actualParties.size());
        Assert.assertEquals(expectedParties,actualParties);
    }

    @Then("comments are available in Filing Comments tab")
    public void verifyFilingComments() {
        packageReviewPage.clickFilingCommentsTab();
        Assert.assertTrue(packageReviewPage.verifyFilingCommentsIsDisplayed());

    }

    @Then("payment status information is correct")
    public void verifyPaymentStatus() {
        packageReviewPage.clickPaymentStatusTab();
        Assert.assertTrue(packageReviewPage.verifyPaymentStatusIsDisplayed());
    }

    @Then("user can navigate to {string} page and return to Efiling hub")
    public void verifyUserCanNavigateToCsoAndReturnToEfilingHub(String csoUrlSlug) {
        logger.info("Current url is: {}", packageReviewPage.getCsoPageUrlAndSwitchToPackageReviewPage());
        Assert.assertTrue(packageReviewPage.getCsoPageUrlAndSwitchToPackageReviewPage().contains(csoUrlSlug));

        packageReviewPage.getCurrentPageTitle();
        logger.info("Current tab title is: {}", packageReviewPage.getCurrentPageTitle());
        Assert.assertEquals("E-File submission", packageReviewPage.getCurrentPageTitle());
    }

}
