package ca.bc.gov.open.jag.efilinglookupclient;

import ca.bc.gov.open.jag.efilingcommons.model.ServiceFees;
import org.joda.time.DateTime;
import org.junit.jupiter.api.*;

import java.math.BigDecimal;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@DisplayName("Efiling ServiceFees Test")
public class ServiceFeesTest {

    private ServiceFees sut;
    private static final DateTime dt = DateTime.now();
    private static final String serviceId = "ServiceID";
    private static final String entUserId = "EntUserID";
    private static final String updUserId = "UpdUserID";

    @BeforeAll
    public void setUp() {
        sut = new ServiceFees(dt.toString(), BigDecimal.ZERO, entUserId, serviceId, dt.toString(), updUserId, dt.toString(), dt.toString());
    }

    @DisplayName("CASE 1: Testing Service Fees Constructor")
    @Test
    public void testServiceFeesConstructor() {

        Assertions.assertEquals(dt.toString(), sut.getEntDtm());
        Assertions.assertEquals(dt.toString(), sut.getEffectiveDt());
        Assertions.assertEquals(dt.toString(), sut.getExpiryDt());
        Assertions.assertEquals(dt.toString(), sut.getUdpDtm());
        Assertions.assertEquals(serviceId, sut.getServiceTypeCd());
        Assertions.assertEquals(entUserId, sut.getEntUserId());
        Assertions.assertEquals(updUserId, sut.getUpdUserId());
        Assertions.assertEquals(BigDecimal.ZERO, sut.getFeeAmt());
    }
}
