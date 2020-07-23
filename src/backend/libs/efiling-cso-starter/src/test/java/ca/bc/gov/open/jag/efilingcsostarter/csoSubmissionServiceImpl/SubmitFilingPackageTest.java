package ca.bc.gov.open.jag.efilingcsostarter.csoSubmissionServiceImpl;

import ca.bc.gov.ag.csows.filing.FilingFacadeBean;
import ca.bc.gov.ag.csows.filing.NestedEjbException_Exception;
import ca.bc.gov.open.jag.efilingcommons.exceptions.EfilingSubmissionServiceException;
import ca.bc.gov.open.jag.efilingcsostarter.CsoSubmissionServiceImpl;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("Efiling Submission Test")
public class SubmitFilingPackageTest {
    public static final UUID CASE_1 = UUID.fromString("77da92db-0791-491e-8c58-1a969e67d2fe");
    CsoSubmissionServiceImpl sut;

    @Mock
    FilingFacadeBean filingFacadeBeanMock;


    @BeforeEach
    public void init() {

        MockitoAnnotations.initMocks(this);

        sut = new CsoSubmissionServiceImpl(filingFacadeBeanMock);

    }

    @DisplayName("Exception: with null submission id should throw IllegalArgumentException")
    @Test
    public void testWithEmptyServiceId() {

        Assertions.assertThrows(IllegalArgumentException.class, () -> sut.submitFilingPackage(null));
    }

    @DisplayName("OK: submitFilingPackage called with any non-empty submissionId")
    @Test
    public void testWithPopulatedSubmissionId() throws ca.bc.gov.ag.csows.filing.NestedEjbException_Exception {
        Mockito.when(filingFacadeBeanMock.submitFiling(any())).thenReturn(BigDecimal.TEN);
        BigDecimal actual = sut.submitFilingPackage(CASE_1);
        Assertions.assertEquals(BigDecimal.TEN, actual);

    }
    @DisplayName("Exception: with NestedEjbException_Exception should throw EfilingLookupServiceException")
    @Test
    public void whenNestedEjbException_ExceptionShouldThrowEfilingSubmissionServiceException() throws ca.bc.gov.ag.csows.filing.NestedEjbException_Exception {
        Mockito.when(filingFacadeBeanMock.submitFiling(any())).thenThrow(new ca.bc.gov.ag.csows.filing.NestedEjbException_Exception());
        Assertions.assertThrows(EfilingSubmissionServiceException.class, () -> sut.submitFilingPackage(CASE_1));

    }
}
