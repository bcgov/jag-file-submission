package ca.bc.gov.open.jag.efiling.demo;

import ca.bc.gov.open.jag.efilingcommons.submission.models.DeleteSubmissionDocumentRequest;
import ca.bc.gov.open.jag.efilingcommons.submission.models.FilingPackageRequest;
import ca.bc.gov.open.jag.efilingcommons.submission.models.review.ReviewFilingPackage;
import org.joda.time.DateTime;
import org.junit.jupiter.api.*;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.Optional;

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
    public void withEmptyCacheShouldReturnPackage() {
        Optional<ReviewFilingPackage> result = sut.findStatusByPackage(new FilingPackageRequest(BigDecimal.ONE, BigDecimal.ONE));

        Assertions.assertTrue(result.isPresent());
        //Package
        Assertions.assertEquals("Han", result.get().getFirstName());
        Assertions.assertEquals("Solo", result.get().getLastName());
        Assertions.assertFalse(result.get().getHasChecklist());
        Assertions.assertFalse(result.get().getHasRegistryNotice());
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

        Assertions.assertEquals(1, result.get().getParties().size());
        Assertions.assertEquals("Efile", result.get().getParties().get(0).getFirstName());
        Assertions.assertEquals("test", result.get().getParties().get(0).getMiddleName());
        Assertions.assertEquals("QA", result.get().getParties().get(0).getLastName());
        Assertions.assertEquals("APP", result.get().getParties().get(0).getRoleTypeCd());
        Assertions.assertEquals("IND", result.get().getParties().get(0).getPartyTypeCd());

        Assertions.assertEquals(2, result.get().getPayments().size());
        Assertions.assertFalse(result.get().getPayments().get(0).getFeeExmpt());
        Assertions.assertEquals(BigDecimal.ONE, result.get().getPayments().get(0).getPaymentCategory());
        Assertions.assertEquals(BigDecimal.ONE, result.get().getPayments().get(0).getProcessedAmt());
        Assertions.assertEquals(BigDecimal.ONE, result.get().getPayments().get(0).getServiceId());
        Assertions.assertEquals(BigDecimal.ONE, result.get().getPayments().get(0).getSubmittedAmt());
        Assertions.assertEquals(DateTime.parse("2020-12-17"), result.get().getPayments().get(0).getTransactionDtm());

        Assertions.assertTrue(result.get().getPayments().get(1).getFeeExmpt());
        Assertions.assertEquals(BigDecimal.TEN, result.get().getPayments().get(1).getPaymentCategory());
        Assertions.assertEquals(BigDecimal.ONE, result.get().getPayments().get(1).getSubmittedAmt());
        Assertions.assertEquals("Affidavit", result.get().getPayments().get(1).getTransactionDesc());

    }


    @Test
    @DisplayName("No result: with not 1 return empty")
    public void withEmptyCacheShouldReturnEmpty() {

        Assertions.assertFalse(sut.findStatusByPackage(new FilingPackageRequest(BigDecimal.ZERO, BigDecimal.ZERO)).isPresent());

    }

    @Test
    @DisplayName("OK: demo returns a one element array")
    public void withRequestReturnOneElementArray() {

        Assertions.assertEquals(1, sut.findStatusByClient(new FilingPackageRequest(BigDecimal.ONE, BigDecimal.ONE)).size());

    }

    @Test
    @DisplayName("OK: demo returns a document byte array")
    public void withRequestReturnByteArray() {

        Optional<byte[]> result = sut.getSubmissionSheet(BigDecimal.ONE);

        Assertions.assertTrue(result.isPresent());

    }

    @Test
    @DisplayName("OK: demo returns a document byte array")
    public void withDocumentRequestReturnByteArray() {

        Optional<byte[]> result = sut.getSubmittedDocument(BigDecimal.ONE, "TEST");

        Assertions.assertTrue(result.isPresent());

    }

    @Test
    @DisplayName("OK: demo returns a boolean")
    public void withDocumentRequestBoolean() {

        boolean result = sut.deleteSubmittedDocument(new DeleteSubmissionDocumentRequest(BigDecimal.ONE, BigDecimal.ONE, "TEST"));

        Assertions.assertTrue(result);

    }

}
