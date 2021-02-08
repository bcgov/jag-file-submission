package ca.bc.gov.open.jag.efiling.demo;

import ca.bc.gov.open.jag.efilingcommons.model.Party;
import ca.bc.gov.open.jag.efilingcommons.submission.EfilingReviewService;
import ca.bc.gov.open.jag.efilingcommons.submission.models.DeleteSubmissionDocumentRequest;
import ca.bc.gov.open.jag.efilingcommons.submission.models.FilingPackageRequest;
import ca.bc.gov.open.jag.efilingcommons.submission.models.review.PackagePayment;
import ca.bc.gov.open.jag.efilingcommons.submission.models.review.ReviewCourt;
import ca.bc.gov.open.jag.efilingcommons.submission.models.review.ReviewDocument;
import ca.bc.gov.open.jag.efilingcommons.submission.models.review.ReviewFilingPackage;
import org.joda.time.DateTime;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;

import java.text.MessageFormat;
import java.util.*;

public class EfilingReviewServiceDemoImpl implements EfilingReviewService {

    @Override
    public Optional<ReviewFilingPackage> findStatusByPackage(FilingPackageRequest filingPackageRequest) {
        if (filingPackageRequest.getPackageNo().equals(BigDecimal.ONE)) {
            return Optional.of(createReviewPackage());
        } else {
            return Optional.empty();
        }
    }

    @Override
    public List<ReviewFilingPackage> findStatusByClient(FilingPackageRequest filingPackageRequest) {
        return Collections.singletonList(createReviewPackage());
    }

    @Override
    public Optional<byte[]> getSubmissionSheet(BigDecimal packageNumber) {

        return getDocument();

    }

    @Override
    public Optional<byte[]> getSubmittedDocument(BigDecimal packageNumber, String documentIdentifier) {

        return getDocument();

    }

    @Override
    public void deleteSubmittedDocument(DeleteSubmissionDocumentRequest deleteSubmissionDocumentRequest) {
        //Do nothing
    }

    private Optional<byte[]> getDocument() {
        try {
            InputStream initialStream = getClass().getResourceAsStream("/demo-file/test-document.pdf");
            byte[] targetArray = new byte[initialStream.available()];
            initialStream.read(targetArray);
            return Optional.of(targetArray);
        } catch (IOException e) {
            return Optional.empty();
        }
    }

    private ReviewFilingPackage createReviewPackage() {
        ReviewFilingPackage reviewFilingPackage = new ReviewFilingPackage();
        reviewFilingPackage.setFirstName("Han");
        reviewFilingPackage.setLastName("Solo");
        reviewFilingPackage.setFilingCommentsTxt(MessageFormat.format("Lorem ipsum dolor sit amet, consectetur adipiscing elit, {1} sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.{0}{0}Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.", System.lineSeparator(), "<script>alert(\"Hello\");</script>"));
        reviewFilingPackage.setHasChecklist(false);
        reviewFilingPackage.setHasRegistryNotice(false);
        reviewFilingPackage.setPackageNo("1");
        reviewFilingPackage.setSubmittedDate(DateTime.parse("2020-5-5"));
        reviewFilingPackage.setCourt(createCourt());
        reviewFilingPackage.setParties(createParty());
        reviewFilingPackage.setDocuments(createReviewDocuments());
        reviewFilingPackage.setPayments(createPayment());
        return reviewFilingPackage;
    }

    private ReviewCourt createCourt() {
        ReviewCourt reviewCourt = new ReviewCourt();
        reviewCourt.setFileNumber("123");
        reviewCourt.setCourtClass("F");
        reviewCourt.setLevel("P");
        reviewCourt.setLocationCd("KEL");
        reviewCourt.setLocationId(BigDecimal.ONE);
        reviewCourt.setLocationName("Kelowna Law Courts");
        reviewCourt.setExistingFileYN(false);
        reviewCourt.setDivision("DIVISION");
        reviewCourt.setLocationDescription("DESCRIPTION");
        reviewCourt.setParticipatingClass("DESCRIPTION");
        reviewCourt.setLocationDescription("DESCRIPTION");
        reviewCourt.setLevelDescription("DESCRIPTION");
        reviewCourt.setClassDescription("DESCRIPTION");
        return reviewCourt;
    }

    private List<ReviewDocument> createReviewDocuments() {

        List<ReviewDocument> documents = new ArrayList<>();
        documents.add(createReviewDocument("1","SUB","Submitted", "test-document.pdf","1"));
        documents.add(createReviewDocument("2", "REF", "Referred", "test-document-hello.pdf","2"));
        documents.add(createReviewDocument("3", "FILE", "Filed", "File12341234.pdf","3"));
        documents.add(createReviewDocument("4", "WDRN", "Withdrawn", "Registration-of-divorce-proceedings2020265814.pdf","4"));
        documents.add(createReviewDocument("5", "CCOR", "Courtesy Correction", "loooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooooong-filename.pdf","5"));
        documents.add(createReviewDocument("6", "RSUB", "Resubmitted", "123.pdf","6"));
        documents.add(createReviewDocument("7", "REJ", "Rejected", "rejected-document.pdf","7"));

        return documents;

    }

    private ReviewDocument createReviewDocument(String documentId, String statusCd, String status, String filename,
                                                String seqNo) {
        ReviewDocument reviewDocument = new ReviewDocument();
        reviewDocument.setDateFiled(DateTime.parse("2020-5-5"));
        reviewDocument.setDocumentId(documentId);
        reviewDocument.setDocumentType("Affidavit");
        reviewDocument.setDocumentTypeCd("AFF");
        reviewDocument.setDocumentUploadStatusCd("CMPL");
        reviewDocument.setFileName(filename);
        reviewDocument.setInitiatingDoc(false);
        reviewDocument.setLargeFileYn("N");
        reviewDocument.setPackageId("1");
        reviewDocument.setPackageSeqNo(seqNo);
        reviewDocument.setPaymentProcessed(false);
        reviewDocument.setStatus(status);
        reviewDocument.setStatusCode(statusCd);
        reviewDocument.setStatusDate(DateTime.parse("2020-12-17"));
        reviewDocument.setTrialDivision(false);
        reviewDocument.setXmlDoc(false);
        return reviewDocument;
    }

    private List<Party> createParty() {
        return Collections.singletonList(Party.builder()
                .firstName("Efile")
                .middleName("test")
                .lastName("QA")
                .roleTypeCd("APP")
                .partyTypeCd("IND")
                .create());

    }

    private List<PackagePayment> createPayment() {
        PackagePayment packagePaymentOne = new PackagePayment();
        packagePaymentOne.setFeeExmpt(false);
        packagePaymentOne.setPaymentCategory(BigDecimal.ONE);
        packagePaymentOne.setProcessedAmt(BigDecimal.ONE);
        packagePaymentOne.setServiceId(BigDecimal.ONE);
        packagePaymentOne.setSubmittedAmt(BigDecimal.ONE);
        packagePaymentOne.setTransactionDtm(DateTime.parse("2020-12-17"));
        PackagePayment packagePaymentTwo = new PackagePayment();
        packagePaymentTwo.setFeeExmpt(true);
        packagePaymentTwo.setPaymentCategory(BigDecimal.TEN);
        packagePaymentTwo.setSubmittedAmt(BigDecimal.ONE);
        packagePaymentTwo.setTransactionDesc("Affidavit");

        return Arrays.asList(packagePaymentOne, packagePaymentTwo);
    }
}
