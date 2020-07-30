package ca.bc.gov.open.jag.efilingcsostarter.csoSubmissionServiceImpl;

import ca.bc.gov.ag.csows.filing.FilingFacadeBean;
import ca.bc.gov.ag.csows.services.NestedEjbException_Exception;
import ca.bc.gov.ag.csows.services.Service;
import ca.bc.gov.ag.csows.services.ServiceFacadeBean;
import ca.bc.gov.open.jag.efilingcommons.exceptions.EfilingSubmissionServiceException;
import ca.bc.gov.open.jag.efilingcommons.model.EfilingService;
import ca.bc.gov.open.jag.efilingcsostarter.CsoSubmissionServiceImpl;
import ca.bc.gov.open.jag.efilingcsostarter.TestHelpers;
import ca.bc.gov.open.jag.efilingcsostarter.mappers.ServiceMapperImpl;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import javax.xml.datatype.XMLGregorianCalendar;
import java.math.BigDecimal;
import java.util.GregorianCalendar;

import static org.mockito.ArgumentMatchers.any;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("Efiling Submission Test")
public class UpdateServiceTest {
    CsoSubmissionServiceImpl sut;
    @Mock
    FilingFacadeBean filingFacadeBeanMock;

    @Mock
    ServiceFacadeBean serviceFacadeBean;

    @BeforeEach
    public void init() {

        MockitoAnnotations.initMocks(this);

        sut = new CsoSubmissionServiceImpl(filingFacadeBeanMock, serviceFacadeBean, new ServiceMapperImpl());

    }
    @DisplayName("Exception: with null service should throw IllegalArgumentException")
    @Test
    public void testWithEmptyServiceId() {

        Assertions.assertThrows(IllegalArgumentException.class, () -> sut.updateService(null));
    }

    @DisplayName("Exception: with null transactions should throw IllegalArgumentException")
    @Test
    public void testWithEmptyTransaction() {

        Assertions.assertThrows(IllegalArgumentException.class, () -> sut.updateService(TestHelpers.createBaseEfilingService()));
    }

    @DisplayName("OK: addService called with any non-empty service")
    @Test
    public void testWithPopulatedSubmissionId() throws NestedEjbException_Exception, DatatypeConfigurationException {
        Mockito.doNothing().when(serviceFacadeBean).updateService(any());

        Assertions.
        Assertions.assertDoesNotThrow(sut.updateService(TestHelpers.createUpdateEfilingService(TestHelpers.createBaseEfilingService())));
    }
    @DisplayName("Exception: with NestedEjbException_Exception should throw EfilingLookupServiceException")
    @Test
    public void whenNestedEjbException_ExceptionShouldThrowEfilingSubmissionServiceException() throws ca.bc.gov.ag.csows.filing.NestedEjbException_Exception, NestedEjbException_Exception {
        Mockito.doThrow(new ca.bc.gov.ag.csows.services.NestedEjbException_Exception()).when(serviceFacadeBean).updateService(any());
        Assertions.assertThrows(EfilingSubmissionServiceException.class, () -> sut.updateService(TestHelpers.createBaseEfilingService()));
    }

}
