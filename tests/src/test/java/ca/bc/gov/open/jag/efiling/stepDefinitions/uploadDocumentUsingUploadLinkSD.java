package ca.bc.gov.open.jag.efiling.stepDefinitions;

import com.google.common.collect.ImmutableList;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class uploadDocumentUsingUploadLinkSD {

    private ca.bc.gov.open.jag.efiling.page.PackageConfirmationPage packageConfirmationPage;
    private ca.bc.gov.open.jag.efiling.page.DocumentUploadPage documentUploadPage;
    private static final String BASE_PATH = "user.dir";
    private static final String SECOND_PDF_PATH = "/src/test/resources/data/test-document-additional.pdf";

    private final List<String> expectedUploadedFilesList = ImmutableList.of("test-document.pdf", "test-document-additional.pdf");

    Logger log = LogManager.getLogger(uploadDocumentUsingUploadLinkSD.class);

    public uploadDocumentUsingUploadLinkSD(ca.bc.gov.open.jag.efiling.page.PackageConfirmationPage packageConfirmationPage, ca.bc.gov.open.jag.efiling.page.DocumentUploadPage documentUploadPage) {
        this.packageConfirmationPage = packageConfirmationPage;
        this.documentUploadPage = documentUploadPage;
    }

    @When("user uploads an additional document")
    public void userUploadsAnAdditionalDocument() {

        this.packageConfirmationPage.verifyContinuePaymentBtnIsEnabled();
        this.packageConfirmationPage.clickUploadLink();

        String filePath = System.getProperty(BASE_PATH) + SECOND_PDF_PATH;
        this.documentUploadPage.selectFileToUpload(filePath);

        this.documentUploadPage.clickIsAmendmentRadioBtn();
        this.documentUploadPage.clickIsSupremeCourtBtn();
        log.info("Submitting the additional document upload");

        this.documentUploadPage.clickContinueBtn();

    }

    @Then("verify the document is uploaded")
    public void verifyTheDocumentIsUploaded() {

        List<String> uploadedFiles = this.packageConfirmationPage.getUploadedFilesList();
        System.out.println(uploadedFiles);
        assertEquals(expectedUploadedFilesList, uploadedFiles);
        log.info("Additional document added to the package.");
    }
}
