package ca.bc.gov.open.jag.efilingapi.filingpackage.service.filingPackageServiceImpl;

import ca.bc.gov.open.jag.efilingapi.TestHelpers;
import ca.bc.gov.open.jag.efilingapi.account.service.AccountService;
import ca.bc.gov.open.jag.efilingapi.filingpackage.mapper.FilingPackageMapperImpl;
import ca.bc.gov.open.jag.efilingapi.filingpackage.service.FilingPackageServiceImpl;
import ca.bc.gov.open.jag.efilingcommons.exceptions.EfilingAccountServiceException;
import ca.bc.gov.open.jag.efilingcommons.submission.EfilingReviewService;
import org.junit.jupiter.api.*;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("FilePackageServiceImplTest")
public class DeleteSubmittedDocumentTest {

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

        sut = new FilingPackageServiceImpl(efilingReviewServiceMock, null, accountServiceMock, new FilingPackageMapperImpl(), null, null);
    }


    @Test
    @DisplayName("Ok: a document was deleted")
    public void withValidRequestReturnDocument() {

        Mockito.doNothing().when(efilingReviewServiceMock).deleteSubmittedDocument(any());

        Assertions.assertDoesNotThrow(() -> sut.deleteSubmittedDocument(TestHelpers.CASE_1_STRING, BigDecimal.ONE, TestHelpers.DOCUMENT_ID_TWO));

    }

    @Test
    @DisplayName("failed: missing account")
    public void withValidRequestButMissingAccountReturnEmpty() {

        Assertions.assertThrows(EfilingAccountServiceException.class, () -> sut.deleteSubmittedDocument(TestHelpers.CASE_2_STRING, BigDecimal.ONE, TestHelpers.DOCUMENT_ID_TWO));


    }


    @Test
    @DisplayName("failed: nothing was deleted")
    public void withValidRequestButMissingPackageReturnEmpty() {

        Mockito.doThrow(RuntimeException.class).when(efilingReviewServiceMock).deleteSubmittedDocument(any());

        Assertions.assertThrows(Exception.class, () ->sut.deleteSubmittedDocument(TestHelpers.CASE_1_STRING, BigDecimal.TEN, TestHelpers.DOCUMENT_ID_TWO));

    }
}
