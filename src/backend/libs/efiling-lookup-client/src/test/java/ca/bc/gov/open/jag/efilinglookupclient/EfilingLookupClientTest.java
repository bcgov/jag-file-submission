package ca.bc.gov.open.jag.efilinglookupclient;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("Efiling Lookup Client Test Suite")
public class EfilingLookupClientTest {

    private DemoLookupServiceImpl sut;

    @BeforeAll
    public void setUp() {
        sut = new DemoLookupServiceImpl();
    }

    @DisplayName("CASE 1: Testing Service Fees Constructor")
    @Test
    public void testDemoLookupServiceTest() {

        String serviceId = "TestServiceId";
        ServiceFees actual = sut.getServiceFee(serviceId);

        Assertions.assertEquals(serviceId, actual.getServiceTypeCd());
        Assertions.assertEquals(0.0f, actual.getFeeAmt());
    }

}
