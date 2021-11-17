package ca.bc.gov.open.jag.efiling.stepDefinitions;

import ca.bc.gov.open.jag.efiling.page.SubmissionHistoryPage;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;

import java.text.MessageFormat;

public class ViewPackagesFilteredByParentAppSD {

    @Value("${SUBMISSION_HISTORY_URL:http://localhost:3000/efilinghub/submissionhistory}")
    private String submissionHistoryUrl;

    private static final String APPLICATION_CODE = "?applicationCode=";

    private final SubmissionHistoryPage submissionHistoryPage;

    private final Logger logger = LoggerFactory.getLogger(ViewPackagesFilteredByParentAppSD.class);

    public ViewPackagesFilteredByParentAppSD(SubmissionHistoryPage submissionHistoryPage) {
        this.submissionHistoryPage = submissionHistoryPage;
    }

    @Given("user is on submission history page with valid application code {string}")
    public void userIsOnSubmissionHistoryPageWithValidAppCode(String appCode) {
        logger.info("Requesting with valid application code");

        String urlWithValidAppCode = MessageFormat.format("{0}{1}{2}", submissionHistoryUrl, APPLICATION_CODE, appCode);

        this.submissionHistoryPage.goTo(urlWithValidAppCode);
        this.submissionHistoryPage.signIn();
    }

    @Then("packages history list is populated")
    public void verifyPackagesHistoryIsPopulated() {
        Assert.assertTrue(this.submissionHistoryPage.submissionListIsDisplayed());
        Assert.assertTrue(this.submissionHistoryPage.verifyTableIsNotEmpty() > 1);

        logger.info("Results are filtered based on the application code");
    }

    @Given("user is on submission history page with invalid application code {string}")
    public void userIsOnSubmissionHistoryPageWithInvalidAppCode(String appCode) {
        logger.info("Requesting with invalid application code");

        String urlWithInvalidAppCode = MessageFormat.format("{0}{1}{2}", submissionHistoryUrl, APPLICATION_CODE, appCode);

        this.submissionHistoryPage.goTo(urlWithInvalidAppCode);
        this.submissionHistoryPage.signIn();
    }

    @Then("packages history list is not populated")
    public void verifyPackagesHistoryIsNotPopulated() {
        Assert.assertTrue(this.submissionHistoryPage.submissionListIsDisplayed());
        Assert.assertEquals(1, this.submissionHistoryPage.verifyTableIsNotEmpty());

        String actualAlertMessage = this.submissionHistoryPage.getAlertText();
        Assert.assertTrue(actualAlertMessage.contains("Something went wrong while trying to retrieve your submissions."));

        logger.info("Alert message is displayed for invalid application code.");

    }
}
