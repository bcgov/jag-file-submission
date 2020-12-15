package ca.bc.gov.open.jag.efilingcsoclient.csoLookupService;

import ca.bc.gov.ag.csows.lookups.LookupFacadeBean;
import ca.bc.gov.ag.csows.lookups.NestedEjbException_Exception;
import ca.bc.gov.ag.csows.lookups.ServiceFee;
import ca.bc.gov.open.jag.efilingcommons.exceptions.EfilingLookupServiceException;
import ca.bc.gov.open.jag.efilingcommons.model.ServiceFees;
import ca.bc.gov.open.jag.efilingcsoclient.CsoLookupServiceImpl;
import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("CSO Efiling Lookup Client Test Suite")
public class CsoLookupServiceImplTest {

    private static final String SERVICE_ID = "ServiceId";
    private static final String SERVICE_ID_NULL = "ServiceIdNull";
    public static final String SERVICE_TYPE_CD = "serviceTypeCd";
    public static final String SERVICE_ID_EXCEPTION = "exception";

    CsoLookupServiceImpl sut;

    @Mock
    LookupFacadeBean lookupFacadeBeanMock;

    @Mock
    ServiceFee serviceFeeMock;

    @BeforeEach
    public void init() throws NestedEjbException_Exception {

        MockitoAnnotations.openMocks(this);
        Mockito.when(serviceFeeMock.getFeeAmt()).thenReturn(BigDecimal.TEN);
        Mockito.when(serviceFeeMock.getServiceTypeCd()).thenReturn(SERVICE_TYPE_CD);

        Mockito
                .when(lookupFacadeBeanMock.getServiceFee(Mockito.eq(SERVICE_ID), any()))
                .thenReturn(serviceFeeMock);

        Mockito
                .when(lookupFacadeBeanMock.getServiceFee(Mockito.eq(SERVICE_ID_EXCEPTION), Mockito.any()))
                .thenThrow(new NestedEjbException_Exception("random"));

        Mockito
                .when(lookupFacadeBeanMock.getServiceFee(Mockito.eq(SERVICE_ID_NULL), Mockito.any()))
                .thenReturn(null);


        sut = new CsoLookupServiceImpl(lookupFacadeBeanMock);

    }

    @DisplayName("Exception: with null service id should throw IllegalArgumentException")
    @Test
    public void testWithEmptyServiceId() {

        Assertions.assertThrows(IllegalArgumentException.class, () -> sut.getServiceFee(""));
    }

    @DisplayName("OK: getServiceFee called with any non-empty serviceId")
    @Test
    public void testWithPopulatedServiceId() {
        ServiceFees actual = sut.getServiceFee(SERVICE_ID);
        Assertions.assertEquals(BigDecimal.TEN, actual.getFeeAmount());
        Assertions.assertEquals(SERVICE_TYPE_CD, actual.getServiceTypeCd());
    }

    @DisplayName("Exception: with NestedEjbException_Exception should throw EfilingLookupServiceException")
    @Test
    public void whenNestedEjbException_ExceptionShouldThrowEfilingLookupServiceException() {

        Assertions.assertThrows(EfilingLookupServiceException.class, () -> sut.getServiceFee(SERVICE_ID_EXCEPTION));

    }
    @DisplayName("Exception: with null result should throw EfilingLookupServiceException")
    @Test
    public void whenNullResultShouldThrowEfilingLookupServiceException() {

        Assertions.assertThrows(EfilingLookupServiceException.class, () -> sut.getServiceFee(SERVICE_ID_NULL));

    }
}
