package ca.bc.gov.open.jag.efilingapi.filingpackage.service.filingPackageServiceImpl;

import ca.bc.gov.open.jag.efilingapi.TestHelpers;
import ca.bc.gov.open.jag.efilingapi.account.service.AccountService;
import ca.bc.gov.open.jag.efilingapi.filingpackage.mapper.FilingPackageMapperImpl;
import ca.bc.gov.open.jag.efilingapi.filingpackage.model.SubmittedDocument;
import ca.bc.gov.open.jag.efilingapi.filingpackage.service.FilingPackageServiceImpl;
import ca.bc.gov.open.jag.efilingcommons.submission.EfilingReviewService;
import org.junit.jupiter.api.*;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.core.io.ByteArrayResource;

import java.math.BigDecimal;
import java.util.Optional;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("FilePackageServiceImplTest")
public class GetSubmissionDocumentTest {

    private static final byte[] DOC_DATA = "TEST".getBytes();
    private static final String DOCUMENT_NOT_FOUND = "NOT FOUND";

    FilingPackageServiceImpl sut;

    @Mock
    EfilingReviewService efilingReviewServiceMock;

    @Mock
    AccountService accountServiceMock;

    @BeforeAll
    public void beforeAll() {

        MockitoAnnotations.openMocks(this);

        Mockito.when(accountServiceMock.getCsoAccountDetails(ArgumentMatchers.eq(TestHelpers.CASE_1_STRING))).thenReturn(TestHelpers.createAccount(BigDecimal.ONE));

        Mockito.when(accountServiceMock.getCsoAccountDetails(ArgumentMatchers.eq(TestHelpers.CASE_2_STRING))).thenReturn(TestHelpers.createAccount(null));

        sut = new FilingPackageServiceImpl(efilingReviewServiceMock, accountServiceMock, new FilingPackageMapperImpl());
    }


    @Test
    @DisplayName("Ok: a submitted document was returned")
    public void withValidRequestReturnDocument() {

        Mockito.when(efilingReviewServiceMock.findStatusByPackage(ArgumentMatchers.any())).thenReturn(Optional.of(TestHelpers.createFilingPackage()));

        Mockito.when(efilingReviewServiceMock.getSubmittedDocument(Mockito.any(), ArgumentMatchers.eq(TestHelpers.DOCUMENT_ID_TWO))).thenReturn(Optional.of(DOC_DATA));

        Optional<SubmittedDocument> result = sut.getSubmittedDocument(TestHelpers.CASE_1_STRING, BigDecimal.ONE, TestHelpers.DOCUMENT_ID_TWO);

        Assertions.assertTrue(result.isPresent());
        Assertions.assertEquals(TestHelpers.NAME, result.get().getName());
        Assertions.assertEquals(new ByteArrayResource(DOC_DATA), result.get().getData());

    }

    @Test
    @DisplayName("Not found: missing account")
    public void withValidRequestButMissingAccountReturnEmpty() {

        Optional<SubmittedDocument> result = sut.getSubmittedDocument(TestHelpers.CASE_2_STRING, BigDecimal.ONE, TestHelpers.DOCUMENT_ID_TWO);

        Assertions.assertFalse(result.isPresent());

    }


    @Test
    @DisplayName("Not found: no filing package")
    public void withValidRequestButMissingPackageReturnEmpty() {

        Mockito.when(efilingReviewServiceMock.findStatusByPackage(ArgumentMatchers.any())).thenReturn(Optional.empty());

        Optional<SubmittedDocument> result = sut.getSubmittedDocument(TestHelpers.CASE_1_STRING, BigDecimal.TEN, TestHelpers.DOCUMENT_ID_TWO);

        Assertions.assertFalse(result.isPresent());

    }

    @Test
    @DisplayName("Not found: document not in filing package")
    public void withValidRequestButMissingDocumentInPackageReturnEmpty() {

        Mockito.when(efilingReviewServiceMock.findStatusByPackage(ArgumentMatchers.any())).thenReturn(Optional.of(TestHelpers.createFilingPackage()));

        Optional<SubmittedDocument> result = sut.getSubmittedDocument(TestHelpers.CASE_1_STRING, BigDecimal.ONE, DOCUMENT_NOT_FOUND);

        Assertions.assertFalse(result.isPresent());

    }

    @Test
    @DisplayName("Not found: no document")
    public void withValidRequestButMissingDocumentReturnEmpty() {

        Mockito.when(efilingReviewServiceMock.findStatusByPackage(ArgumentMatchers.any())).thenReturn(Optional.of(TestHelpers.createFilingPackage()));

        Mockito.when(efilingReviewServiceMock.getSubmittedDocument(Mockito.any(), ArgumentMatchers.eq(TestHelpers.DOCUMENT_ID_TWO))).thenReturn(Optional.empty());

        Optional<SubmittedDocument> result = sut.getSubmittedDocument(TestHelpers.CASE_1_STRING, BigDecimal.TEN, TestHelpers.DOCUMENT_ID_TWO);

        Assertions.assertFalse(result.isPresent());

    }
}
