package ca.bc.gov.open.jag.efilingcsostarter.csoSubmissionServiceImpl;

import ca.bc.gov.ag.csows.filing.FilingFacadeBean;
import ca.bc.gov.ag.csows.services.NestedEjbException_Exception;
import ca.bc.gov.ag.csows.services.ServiceFacadeBean;
import ca.bc.gov.open.jag.efilingcommons.exceptions.EfilingSubmissionServiceException;
import ca.bc.gov.open.jag.efilingcommons.model.EfilingFilingPackage;
import ca.bc.gov.open.jag.efilingcommons.model.EfilingService;
import ca.bc.gov.open.jag.efilingcsostarter.CsoSubmissionServiceImpl;
import ca.bc.gov.open.jag.efilingcsostarter.TestHelpers;
import ca.bc.gov.open.jag.efilingcsostarter.mappers.FilingPackageMapperImpl;
import ca.bc.gov.open.jag.efilingcsostarter.mappers.ServiceMapperImpl;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import javax.xml.datatype.DatatypeConfigurationException;
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

    @Mock
    ServiceFacadeBean serviceFacadeBean;

    @BeforeEach
    public void init() {

        MockitoAnnotations.initMocks(this);

        sut = new CsoSubmissionServiceImpl(filingFacadeBeanMock, serviceFacadeBean, new ServiceMapperImpl(), new FilingPackageMapperImpl());

    }

    @DisplayName("Exception: with null service should throw IllegalArgumentException")
    @Test
    public void testWithEmptyService() {

        Assertions.assertThrows(IllegalArgumentException.class, () -> sut.submitFilingPackage(null, new EfilingFilingPackage(), null));
    }

    @DisplayName("Exception: with null filingpackage should throw IllegalArgumentException")
    @Test
    public void testWithEmptyFilingPackage() {

        Assertions.assertThrows(IllegalArgumentException.class, () -> sut.submitFilingPackage(new EfilingService(), null, null));
    }

    @DisplayName("OK: submitFilingPackage called with any non-empty submissionId")
    @Test
    public void testWithPopulatedSubmissionId() throws ca.bc.gov.ag.csows.filing.NestedEjbException_Exception, NestedEjbException_Exception, DatatypeConfigurationException {
        Mockito.when(filingFacadeBeanMock.submitFiling(any())).thenReturn(BigDecimal.TEN);
        Mockito.when(serviceFacadeBean.addService(any())).thenReturn(TestHelpers.createService());
        Mockito.doNothing().when(serviceFacadeBean).updateService(any());
        BigDecimal actual = sut.submitFilingPackage(TestHelpers.createBaseEfilingService(), new EfilingFilingPackage(), null);
        Assertions.assertEquals(BigDecimal.TEN, actual);
    }
    @DisplayName("Exception: with NestedEjbException_Exception should throw EfilingLookupServiceException")
    @Test
    public void whenFilingFacadeNestedEjbException_ExceptionShouldThrowEfilingSubmissionServiceException() throws ca.bc.gov.ag.csows.filing.NestedEjbException_Exception, NestedEjbException_Exception, DatatypeConfigurationException {
        Mockito.when(serviceFacadeBean.addService(any())).thenReturn(TestHelpers.createService());
        Mockito.when(filingFacadeBeanMock.submitFiling(any())).thenThrow(new ca.bc.gov.ag.csows.filing.NestedEjbException_Exception());
        Assertions.assertThrows(EfilingSubmissionServiceException.class, () -> sut.submitFilingPackage(new EfilingService(), new EfilingFilingPackage(), null));
    }

    @DisplayName("Exception: with NestedEjbException_Exception should throw EfilingLookupServiceException")
    @Test
    public void whenServiceFacadeNestedEjbException_ExceptionShouldThrowEfilingSubmissionServiceException() throws ca.bc.gov.ag.csows.filing.NestedEjbException_Exception, NestedEjbException_Exception, DatatypeConfigurationException {
        Mockito.when(serviceFacadeBean.addService(any())).thenThrow(new ca.bc.gov.ag.csows.services.NestedEjbException_Exception());
        Assertions.assertThrows(EfilingSubmissionServiceException.class, () -> sut.submitFilingPackage(new EfilingService(), new EfilingFilingPackage(), null));
    }
}
