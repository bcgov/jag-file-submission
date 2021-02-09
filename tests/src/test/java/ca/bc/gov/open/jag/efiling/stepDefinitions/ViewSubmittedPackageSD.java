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

import java.text.MessageFormat;
import java.util.List;

public class ViewSubmittedPackageSD {

    @Value("${PACKAGE_REVIEW_URL:http://localhost:3000/efilinghub/packagereview}")
    private String packageReviewUrl;

    private AuthenticationPage authenticationPage;
    private PackageReviewPage packageReviewPage;

    private static final int packageId = 1;

    private Logger logger = LoggerFactory.getLogger(ViewSubmittedPackageSD.class);

    public ViewSubmittedPackageSD(AuthenticationPage authenticationPage, PackageReviewPage packageReviewPage) {
        this.authenticationPage = authenticationPage;
        this.packageReviewPage = packageReviewPage;
    }

    @Given("user is on package review page with package id {int}")
    public void userIsOnPackageReviewPage(int packageId) {

        String packageReviewPageUrl = MessageFormat.format( "{0}/{1}", packageReviewUrl, packageId);
        logger.info("Formatted package review page url:{}", packageReviewPageUrl);

        this.packageReviewPage.goTo(packageReviewPageUrl);
        this.authenticationPage.signInWithBceid();

    }

    @When("package details are populated")
    public void verifyPackageDetails() {

        List<String> actualPackageDetails = packageReviewPage.getPackageDetails();

        Assert.assertEquals("Han Solo", actualPackageDetails.get(0));
<<<<<<< refs/remotes/origin/qa-package-review
        Assert.assertEquals("04-May-2020 17:00", actualPackageDetails.get(1));
        //Date problem between local times eg. git runs on utc
=======
>>>>>>> fixed conflicts
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

    @And("comments are available in Filing Comments tab")
    public void verifyFilingComments() {

        packageReviewPage.clickFilingCommentsTab();
        Assert.assertTrue(packageReviewPage.verifyFilingCommentsIsDisplayed());

    }
}
