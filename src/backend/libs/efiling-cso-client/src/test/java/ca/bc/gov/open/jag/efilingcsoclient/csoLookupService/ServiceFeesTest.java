package ca.bc.gov.open.jag.efilingcsoclient.csoLookupService;

import ca.bc.gov.open.jag.efilingcommons.model.ServiceFees;
import org.junit.jupiter.api.*;

import java.math.BigDecimal;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("Efiling ServiceFees Test")
public class ServiceFeesTest {

    private ServiceFees sut;
    private static final String serviceId = "ServiceID";

    @BeforeAll
    public void setUp() {
        sut = new ServiceFees(BigDecimal.ZERO, serviceId);
    }

    @DisplayName("CASE 1: Testing Service Fees Constructor")
    @Test
    public void testServiceFeesConstructor() {
        Assertions.assertEquals(serviceId, sut.getServiceTypeCd());
        Assertions.assertEquals(BigDecimal.ZERO, sut.getFeeAmount());
    }
}
