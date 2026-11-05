package ca.bc.gov.open.jag.efiling.stepDefinitions;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ImmutableList;

import ca.bc.gov.open.jag.efiling.Keys;
import ca.bc.gov.open.jag.efiling.page.DocumentUploadPage;
import ca.bc.gov.open.jag.efiling.page.PackageConfirmationPage;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class UploadDocumentUsingUploadLinkSD {

	private PackageConfirmationPage packageConfirmationPage;
	private DocumentUploadPage documentUploadPage;
	private final List<String> expectedUploadedFilesList = ImmutableList.of("test-document.pdf",
			"test-document-additional.pdf");

	private final Logger logger = LoggerFactory.getLogger(UploadDocumentUsingUploadLinkSD.class);

	public UploadDocumentUsingUploadLinkSD(PackageConfirmationPage packageConfirmationPage,
			DocumentUploadPage documentUploadPage) {
		this.packageConfirmationPage = packageConfirmationPage;
		this.documentUploadPage = documentUploadPage;
	}

	@When("user uploads an additional document")
	public void userUploadsAnAdditionalDocument() {

		this.packageConfirmationPage.verifyContinuePaymentBtnIsEnabled();
		this.packageConfirmationPage.clickUploadLink();

		String filePath = Keys.BASE_PATH + Keys.RESOURCES_PATH + Keys.SECOND_DOCUMENT_PDF;
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

	@When("user uploads the same document")
	public void userUploadsTheSameDocument() {
		this.packageConfirmationPage.verifyContinuePaymentBtnIsEnabled();
		this.packageConfirmationPage.clickUploadLink();

		String filePath = Keys.BASE_PATH + Keys.RESOURCES_PATH + Keys.TEST_DOCUMENT_PDF;
		this.documentUploadPage.selectFileToUpload(filePath);
	}

	@Then("verify duplicate uploaded filename error exists")
	public void verifyDuplicateFileError() {
		// assert error element exists
		String errorMsg = documentUploadPage.getDuplicateFileError();
		assertEquals(DocumentUploadPage.DUPLICATE_FILE_ERROR, errorMsg);
	}

	@Then("verify duplicate uploaded filename error doesn't exist")
	public void verifyDuplicateFileErrorNotExist() {
		// assert error element exists
		String errorMsg = documentUploadPage.getDuplicateFileError();
		assertNull(errorMsg);
	}
}
