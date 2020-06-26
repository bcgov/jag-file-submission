package ca.bc.gov.open.jag.efilinglookupclient;

import ca.bc.gov.ag.csows.lookups.ServiceFee;
import org.joda.time.DateTime;

import java.math.BigDecimal;

public class DemoLookupServiceImpl implements EfilingLookupService {

    @Override
    public ServiceFee getServiceFee(String serviceId) {

        ServiceFee serviceFees = new ServiceFee();
        serviceFees.setFeeAmt(BigDecimal.ZERO);
        return serviceFees;
    }
}
