package ca.bc.gov.open.jag.efiling.stepDefinitions;

import ca.bc.gov.open.jag.efiling.error.EfilingTestException;
import ca.bc.gov.open.jag.efiling.page.PackageReviewPage;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class ViewSubmittedPackageSD {

    private final PackageReviewPage packageReviewPage;

    private static final String DOWNLOADED_FILES_PATH = System.getProperty("user.dir") + File.separator + "downloadedFiles";

    private Logger logger = LoggerFactory.getLogger(ViewSubmittedPackageSD.class);

    public ViewSubmittedPackageSD(PackageReviewPage packageReviewPage) {
        this.packageReviewPage = packageReviewPage;
    }

    @Given("user is on package review page with package id {int}")
    public void userIsOnPackageReviewPage(int packageId) {
        this.packageReviewPage.signIn(packageId);

    }

    @When("package details are populated")
    public void verifyPackageDetails() {

        List<String> actualPackageDetails = packageReviewPage.getPackageDetails();

        logger.info("There are {} elements", actualPackageDetails.size());

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
    public void verifyDocumentDownload() throws IOException, InterruptedException {
        packageReviewPage.clickToDownloadDocument();

        File folder = new File(DOWNLOADED_FILES_PATH);
        File[] listOfFiles = folder.listFiles();

        if (listOfFiles == null) throw new EfilingTestException("Downloaded file is not present");
        for (File file : listOfFiles) {
            if (file.isFile()) {
                logger.info("Downloaded file name is: {}", file.getName());
                Assert.assertTrue(file.length() > 0);
                logger.info("Files successfully downloaded");

                logger.info("Files deleted after validation: {}", file.delete());

            }
            Assert.assertEquals(0, file.length());
        }
    }

    @And("comments are available in Filing Comments tab")
    public void verifyFilingComments() {
        packageReviewPage.clickFilingCommentsTab();
        Assert.assertTrue(packageReviewPage.verifyFilingCommentsIsDisplayed());

    }

    @And("payment status information is correct")
    public void verifyPaymentStatus() {
        packageReviewPage.clickPaymentStatusTab();
        Assert.assertTrue(packageReviewPage.verifyPaymentStatusIsDisplayed());
    }
}
