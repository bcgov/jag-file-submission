package ca.bc.gov.open.jag.efilinglookupclient;

import ca.bc.gov.ag.csows.lookups.ServiceFee;
import org.junit.jupiter.api.*;

import java.math.BigDecimal;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("Demo Efiling Lookup Client Test Suite")
public class DemoLookupServiceImplTest {

    @DisplayName("CASE 1: Testing Demo getServiceFee")
    @Test
    public void testDemoLookupServiceTest() {

        DemoLookupServiceImpl service = new DemoLookupServiceImpl();

        String serviceId = "TestServiceId";
        ServiceFee actual = service.getServiceFee(serviceId);

        Assertions.assertEquals(BigDecimal.ZERO, actual.getFeeAmt());
    }

}
