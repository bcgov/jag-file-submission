package ca.bc.gov.open.jag.efiling.stepDefinitions;

import ca.bc.gov.open.jag.efiling.page.AuthenticationPage;
import io.cucumber.java.en.Given;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;


public class redirectToEfilingHubSD {

    private AuthenticationPage authenticationPage;

    // EFileSubmissionPage eFileSubmissionPage;
    /*PackageConfirmationPage packageConfirmationPage;
    private static final String EFILE_SUBMISSION_PAGE_TITLE = "E-File submission";
    private static final String BASE_PATH = "user.dir";
    private static final String SECOND_PDF_PATH = "/src/test/java/testdatasource/test-document-2.pdf";
    private final List<String> expectedUploadedFilesList = ImmutableList.of("data/test-document.pdf", "test-document-2.pdf");
*/
    Logger log = LogManager.getLogger(redirectToEfilingHubSD.class);

    public redirectToEfilingHubSD(AuthenticationPage authenticationPage) {
        this.authenticationPage = authenticationPage;
    }

    @Given("user is on the eFiling submission page")
    public void userIsOnTheEfilingSubmissionPage() throws IOException {

        this.authenticationPage.goTo();
        this.authenticationPage.signInWithBceid("bobross", "changeme");
        log.info("user is authenticated with keycloak");
    }

/*        try {
            GenerateUrlHelper generateUrlHelper = new GenerateUrlHelper();
            String respUrl = generateUrlHelper.getGeneratedUrl();

            driver.get(respUrl);
            log.info("EFiling submission page url is accessed successfully");

            AuthenticationPage authenticationPage = new AuthenticationPage(driver);
            if(System.getProperty("ENV").equals("demo")) {
                authenticationPage.clickBceid();
            }
            Thread.sleep(4000L);
            authenticationPage.signInWithBceid(System.getProperty("BCEID_USERNAME"), System.getProperty("BCEID_PASSWORD"));


        } catch (TimeoutException | InterruptedException tx) {
            log.info("Efiling hub page is not displayed");
        }
        Assert.assertEquals(EFILE_SUBMISSION_PAGE_TITLE, eFileSubmissionPage.verifyEfilingPageTitle());
        log.info("Efiling submission page title is verified");
    }*/


}
