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

    private Logger logger = LoggerFactory.getLogger(ViewSubmittedPackageSD.class);

    public ViewSubmittedPackageSD(AuthenticationPage authenticationPage, PackageReviewPage packageReviewPage) {
        this.authenticationPage = authenticationPage;
        this.packageReviewPage = packageReviewPage;
    }

    @Given("user is on package review page")
    public void userIsOnPackageReviewPage() {

        String packageReviewPageUrl = MessageFormat.format( "{0}/{1}", packageReviewUrl, "1");
        logger.info("Formatted package review page url:{}", packageReviewPageUrl);

        this.packageReviewPage.goTo(packageReviewPageUrl);
        this.authenticationPage.signInWithBceid("bobross", "changeme");

    }

    @When("package details are populated")
    public void verifyPackageDetails() {

        List<String> actualPackageDetails = this.packageReviewPage.getPackageDetails();

        Assert.assertEquals("Han Solo", actualPackageDetails.get(0));
        Assert.assertEquals("04-May-2020 17:00", actualPackageDetails.get(1));
        Assert.assertEquals("Kelowna Law Courts", actualPackageDetails.get(2));
        Assert.assertEquals("1", actualPackageDetails.get(3));
        Assert.assertEquals("123", actualPackageDetails.get(4));

    }

    @Then("documents details are available in Documents tab")
    public void verifyDocumentDetails() {

        this.packageReviewPage.clickDocumentsTab();
        Assert.assertTrue(this.packageReviewPage.verifyDocumentsPaneIsDisplayed());
    }

    @And("comments are available in Filing Comments tab")
    public void verifyFilingComments() {

        this.packageReviewPage.clickFilingCommentsTab();
        Assert.assertTrue(this.packageReviewPage.verifyFilingCommentsIsDisplayed());

    }
}
