package ca.bc.gov.open.jag.efilingsubmissionclient;

import ca.bc.gov.ag.csows.filing.FilingFacade;
import ca.bc.gov.ag.csows.filing.FilingFacadeBean;
import ca.bc.gov.ag.csows.filing.FilingPackage;
import ca.bc.gov.ag.csows.filing.NestedEjbException_Exception;
import org.junit.jupiter.api.*;
import org.mockito.*;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("CSO Submission Service Suite")
public class CsoSubmissionServiceImplTest {

    private static final String CASE_1 = "CASE_1";
    private static final String CASE_2 = "CASE 2";

    CsoSubmissionServiceImpl sut;

    @Mock
    FilingFacade mockFilingFacade;

    @Mock
    FilingFacadeBean mockFilingFacadeBean;

    @BeforeEach
    public void init() throws NestedEjbException_Exception {

        MockitoAnnotations.initMocks(this);

        Mockito.when(mockFilingFacade.getFilingFacadeBeanPort()).thenReturn(mockFilingFacadeBean);

        Mockito
                .doReturn(BigDecimal.ONE)
                .when(mockFilingFacadeBean)
                .submitFiling(ArgumentMatchers.argThat(x -> x.getApplicationCd().equals(CASE_1)));

        Mockito
                .doThrow(new NestedEjbException_Exception("random"))
                .when(mockFilingFacadeBean)
                .submitFiling(ArgumentMatchers.argThat(x -> x.getApplicationCd().equals(CASE_2)));

        sut = new CsoSubmissionServiceImpl(mockFilingFacade);

    }

    @Test
    @DisplayName("CASE 1: With valid params should return submission status")
    public void testSubmission() throws NestedEjbException_Exception {


        FilingPackage filingPackage = new FilingPackage();
        filingPackage.setApplicationCd(CASE_1);
        BigDecimal response = sut.submitFiling(filingPackage);

        Assertions.assertEquals(BigDecimal.ONE, response);
        verify(mockFilingFacade, times(1)).getFilingFacadeBeanPort();
        verify(mockFilingFacadeBean, times(1)).submitFiling(any());
    }

    @Test
    @DisplayName("CASE 2: With exception should return default value")
    public void withExceptionShouldReturnDefault() throws NestedEjbException_Exception {

        FilingPackage filingPackage = new FilingPackage();
        filingPackage.setApplicationCd(CASE_2);

        BigDecimal response = sut.submitFiling(filingPackage);

        Assertions.assertEquals(BigDecimal.valueOf(-1), response);

    }

}
