package ca.bc.gov.open.jag.efilingcsostarter.csoLookupService;

import ca.bc.gov.open.jag.efilingcommons.model.SubmissionFee;
import org.junit.jupiter.api.*;

import java.math.BigDecimal;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("Efiling ServiceFees Test")
public class SubmissionFeeTest {

    private SubmissionFee sut;
    private static final String serviceId = "ServiceID";

    @BeforeAll
    public void setUp() {
        sut = new SubmissionFee(BigDecimal.ZERO, serviceId);
    }

    @DisplayName("CASE 1: Testing Service Fees Constructor")
    @Test
    public void testServiceFeesConstructor() {
        Assertions.assertEquals(serviceId, sut.getServiceTypeCd());
        Assertions.assertEquals(BigDecimal.ZERO, sut.getFeeAmount());
    }
}
