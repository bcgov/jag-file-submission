package ca.bc.gov.open.jag.efiling.stepDefinitions;

import ca.bc.gov.open.jag.efiling.Keys;
import com.google.common.collect.ImmutableList;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static org.junit.Assert.assertEquals;

public class UploadDocumentUsingUploadLinkSD {

    private ca.bc.gov.open.jag.efiling.page.PackageConfirmationPage packageConfirmationPage;
    private ca.bc.gov.open.jag.efiling.page.DocumentUploadPage documentUploadPage;
    private final List<String> expectedUploadedFilesList = ImmutableList.of("test-document.pdf", "test-document-additional.pdf");

    private final Logger logger = LoggerFactory.getLogger(UploadDocumentUsingUploadLinkSD.class);

    public UploadDocumentUsingUploadLinkSD(ca.bc.gov.open.jag.efiling.page.PackageConfirmationPage packageConfirmationPage, ca.bc.gov.open.jag.efiling.page.DocumentUploadPage documentUploadPage) {
        this.packageConfirmationPage = packageConfirmationPage;
        this.documentUploadPage = documentUploadPage;
    }

    @When("user uploads an additional document")
    public void userUploadsAnAdditionalDocument() {

        this.packageConfirmationPage.verifyContinuePaymentBtnIsEnabled();
        this.packageConfirmationPage.clickUploadLink();

        String filePath = Keys.BASE_PATH + Keys.SECOND_PDF_PATH;
        this.documentUploadPage.selectFileToUpload(filePath);

        this.documentUploadPage.clickIsAmendmentRadioBtn();
        this.documentUploadPage.clickIsSupremeCourtBtn();
        logger.info("Submitting the additional document upload");

        this.documentUploadPage.clickContinueBtn();

    }

    @Then("verify the document is uploaded")
    public void verifyTheDocumentIsUploaded() {

        List<String> uploadedFiles = this.packageConfirmationPage.getUploadedFilesList();
        assertEquals(expectedUploadedFilesList, uploadedFiles);

        logger.info("Additional document added to the package.");
    }
}
