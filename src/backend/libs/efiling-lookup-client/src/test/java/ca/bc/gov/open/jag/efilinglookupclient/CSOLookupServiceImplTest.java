package ca.bc.gov.open.jag.efilinglookupclient;

import ca.bc.gov.ag.csows.lookups.LookupFacadeBean;
import ca.bc.gov.ag.csows.lookups.NestedEjbException_Exception;
import ca.bc.gov.ag.csows.lookups.ServiceFee;
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
@DisplayName("CSO Efiling Lookup Client Test Suite")
public class CSOLookupServiceImplTest {

    private static final String SERVICEID = "ServiceId";

    @InjectMocks
    CSOLookupServiceImpl sut;

    @Mock
    LookupFacadeBean mocklookupFacadeItf;

    @Mock
    ServiceFee serviceFeeMock;

    @BeforeEach
    public void init() throws NestedEjbException_Exception {

        MockitoAnnotations.initMocks(this);
        Mockito.when(serviceFeeMock.getFeeAmt()).thenReturn(BigDecimal.valueOf(0));
        Mockito.when(mocklookupFacadeItf.getServiceFee(any(), any())).thenReturn(serviceFeeMock);
    }

    @DisplayName("CASE 1: getServiceFee called with empty serviceId")
    @Test
    public void testWithEmptyServiceId() {

        ServiceFee fees = sut.getServiceFee("");
        Assertions.assertEquals(null, fees);
    }

    @DisplayName("CASE 2: getServiceFee called with any non-empty serviceId")
    @Test
    public void testWithPopulatedServiceId() {

        ServiceFee fees = sut.getServiceFee(SERVICEID);
        Assertions.assertNotEquals(null, fees);
    }
}
