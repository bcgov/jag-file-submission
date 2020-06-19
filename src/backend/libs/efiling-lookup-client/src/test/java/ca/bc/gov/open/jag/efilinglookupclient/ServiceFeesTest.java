package ca.bc.gov.open.jag.efilinglookupclient;

import org.joda.time.DateTime;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

public class ServiceFeesTest {

    private ServiceFees sut;
    private static final DateTime dt = DateTime.now();
    private static final String serviceId = "ServiceID";
    private static final String entUserId = "EntUserID";
    private static final String updUserId = "UpdUserID";

    @BeforeAll
    public void setUp() {
        sut = new ServiceFees(dt, BigDecimal.ZERO, entUserId, serviceId, dt, updUserId, dt, dt);
    }

    @DisplayName("CASE 1: Testing Service Fees Constructor")
    @Test
    public void testServiceFeesConstructor() {

        Assertions.assertEquals(dt, sut.getEntDtm());
        Assertions.assertEquals(dt, sut.getEffectiveDt());
        Assertions.assertEquals(dt, sut.getExpiryDt());
        Assertions.assertEquals(dt, sut.getUdpDtm());
        Assertions.assertEquals(serviceId, sut.getServiceTypeCd());
        Assertions.assertEquals(entUserId, sut.getEntUserId());
        Assertions.assertEquals(updUserId, sut.getUpdUserId());
        Assertions.assertEquals(BigDecimal.ZERO, sut.getFeeAmt());
    }
}
