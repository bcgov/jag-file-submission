package ca.bc.gov.open.jag.efiling.stepDefinitions;

import ca.bc.gov.open.jag.efiling.page.AuthenticationPage;
import ca.bc.gov.open.jag.efiling.page.PackageReviewPage;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import java.io.File;
import java.text.MessageFormat;
import java.util.List;

public class ViewSubmittedPackageSD {

    @Value("${PACKAGE_REVIEW_URL:http://localhost:3000/efilinghub/packagereview}")
    private String packageReviewUrl;

    private final AuthenticationPage authenticationPage;
    private final PackageReviewPage packageReviewPage;

    private Logger logger = LoggerFactory.getLogger(ViewSubmittedPackageSD.class);

    public ViewSubmittedPackageSD(AuthenticationPage authenticationPage, PackageReviewPage packageReviewPage) {
        this.authenticationPage = authenticationPage;
        this.packageReviewPage = packageReviewPage;
    }

    @Given("user is on package review page with package id {int}")
    public void userIsOnPackageReviewPage(int packageId) {

        String packageReviewPageUrl = MessageFormat.format("{0}/{1}", packageReviewUrl, packageId);
        logger.info("Formatted package review page url:{}", packageReviewPageUrl);

        this.packageReviewPage.goTo(packageReviewPageUrl);
        this.authenticationPage.signInWithBceid();

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
    public void verifyDocumentDownload() throws InterruptedException {
        packageReviewPage.clickToDownloadDocument();

        File folder = new File(System.getProperty("user.dir") + File.separator + "downloadedFiles");
        File[] listOfFiles = folder.listFiles();

        if (listOfFiles == null) throw new EfilingTestException("Downloaded file is not present");
        for (File file : listOfFiles) {
            if (file.isFile()) {
                logger.info("Downloaded file name is: {}", file.getName());
                Assert.assertTrue(file.length() > 0);
                logger.info("Files successfully downloaded");

                logger.info("Files deleted after validation: {}", file.delete());
                Assert.assertEquals(0, file.length());
            }
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
