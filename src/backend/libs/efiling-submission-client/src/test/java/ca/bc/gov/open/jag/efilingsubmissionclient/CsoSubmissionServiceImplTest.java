package ca.bc.gov.open.jag.efilingsubmissionclient;

import ca.bc.gov.ag.csows.filing.FilingFacade;
import ca.bc.gov.ag.csows.filing.FilingFacadeBean;
import ca.bc.gov.ag.csows.filing.FilingPackage;
import ca.bc.gov.ag.csows.filing.NestedEjbException_Exception;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("CSO Submission Service Suite")
public class CsoSubmissionServiceImplTest {

    @InjectMocks
    CsoSubmissionServiceImpl sut;

    @Mock
    FilingFacade mockFilingFacade;

    @Mock
    FilingFacadeBean mockFilingFacadeBean;

    @BeforeAll
    public void init() throws NestedEjbException_Exception {

        MockitoAnnotations.initMocks(this);
        Mockito.when(mockFilingFacade.getFilingFacadeBeanPort()).thenReturn(mockFilingFacadeBean);
        Mockito.when(mockFilingFacadeBean.submitFiling(any())).thenReturn(BigDecimal.ONE);
    }

    @Test
    @DisplayName("CASE1: Test")
    public void testSubmission() throws NestedEjbException_Exception {

        BigDecimal response = sut.submitFiling(new FilingPackage());

        Assertions.assertEquals(BigDecimal.ONE, response);
        verify(mockFilingFacade, times(1)).getFilingFacadeBeanPort();
        verify(mockFilingFacadeBean, times(1)).submitFiling(any());
    }

}
