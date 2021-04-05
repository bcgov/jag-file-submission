package ca.bc.gov.open.jag.efiling.stepDefinitions;

import ca.bc.gov.open.jag.efiling.page.SubmissionHistoryPage;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.When;
import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ViewSubmissionHistorySD {

    private final SubmissionHistoryPage submissionHistoryPage;

    private Logger logger = LoggerFactory.getLogger(ViewSubmissionHistorySD.class);

    public ViewSubmissionHistorySD(SubmissionHistoryPage submissionHistoryPage) {
        this.submissionHistoryPage = submissionHistoryPage;
    }

    @Given("user is on submission history page")
    public void userIsOnSubmissionHistoryPage() {
        this.submissionHistoryPage.signIn();

    }

    @When("packages history is populated")
    public void verifyPackagesHistoryIsPopulated() {
        Assert.assertTrue(submissionHistoryPage.submissionListIsDisplayed());

    }
}
