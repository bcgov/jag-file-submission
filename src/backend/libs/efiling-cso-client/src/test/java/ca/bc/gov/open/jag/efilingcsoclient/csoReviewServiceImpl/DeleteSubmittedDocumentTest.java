package ca.bc.gov.open.jag.efilingcsoclient.csoReviewServiceImpl;

import ca.bc.gov.ag.csows.filing.FilingFacadeBean;
import ca.bc.gov.ag.csows.filing.NestedEjbException_Exception;
import ca.bc.gov.open.jag.efilingcommons.exceptions.EfilingReviewServiceException;
import ca.bc.gov.open.jag.efilingcommons.submission.models.DeleteSubmissionDocumentRequest;
import ca.bc.gov.open.jag.efilingcommons.submission.models.FilingPackageRequest;
import ca.bc.gov.open.jag.efilingcommons.submission.models.review.ReviewFilingPackage;
import ca.bc.gov.open.jag.efilingcsoclient.CsoReviewServiceImpl;
import ca.bc.gov.open.jag.efilingcsoclient.mappers.FilePackageMapperImpl;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import javax.xml.datatype.DatatypeConfigurationException;
import java.math.BigDecimal;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("Review Service Test Suite")
public class DeleteSubmittedDocumentTest {
    @Mock
    FilingFacadeBean filingFacadeBeanMock;

    private static CsoReviewServiceImpl sut;

    @BeforeAll
    public void beforeAll() {

        MockitoAnnotations.openMocks(this);


        sut = new CsoReviewServiceImpl(null, null, filingFacadeBeanMock, new FilePackageMapperImpl());
    }

    @DisplayName("OK: document withdrawn")
    @Test
    public void testWithFoundResult() throws NestedEjbException_Exception {

        Mockito.doNothing().when(filingFacadeBeanMock).updateDocumentStatus(Mockito.any());
        Mockito.doNothing().when(filingFacadeBeanMock).inactivateReferrals(Mockito.any(), Mockito.any(), Mockito.any());
        Mockito.doNothing().when(filingFacadeBeanMock).removePackageParties(Mockito.any());

        Assertions.assertDoesNotThrow(() -> sut.deleteSubmittedDocument(new DeleteSubmissionDocumentRequest(BigDecimal.ONE, BigDecimal.ONE, "1")));

    }


    @DisplayName("Exception: document not withdrawn")
    @Test
    public void testWithException() throws NestedEjbException_Exception {

        Mockito.doThrow(NestedEjbException_Exception.class).when(filingFacadeBeanMock).updateDocumentStatus(Mockito.any());
        Assertions.assertThrows(EfilingReviewServiceException.class, () -> sut.deleteSubmittedDocument(new DeleteSubmissionDocumentRequest(BigDecimal.ONE, BigDecimal.ONE, "1")));

    }
}
