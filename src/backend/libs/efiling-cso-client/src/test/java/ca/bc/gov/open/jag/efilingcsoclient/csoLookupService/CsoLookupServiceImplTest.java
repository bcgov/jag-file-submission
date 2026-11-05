package ca.bc.gov.open.jag.efilingcsoclient.csoLookupService;

import ca.bc.gov.ag.csows.lookups.LookupFacadeBean;
import ca.bc.gov.ag.csows.lookups.NestedEjbException_Exception;
import ca.bc.gov.ag.csows.lookups.ServiceFee;
import ca.bc.gov.open.jag.efilingcommons.exceptions.EfilingLookupServiceException;
import ca.bc.gov.open.jag.efilingcommons.model.ServiceFees;
import ca.bc.gov.open.jag.efilingcommons.model.SubmissionFeeRequest;
import ca.bc.gov.open.jag.efilingcsoclient.CsoLookupServiceImpl;
import org.junit.jupiter.api.*;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("CSO Efiling Lookup Client Test Suite")
public class CsoLookupServiceImplTest {

    private static final String SERVICE_TYPE = "ServiceId";
    private static final String SERVICE_ID_NULL = "ServiceIdNull";
    public static final String SERVICE_TYPE_CD = "serviceTypeCd";
    public static final String SERVICE_ID_EXCEPTION = "exception";
    public static final String TEST = "TEST";

    CsoLookupServiceImpl sut;

    @Mock
    LookupFacadeBean lookupFacadeBeanMock;

    @BeforeAll
    public void beforeEach() throws NestedEjbException_Exception {

        MockitoAnnotations.openMocks(this);

        ServiceFee serviceFee = new ServiceFee();

        serviceFee.setFeeAmt(BigDecimal.TEN);
        serviceFee.setServiceTypeCd(SERVICE_TYPE_CD);

        Mockito
                .when(lookupFacadeBeanMock.getServiceFeeByClassification(ArgumentMatchers.eq(SERVICE_TYPE), any(), any(), any(), any(), any()))
                .thenReturn(serviceFee);

        Mockito
                .when(lookupFacadeBeanMock.getServiceFeeByClassification(ArgumentMatchers.eq(SERVICE_ID_EXCEPTION), any(), any(), any(), any(), any()))
                .thenThrow(new NestedEjbException_Exception("random"));

        Mockito
                .when(lookupFacadeBeanMock.getServiceFeeByClassification(ArgumentMatchers.eq(SERVICE_ID_NULL), any(), any(), any(), any(), any()))
                .thenReturn(null);

        sut = new CsoLookupServiceImpl(lookupFacadeBeanMock);

    }

    @DisplayName("Exception: with null service id should throw IllegalArgumentException")
    @Test
    public void testWithEmptyServiceType() {

        Assertions.assertThrows(IllegalArgumentException.class, () -> sut.getServiceFee(SubmissionFeeRequest.builder().create()));
    }

    @DisplayName("Exception: with null service id should throw IllegalArgumentException")
    @Test
    public void testWithEmptyApplication() {

        Assertions.assertThrows(IllegalArgumentException.class, () -> sut.getServiceFee(SubmissionFeeRequest.builder()
                .serviceType(TEST)
                .create()));

    }

    @DisplayName("Exception: with null service id should throw IllegalArgumentException")
    @Test
    public void testWithEmptyClassification() {

        Assertions.assertThrows(IllegalArgumentException.class, () -> sut.getServiceFee(SubmissionFeeRequest.builder()
                .serviceType(TEST)
                .application(TEST)
                .create()));

    }

    @DisplayName("Exception: with null service id should throw IllegalArgumentException")
    @Test
    public void testWithEmptyDivision() {

        Assertions.assertThrows(IllegalArgumentException.class, () -> sut.getServiceFee(SubmissionFeeRequest.builder()
                .serviceType(TEST)
                .application(TEST)
                .classification(TEST)
                .create()));

    }

    @DisplayName("Exception: with null service id should throw IllegalArgumentException")
    @Test
    public void testWithEmptyLevel() {

        Assertions.assertThrows(IllegalArgumentException.class, () -> sut.getServiceFee(SubmissionFeeRequest.builder()
                .serviceType(TEST)
                .application(TEST)
                .classification(TEST)
                .division(TEST)
                .create()));

    }

    @DisplayName("OK: getServiceFee called with any non-empty serviceId")
    @Test
    public void testWithPopulatedServiceId() {

        ServiceFees actual = sut.getServiceFee(SubmissionFeeRequest.builder()
                .application(TEST)
                .classification(TEST)
                .division(TEST)
                .level(TEST)
                .serviceType(SERVICE_TYPE).create());
        Assertions.assertEquals(BigDecimal.TEN, actual.getFeeAmount());
        Assertions.assertEquals(SERVICE_TYPE_CD, actual.getServiceTypeCd());
    }

    @DisplayName("Exception: with NestedEjbException_Exception should throw EfilingLookupServiceException")
    @Test
    public void whenNestedEjbException_ExceptionShouldThrowEfilingLookupServiceException() {

        Assertions.assertThrows(EfilingLookupServiceException.class, () -> sut.getServiceFee(SubmissionFeeRequest.builder()
                .application(TEST)
                .classification(TEST)
                .division(TEST)
                .level(TEST)
                .serviceType(SERVICE_ID_EXCEPTION).create()));

    }

    @DisplayName("OK: with null result should throw EfilingLookupServiceException")
    @Test
    public void whenNullResultShouldThrowEfilingLookupServiceException() {

        ServiceFees actual = sut.getServiceFee(SubmissionFeeRequest.builder()
                .application(TEST)
                .classification(TEST)
                .division(TEST)
                .level(TEST)
                .serviceType(SERVICE_ID_NULL).create());

        Assertions.assertEquals(BigDecimal.ZERO, actual.getFeeAmount());
        Assertions.assertNull(actual.getServiceTypeCd());

    }

}
