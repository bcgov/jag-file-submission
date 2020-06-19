package ca.bc.gov.open.jag.efilinglookupclient;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.joda.time.DateTime;

import java.util.Date;

public class DemoLookupServiceImpl implements EfilingLookupService {

    @Override
    public ServiceFees getServiceFee(String serviceId) {

        ServiceFees serviceFees = new ServiceFees(
            DateTime.now(),
            0.0f,
            "entUserId",
            serviceId,
            DateTime.now(),
            "updUserId",
            DateTime.now(),
            DateTime.now());

        return serviceFees;
    }
}
