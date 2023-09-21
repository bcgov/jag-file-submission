package ca.bc.gov.open.jag.efiling.demo;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.Optional;

import org.joda.time.DateTime;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import ca.bc.gov.open.jag.efilingcommons.model.RushDocumentRequest;
import ca.bc.gov.open.jag.efilingcommons.submission.models.DeleteSubmissionDocumentRequest;
import ca.bc.gov.open.jag.efilingcommons.submission.models.FilingPackageRequest;
import ca.bc.gov.open.jag.efilingcommons.submission.models.ReportRequest;
import ca.bc.gov.open.jag.efilingcommons.submission.models.review.ReviewFilingPackage;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class EfilingReviewServiceDemoImplTest {
    public static final String DESCRIPTION = "DESCRIPTION";
    public static final String COMMENTS = MessageFormat.format("Lorem ipsum dolor sit amet, consectetur adipiscing elit, {1} sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.{0}{0}Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.", System.lineSeparator(), "<script>alert(\"Hello\");</script>");
    EfilingReviewServiceDemoImpl sut;

    @BeforeAll
    public void setup() {
        sut = new EfilingReviewServiceDemoImpl();
    }

    @Test
    @DisplayName("OK: with correct id return payload")
    public void requestPackageOneShouldReturnData() {
        Optional<ReviewFilingPackage> result = sut.findStatusByPackage(new FilingPackageRequest(BigDecimal.ONE, BigDecimal.ONE, BigDecimal.ONE, ""));

        Assertions.assertTrue(result.isPresent());
        //Package
        Assertions.assertEquals("Han", result.get().getFirstName());
        Assertions.assertEquals("Solo", result.get().getLastName());
        Assertions.assertFalse(result.get().getHasChecklist());
        Assertions.assertTrue(result.get().getHasRegistryNotice());
        Assertions.assertEquals("1", result.get().getPackageNo());
        Assertions.assertEquals(DateTime.parse("2020-5-5"), result.get().getSubmittedDate());
        Assertions.assertEquals(COMMENTS, result.get().getFilingCommentsTxt());

        //Court
        Assertions.assertEquals("F", result.get().getCourt().getCourtClass());
        Assertions.assertEquals("P", result.get().getCourt().getLevel());
        Assertions.assertEquals("KEL", result.get().getCourt().getLocationCd());
        Assertions.assertEquals(BigDecimal.ONE, result.get().getCourt().getLocationId());
        Assertions.assertEquals("Kelowna Law Courts", result.get().getCourt().getLocationName());
        Assertions.assertEquals("DIVISION", result.get().getCourt().getDivision());
        Assertions.assertEquals(DESCRIPTION, result.get().getCourt().getLocationDescription());
        Assertions.assertEquals(DESCRIPTION, result.get().getCourt().getParticipatingClass());
        Assertions.assertEquals(DESCRIPTION, result.get().getCourt().getLocationDescription());
        Assertions.assertEquals(DESCRIPTION, result.get().getCourt().getLevelDescription());
        Assertions.assertEquals(DESCRIPTION, result.get().getCourt().getClassDescription());
        Assertions.assertEquals("123", result.get().getCourt().getFileNumber());
        Assertions.assertFalse(result.get().getCourt().getExistingFileYN());

        //Documents
        Assertions.assertEquals(7, result.get().getDocuments().size());
        Assertions.assertEquals(DateTime.parse("2020-5-5"), result.get().getDocuments().get(0).getDateFiled());
        Assertions.assertEquals("1", result.get().getDocuments().get(0).getDocumentId());
        Assertions.assertEquals("Affidavit", result.get().getDocuments().get(0).getDocumentType());
        Assertions.assertEquals("AFF", result.get().getDocuments().get(0).getDocumentTypeCd());
        Assertions.assertEquals("CMPL", result.get().getDocuments().get(0).getDocumentUploadStatusCd());
        Assertions.assertEquals("test-document.pdf", result.get().getDocuments().get(0).getFileName());
        Assertions.assertFalse(result.get().getDocuments().get(0).getInitiatingDoc());
        Assertions.assertEquals("N", result.get().getDocuments().get(0).getLargeFileYn());
        Assertions.assertEquals("1", result.get().getDocuments().get(0).getPackageId());
        Assertions.assertEquals("1", result.get().getDocuments().get(0).getPackageSeqNo());
        Assertions.assertFalse(result.get().getDocuments().get(0).getPaymentProcessed());
        Assertions.assertEquals("Submitted", result.get().getDocuments().get(0).getStatus());
        Assertions.assertEquals("SUB", result.get().getDocuments().get(0).getStatusCode());
        Assertions.assertEquals(DateTime.parse("2020-12-17"), result.get().getDocuments().get(0).getStatusDate());
        Assertions.assertFalse(result.get().getDocuments().get(0).getTrialDivision());
        Assertions.assertFalse(result.get().getDocuments().get(0).getXmlDoc());

        //Parties
        Assertions.assertEquals(2, result.get().getParties().size());
        Assertions.assertEquals("Bob", result.get().getParties().get(0).getFirstName());
        Assertions.assertEquals("Q", result.get().getParties().get(0).getMiddleName());
        Assertions.assertEquals("Ross", result.get().getParties().get(0).getLastName());
        Assertions.assertEquals("APP", result.get().getParties().get(0).getRoleTypeCd());
        Assertions.assertEquals("Applicant", result.get().getParties().get(0).getRoleTypeDesc());
        Assertions.assertEquals("Individual", result.get().getParties().get(0).getPartyTypeDesc());

        //Organization
        Assertions.assertEquals(2, result.get().getOrganizations().size());
        Assertions.assertEquals("The Organization Org.", result.get().getOrganizations().get(0).getName());
        Assertions.assertEquals("APP", result.get().getOrganizations().get(0).getRoleTypeCd());
        Assertions.assertEquals("Applicant", result.get().getOrganizations().get(0).getRoleTypeDesc());
        Assertions.assertEquals("Organization", result.get().getOrganizations().get(0).getPartyTypeDesc());

        //Payments
        Assertions.assertEquals(3, result.get().getPayments().size());
        Assertions.assertFalse(result.get().getPayments().get(0).getFeeExmpt());
        Assertions.assertEquals(BigDecimal.ONE, result.get().getPayments().get(0).getPaymentCategory());
        Assertions.assertEquals(new BigDecimal(7), result.get().getPayments().get(0).getProcessedAmt());
        Assertions.assertEquals(BigDecimal.ONE, result.get().getPayments().get(0).getServiceId());
        Assertions.assertEquals(new BigDecimal(7), result.get().getPayments().get(0).getSubmittedAmt());
        Assertions.assertEquals(DateTime.parse("2020-12-17"), result.get().getPayments().get(0).getTransactionDtm());

        Assertions.assertTrue(result.get().getPayments().get(1).getFeeExmpt());
        Assertions.assertEquals(new BigDecimal(3), result.get().getPayments().get(1).getPaymentCategory());
        Assertions.assertEquals(BigDecimal.ONE, result.get().getPayments().get(1).getSubmittedAmt());
        Assertions.assertEquals("Affidavit", result.get().getPayments().get(1).getTransactionDesc());

        //Rush
        Assertions.assertEquals("hello@hello.com", result.get().getRushOrder().getContactEmailTxt());
        Assertions.assertEquals("Bob", result.get().getRushOrder().getContactFirstGivenNm());
        Assertions.assertEquals("Ross", result.get().getRushOrder().getContactSurnameNm());
        Assertions.assertEquals("Paint It", result.get().getRushOrder().getContactOrganizationNm());
        Assertions.assertEquals("1231231234", result.get().getRushOrder().getContactPhoneNo());
        Assertions.assertEquals("Processing", result.get().getRushOrder().getCurrentStatusDsc());
        Assertions.assertEquals(BigDecimal.ONE, result.get().getRushOrder().getCtryId());
        Assertions.assertEquals("Canada", result.get().getRushOrder().getCountryDsc());
        Assertions.assertNotNull(result.get().getRushOrder().getCourtOrderDt());
        Assertions.assertEquals(BigDecimal.ONE, result.get().getRushOrder().getPackageId());
        Assertions.assertEquals(DateTime.parse("2020-12-17"), result.get().getRushOrder().getCourtOrderDt());
        Assertions.assertEquals("Registry notice reason text", result.get().getRushOrder().getProcessingCommentTxt());
        Assertions.assertEquals("This is a reason. This is a reason. This is a reason. This is a reason.", result.get().getRushOrder().getRushFilingReasonTxt());

        //Rush Documents
        Assertions.assertEquals(2, result.get().getRushOrder().getSupportDocs().size());

        Assertions.assertEquals("Test.pdf", result.get().getRushOrder().getSupportDocs().get(0).getClientFileNm());
        Assertions.assertNotNull(result.get().getRushOrder().getSupportDocs().get(0).getEntDtm());
        Assertions.assertEquals("1", result.get().getRushOrder().getSupportDocs().get(0).getEntUserId());
        Assertions.assertEquals("www.google.com", result.get().getRushOrder().getSupportDocs().get(0).getFileServer());
        Assertions.assertEquals("9b35f5d6-50e9-4cd5-9d46-8ce1f9e484c8", result.get().getRushOrder().getSupportDocs().get(0).getObjectGuid());
        Assertions.assertEquals(BigDecimal.ONE, result.get().getRushOrder().getSupportDocs().get(0).getProcessItemSeqNo());
        Assertions.assertEquals(BigDecimal.ONE, result.get().getRushOrder().getSupportDocs().get(0).getProcessRequestId());
        Assertions.assertEquals(BigDecimal.ONE, result.get().getRushOrder().getSupportDocs().get(0).getProcessSupportDocSeqNo());
        Assertions.assertEquals("Test.pdf", result.get().getRushOrder().getSupportDocs().get(0).getTempFileName());
        Assertions.assertEquals("1", result.get().getRushOrder().getSupportDocs().get(0).getUpdUserId());
        Assertions.assertNotNull(result.get().getRushOrder().getSupportDocs().get(0));

        Assertions.assertEquals("Test1.pdf", result.get().getRushOrder().getSupportDocs().get(1).getClientFileNm());
        Assertions.assertNotNull(result.get().getRushOrder().getSupportDocs().get(1).getEntDtm());
        Assertions.assertEquals("1", result.get().getRushOrder().getSupportDocs().get(1).getEntUserId());
        Assertions.assertEquals("www.google.com", result.get().getRushOrder().getSupportDocs().get(1).getFileServer());
        Assertions.assertEquals("9b35f5d6-50e9-4cd5-9d46-8ce1f9e484c8", result.get().getRushOrder().getSupportDocs().get(1).getObjectGuid());
        Assertions.assertEquals(BigDecimal.ONE, result.get().getRushOrder().getSupportDocs().get(1).getProcessItemSeqNo());
        Assertions.assertEquals(BigDecimal.ONE, result.get().getRushOrder().getSupportDocs().get(1).getProcessRequestId());
        Assertions.assertEquals(BigDecimal.ONE, result.get().getRushOrder().getSupportDocs().get(1).getProcessSupportDocSeqNo());
        Assertions.assertEquals("Test.pdf", result.get().getRushOrder().getSupportDocs().get(1).getTempFileName());
        Assertions.assertEquals("1", result.get().getRushOrder().getSupportDocs().get(1).getUpdUserId());
        Assertions.assertNotNull(result.get().getRushOrder().getSupportDocs().get(1).getUpdDtm());

        Assertions.assertEquals("http://localhost:8080/wherearemypackage", result.get().getPackageLinks().getPackageHistoryUrl());

    }

    @Test
    @DisplayName("OK: with correct id return payload")
    public void requestPackageTwoShouldReturnData() {
        Optional<ReviewFilingPackage> result = sut.findStatusByPackage(new FilingPackageRequest(BigDecimal.ONE, BigDecimal.ONE, new BigDecimal(2), ""));

        Assertions.assertTrue(result.isPresent());
        //Package
        Assertions.assertEquals("Han", result.get().getFirstName());
        Assertions.assertEquals("Solo", result.get().getLastName());
        Assertions.assertFalse(result.get().getHasChecklist());
        Assertions.assertFalse(result.get().getHasRegistryNotice());
        Assertions.assertEquals("2", result.get().getPackageNo());
        Assertions.assertEquals(DateTime.parse("2020-5-5"), result.get().getSubmittedDate());
        Assertions.assertEquals(COMMENTS, result.get().getFilingCommentsTxt());

        //Court
        Assertions.assertEquals("F", result.get().getCourt().getCourtClass());
        Assertions.assertEquals("P", result.get().getCourt().getLevel());
        Assertions.assertEquals("KEL", result.get().getCourt().getLocationCd());
        Assertions.assertEquals(BigDecimal.ONE, result.get().getCourt().getLocationId());
        Assertions.assertEquals("Kelowna Law Courts", result.get().getCourt().getLocationName());
        Assertions.assertEquals("DIVISION", result.get().getCourt().getDivision());
        Assertions.assertEquals(DESCRIPTION, result.get().getCourt().getLocationDescription());
        Assertions.assertEquals(DESCRIPTION, result.get().getCourt().getParticipatingClass());
        Assertions.assertEquals(DESCRIPTION, result.get().getCourt().getLocationDescription());
        Assertions.assertEquals(DESCRIPTION, result.get().getCourt().getLevelDescription());
        Assertions.assertEquals(DESCRIPTION, result.get().getCourt().getClassDescription());
        Assertions.assertEquals("123", result.get().getCourt().getFileNumber());
        Assertions.assertFalse(result.get().getCourt().getExistingFileYN());

        Assertions.assertEquals(7, result.get().getDocuments().size());
        Assertions.assertEquals(DateTime.parse("2020-5-5"), result.get().getDocuments().get(0).getDateFiled());
        Assertions.assertEquals("1", result.get().getDocuments().get(0).getDocumentId());
        Assertions.assertEquals("Affidavit", result.get().getDocuments().get(0).getDocumentType());
        Assertions.assertEquals("AFF", result.get().getDocuments().get(0).getDocumentTypeCd());
        Assertions.assertEquals("CMPL", result.get().getDocuments().get(0).getDocumentUploadStatusCd());
        Assertions.assertEquals("test-document.pdf", result.get().getDocuments().get(0).getFileName());
        Assertions.assertFalse(result.get().getDocuments().get(0).getInitiatingDoc());
        Assertions.assertEquals("N", result.get().getDocuments().get(0).getLargeFileYn());
        Assertions.assertEquals("1", result.get().getDocuments().get(0).getPackageId());
        Assertions.assertEquals("1", result.get().getDocuments().get(0).getPackageSeqNo());
        Assertions.assertFalse(result.get().getDocuments().get(0).getPaymentProcessed());
        Assertions.assertEquals("Submitted", result.get().getDocuments().get(0).getStatus());
        Assertions.assertEquals("SUB", result.get().getDocuments().get(0).getStatusCode());
        Assertions.assertEquals(DateTime.parse("2020-12-17"), result.get().getDocuments().get(0).getStatusDate());
        Assertions.assertFalse(result.get().getDocuments().get(0).getTrialDivision());
        Assertions.assertFalse(result.get().getDocuments().get(0).getXmlDoc());

        Assertions.assertEquals(2, result.get().getParties().size());
        Assertions.assertEquals("Bob", result.get().getParties().get(0).getFirstName());
        Assertions.assertEquals("Q", result.get().getParties().get(0).getMiddleName());
        Assertions.assertEquals("Ross", result.get().getParties().get(0).getLastName());
        Assertions.assertEquals("APP", result.get().getParties().get(0).getRoleTypeCd());
        Assertions.assertEquals("Applicant", result.get().getParties().get(0).getRoleTypeDesc());
        Assertions.assertEquals("Individual", result.get().getParties().get(0).getPartyTypeDesc());

        Assertions.assertEquals(2, result.get().getOrganizations().size());
        Assertions.assertEquals("The Organization Org.", result.get().getOrganizations().get(0).getName());
        Assertions.assertEquals("APP", result.get().getOrganizations().get(0).getRoleTypeCd());
        Assertions.assertEquals("Applicant", result.get().getOrganizations().get(0).getRoleTypeDesc());
        Assertions.assertEquals("Organization", result.get().getOrganizations().get(0).getPartyTypeDesc());

        Assertions.assertEquals(3, result.get().getPayments().size());
        Assertions.assertFalse(result.get().getPayments().get(0).getFeeExmpt());
        Assertions.assertEquals(BigDecimal.ONE, result.get().getPayments().get(0).getPaymentCategory());
        Assertions.assertEquals(new BigDecimal(7), result.get().getPayments().get(0).getProcessedAmt());
        Assertions.assertEquals(BigDecimal.ONE, result.get().getPayments().get(0).getServiceId());
        Assertions.assertEquals(new BigDecimal(7), result.get().getPayments().get(0).getSubmittedAmt());
        Assertions.assertEquals(DateTime.parse("2020-12-17"), result.get().getPayments().get(0).getTransactionDtm());

        Assertions.assertTrue(result.get().getPayments().get(1).getFeeExmpt());
        Assertions.assertEquals(new BigDecimal(3), result.get().getPayments().get(1).getPaymentCategory());
        Assertions.assertEquals(BigDecimal.ONE, result.get().getPayments().get(1).getSubmittedAmt());
        Assertions.assertEquals("Affidavit", result.get().getPayments().get(1).getTransactionDesc());


        Assertions.assertEquals(new BigDecimal(1), result.get().getRushOrder().getPackageId());
        Assertions.assertNotNull(result.get().getRushOrder().getCourtOrderDt());
        Assertions.assertEquals("This is a reason. This is a reason. This is a reason. This is a reason.", result.get().getRushOrder().getRushFilingReasonTxt());
        Assertions.assertEquals(2, result.get().getRushOrder().getSupportDocs().size());

        Assertions.assertEquals("Test.pdf", result.get().getRushOrder().getSupportDocs().get(0).getClientFileNm());
        Assertions.assertNotNull(result.get().getRushOrder().getSupportDocs().get(0).getEntDtm());
        Assertions.assertEquals("1", result.get().getRushOrder().getSupportDocs().get(0).getEntUserId());
        Assertions.assertEquals("www.google.com",result.get().getRushOrder().getSupportDocs().get(0).getFileServer());
        Assertions.assertEquals("9b35f5d6-50e9-4cd5-9d46-8ce1f9e484c8", result.get().getRushOrder().getSupportDocs().get(0).getObjectGuid());
        Assertions.assertEquals(BigDecimal.ONE, result.get().getRushOrder().getSupportDocs().get(0).getProcessItemSeqNo());
        Assertions.assertEquals(BigDecimal.ONE, result.get().getRushOrder().getSupportDocs().get(0).getProcessRequestId());
        Assertions.assertEquals(BigDecimal.ONE, result.get().getRushOrder().getSupportDocs().get(0).getProcessSupportDocSeqNo());
        Assertions.assertEquals("Test.pdf", result.get().getRushOrder().getSupportDocs().get(0).getTempFileName());
        Assertions.assertEquals("1", result.get().getRushOrder().getSupportDocs().get(0).getUpdUserId());
        Assertions.assertNotNull(result.get().getRushOrder().getSupportDocs().get(0).getUpdDtm());

        Assertions.assertEquals("Test1.pdf", result.get().getRushOrder().getSupportDocs().get(1).getClientFileNm());
        Assertions.assertNotNull(result.get().getRushOrder().getSupportDocs().get(1).getEntDtm());
        Assertions.assertEquals("1", result.get().getRushOrder().getSupportDocs().get(1).getEntUserId());
        Assertions.assertEquals("www.google.com",result.get().getRushOrder().getSupportDocs().get(1).getFileServer());
        Assertions.assertEquals("9b35f5d6-50e9-4cd5-9d46-8ce1f9e484c8", result.get().getRushOrder().getSupportDocs().get(1).getObjectGuid());
        Assertions.assertEquals(BigDecimal.ONE, result.get().getRushOrder().getSupportDocs().get(1).getProcessItemSeqNo());
        Assertions.assertEquals(BigDecimal.ONE, result.get().getRushOrder().getSupportDocs().get(1).getProcessRequestId());
        Assertions.assertEquals(BigDecimal.ONE, result.get().getRushOrder().getSupportDocs().get(1).getProcessSupportDocSeqNo());
        Assertions.assertEquals("Test.pdf", result.get().getRushOrder().getSupportDocs().get(1).getTempFileName());
        Assertions.assertEquals("1", result.get().getRushOrder().getSupportDocs().get(1).getUpdUserId());
        Assertions.assertNotNull(result.get().getRushOrder().getSupportDocs().get(1).getUpdDtm());

        Assertions.assertEquals("http://localhost:8080/wherearemypackage", result.get().getPackageLinks().getPackageHistoryUrl());

    }

    @Test
    @DisplayName("OK: with correct id return payload")
    public void requestPackageThreeShouldReturnData() {
        Optional<ReviewFilingPackage> result = sut.findStatusByPackage(new FilingPackageRequest(BigDecimal.ONE, BigDecimal.ONE, new BigDecimal(3), ""));

        Assertions.assertTrue(result.isPresent());
        //Package
        Assertions.assertEquals("Han", result.get().getFirstName());
        Assertions.assertEquals("Solo", result.get().getLastName());
        Assertions.assertFalse(result.get().getHasChecklist());
        Assertions.assertFalse(result.get().getHasRegistryNotice());
        Assertions.assertEquals("3", result.get().getPackageNo());
        Assertions.assertEquals(DateTime.parse("2020-5-5"), result.get().getSubmittedDate());
        Assertions.assertEquals(COMMENTS, result.get().getFilingCommentsTxt());

        //Court
        Assertions.assertEquals("F", result.get().getCourt().getCourtClass());
        Assertions.assertEquals("P", result.get().getCourt().getLevel());
        Assertions.assertEquals("KEL", result.get().getCourt().getLocationCd());
        Assertions.assertEquals(BigDecimal.ONE, result.get().getCourt().getLocationId());
        Assertions.assertEquals("Kelowna Law Courts", result.get().getCourt().getLocationName());
        Assertions.assertEquals("DIVISION", result.get().getCourt().getDivision());
        Assertions.assertEquals(DESCRIPTION, result.get().getCourt().getLocationDescription());
        Assertions.assertEquals(DESCRIPTION, result.get().getCourt().getParticipatingClass());
        Assertions.assertEquals(DESCRIPTION, result.get().getCourt().getLocationDescription());
        Assertions.assertEquals(DESCRIPTION, result.get().getCourt().getLevelDescription());
        Assertions.assertEquals(DESCRIPTION, result.get().getCourt().getClassDescription());
        Assertions.assertEquals("123", result.get().getCourt().getFileNumber());
        Assertions.assertFalse(result.get().getCourt().getExistingFileYN());

        Assertions.assertEquals(7, result.get().getDocuments().size());
        Assertions.assertEquals(DateTime.parse("2020-5-5"), result.get().getDocuments().get(0).getDateFiled());
        Assertions.assertEquals("1", result.get().getDocuments().get(0).getDocumentId());
        Assertions.assertEquals("Affidavit", result.get().getDocuments().get(0).getDocumentType());
        Assertions.assertEquals("AFF", result.get().getDocuments().get(0).getDocumentTypeCd());
        Assertions.assertEquals("CMPL", result.get().getDocuments().get(0).getDocumentUploadStatusCd());
        Assertions.assertEquals("test-document.pdf", result.get().getDocuments().get(0).getFileName());
        Assertions.assertFalse(result.get().getDocuments().get(0).getInitiatingDoc());
        Assertions.assertEquals("N", result.get().getDocuments().get(0).getLargeFileYn());
        Assertions.assertEquals("1", result.get().getDocuments().get(0).getPackageId());
        Assertions.assertEquals("1", result.get().getDocuments().get(0).getPackageSeqNo());
        Assertions.assertFalse(result.get().getDocuments().get(0).getPaymentProcessed());
        Assertions.assertEquals("Submitted", result.get().getDocuments().get(0).getStatus());
        Assertions.assertEquals("SUB", result.get().getDocuments().get(0).getStatusCode());
        Assertions.assertEquals(DateTime.parse("2020-12-17"), result.get().getDocuments().get(0).getStatusDate());
        Assertions.assertFalse(result.get().getDocuments().get(0).getTrialDivision());
        Assertions.assertFalse(result.get().getDocuments().get(0).getXmlDoc());

        Assertions.assertEquals(2, result.get().getParties().size());
        Assertions.assertEquals("Bob", result.get().getParties().get(0).getFirstName());
        Assertions.assertEquals("Q", result.get().getParties().get(0).getMiddleName());
        Assertions.assertEquals("Ross", result.get().getParties().get(0).getLastName());
        Assertions.assertEquals("APP", result.get().getParties().get(0).getRoleTypeCd());
        Assertions.assertEquals("Applicant", result.get().getParties().get(0).getRoleTypeDesc());
        Assertions.assertEquals("Individual", result.get().getParties().get(0).getPartyTypeDesc());

        Assertions.assertEquals(2, result.get().getOrganizations().size());
        Assertions.assertEquals("The Organization Org.", result.get().getOrganizations().get(0).getName());
        Assertions.assertEquals("APP", result.get().getOrganizations().get(0).getRoleTypeCd());
        Assertions.assertEquals("Applicant", result.get().getOrganizations().get(0).getRoleTypeDesc());
        Assertions.assertEquals("Organization", result.get().getOrganizations().get(0).getPartyTypeDesc());

        Assertions.assertEquals(3, result.get().getPayments().size());
        Assertions.assertFalse(result.get().getPayments().get(0).getFeeExmpt());
        Assertions.assertEquals(BigDecimal.ONE, result.get().getPayments().get(0).getPaymentCategory());
        Assertions.assertEquals(new BigDecimal(7), result.get().getPayments().get(0).getProcessedAmt());
        Assertions.assertEquals(BigDecimal.ONE, result.get().getPayments().get(0).getServiceId());
        Assertions.assertEquals(new BigDecimal(7), result.get().getPayments().get(0).getSubmittedAmt());
        Assertions.assertEquals(DateTime.parse("2020-12-17"), result.get().getPayments().get(0).getTransactionDtm());

        Assertions.assertTrue(result.get().getPayments().get(1).getFeeExmpt());
        Assertions.assertEquals(new BigDecimal(3), result.get().getPayments().get(1).getPaymentCategory());
        Assertions.assertEquals(BigDecimal.ONE, result.get().getPayments().get(1).getSubmittedAmt());
        Assertions.assertEquals("Affidavit", result.get().getPayments().get(1).getTransactionDesc());

        Assertions.assertEquals("http://localhost:8080/wherearemypackage", result.get().getPackageLinks().getPackageHistoryUrl());

    }

    @Test
    @DisplayName("No result: with not 1 return empty")
    public void withEmptyCacheShouldReturnEmpty() {

        Assertions.assertFalse(sut.findStatusByPackage(new FilingPackageRequest(BigDecimal.ZERO, BigDecimal.ONE, BigDecimal.ZERO, "")).isPresent());

    }

    @Test
    @DisplayName("OK: demo returns a 3 element array")
    public void withRequestReturnThreeElementArray() {

        Assertions.assertEquals(3, sut.findStatusByClient(new FilingPackageRequest(BigDecimal.ONE, BigDecimal.ONE, BigDecimal.ONE, null)).size());

    }

    @Test
    @DisplayName("OK: demo returns a 2 element array")
    public void withFLARequestReturnTwoElementArray() {

        Assertions.assertEquals(2, sut.findStatusByClient(new FilingPackageRequest(BigDecimal.ONE, BigDecimal.ONE, BigDecimal.ONE, Keys.PARENT_APPLICATION_FLA)).size());

    }

    @Test
    @DisplayName("OK: demo returns a 1 element array")
    public void withCOARequestReturnOneElementArray() {

        Assertions.assertEquals(1, sut.findStatusByClient(new FilingPackageRequest(BigDecimal.ONE, BigDecimal.ONE, BigDecimal.ONE, Keys.PARENT_APPLICATION_COA)).size());

    }

    @Test
    @DisplayName("OK: demo returns a 1 element array")
    public void withOTHERRequestReturnOneElementArray() {

        Assertions.assertEquals(1, sut.findStatusByClient(new FilingPackageRequest(BigDecimal.ONE, BigDecimal.ONE, BigDecimal.ONE, Keys.PARENT_APPLICATION_OTHER)).size());

    }

    @Test
    @DisplayName("OK: demo returns a empty array")
    public void withUNKNOWNRequestReturnEmptyArray() {

        Assertions.assertEquals(0, sut.findStatusByClient(new FilingPackageRequest(BigDecimal.ONE, BigDecimal.ONE, BigDecimal.ONE, "UNKNOWN")).size());

    }

    @Test
    @DisplayName("OK: demo returns a document byte array")
    public void withRequestReturnByteArray() {

        Optional<byte[]> result = sut.getReport(ReportRequest.builder().create());

        Assertions.assertTrue(result.isPresent());

    }

    @Test
    @DisplayName("OK: demo returns a document byte array")
    public void withDocumentRequestReturnByteArray() {

        Optional<byte[]> result = sut.getSubmittedDocument(BigDecimal.ONE);

        Assertions.assertTrue(result.isPresent());

    }

    @Test
    @DisplayName("OK: demo returns a document byte array")
    public void withRushDocumentRequestReturnByteArray() {

        Optional<byte[]> result = sut.getRushDocument(RushDocumentRequest.builder().create());

        Assertions.assertTrue(result.isPresent());

    }

    @Test
    @DisplayName("OK: demo returns a boolean")
    public void withDocumentRequestBoolean() {

        Assertions.assertDoesNotThrow(() -> sut.deleteSubmittedDocument(new DeleteSubmissionDocumentRequest(BigDecimal.ONE, BigDecimal.ONE, "","TEST")));

    }

}
