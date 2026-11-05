package ca.bc.gov.open.jag.efiling.stepDefinitions;

import ca.bc.gov.open.jag.efiling.page.PackageReviewPage;
import ca.bc.gov.open.jag.efiling.page.SubmissionHistoryPage;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import java.util.List;

public class ViewSubmissionHistorySD {

    @Value("${SUBMISSION_HISTORY_URL:http://localhost:3000/efilinghub/submissionhistory}")
    private String submissionHistoryUrl;

    private final SubmissionHistoryPage submissionHistoryPage;
    private final PackageReviewPage packageReviewPage;

    private final Logger logger = LoggerFactory.getLogger(ViewSubmissionHistorySD.class);

    public ViewSubmissionHistorySD(SubmissionHistoryPage submissionHistoryPage, PackageReviewPage packageReviewPage) {
        this.submissionHistoryPage = submissionHistoryPage;
        this.packageReviewPage = packageReviewPage;
    }

    @Given("user is on submission history page")
    public void userIsOnSubmissionHistoryPage() {
        this.submissionHistoryPage.goTo(submissionHistoryUrl);
        this.submissionHistoryPage.signIn();
    }

    @When("packages history is populated")
    public void verifyPackagesHistoryIsPopulated() {
        Assert.assertTrue(submissionHistoryPage.submissionListIsDisplayed());
        Assert.assertTrue(submissionHistoryPage.verifyTableIsNotEmpty() > 1);
    }

    @Then("user can search for a package id {string}")
    public void searchWithPackageId(String packageId) {
        submissionHistoryPage.searchByPackageNumber(packageId);

        if (submissionHistoryPage.submissionListIsDisplayed()) {

            String packageReviewPageHeader = submissionHistoryPage.navigateToPackageReviewFromSearchResult();

            logger.info("Package details for : {}", packageReviewPageHeader);
            Assert.assertEquals("View Recently Submitted Package # 1", packageReviewPageHeader);

            List<String> actualPackageDetails = packageReviewPage.getPackageDetails();
            logger.info("There are {} elements", Integer.valueOf(actualPackageDetails.size()));

            Assert.assertNotNull(actualPackageDetails.get(1));
            Assert.assertEquals("Kelowna Law Courts", actualPackageDetails.get(2));
            Assert.assertEquals(packageId, actualPackageDetails.get(3));

        } else {
            logger.info("Submitted packages table is empty");
        }
    }
}
