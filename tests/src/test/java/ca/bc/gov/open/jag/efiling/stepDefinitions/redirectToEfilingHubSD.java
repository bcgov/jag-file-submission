package ca.bc.gov.open.jag.efiling.stepDefinitions;

import ca.bc.gov.open.jag.efiling.page.AuthenticationPage;
import io.cucumber.java.en.Given;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;


public class redirectToEfilingHubSD {

    @Autowired
    private AuthenticationPage authenticationPage;

   // EFileSubmissionPage eFileSubmissionPage;
    /*PackageConfirmationPage packageConfirmationPage;
    private static final String EFILE_SUBMISSION_PAGE_TITLE = "E-File submission";
    private static final String BASE_PATH = "user.dir";
    private static final String SECOND_PDF_PATH = "/src/test/java/testdatasource/test-document-2.pdf";
    private final List<String> expectedUploadedFilesList = ImmutableList.of("data/test-document.pdf", "test-document-2.pdf");
*/



    @Given("user is on the eFiling submission page")
    public void userIsOnTheEfilingSubmissionPage() throws IOException, InterruptedException {

            authenticationPage.goTo();


      //  this.authenticationPage.clickBceid();
        this.authenticationPage.signInWithBceid("bobross", "changeme");
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
            log.info("user is authenticated before reaching eFiling hub page");

        } catch (TimeoutException | InterruptedException tx) {
            log.info("Efiling hub page is not displayed");
        }
        Assert.assertEquals(EFILE_SUBMISSION_PAGE_TITLE, eFileSubmissionPage.verifyEfilingPageTitle());
        log.info("Efiling submission page title is verified");
    }*/


}
