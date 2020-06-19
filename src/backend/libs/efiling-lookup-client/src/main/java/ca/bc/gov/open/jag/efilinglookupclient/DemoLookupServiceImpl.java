package ca.bc.gov.open.jag.efilinglookupclient;

import org.joda.time.DateTime;

import java.math.BigDecimal;

public class DemoLookupServiceImpl implements EfilingLookupService {

    @Override
    public ServiceFees getServiceFee(String serviceId) {

        ServiceFees serviceFees = new ServiceFees(
            DateTime.now(),
            BigDecimal.ZERO,
            "entUserId",
            serviceId,
            DateTime.now(),
            "updUserId",
            DateTime.now(),
            DateTime.now());

        return serviceFees;
    }
}
