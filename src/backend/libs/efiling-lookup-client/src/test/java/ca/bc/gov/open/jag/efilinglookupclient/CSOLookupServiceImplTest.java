package ca.bc.gov.open.jag.efilinglookupclient;

import ca.bc.gov.ag.csows.LookupFacadeItf;
import ca.bc.gov.ag.csows.LookupsLookupFacade;
import ca.bc.gov.ag.csows.lookups.GetServiceFeeResponseElement;
import ca.bc.gov.ag.csows.lookups.ServiceFee;
import org.junit.jupiter.api.*;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

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
    LookupsLookupFacade mockLookupsLookupFacade;

    @Mock
    LookupFacadeItf mocklookupFacadeItf;

    @Mock
    GetServiceFeeResponseElement mockResponseElement;

    @BeforeAll
    public void init() {

        MockitoAnnotations.initMocks(this);
        Mockito.when(mockLookupsLookupFacade.getLookupFacade()).thenReturn(mocklookupFacadeItf);
        Mockito.when(mocklookupFacadeItf.getServiceFee(any())).thenReturn(mockResponseElement);
        Mockito.when(mockResponseElement.getResult()).thenReturn(new ServiceFee());
    }

    @DisplayName("CASE 1: getServiceFee called with empty serviceId")
    @Test
    public void testWithEmptyServiceId() {

        ServiceFees fees = sut.getServiceFee("");
        Assertions.assertEquals(null, fees);
        verify(mockLookupsLookupFacade, times(0)).getLookupFacade();
    }

    @DisplayName("CASE 2: getServiceFee called with any non-empty serviceId")
    @Test
    public void testWithPopulatedServiceId() {

        ServiceFees fees = sut.getServiceFee(SERVICEID);
        Assertions.assertNotEquals(null, fees);
        verify(mockLookupsLookupFacade, times(1)).getLookupFacade();
        verify(mocklookupFacadeItf, times(1)).getServiceFee(any());
    }
}
