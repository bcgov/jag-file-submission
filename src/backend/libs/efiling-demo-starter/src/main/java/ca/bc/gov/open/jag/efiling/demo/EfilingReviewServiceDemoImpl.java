package ca.bc.gov.open.jag.efiling.demo;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;

import ca.bc.gov.open.jag.efilingcommons.model.Individual;
import ca.bc.gov.open.jag.efilingcommons.model.Organization;
import ca.bc.gov.open.jag.efilingcommons.model.RushDocumentRequest;
import ca.bc.gov.open.jag.efilingcommons.submission.EfilingReviewService;
import ca.bc.gov.open.jag.efilingcommons.submission.models.DeleteSubmissionDocumentRequest;
import ca.bc.gov.open.jag.efilingcommons.submission.models.FilingPackageRequest;
import ca.bc.gov.open.jag.efilingcommons.submission.models.ReportRequest;
import ca.bc.gov.open.jag.efilingcommons.submission.models.review.PackageLinks;
import ca.bc.gov.open.jag.efilingcommons.submission.models.review.PackagePayment;
import ca.bc.gov.open.jag.efilingcommons.submission.models.review.ReviewCourt;
import ca.bc.gov.open.jag.efilingcommons.submission.models.review.ReviewDocument;
import ca.bc.gov.open.jag.efilingcommons.submission.models.review.ReviewFilingPackage;
import ca.bc.gov.open.jag.efilingcommons.submission.models.review.ReviewRushOrder;
import ca.bc.gov.open.jag.efilingcommons.submission.models.review.RushDocument;

public class EfilingReviewServiceDemoImpl implements EfilingReviewService {

    @Override
    public Optional<ReviewFilingPackage> findStatusByPackage(FilingPackageRequest filingPackageRequest) {
        if (filingPackageRequest.getPackageNo().equals(BigDecimal.ONE)) {
            return Optional.of(createReviewPackage("1", true));
        } else if (filingPackageRequest.getPackageNo().equals(new BigDecimal(2))) {
            return Optional.of(createReviewPackage("2", false));
        } else if (filingPackageRequest.getPackageNo().equals(new BigDecimal(3))) {
            return Optional.of(createReviewPackage("3", false));
        } else {
            return Optional.empty();
        }
    }

    @Override
    public List<ReviewFilingPackage> findStatusByClient(FilingPackageRequest filingPackageRequest) {

        if (StringUtils.isBlank(filingPackageRequest.getParentApplication())) {
            return Arrays.asList(createReviewPackage("1", true), createReviewPackage("2", false), createReviewPackage("3", true));
        } else if (filingPackageRequest.getParentApplication().equals(Keys.PARENT_APPLICATION_FLA)) {
            return Arrays.asList(createReviewPackage("1", true), createReviewPackage("2", false));
        } else if (filingPackageRequest.getParentApplication().equals(Keys.PARENT_APPLICATION_COA)) {
            return Collections.singletonList(createReviewPackage("3", true));
        } else if (filingPackageRequest.getParentApplication().equals(Keys.PARENT_APPLICATION_OTHER)) {
            return Collections.singletonList(createReviewPackage("4", true));
        } else {
            return new ArrayList<>();
        }

    }

    @Override
    public Optional<byte[]> getReport(ReportRequest reportRequest) {

        return getDocument();

    }

    @Override
    public Optional<byte[]> getSubmittedDocument(BigDecimal documentIdentifier) {

        return getDocument();

    }

    @Override
    public void deleteSubmittedDocument(DeleteSubmissionDocumentRequest deleteSubmissionDocumentRequest) {
        //Do nothing
    }

    @Override
    public Optional<byte[]> getRushDocument(RushDocumentRequest rushDocumentRequest) {

        return getDocument();

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

    private ReviewFilingPackage createReviewPackage(String packageNo, Boolean hasRegistry) {
        ReviewFilingPackage reviewFilingPackage = new ReviewFilingPackage();
        reviewFilingPackage.setFirstName("Han");
        reviewFilingPackage.setLastName("Solo");
        reviewFilingPackage.setFilingCommentsTxt(MessageFormat.format("Lorem ipsum dolor sit amet, consectetur adipiscing elit, {1} sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.{0}{0}Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.", System.lineSeparator(), "<script>alert(\"Hello\");</script>"));
        reviewFilingPackage.setHasChecklist(false);
        reviewFilingPackage.setHasRegistryNotice(hasRegistry);
        reviewFilingPackage.setPackageNo(packageNo);
        reviewFilingPackage.setSubmittedDate(DateTime.parse("2020-5-5"));
        reviewFilingPackage.setCourt(createCourt());
        reviewFilingPackage.setParties(createParty());
        reviewFilingPackage.setDocuments(createReviewDocuments());
        reviewFilingPackage.setPayments(createPayment());
        reviewFilingPackage.setPackageLinks(PackageLinks.builder().packageHistoryUrl("http://localhost:8080/wherearemypackage").create());
        reviewFilingPackage.setOrganizations(createOrganizations());
        reviewFilingPackage.setRushOrder(createRushOrder());
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

    private List<Individual> createParty() {

        Individual individualOne = Individual.builder()
                .firstName("Bob")
                .middleName("Q")
                .lastName("Ross")
                .roleTypeCd("APP")
                .roleTypeDesc("Applicant")
                .partyTypeDesc("Individual")
                .create();

        Individual individualTwo = Individual.builder()
                .firstName("Looooooongname")
                .middleName("Q")
                .lastName("Loooooooooong-Looooooooooonglast")
                .roleTypeCd("APP")
                .roleTypeDesc("Applicant")
                .partyTypeDesc("Individual")
                .create();

        return Arrays.asList(individualOne, individualTwo);

    }

    private List<Organization> createOrganizations() {

        Organization organizationOne = Organization.builder()
                .name("The Organization Org.")
                .roleTypeCd("APP")
                .roleTypeDesc("Applicant")
                .partyTypeDesc("Organization")
                .create();

        Organization organizationTwo = Organization.builder()
                .name("This is a very very very very loooooong organization name")
                .roleTypeCd("APP")
                .roleTypeDesc("Applicant")
                .partyTypeDesc("Organization")
                .create();

        return Arrays.asList(organizationOne, organizationTwo);

    }

    private List<PackagePayment> createPayment() {
        PackagePayment packagePaymentOne = new PackagePayment();
        packagePaymentOne.setFeeExmpt(false);
        packagePaymentOne.setPaymentCategory(BigDecimal.ONE);
        packagePaymentOne.setProcessedAmt(new BigDecimal(7));
        packagePaymentOne.setServiceId(BigDecimal.ONE);
        packagePaymentOne.setSubmittedAmt(new BigDecimal(7));
        packagePaymentOne.setTransactionDtm(DateTime.parse("2020-12-17"));
        PackagePayment packagePaymentTwo = new PackagePayment();
        packagePaymentTwo.setFeeExmpt(true);
        packagePaymentTwo.setPaymentCategory(new BigDecimal(3));
        packagePaymentTwo.setSubmittedAmt(BigDecimal.ONE);
        packagePaymentTwo.setTransactionDesc("Affidavit");
        PackagePayment packagePaymentThree = new PackagePayment();
        packagePaymentThree.setFeeExmpt(true);
        packagePaymentThree.setPaymentCategory(new BigDecimal(3));
        packagePaymentThree.setSubmittedAmt(BigDecimal.ONE);
        packagePaymentThree.setTransactionDesc("Affidavit");
        return Arrays.asList(packagePaymentOne, packagePaymentTwo, packagePaymentThree);
    }

    private ReviewRushOrder createRushOrder() {
        ReviewRushOrder reviewRushOrder = new ReviewRushOrder();
        reviewRushOrder.setContactEmailTxt("hello@hello.com");
        reviewRushOrder.setContactFirstGivenNm("Bob");
        reviewRushOrder.setContactSurnameNm("Ross");
        reviewRushOrder.setContactOrganizationNm("Paint It");
        reviewRushOrder.setContactPhoneNo("1231231234");
        reviewRushOrder.setCurrentStatusDsc("Processing");
        reviewRushOrder.setCtryId(BigDecimal.ONE);
        reviewRushOrder.setCountryDsc("Canada");
        reviewRushOrder.setCourtOrderDt(DateTime.parse("2020-12-17"));
        reviewRushOrder.setPackageId(BigDecimal.ONE);
        reviewRushOrder.setProcessReasonCd("OTHR");
        reviewRushOrder.setProcessingCommentTxt("Registry notice reason text");
        reviewRushOrder.setRushFilingReasonTxt("This is a reason. This is a reason. This is a reason. This is a reason.");
        reviewRushOrder.setSupportDocs(createRushDocuments());
        return reviewRushOrder;
    }

    private List<RushDocument> createRushDocuments() {
        RushDocument rushDocumentOne = new RushDocument();
        rushDocumentOne.setClientFileNm("Test.pdf");
        rushDocumentOne.setEntDtm(new DateTime());
        rushDocumentOne.setEntUserId("1");
        rushDocumentOne.setFileServer("www.google.com");
        rushDocumentOne.setObjectGuid("9b35f5d6-50e9-4cd5-9d46-8ce1f9e484c8");
        rushDocumentOne.setProcessItemSeqNo(BigDecimal.ONE);
        rushDocumentOne.setProcessRequestId(BigDecimal.ONE);
        rushDocumentOne.setProcessSupportDocSeqNo(BigDecimal.ONE);
        rushDocumentOne.setTempFileName("Test.pdf");
        rushDocumentOne.setUpdUserId("1");
        rushDocumentOne.setUpdDtm(new DateTime());

        RushDocument rushDocumentTwo = new RushDocument();
        rushDocumentTwo.setClientFileNm("Test1.pdf");
        rushDocumentTwo.setEntDtm(new DateTime());
        rushDocumentTwo.setEntUserId("1");
        rushDocumentTwo.setFileServer("www.google.com");
        rushDocumentTwo.setObjectGuid("9b35f5d6-50e9-4cd5-9d46-8ce1f9e484c8");
        rushDocumentTwo.setProcessItemSeqNo(BigDecimal.ONE);
        rushDocumentTwo.setProcessRequestId(BigDecimal.ONE);
        rushDocumentTwo.setProcessSupportDocSeqNo(BigDecimal.ONE);
        rushDocumentTwo.setTempFileName("Test.pdf");
        rushDocumentTwo.setUpdUserId("1");
        rushDocumentTwo.setUpdDtm(new DateTime());

        return Arrays.asList(rushDocumentOne, rushDocumentTwo);

    }
}
