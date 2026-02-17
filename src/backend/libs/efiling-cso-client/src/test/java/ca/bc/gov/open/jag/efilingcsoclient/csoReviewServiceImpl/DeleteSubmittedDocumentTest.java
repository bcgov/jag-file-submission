package ca.bc.gov.open.jag.efilingcsoclient.csoReviewServiceImpl;

import ca.bc.gov.ag.csows.filing.FilingFacadeBean;
import ca.bc.gov.ag.csows.filing.NestedEjbException_Exception;
import ca.bc.gov.open.jag.efilingcommons.exceptions.EfilingReviewServiceException;
import ca.bc.gov.open.jag.efilingcommons.submission.models.DeleteSubmissionDocumentRequest;
import ca.bc.gov.open.jag.efilingcsoclient.CsoReviewServiceImpl;
import ca.bc.gov.open.jag.efilingcsoclient.config.CsoProperties;
import ca.bc.gov.open.jag.efilingcsoclient.mappers.FilePackageMapperImpl;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("Review Service Test Suite")
public class DeleteSubmittedDocumentTest {
    @Mock
    FilingFacadeBean filingFacadeBeanMock;

    private static CsoReviewServiceImpl sut;

    @Mock
    private RestTemplate restTemplateMock;

    @BeforeAll
    public void beforeAll() {

        MockitoAnnotations.openMocks(this);


        CsoProperties csoProperties = new CsoProperties();
        csoProperties.setCsoBasePath("http://locahost:8080");
        sut = new CsoReviewServiceImpl(null, null, filingFacadeBeanMock, new FilePackageMapperImpl(), csoProperties, restTemplateMock, null);
    }

    @DisplayName("OK: document withdrawn")
    @Test
    public void testWithFoundResult() throws NestedEjbException_Exception {

        Mockito.doNothing().when(filingFacadeBeanMock).updateDocumentStatus(Mockito.any());
        Mockito.doNothing().when(filingFacadeBeanMock).inactivateReferrals(Mockito.any(), Mockito.any(), Mockito.any());
        Mockito.doNothing().when(filingFacadeBeanMock).removePackageParties(Mockito.any());

        Assertions.assertDoesNotThrow(() -> sut.deleteSubmittedDocument(new DeleteSubmissionDocumentRequest(BigDecimal.ONE, BigDecimal.ONE, null,"1")));

    }

    @DisplayName("Exception: updateDocumentStatus document not withdrawn")
    @Test
    public void testWithUpdateDocumentStatusException() throws NestedEjbException_Exception {

        Mockito.doThrow(NestedEjbException_Exception.class).when(filingFacadeBeanMock).updateDocumentStatus(Mockito.any());
        Mockito.doNothing().when(filingFacadeBeanMock).inactivateReferrals(Mockito.any(), Mockito.any(), Mockito.any());
        Mockito.doNothing().when(filingFacadeBeanMock).removePackageParties(Mockito.any());

        Assertions.assertThrows(EfilingReviewServiceException.class, () -> sut.deleteSubmittedDocument(new DeleteSubmissionDocumentRequest(BigDecimal.ONE, BigDecimal.ONE, null,"1")));

    }

    @DisplayName("Exception: inactivateReferrals document not withdrawn")
    @Test
    public void testWithInactivateReferralsException() throws NestedEjbException_Exception {

        Mockito.doNothing().when(filingFacadeBeanMock).updateDocumentStatus(Mockito.any());
        Mockito.doThrow(NestedEjbException_Exception.class).when(filingFacadeBeanMock).inactivateReferrals(Mockito.any(), Mockito.any(), Mockito.any());
        Mockito.doNothing().when(filingFacadeBeanMock).removePackageParties(Mockito.any());

        Assertions.assertThrows(EfilingReviewServiceException.class, () -> sut.deleteSubmittedDocument(new DeleteSubmissionDocumentRequest(BigDecimal.ONE, BigDecimal.ONE, null,"1")));

    }

    @DisplayName("Exception: removePackageParties document not withdrawn")
    @Test
    public void testWithRemovePackagePartiesException() throws NestedEjbException_Exception {

        Mockito.doNothing().when(filingFacadeBeanMock).updateDocumentStatus(Mockito.any());
        Mockito.doNothing().when(filingFacadeBeanMock).inactivateReferrals(Mockito.any(), Mockito.any(), Mockito.any());
        Mockito.doThrow(NestedEjbException_Exception.class).when(filingFacadeBeanMock).removePackageParties(Mockito.any());

        Assertions.assertThrows(EfilingReviewServiceException.class, () -> sut.deleteSubmittedDocument(new DeleteSubmissionDocumentRequest(BigDecimal.ONE, BigDecimal.ONE,null, "1")));

    }

}
